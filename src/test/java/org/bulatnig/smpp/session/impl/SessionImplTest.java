package org.bulatnig.smpp.session.impl;

import org.bulatnig.smpp.net.impl.TcpConnection;
import org.bulatnig.smpp.pdu.CommandId;
import org.bulatnig.smpp.pdu.CommandStatus;
import org.bulatnig.smpp.pdu.Pdu;
import org.bulatnig.smpp.pdu.PduException;
import org.bulatnig.smpp.pdu.impl.BindTransceiver;
import org.bulatnig.smpp.pdu.impl.BindTransceiverResp;
import org.bulatnig.smpp.session.Session;
import org.bulatnig.smpp.testutil.SmscStub;
import org.bulatnig.smpp.testutil.UniquePortGenerator;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * SessionImpl test.
 *
 * @author Bulat Nigmatullin
 */
public class SessionImplTest {

    private static final Logger logger = LoggerFactory.getLogger(SessionImplTest.class);

    @Test
    public void openAndClose() throws Exception {
        final Pdu request = new BindTransceiver();
        final Pdu response = new BindTransceiverResp();
        response.setSequenceNumber(1);
        final int port = UniquePortGenerator.generate();
        SmscStub smsc = new SmscStub(port);
        smsc.start();
        Pdu bindResp = null;

        try {
            Session session = new SessionImpl(new TcpConnection(new InetSocketAddress("localhost", port)));
            ScheduledExecutorService es = Executors.newSingleThreadScheduledExecutor();
            es.schedule(new DelayedBindResponse(smsc), 500, TimeUnit.MILLISECONDS);
            bindResp = session.open(request);

            session.close();
        } finally {
            smsc.stop();
        }

        assertNotNull(bindResp);
        assertEquals(CommandStatus.ESME_ROK, bindResp.getCommandStatus());
        assertArrayEquals(request.buffer().array(), smsc.input.get(0));
    }

    private class DelayedBindResponse implements Runnable {

        private final SmscStub smscStub;

        private DelayedBindResponse(SmscStub smscStub) {
            this.smscStub = smscStub;
        }

        @Override
        public void run() {
            try {
                smscStub.write(new BindTransceiverResp().buffer().array());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (PduException e) {
                e.printStackTrace();
            }
        }

    }

    @Test(expected = IOException.class)
    public void openFailed() throws Exception {
        final Pdu request = new BindTransceiver();
        final Pdu response = new BindTransceiverResp();
        response.setSequenceNumber(1);
        final int port = UniquePortGenerator.generate();
        SmscStub smsc = new SmscStub(port);
        smsc.start();

        try {
            Session session = new SessionImpl(new TcpConnection(new InetSocketAddress("localhost", port)));
            session.open(request);
        } finally {
            smsc.stop();
        }
    }

}
