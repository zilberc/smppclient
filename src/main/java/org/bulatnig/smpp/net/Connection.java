package org.bulatnig.smpp.net;

import org.bulatnig.smpp.pdu.Pdu;
import org.bulatnig.smpp.pdu.PduException;

import java.io.IOException;

/**
 * Connection with SMPP entity. Converts bytes to PDU and PDU to bytes. The same
 * connection may be reused many times.
 *
 * Note: If connection receive PDU wich does not fit int buffer, IOException throwed.
 * If such PDU not and error, incoming PDU's buffer size should be increased.
 *
 * @author Bulat Nigmatullin
 */
public interface Connection {

    /**
     * Default incoming PDU buffer size.
     */
    static final int DEFAULT_BUFFER_SIZE = 1000;

    /**
     * Set max read PDU length in bytes.
     *
     * @param bufferSize  max PDU length
     */
    void setIncomingBufferSize(int bufferSize);

    /**
     * Open connection.
     *
     * @throws IOException connection failed
     */
    void open() throws IOException;

    /**
     * Read PDU from input. Blocks until PDU received or exception throwed.
     *
     * @return PDU
     * @throws PduException parsing error
     * @throws IOException  I/O error
     */
    Pdu read() throws PduException, IOException;

    /**
     * Write PDU to output.
     *
     * @param pdu PDU for sending
     * @throws PduException PDU to bytes converting error
     * @throws IOException  I/O error
     */
    void write(Pdu pdu) throws PduException, IOException;

    /**
     * Close connection.
     */
    void close();

}
