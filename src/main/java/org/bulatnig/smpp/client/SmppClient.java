package org.bulatnig.smpp.client;

import org.bulatnig.smpp.pdu.NPI;
import org.bulatnig.smpp.pdu.TON;
import org.bulatnig.smpp.session.NoResponseException;

/**
 * Simplifyes work with Session and provides higher level abstraction. Automatically recognize UCS messages and properly
 * encodes them. Depends on MessageSplitStrategy automatically splits messages on small chunks. Smart PDU error handling.
 *
 * User: Bulat Nigmatullin
 * Date: 25.07.2008
 * Time: 20:27:38
 */
public interface SmppClient {

    /**
     * Stop Client and close session with SMSC.
     */
    public void stop();

    /**
     * Send message to SMSC.
     *
     * @param message                       SMS
     * @return                              SMSC assigned message ID
     * @throws ProtocolException            exception during interaction with SMSC
     * @throws DeliveryFailedException      SMSC unrecoverable error returned on message submit
     * @throws DeliveryDelayedException     SMSC recoverable error returned on message submit
     * @throws NoResponseException          no response from SMSC on message
     */
    public String send(Message message) throws ProtocolException, DeliveryFailedException,
            DeliveryDelayedException, NoResponseException;

    /**
     * Defines message split strategy.
     *
     * @param mss   метод разбиения
     */
    public void setMessageSplitStrategy(MessageSplitStrategy mss);

    /**
     * If message conforms defined data coding format, data_coding field set to 0 (zero),
     * else it's set to proper value.
     * @param cd    smsc default data coding
     */
    public void setDefaultSMSCDataCoding(MessageCoding cd);

    /**
     * Устанавливает параметр sourceAddrTon всех исходящих сообщений.
     *
     * @param ton   sourceAddrTon
     */
    public void setSourceAddrTon(TON ton);

    /**
     * Возвращает параметр sourceAddrTon всех исходящих сообщений.
     *
     * @return   sourceAddrTon
     */
    public TON getSourceAddrTon();

    /**
     * Устанавливает параметр sourceAddrNpi всех исходящих сообщений.
     *
     * @param npi   sourceAddrNpi
     */
    public void setSourceAddrNpi(NPI npi);

    /**
     * Устанавливает параметр sourceAddrNpi всех исходящих сообщений.
     *
     * @return   sourceAddrNpi
     */
    public NPI getSourceAddrNpi();

    /**
     * Устанавливает параметр destAddrTon всех исходящих сообщений.
     *
     * @param ton   destAddrTon
     */
    public void setDestAddrTon(TON ton);

    /**
     * Устанавливает параметр destAddrTon всех исходящих сообщений.
     *
     * @return   destAddrTon
     */
    public TON getDestAddrTon();

    /**
     * Устанавливает параметр destAddrNpi всех исходящих сообщений.
     *
     * @param npi   destAddrNpi
     */
    public void setDestAddrNpi(NPI npi);

    /**
     * Устанавливает параметр destAddrNpi всех исходящих сообщений.
     *
     * @return   destAddrNpi
     */
    public NPI getDestAddrNpi();

}
