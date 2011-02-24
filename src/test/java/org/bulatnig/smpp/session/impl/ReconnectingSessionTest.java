package org.bulatnig.smpp.session.impl;

import org.bulatnig.smpp.net.impl.TcpConnection;
import org.bulatnig.smpp.pdu.impl.BindTransceiver;
import org.bulatnig.smpp.pdu.impl.BindTransceiverResp;
import org.bulatnig.smpp.session.Session;
import org.bulatnig.smpp.testutil.SmscStub;
import org.bulatnig.smpp.testutil.UniquePortGenerator;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * ReconnectingSession test
 *
 * @author Bulat Nigmatullin
 */
public class ReconnectingSessionTest {

    private static final Logger logger = LoggerFactory.getLogger(ReconnectingSessionTest.class);

    @Test
    public void reconnect() throws Exception {
        final int port = UniquePortGenerator.generate();
        SmscStub smsc = new SmscStub(port);
        final int reconnect = 100;
        smsc.start();

        ScheduledExecutorService es = Executors.newSingleThreadScheduledExecutor();
        es.schedule(new BindResponder(smsc), 100, TimeUnit.MILLISECONDS);

        try {
            Session session = new ReconnectingSession(new BasicSession(new TcpConnection(new InetSocketAddress("localhost", port))), reconnect);
            session.setSmscResponseTimeout(200);
            session.setPingTimeout(200);
            session.open(new BindTransceiver());

            smsc.stop();

            Thread.sleep(1000);
            logger.info("Sleep done.");

            smsc = new SmscStub(port);
            smsc.start();
            es.schedule(new BindResponder(smsc), 100, TimeUnit.MILLISECONDS);
            logger.info("SMSC started.");

            Thread.sleep(300);
            logger.info("Closing session.");

            session.close();
        } finally {
            es.shutdownNow();
            smsc.stop();
        }
    }

    private class BindResponder implements Runnable {

        private final SmscStub smsc;

        private BindResponder(SmscStub smsc) {
            this.smsc = smsc;
        }

        @Override
        public void run() {
            try {
                smsc.write(new BindTransceiverResp().buffer().array());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
