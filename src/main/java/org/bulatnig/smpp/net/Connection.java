package org.bulatnig.smpp.net;

import java.io.IOException;

/**
 * Connection with SMPP entity.
 * Converts bytes to PDU and PDU to bytes.
 *
 * @author Bulat Nigmatullin
 */
public interface Connection {

    /**
     * Open connection.
     *
     * @throws IOException connection failed
     */
    void open() throws IOException;



    /**
     * Close connection.
     */
    void close();

}
