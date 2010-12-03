package org.bulatnig.smpp.session.impl;

import org.bulatnig.smpp.pdu.Pdu;
import org.bulatnig.smpp.pdu.impl.BindTransceiver;
import org.bulatnig.smpp.pdu.impl.BindTransceiverResp;
import org.bulatnig.smpp.session.Session;
import org.bulatnig.smpp.testutil.SmscStub;
import org.bulatnig.smpp.testutil.UniquePortGenerator;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Future;

import static org.junit.Assert.assertArrayEquals;

/**
 * SessionImpl test.
 *
 * @author Bulat Nigmatullin
 */
public class SessionImplTest {

    private static final Logger logger = LoggerFactory.getLogger(SessionImplTest.class);

    @Ignore
    @Test
    public void openAndClose() throws Exception {
        final Pdu request = new BindTransceiver();
        final Pdu response = new BindTransceiverResp();
        response.setSequenceNumber(1);
        final int port = UniquePortGenerator.generate();
        SmscStub smsc = new SmscStub(port);
        smsc.start();

        try {
            logger.debug("Creating session.");
            Session session = null;
            logger.debug("Opening session.");
            Future<Pdu> bindResp = session.open(request);
//            smsc.write(response.buffer().array());
            Thread.sleep(20);
            smsc.write(new BindTransceiverResp().buffer().array());
            logger.debug("Receiving bind response.");
            Pdu resp = bindResp.get();
            logger.debug("Get returned: {}.", resp);
            logger.debug("Is done: {}.", bindResp.isDone());
            Thread.sleep(1000);
            logger.debug("Close session.");
            session.close();
            logger.debug("Test successfully done.");
        } finally {
            smsc.stop();
        }

        assertArrayEquals(request.buffer().array(), smsc.input.get(0));
    }
}
