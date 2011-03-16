package org.bulatnig.smpp.session.impl;

import org.bulatnig.smpp.net.impl.TcpConnection;
import org.bulatnig.smpp.pdu.CommandStatus;
import org.bulatnig.smpp.pdu.Pdu;
import org.bulatnig.smpp.pdu.impl.*;
import org.bulatnig.smpp.session.MessageListener;
import org.bulatnig.smpp.session.Session;
import org.bulatnig.smpp.session.State;
import org.bulatnig.smpp.session.StateListener;
import org.bulatnig.smpp.testutil.ComplexSmscStub;
import org.bulatnig.smpp.testutil.SimpleSmscStub;
import org.bulatnig.smpp.testutil.UniquePortGenerator;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * BasicSession test.
 *
 * @author Bulat Nigmatullin
 */
public class BasicSessionTest {

    private static final Logger logger = LoggerFactory.getLogger(BasicSessionTest.class);

    @Test
    public void openAndClose() throws Exception {
        final int port = UniquePortGenerator.generate();
        final Pdu request = new BindTransceiver();
        final ComplexSmscStub smsc = new ComplexSmscStub(port);
        smsc.start();
        Pdu bindResp = null;
        try {

            BasicSession session = basicSession(smsc, port, null);
            bindResp = session.open(request);
            session.close();

        } finally {
            smsc.stop();
        }
        assertNotNull(bindResp);
        assertEquals(CommandStatus.ESME_ROK, bindResp.getCommandStatus());
        assertArrayEquals(request.buffer().array(), smsc.input.get(0));
    }

    @Test(expected = IOException.class)
    public void openFailedPortClosed() throws Exception {
        final int port = UniquePortGenerator.generate();
        final Pdu request = new BindTransceiver();

        BasicSession session = new BasicSession(new TcpConnection(new InetSocketAddress("localhost", port)));
        session.setSmscResponseTimeout(100);
        session.open(request);
    }

    @Test(expected = IOException.class)
    public void openFailedBindResponseNotReceived() throws Exception {
        final int port = UniquePortGenerator.generate();
        final Pdu request = new BindTransceiver();
        SimpleSmscStub smsc = new SimpleSmscStub(port);
        smsc.start();
        try {

            BasicSession session = new BasicSession(new TcpConnection(new InetSocketAddress("localhost", port)));
            session.setSmscResponseTimeout(100);
            session.open(request);

        } finally {
            smsc.stop();
        }
    }

    @Test(timeout = 2000)
    public void read() throws Exception {
        final int port = UniquePortGenerator.generate();
        final Pdu request = new BindTransceiver();
        final ComplexSmscStub smsc = new ComplexSmscStub(port);
        smsc.start();
        final MessageListenerImpl listener = new MessageListenerImpl();
        final byte[] incoming = new DeliverSm().buffer().array();
        try {

            BasicSession session = new BasicSession(new TcpConnection(new InetSocketAddress("localhost", port)));
            session.setMessageListener(listener);
            session.setSmscResponseTimeout(500);
            session.setEnquireLinkTimeout(300);
            session.open(request);

            smsc.write(incoming);

            while (smsc.input.size() < 3)
                Thread.sleep(10);

            smsc.write(incoming);

            session.close();

        } finally {
            smsc.stop();
        }
        assertEquals(2, listener.pdus.size());
        assertArrayEquals(incoming, listener.pdus.get(0).buffer().array());
        assertArrayEquals(incoming, listener.pdus.get(1).buffer().array());
        assertEquals(4, smsc.input.size());
        assertArrayEquals(request.buffer().array(), smsc.input.get(0));
        Pdu ping = new EnquireLink();
        ping.setSequenceNumber(2);
        assertArrayEquals(ping.buffer().array(), smsc.input.get(1));
        Pdu ping2 = new EnquireLink();
        ping2.setSequenceNumber(3);
        assertArrayEquals(ping2.buffer().array(), smsc.input.get(2));
    }

