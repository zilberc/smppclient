package org.bulatnig.smpp.session;

import org.bulatnig.smpp.pdu.Pdu;
import org.bulatnig.smpp.pdu.PduException;

import java.io.IOException;

/**
 * Asynchronous session with SMSC.
 * Supports connection by sending EnquireLink requests, session reuse (reconnect operations),
 * automatically reconnects on IO failure after successful connect.
 *
 * @author Bulat Nigmatullin
 */
public interface Session {

    /**
     * Wait 30 seconds for response by default.
     */
    static final int DEFAULT_SMSC_RESPONSE_TIMEOUT = 30000;

    /**
     * Send ENQUIRE_LINK requests every 30 seconds to check SMSC alive.
     */
    static final int DEFAULT_ENQUIRE_LINK_TIMEOUT = 30000;

    /**
     * Try to reconnect every N ms.
     */
    static final int DEFAULT_RECONNECT_TIMEOUT = 1000;

    /**
     * Unique PDU sequence number, used to track SMSC response.
     * Application should first call this method, then set returned sequence number to PDU and
     * send it.
     *
     * @return relatively unique PDU sequence number
     */
    long nextSequenceNumber();

    /**
     * Send PDU to SMSC.
     *
     * @param pdu pdu to send
     * @return send successful
     * @throws PduException PDU parsing failed
     */
    boolean send(Pdu pdu) throws PduException;

}
