package org.bulatnig.smpp.session;

import org.bulatnig.smpp.pdu.Pdu;
import org.bulatnig.smpp.pdu.PduException;

import java.io.IOException;

/**
 * Asynchronous session with SMSC.
 * Supports connection by sending EnquireLink requests, session reuse (reconnect operations),
 * limiting number of messages per second.
 *
 * @author Bulat Nigmatullin
 */
public interface Session {

    /**
     * Wait 30 seconds for response by default.
     */
    static final int DEFAULT_SMSC_RESPONSE_TIMEOUT = 30000;

    /**
     * Send ENQUIRE_LINK requests every 30 seconds to check SMSC alive..
     */
    static final int DEFAULT_PING_TIMEOUT = 30000;

    /**
     * Set incoming messages from SMSC listener.
     *
     * @param sessionListener listener
     */
    void setSessionListener(SessionListener sessionListener);

    /**
     * Set time in ms in which SMSC should response.
     *
     * @param timeout time in milliseconds
     */
    void setSmscResponseTimeout(int timeout);

    /**
     * Session inactivity time in milliseconds after that ENQUIRE_LINK should be sent,
     * to check SMSC availability.
     *
     * @param timeout time in milliseconds
     */
    void setPingTimeout(int timeout);

    /**
     * Open session. Establish TCP connection and send provided bind PDU.
     * Sequence number 1 assigned to PDU automatically.
     *
     * @param pdu bind request
     * @return bind response
     * @throws PduException PDU parsing failed
     * @throws IOException  input-output exception
     */
    Pdu open(Pdu pdu) throws PduException, IOException;

    /**
     * Automatically generated unique PDU sequence number, used to track SMSC response.
     * Application should first call this method, then set returned value to PDU and send it.
     *
     * @return relatively unique PDU sequence number
     */
    long nextSequenceNumber();

    /**
     * Send PDU to SMSC.
     *
     * @param pdu pdu to send
     * @throws PduException PDU parsing failed
     * @throws IOException  input-output exception
     */
    void send(Pdu pdu) throws PduException, IOException;

    /**
     * Send Unbind request, wait for UnbindResp, then close TCP connection and free all resources.
     * Session may be reopened.
     */
    void close();

}
