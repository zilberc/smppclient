package org.bulatnig.smpp.session.impl;

import org.bulatnig.smpp.pdu.CommandStatus;
import org.bulatnig.smpp.pdu.Pdu;
import org.bulatnig.smpp.pdu.PduException;
import org.bulatnig.smpp.session.MessageListener;
import org.bulatnig.smpp.session.Session;
import org.bulatnig.smpp.session.State;
import org.bulatnig.smpp.session.StateListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Session, reconnecting on failure.
 *
 * @author Bulat Nigmatullin
 */
public class ReconnectingSession implements Session {

    private static final Logger logger = LoggerFactory.getLogger(ReconnectingSession.class);

    private final Session session;
    private final int reconnectPeriod;

    private StateListener stateListener = new DefaultStateListener();
    private Pdu bindPdu;

    private volatile boolean closed;

    public ReconnectingSession(Session session, int reconnectPeriod) {
        this.session = session;
        this.reconnectPeriod = reconnectPeriod;
        session.setStateListener(new StateListenerImpl());
    }

    @Override
    public void setMessageListener(MessageListener messageListener) {
        session.setMessageListener(messageListener);
    }

    @Override
    public void setStateListener(StateListener stateListener) {
        this.stateListener = stateListener;
    }

    @Override
    public void setSmscResponseTimeout(int timeout) {
        session.setSmscResponseTimeout(timeout);
    }

    @Override
    public void setPingTimeout(int timeout) {
        session.setPingTimeout(timeout);
    }

    @Override
    public Pdu open(Pdu pdu) throws PduException, IOException {
        bindPdu = pdu;
        closed = false;
        return session.open(pdu);
    }

    @Override
    public long nextSequenceNumber() {
        return session.nextSequenceNumber();
    }

    @Override
    public void send(Pdu pdu) throws PduException, IOException {
        session.send(pdu);
    }

    @Override
    public void close() {
        closed = true;
        session.close();
    }

    private class StateListenerImpl implements StateListener {
        @Override
        public void changed(State state, Exception e) {
            if (State.DISCONNECTED == state) {
                stateListener.changed(state, e);
                try {
                    try {
                        Thread.sleep(reconnectPeriod);
                    } catch (InterruptedException e1) {
                        logger.debug("Reconnection sleep interrupted.", e1);
                    }
                    if (!closed) {
                        stateListener.changed(State.RECONNECTING, null);
                        Pdu bindResponse = session.open(bindPdu);
                        if (CommandStatus.ESME_ROK == bindResponse.getCommandStatus()) {
                            logger.info("Session successfully restarted.");
                        } else {
                            logger.warn("Session reconnect failed, SMSC response: {}.", bindResponse.getCommandStatus());
                        }
                    }
                } catch (Exception e1) {
                    logger.warn("Session reconnect failed.", e1);
                }
            } else {
                stateListener.changed(state, e);
            }
        }

    }

}
