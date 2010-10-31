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
public interface Session {

    /**
     * Stop interaction with SMSC.
     */
    void stop();
    
    /**
     * Sends PDU to SMSC. Method blocks and returns SMSC response.
     *
     * @param pdu PDU
     * @return response PDU
     * @throws IOException I/O error
     * @throws PDUException PDU parsing error
     * @throws NoResponseException response PDU not received in defined amount of time
     */
    PDU send(PDU pdu) throws IOException, PDUException, NoResponseException;

    /**
     * Sets PDU handler for this SMPP session.
     *
     * @param pduHandler user PDUHandler interface implementation
     */
    void setPDUHandler(PDUHandler pduHandler);

    /**
     * Listens to SMPP session state changes such as disconnect or stop.
     *
     * @param listener  user SmppSessionStateListener interface implementation
     */
    void setSMPPSessionStateListener(SmppSessionStateListener listener);

    ConnectionType getConnType();

    String getSystemId();

    String getPassword();

    String getSystemType();

    short getInterfaceVersion();

    TON getAddrTon();

    NPI getAddrNpi();

    String getAddressRange();

}
