package org.bulatnig.smpp.session;

import org.bulatnig.smpp.pdu.PDU;
import org.bulatnig.smpp.pdu.PDUException;
import org.bulatnig.smpp.pdu.TON;
import org.bulatnig.smpp.pdu.NPI;

import java.io.IOException;

/**
 * Session with SMSC.
 * Constructor should retern only established session instance and throw exception otherwise.
 *
 * @author Bulat Nigmatullin
 */
public interface SMPPSession {

    /**
     * Stop interaction with SMSC.
     */
    public void stop();
    
    /**
     * Sends PDU to SMSC. Method blocks and returns SMSC response.
     *
     * @param pdu PDU
     * @return response PDU
     * @throws IOException I/O error
     * @throws PDUException PDU parsing error
     * @throws NoResponseException response PDU not received in defined amount of time
     */
    public PDU send(PDU pdu) throws IOException, PDUException, NoResponseException;

    /**
     * Sets PDU handler for this SMPP session.
     *
     * @param pduHandler user PDUHandler interface implementation
     */
    public void setPDUHandler(PDUHandler pduHandler);

    /**
     * Listens to SMPP session state changes such as disconnect or stop.
     *
     * @param listener  user SMPPSessionStateListener interface implementation
     */
    public void setSMPPSessionStateListener(SMPPSessionStateListener listener);

    public ConnectionType getConnType();

    public String getSystemId();

    public String getPassword();

    public String getSystemType();

    public short getInterfaceVersion();

    public TON getAddrTon();

    public NPI getAddrNpi();

    public String getAddressRange();

}
