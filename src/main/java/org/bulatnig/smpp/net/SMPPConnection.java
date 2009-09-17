package org.bulatnig.smpp.net;

import org.bulatnig.smpp.pdu.PDU;
import org.bulatnig.smpp.pdu.PDUException;

import java.io.IOException;
import java.util.List;

/**
 * Connection with SMSC.
 * Converts bytes to corresponding PDU when reads them and PDU to bytes when writes.
 * Constructor should return only working SMPP connection and throw exception on error.
 * <p/>
 * User: Bulat Nigmatullin
 * Date: 25.06.2008
 * Time: 6:24:20
 */
public interface SMPPConnection {

    /**
     * Connect try timeout. Used only once on connect creation.
     */
    public static final int DEFAULT_CONNECT_TIMEOUT = 3000;

    /**
     * Closes SMPP connection.
     */
    public void close();

    /**
     * Reads all available PDUs from input and returns them.
     *
     * @return read PDUs
     * @throws IOException   I/O error
     */
    public List<PDU> read() throws IOException;

    /**
     * Writes PDU and returns. Don't wait for reply or something.
     *
     * @param pdu PDU for sending
     * @throws IOException  I/O error
     * @throws PDUException PDU to bytes converting error
     */
    public void write(PDU pdu) throws IOException, PDUException;

    /**
     * @return  SMSC host
     */
    public String getHost();

    /**
     *
     * @return SMSC port
     */
    public int getPort();

}
