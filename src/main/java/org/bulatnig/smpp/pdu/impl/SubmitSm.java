package org.bulatnig.smpp.pdu.impl;

import org.bulatnig.smpp.pdu.CommandId;
import org.bulatnig.smpp.pdu.PduParsingException;
import org.bulatnig.smpp.util.ByteBuffer;
import org.bulatnig.smpp.util.TerminatingNullNotFoundException;

/**
 * This operation is used by an ESME to submit a short message to the SMSC for
 * onward transmission to a specified short message entity (SME). The submit_sm
 * PDU does not support the transaction message mode.
 *
 * @author Bulat Nigmatullin
 */
public class SubmitSm extends AbstractPdu {

    /**
     * The service_type parameter can be used to indicate the SMS Application
     * service associated with the message. Specifying the service_type allows
     * the ESME to • avail of enhanced messaging services such as “replace by
     * service” type • to control the teleservice used on the air interface. Set
     * to NULL for default SMSC settings.
     */
    private String serviceType;
    /**
     * Type of Number for source address. If not known, set to NULL (Unknown).
     */
    private int sourceAddrTon;
    /**
     * Numbering Plan Indicator for source address. If not known, set to NULL
     * (Unknown).
     */
    private int sourceAddrNpi;
    /**
     * Address of SME which originated this message. If not known, set to NULL
     * (Unknown).
     */
    private String sourceAddr;
    /**
     * Type of Number for destination.
     */
    private int destAddrTon;
    /**
     * Numbering Plan Indicator for destination.
     */
    private int destAddrNpi;
    /**
     * Destination address of this short message. For mobile terminated
     * messages, this is the directory number of the recipient MS.
     */
    private String destinationAddr;
    /**
     * Indicates Message Mode & Message Type.
     */
    private int esmClass;
    /**
     * Protocol Identifier. Network specific field.
     */
    private int protocolId;
    /**
     * Designates the priority level of the message.
     */
    private int priorityFlag;
    /**
     * The short message is to be scheduled by the SMSC for delivery. Set to
     * NULL for immediate message delivery.
     */
    private String scheduleDeliveryTime;
    /**
     * The validity period of this message. Set to NULL to request the SMSC
     * default validity period.
     */
    private String validityPeriod;
    /**
     * Indicator to signify if an SMSC delivery receipt or an SME
     * acknowledgement is required.
     */
    private int registeredDelivery;
    /**
     * Flag indicating if submitted message should replace an existing message.
     */
    private int replaceIfPresentFlag;
    /**
     * Defines the encoding scheme of the short message user data.
     */
    private int dataCoding;
    /**
     * Indicates the short message to send from a list of predefined (‘canned’)
     * short messages stored on the SMSC. If not using an SMSC canned message,
     * set to NULL.
     */
    private int smDefaultMsgId;
    /**
     * Length in octets of the short_message user data.
     */
    private int smLength;
    /**
     * Up to 254 octets of short message user data. The exact physical limit for
     * short_message size may vary according to the underlying network.<br/>
     * <p/>
     * Applications which need to send messages longer than 254 octets should
     * use the message_payload parameter. In this case the sm_length field
     * should be set to zero.<br/>
     * <p/>
     * Note: The short message data should be inserted in either the
     * short_message or message_payload fields. Both fields must not be used
     * simultaneously.
     */
    private byte[] shortMessage;

    protected SubmitSm() {
        super(CommandId.SUBMIT_SM);
    }

    protected SubmitSm(ByteBuffer bb) throws PduParsingException {
        super(bb);
        try {
            serviceType = bb.removeCString();
        } catch (TerminatingNullNotFoundException e) {
            throw new PduParsingException("Service type parsing failed.", e);
        }
        sourceAddrTon = bb.removeByte();
        sourceAddrNpi = bb.removeByte();
        try {
            sourceAddr = bb.removeCString();
        } catch (TerminatingNullNotFoundException e) {
            throw new PduParsingException("Service type parsing failed.", e);
        }
        destAddrTon = bb.removeByte();
        destAddrNpi = bb.removeByte();
        try {
            destinationAddr = bb.removeCString();
        } catch (TerminatingNullNotFoundException e) {
            throw new PduParsingException("Service type parsing failed.", e);
        }
        esmClass = bb.removeByte();
        protocolId = bb.removeByte();
        priorityFlag = bb.removeByte();
        try {
            scheduleDeliveryTime = bb.removeCString();
        } catch (TerminatingNullNotFoundException e) {
            throw new PduParsingException("Service type parsing failed.", e);
        }
        try {
            validityPeriod = bb.removeCString();
        } catch (TerminatingNullNotFoundException e) {
            throw new PduParsingException("Service type parsing failed.", e);
        }
        registeredDelivery = bb.removeByte();
        replaceIfPresentFlag = bb.removeByte();
        dataCoding = bb.removeByte();
        smDefaultMsgId = bb.removeByte();
        smLength = bb.removeByte();
        shortMessage = bb.removeBytes(smLength);
    }

    @Override
    protected ByteBuffer body() throws PduParsingException {
        return null;
    }

}
