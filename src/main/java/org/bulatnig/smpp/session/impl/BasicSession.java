package org.bulatnig.smpp.session.impl;

import org.bulatnig.smpp.net.Connection;
import org.bulatnig.smpp.pdu.CommandId;
import org.bulatnig.smpp.pdu.CommandStatus;
import org.bulatnig.smpp.pdu.Pdu;
import org.bulatnig.smpp.pdu.PduException;
import org.bulatnig.smpp.pdu.impl.EnquireLink;
import org.bulatnig.smpp.pdu.impl.EnquireLinkResp;
import org.bulatnig.smpp.pdu.impl.Unbind;
import org.bulatnig.smpp.session.Session;
import org.bulatnig.smpp.session.SessionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Asynchronous session implementation.
 *
 * @author Bulat Nigmatullin
 */
public class BasicSession implements Session {

    private static final Logger logger = LoggerFactory.getLogger(BasicSession.class);

    private final Connection conn;

    private int smscResponseTimeout = DEFAULT_SMSC_RESPONSE_TIMEOUT;
    private int pingTimeout = DEFAULT_PING_TIMEOUT;
    private SessionListener sessionListener = new DefaultSessionListener();
    private PingThread pingThread;
    private ReadThread readThread;

    private volatile long sequenceNumber;
    private volatile long lastActivity;

    private volatile boolean closed;
    private volatile IOException ioe;
    private volatile PduException pdue;

    public BasicSession(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void setSessionListener(SessionListener sessionListener) {
        this.sessionListener = sessionListener;
    }

    @Override
    public void setSmscResponseTimeout(int timeout) {
        this.smscResponseTimeout = timeout;
    }

    @Override
    public void setPingTimeout(int timeout) {
        pingTimeout = timeout;
    }

    @Override
    public Pdu open(Pdu pdu) throws PduException, IOException {
        sequenceNumber = 0;
        closed = false;
        ioe = null;
        pdue = null;
        conn.open();
        send(pdu);
        ScheduledExecutorService es = Executors.newSingleThreadScheduledExecutor();
        es.schedule(new Runnable() {
            @Override
            public void run() {
                logger.warn("SMSC bind response not received, terminating connection.");
                conn.close();
            }
        }, smscResponseTimeout, TimeUnit.MILLISECONDS);
        try {
            Pdu bindResp = conn.read();
            if (CommandStatus.ESME_ROK == bindResp.getCommandStatus()) {
                updateLastActivity();
                pingThread = new PingThread();
                Thread t1 = new Thread(pingThread);
                t1.setName("Ping");
                t1.start();
                readThread = new ReadThread();
                Thread t2 = new Thread(readThread);
                t2.setName("Read");
                t2.start();
            }
            return bindResp;
        } finally {
            es.shutdownNow();
        }
    }

    @Override
    public synchronized long send(Pdu pdu) throws PduException, IOException {
        if (closed) {
            if (ioe != null)
                throw ioe;
            if (pdue != null)
                throw pdue;
            throw new IOException("Connection already closed.");
        }
        if (pdu.getCommandId() < CommandId.GENERIC_NACK)
            pdu.setSequenceNumber(nextSequenceNumber());
        conn.write(pdu);
        return pdu.getSequenceNumber();
    }

    @Override
    public synchronized void close() {
        if (!closed) {
            try {
                send(new Unbind());
//                Thread.sleep(smscResponseTimeout);
                synchronized (conn) {
                    conn.wait(smscResponseTimeout);
                }
            } catch (Exception e) {
                logger.debug("Unbind request send failed.", e);
            }
            pingThread.stop();
            pingThread = null;
            readThread.stop();
            readThread = null;
            conn.close();
            closed = true;
            logger.trace("Session closed.");
        }
    }

    private synchronized long nextSequenceNumber() {
        if (sequenceNumber == 4294967295L)
            sequenceNumber = 1;
        else
            sequenceNumber++;
        return sequenceNumber;
    }

    private void updateLastActivity() {
        lastActivity = System.currentTimeMillis();
    }

    private class PingThread implements Runnable {

        private volatile boolean run = true;

        @Override
        public void run() {
            try {
                while (run) {
                    logger.trace("Checking last activity");
                    if (pingTimeout < (System.currentTimeMillis() - lastActivity)) {
                        long prevLastActivity = lastActivity;
                        send(new EnquireLink());
                        synchronized (conn) {
                            conn.wait(smscResponseTimeout);
                        }
                        if (run && lastActivity == prevLastActivity) {
                            ioe = new IOException("Enquire link response not received. Session closed.");
                            close();
                            break;
                        }
                    }
                    logger.trace("Going to sleep {}", pingTimeout);
                    Thread.sleep(pingTimeout);
                }
            } catch (PduException e) {
                if (run) {
                    logger.warn("EnquireLink request failed.", e);
                    pdue = e;
                    close();
                }
            } catch (IOException e) {
                if (run) {
                    logger.warn("Ping thread IO failed", e);
                    ioe = e;
                    close();
                }
            } catch (InterruptedException e) {
                logger.debug("Ping thread interrupted.");
            }
        }

        void stop() {
            run = false;
        }

    }

    private class ReadThread implements Runnable {

        private volatile boolean run = true;

        @Override
        public void run() {
            try {
                while (run) {
                    Pdu request = conn.read();
                    updateLastActivity();
                    Pdu response;
                    if (CommandId.ENQUIRE_LINK == request.getCommandId()) {
                        response = new EnquireLinkResp();
                        response.setSequenceNumber(request.getSequenceNumber());
                        send(response);
                    } else if (CommandId.ENQUIRE_LINK_RESP == request.getCommandId()) {
                        synchronized (conn) {
                            conn.notifyAll();
                        }
                    } else if (CommandId.UNBIND_RESP == request.getCommandId()) {
                        synchronized (conn) {
                            conn.notifyAll();
                        }
                    } else {
                        sessionListener.received(request);
                    }
                }
            } catch (PduException e) {
                if (run) {
                    logger.warn("Incoming message parsing failed.", e);
                    pdue = e;
                    close();
                }
            } catch (IOException e) {
                if (run) {
                    logger.warn("Reading IO failure.", e);
                    ioe = e;
                    close();
                }
            }
        }

        void stop() {
            run = false;
        }

    }

    private class DefaultSessionListener implements SessionListener {
        @Override
        public void received(Pdu pdu) {
            logger.debug("{} received, but no session listener set.", pdu.getClass().getName());
        }
    }

}
