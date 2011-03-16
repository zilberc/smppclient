package org.bulatnig.smpp.session.impl;

import org.bulatnig.smpp.pdu.CommandId;
import org.bulatnig.smpp.pdu.Pdu;
import org.bulatnig.smpp.pdu.PduException;
import org.bulatnig.smpp.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Session, limiting the number of outgoing requests per second.
 *
 * @author Bulat Nigmatullin
 */
public class LimitingSession implements Session {

    private static final Logger logger = LoggerFactory.getLogger(LimitingSession.class);

    /**
     * Limit message count per this amount of time.
     */
    private static final int TIMEOUT = 1010;

    private final Session session;

    /**
     * Holds the times when the last messages was sent.
     */
    private final BlockingQueue<Long> sentTimes;

    public LimitingSession(Session session, int maxMessagesPerSecond) {
        this.session = session;
        sentTimes = new LinkedBlockingQueue<Long>(maxMessagesPerSecond);
        for (int i = 0; i < maxMessagesPerSecond; i++)
            sentTimes.add(0L);
    }

    @Override
    public long nextSequenceNumber() {
        return session.nextSequenceNumber();
    }

    @Override
    public boolean send(Pdu pdu) throws PduException {
        if (CommandId.SUBMIT_SM != pdu.getCommandId()) {
            return session.send(pdu);
        } else {
            try {
                long timeToSleep = sentTimes.poll() + TIMEOUT - System.currentTimeMillis();
                logger.trace("Time spent from N message back to this: {}.", timeToSleep);
                if (timeToSleep > 0) {
                    logger.trace("Going to sleep {} ms.", timeToSleep);
                    Thread.sleep(timeToSleep);
                }
                return session.send(pdu);
            } catch (InterruptedException e) {
                logger.warn("Send interrupted.", e);
                return false;
            } finally {
                sentTimes.add(System.currentTimeMillis());
            }
        }
    }
}
