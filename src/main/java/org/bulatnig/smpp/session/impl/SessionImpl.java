package org.bulatnig.smpp.session.impl;

import org.bulatnig.smpp.net.Connection;
import org.bulatnig.smpp.pdu.CommandId;
import org.bulatnig.smpp.pdu.CommandStatus;
import org.bulatnig.smpp.pdu.Pdu;
import org.bulatnig.smpp.pdu.PduException;
import org.bulatnig.smpp.pdu.impl.EnquireLink;
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
public class SessionImpl implements Session {

    private static final Logger logger = LoggerFactory.getLogger(SessionImpl.class);

    private final Connection conn;

    private int smscResponseTimeout = DEFAULT_SMSC_RESPONSE_TIMEOUT;
    private int pingTimeout = DEFAULT_PING_TIMEOUT;
    private SessionListener sessionListener;
    private PingThread pingThread;
    private ReadThread readThread;

    private volatile long sequenceNumber;
    private volatile long lastActivity;

    public SessionImpl(Connection conn) {
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
                sequenceNumber = 0;
                updateLastActivity();
                pingThread = new PingThread();
                new Thread(pingThread).start();
                readThread = new ReadThread();
                new Thread(readThread).start();
            }
            return bindResp;
        } finally {
            es.shutdownNow();
        }
    }

    @Override
    public long send(Pdu pdu) throws PduException, IOException {
        // TODO implement send limit
        pdu.setSequenceNumber(nextSequenceNumber());
        conn.write(pdu);
        return pdu.getSequenceNumber();
    }

    @Override
    public void close() {
        pingThread.stop();
        pingThread = null;
        readThread.stop();
        readThread = null;
        conn.close();
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
                    if (isInactive()) {
                        send(new EnquireLink());
                        Thread.sleep(smscResponseTimeout);
                        isInactive();
                        logger.warn("Enquire link response not received.");
                    }
                    Thread.sleep(pingTimeout);
                }
            } catch (Exception e) {
                if (run)
                    close();
            }
        }

        private boolean isInactive() {
            return pingTimeout < (System.currentTimeMillis() - lastActivity);
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
                        response = new EnquireLink();
                        response.setSequenceNumber(request.getSequenceNumber());
                    } else if (CommandId.ENQUIRE_LINK_RESP == request.getCommandId()) {
                        response = null;
                    } else {
                        response = sessionListener.received(request);
                    }
                    if (response != null) {
                        send(response);
                    }
                }
            } catch (Exception e) {
                if (run) {
                    close();
                }
            }
        }

        void stop() {
            run = false;
        }

    }

}
