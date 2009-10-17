package org.bulatnig.smpp.net;

import org.bulatnig.smpp.pdu.PDU;
import org.bulatnig.smpp.pdu.PDUException;

import java.io.IOException;
import java.util.List;

/**
 * Connection with SMSC.
 * Converts bytes to corresponding PDU when reads them and PDU to bytes when writes.
 * Constructor should return only working SMPP connection and throw exception on connection error.
 *
 * @author Bulat Nigmatullin
 */
public interface Connection {

    /**
     * Connect try timeout. Used only once on connect creation.
     */
    static final int DEFAULT_CONNECT_TIMEOUT = 3000;

    /**
     * @return SMSC host
     */
    String getHost();

    /**
     * @return SMSC port
     */
    int getPort();

    /**
     * Read PDUs from input.
     *
     * @return read PDUs
     * @throws IOException I/O error
     */
    List<PDU> read() throws IOException;

    /**
     * Write PDU and return. Don't wait for reply or something.
     *
     * @param pdu PDU for sending
     * @throws IOException  I/O error
     * @throws PDUException PDU to bytes converting error
     */
    void write(PDU pdu) throws IOException, PDUException;

    /**
     * Close SMPP connection.
     */
    void close();

}
