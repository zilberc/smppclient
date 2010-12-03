package org.bulatnig.smpp.session;

import org.bulatnig.smpp.pdu.Pdu;

import java.io.IOException;
import java.util.concurrent.Future;

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

    /**
     * Set incoming messages from SMSC listener.
     *
     * @param sessionListener   listener
     */
    void setSessionListener(SessionListener sessionListener);

    /**
     * Set time in ms in which SMSC should response.
     *
     * @param timeout   time in milliseconds
     */
    void setSmscResponseTimeout(int timeout);

    /**
     * Open session. Establish TCP connection and send provided bind PDU.
     *
     * @param pdu   bind request
     * @return  bind response
     * @throws IOException  input-output exception
     */
    Future<Pdu> open(Pdu pdu) throws IOException;

    /**
     * Send PDU to SMSC.
     * If SMSC response not received in SmscResponseTimeout, null returned.
     *
     * @param pdu   pdu to send
     * @return  response pdu, can be null
     */
    Future<Pdu> send(Pdu pdu);

    /**
     * Close TCP connection and free all resources. Session may be reopened.
     */
    void close();

}
