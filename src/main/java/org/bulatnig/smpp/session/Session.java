package org.bulatnig.smpp.session;

import org.bulatnig.smpp.pdu.Pdu;
import org.bulatnig.smpp.pdu.PduNotFoundException;
import org.bulatnig.smpp.pdu.PduParsingException;

import java.io.IOException;
import java.util.concurrent.Future;

/**
 * Session with SMSC.
 *
 * @author Bulat Nigmatullin
 */
public interface Session {

    /**
     * Open session. Establish TCP connection and send provided bind PDU.
     *
     * @param pdu   bind request
     * @return  bind response
     * @throws PduParsingException  wrong PDU
     * @throws PduNotFoundException PDU not found by id
     * @throws IOException  input-output exception
     */
    Pdu open(Pdu pdu) throws PduParsingException, PduNotFoundException, IOException;

    /**
     * Send PDU to SMSC.
     *
     * @param pdu   pdu to send
     * @return  response pdu
     */
    Future<Pdu> send(Pdu pdu);

    /**
     * Close TCP connection and free all resources. Session may be reopened.
     */
    void close();

}
