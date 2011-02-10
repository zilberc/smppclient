package org.bulatnig.smpp.session;

import org.bulatnig.smpp.pdu.Pdu;

/**
 * Session incoming messages listener.
 *
 * @author Bulat Nigmatullin
 */
public interface SessionListener {

    /**
     * Process incoming PDU.
     *
     * @param pdu   incoming PDU from SMSC
     */
    void received(Pdu pdu);

    /**
     * Session closed cause exception.
     *
     * @param e asynchronous  exception
     */
    void closed(Exception e);

}