    @Test
    public void write() throws Exception {
        final int port = UniquePortGenerator.generate();
        final ComplexSmscStub smsc = new ComplexSmscStub(port);
        smsc.start();
        final MessageListenerImpl listener = new MessageListenerImpl();
        final Pdu request = new SubmitSm();

        try {
            BasicSession session = basicSession(smsc, port, listener);
            session.open(new BindTransceiver());
            session.send(request);

            Thread.sleep(20);

            session.close();
        } finally {
            smsc.stop();
        }

        assertEquals(1, listener.pdus.size());

        assertEquals(3, smsc.input.size());
        assertArrayEquals(request.buffer().array(), smsc.input.get(1));
    }

    @Test(timeout = 500)
    public void close() throws Exception {
        final Pdu request = new BindTransceiver();
        final Pdu response = new BindTransceiverResp();
        response.setSequenceNumber(1);
        final int port = UniquePortGenerator.generate();
        final ComplexSmscStub smsc = new ComplexSmscStub(port);
        smsc.start();

        ScheduledExecutorService es = Executors.newSingleThreadScheduledExecutor();
        es.schedule(new Runnable() {
            @Override
            public void run() {
                try {
                    smsc.write(new UnbindResp().buffer().array());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 200, TimeUnit.MILLISECONDS);

        try {
            BasicSession session = basicSession(smsc, port, null);
            session.setSmscResponseTimeout(Session.DEFAULT_SMSC_RESPONSE_TIMEOUT);
            session.open(request);

            session.close();
        } finally {
            smsc.stop();
            es.shutdownNow();
        }
    }

    @Test
    public void closeWhileConnect() throws Exception {

    }

    @Test
    public void closeWhilePing() throws Exception {
        final Pdu request = new BindTransceiver();
        final Pdu response = new BindTransceiverResp();
        response.setSequenceNumber(1);
        final int port = UniquePortGenerator.generate();
        final ComplexSmscStub smsc = new ComplexSmscStub(port);
        smsc.start();

        try {
            BasicSession session = basicSession(smsc, port, null);
            session.setEnquireLinkTimeout(250);
            session.open(request);

            while (smsc.input.size() == 1)
                Thread.sleep(10);

            session.close();
        } finally {
            smsc.stop();
        }
    }

    @Test
    public void closeByPingThread() throws Exception {
        final Pdu request = new BindTransceiver();
        final Pdu response = new BindTransceiverResp();
        response.setSequenceNumber(1);
        final int port = UniquePortGenerator.generate();
        final ComplexSmscStub smsc = new ComplexSmscStub(port);
        final StateListenerImpl listener = new StateListenerImpl();
        smsc.start();

        try {
            BasicSession session = basicSession(smsc, port, null);
            session.setStateListener(listener);
            session.setSmscResponseTimeout(200);
            session.setEnquireLinkTimeout(250);
            session.open(request);

            while (smsc.input.size() < 2)
                Thread.sleep(10);

            Thread.sleep(250);

            assertNotNull(listener.closed);
            assertEquals(IOException.class, listener.closed.getClass());

        } finally {
            smsc.stop();
        }
    }

    @Test
    public void reconnect() throws Exception {
        final int port = UniquePortGenerator.generate();
        ComplexSmscStub smsc = new ComplexSmscStub(port);
        smsc.start();

        try {
            BasicSession session = new BasicSession(new TcpConnection(new InetSocketAddress("localhost", port)));
            session.setSmscResponseTimeout(200);
            session.setEnquireLinkTimeout(200);
            session.open(new BindTransceiver());

            smsc.stop();

            Thread.sleep(1000);
            logger.info("Sleep done.");

            smsc = new ComplexSmscStub(port);
            smsc.start();
            logger.info("SMSC started.");

            Thread.sleep(300);
            logger.info("Closing session.");

            session.close();
        } finally {
            smsc.stop();
        }
    }

    protected BasicSession basicSession(final ComplexSmscStub smsc, int port, MessageListener listener) {
        BasicSession session = new BasicSession(new TcpConnection(new InetSocketAddress("localhost", port)));
        session.setSmscResponseTimeout(500);
        if (listener != null)
            session.setMessageListener(listener);
        return session;
    }

    private class MessageListenerImpl implements MessageListener {

        private final List<Pdu> pdus = new ArrayList<Pdu>();

        @Override
        public void received(Pdu pdu) {
            pdus.add(pdu);
        }
    }

    private class StateListenerImpl implements StateListener {

        private Exception closed;

        @Override
        public void changed(State state, Exception e) {
            closed = e;
        }
    }

}
