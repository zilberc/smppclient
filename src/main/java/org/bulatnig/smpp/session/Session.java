package org.bulatnig.smpp.session;

import org.bulatnig.smpp.pdu.Pdu;
import org.bulatnig.smpp.pdu.PduException;

import java.io.IOException;

/**
 * Session with SMSC.
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
     *
     * @param pdu bind request
     * @return bind response
     * @throws PduException PDU parsing failed
     * @throws IOException  input-output exception
     */
    Pdu open(Pdu pdu) throws PduException, IOException;

    /**
     * Send PDU to SMSC.
     * If SMSC response not received in SmscResponseTimeout, null returned.
     *
     * @param pdu pdu to send
     * @return sent PDU sequence number
     * @throws PduException PDU parsing failed
     * @throws IOException  input-output exception
     */
    long send(Pdu pdu) throws PduException, IOException;

    /**
     * Close TCP connection and free all resources. Session may be reopened.
     */
    void close();

}
