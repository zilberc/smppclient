package org.bulatnig.smpp.pdu;

import org.bulatnig.smpp.pdu.tlv.*;
import org.bulatnig.smpp.pdu.udh.UDH;
import org.bulatnig.smpp.util.SmppByteBuffer;
import org.bulatnig.smpp.util.WrongLengthException;
import org.bulatnig.smpp.util.WrongParameterException;

import java.util.List;

/**
 * The deliver_sm is issued by the SMSC to send a message to an ESME. Using this
 * command,the SMSC may route a short message to the ESME for delivery.<br/>
 * <p/>
 * In addition the SMSC uses the deliver_sm operation to transfer the following
 * types of short messages to the ESME:-<br/> • SMSC Delivery Receipt. A
 * delivery receipt relating to a a message which had been previously submitted
 * with the submit_sm operation and the ESME had requested a delivery receipt
 * via the registered_delivery parameter. The delivery receipt data relating to
 * the original short message will be included in the short_message field of the
 * deliver_sm. (Reference Appendix B for an example Delivery Receipt format.)<br/> •
 * SME Delivery Acknowledgement. The user data of the SME delivery
 * acknowledgement is included in the short_message field of the deliver_sm<br/> •
 * SME Manual/User Acknowledgement. The user data of the SME delivery
 * acknowledgement is included in the short_message field of the deliver_sm<br/> •
 * Intermediate Notification.
 * <p/>
 * Realization note: during PDU parsing UDH fields translates to sar_*
 *
 * @author Bulat Nigmatullin
 */
public class DeliverSM extends PDU implements Responsable {

    /**
     * Максимальная длина serviceType поля.
     */
    private static final int MAX_SERVICETYPE_LENGTH = 5;
    /**
     * Максимальная длина sourceAddr и destinationAddr полей.
     */
    private static final int MAX_ADDRESS_LENGTH = 20;

    /**
     * The service_type parameter can be used to indicate the SMS Application
     * service associated with the message.
     */
    private String serviceType;
    /**
     * Type of Number for source address. If not known, set to NULL (Unknown).
     */
    private TON sourceAddrTon;
    /**
     * Numbering Plan Indicator for source. If not known, set to NULL (Unknown).
     */
    private NPI sourceAddrNpi;
    /**
     * Address of SME which originated this message. If not known, set to NULL
     * (Unknown).
     */
    private String sourceAddr;
    /**
     * Type of number of destination SME.
     */
    private TON destAddrTon;
    /**
     * Numbering Plan Indicator of destination SME.
     */
    private NPI destAddrNpi;
    /**
     * Destination address of destination SME.
     */
    private String destinationAddr;
    /**
     * Indicates Message Type and enhanced network services.
     */
    private SmscEsmClass esmClass;
    /**
     * Protocol Identifier. Network Specific Field.
     */
    private short protocolId;
    /**
     * Designates the priority level of the message.
     */
    private short priorityFlag;
    /**
     * This field is unused for deliver_sm. It must be set to NULL.
     */
    private String scheduleDeliveryTime;
    /**
     * This field is unused for deliver_sm It must be set to NULL.
     */
    private String validityPeriod;
    /**
     * Indicates if an ESME acknowledgement is required.
     */
    private short registeredDelivery;
    /**
     * Not used in deliver_sm. It must be set to NULL.
     */
    private short replaceIfPresentFlag;
    /**
     * Indicates the encoding scheme of the short message.
     */
    private short dataCoding;
    /**
     * Unused in deliver_sm. It must be set to NULL.
     */
    private short smDefaultMsgId;
    /**
     * Length of short message user data in octets.
     */
    private short smLength;
    /**
     * Up to 254 octets of short message user data.<br/>
     * <p/>
     * When sending messages longer than 254 octets the message_payload
     * parameter should be used and the sm_length parameter should be set to
     * zero.<br/>
     * <p/>
     * Note: The message data should be inserted in either the short_message or
     * the message_payload parameters. Both parameters must not be used
     * simultaneously.
     */
    private String shortMessage;

    private UDH udh;

    /**
     * A reference assigned by the originating SME to the message. In the case
     * that the deliver_sm is carrying an SMSC delivery receipt, an SME delivery
     * acknowledgement or an SME user acknowledgement (as indicated in the
     * esm_class field), the user_message_reference parameter is set to the
     * message reference of the original message.
     */
    private UserMessageReference userMessageReference;
    /**
     * Indicates the application port number associated with the source address
     * of the message. The parameter should be present for WAP applications.
     */
    private SourcePort sourcePort;
    /**
     * Indicates the application port number associated with the destination
     * address of the message. The parameter should be present for WAP
     * applications.
     */
    private DestinationPort destinationPort;
    /**
     * The reference number for a particular concatenated short message.
     */
    private SarMsgRefNum sarMsgRefNum;
    /**
     * Indicates the total number of short messages within the concatenated
     * short message.
     */
    private SarTotalSegments sarTotalSegments;
    /**
     * Indicates the sequence number of a particular short message fragment
     * within the concatenated short message.
     */
    private SarSegmentSeqnum sarSegmentSeqnum;
    /**
     * A user response code. The actual response codes are SMS application
     * specific.
     */
    private UserResponseCode userResponseCode;
    /**
     * Indicates a level of privacy associated with the message.
     */
    private PrivacyIndicator privacyIndicator;
    /**
     * Defines the type of payload (e.g. WDP, WCMP, etc.)
     */
    private PayloadType payloadType;
    /**
     * Contains the extended short message user data. Up to 64K octets can be
     * transmitted.<br/>
     * <p/>
     * Note: The short message data should be inserted in either the
     * short_message or message_payload fields. Both fields should not be used
     * simultaneously.<br/>
     * <p/>
     * The sm_length field should be set to zero if using the message_payload
     * parameter.
     */
    private MessagePayload messagePayload;
    /**
     * A callback number associated with the short message. This parameter can
     * be included a number of times for multiple call back addresses.
     */
    private CallbackNum callbackNum;
    /**
     * The subaddress of the message originator.
     */
    private SourceSubaddress sourceSubaddress;
    /**
     * The subaddress of the message destination.
     */
    private DestSubaddress destSubaddress;
    /**
     * Indicates the language of an alphanumeric text message.
     */
    private LanguageIndicator languageIndicator;
    /**
     * Session control information for Interactive Teleservice.
     */
    private ItsSessionInfo itsSessionInfo;
    /**
     * Network Error Code. May be present for Intermediate Notifications and
     * SMSC Delivery Receipts
     */
    private NetworkErrorCode networkErrorCode;
    /**
     * Message State. Should be present for SMSC Delivery Receipts and
     * Intermediate Notifications.
     */
    private MessageStateTlv messageStateTlv;
    /**
     * SMSC message ID of receipted message Should be present for SMSC Delivery
     * Receipts and Intermediate Notifications.
     */
    private ReceiptedMessageId receiptedMessageId;

    /**
     * Constructor.
     */
    public DeliverSM() {
        super(CommandId.DELIVER_SM);
    }

    /**
     * Constructor.
     *
     * @param bytes байткод PDU
     * @throws PDUException ошибка обработки PDU
     */
    public DeliverSM(final byte[] bytes) throws PDUException {
        super(bytes);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected final byte[] getBodyBytes() throws PDUException {
        SmppByteBuffer bb = new SmppByteBuffer();
        bb.appendString(shortMessage, getCharsetName(dataCoding));
        smLength = (short) bb.length();
        bb = new SmppByteBuffer();
        if (serviceType != null && serviceType.length() > MAX_SERVICETYPE_LENGTH) {
            throw new PDUException("serviceType field is too long");
        }
        bb.appendCString(serviceType);
        try {
            bb.appendByte(sourceAddrTon != null ? sourceAddrTon.getValue() : TON.UNKNOWN.getValue());
        } catch (
                WrongParameterException e) {
            throw new PDUException("sourceAddrTon field is invalid", e);
        }
        try {
            bb.appendByte(sourceAddrNpi != null ? sourceAddrNpi.getValue() : NPI.UNKNOWN.getValue());
        } catch (WrongParameterException e) {
            throw new PDUException("sourceAddrNpi field is invalid", e);
        }
        if (sourceAddr != null && sourceAddr.length() > MAX_ADDRESS_LENGTH) {
            throw new PDUException("sourceAddr field is too long");
        }
        bb.appendCString(sourceAddr);
        try {
            bb.appendByte(destAddrTon != null ? destAddrTon.getValue() : TON.UNKNOWN.getValue());
        } catch (WrongParameterException e) {
            throw new PDUException("destAddrTon field is invalid", e);
        }
        try {
            bb.appendByte(destAddrNpi != null ? destAddrNpi.getValue() : NPI.UNKNOWN.getValue());
        } catch (WrongParameterException e) {
            throw new PDUException("destAddrNpi field is invalid", e);
        }
        if (destinationAddr != null && destinationAddr.length() > MAX_ADDRESS_LENGTH) {
            throw new PDUException("destinationAddr field is too long");
        }
        bb.appendCString(destinationAddr);
        try {
            bb.appendByte(esmClass != null ? esmClass.getValue() : 0);
        } catch (WrongParameterException e) {
            throw new PDUException("esmClass field is invalid", e);
        }
        try {
            bb.appendByte(protocolId);
        } catch (WrongParameterException e) {
            throw new PDUException("protocolId field is invalid", e);
        }
        try {
            bb.appendByte(priorityFlag);
        } catch (WrongParameterException e) {
            throw new PDUException("priorityFlag field is invalid", e);
        }
        bb.appendCString(scheduleDeliveryTime);
        bb.appendCString(validityPeriod);
        try {
            bb.appendByte(registeredDelivery);
        } catch (WrongParameterException e) {
            throw new PDUException("registeredDelivery field is invalid", e);
        }
        try {
            bb.appendByte(replaceIfPresentFlag);
        } catch (WrongParameterException e) {
            throw new PDUException("replaceIfPresentFlag field is invalid", e);
        }
        try {
            bb.appendByte(dataCoding);
        } catch (WrongParameterException e) {
            throw new PDUException("dataCoding field is invalid", e);
        }
        try {
            bb.appendByte(smDefaultMsgId);
        } catch (WrongParameterException e) {
            throw new PDUException("smDefaultMsgId field is invalid", e);
        }
        try {
            bb.appendByte(smLength);
        } catch (WrongParameterException e) {
            throw new PDUException("smLength field is invalid", e);
        }
        bb.appendString(shortMessage, getCharsetName(dataCoding));
        try {
            if (userMessageReference != null) {
                bb.appendBytes(userMessageReference.getBytes());
            }
            if (sourcePort != null) {
                bb.appendBytes(sourcePort.getBytes());
            }
            if (destinationPort != null) {
                bb.appendBytes(destinationPort.getBytes());
            }
            if (sarMsgRefNum != null) {
                bb.appendBytes(sarMsgRefNum.getBytes());
            }
            if (sarTotalSegments != null) {
                bb.appendBytes(sarTotalSegments.getBytes());
            }
            if (sarSegmentSeqnum != null) {
                bb.appendBytes(sarSegmentSeqnum.getBytes());
            }
            if (payloadType != null) {
                bb.appendBytes(payloadType.getBytes());
            }
            if (messagePayload != null) {
                bb.appendBytes(messagePayload.getBytes());
            }
            if (privacyIndicator != null) {
                bb.appendBytes(privacyIndicator.getBytes());
            }
            if (callbackNum != null) {
                bb.appendBytes(callbackNum.getBytes());
            }
            if (sourceSubaddress != null) {
                bb.appendBytes(sourceSubaddress.getBytes());
            }
            if (destSubaddress != null) {
                bb.appendBytes(destSubaddress.getBytes());
            }
            if (userResponseCode != null) {
                bb.appendBytes(userResponseCode.getBytes());
            }
            if (languageIndicator != null) {
                bb.appendBytes(languageIndicator.getBytes());
            }
            if (itsSessionInfo != null) {
                bb.appendBytes(itsSessionInfo.getBytes());
            }
            if (networkErrorCode != null) {
                bb.appendBytes(networkErrorCode.getBytes());
            }
            if (messageStateTlv != null) {
                bb.appendBytes(messageStateTlv.getBytes());
            }
            if (receiptedMessageId != null) {
                bb.appendBytes(receiptedMessageId.getBytes());
            }
        } catch (TLVException e) {
            throw new PDUException("TLVs parsing failed", e);
        }
        return bb.array();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected final void parseBody(final byte[] bytes)
            throws PDUException {
        if (getCommandId() != CommandId.DELIVER_SM) {
            throw new ClassCastException();
        }
        SmppByteBuffer bb = new SmppByteBuffer(bytes);
        try {
            serviceType = bb.removeCString();
            if (serviceType.length() > MAX_SERVICETYPE_LENGTH) {
                throw new PDUException("serviceType field is too long");
            }
            short b = bb.removeByte();
            for (TON ton : TON.values()) {
                if (ton.getValue() == b) {
                    sourceAddrTon = ton;
                }
            }
            if (sourceAddrTon == null) {
                sourceAddrTon = TON.RESERVED;
            }
            b = bb.removeByte();
            for (NPI npi : NPI.values()) {
                if (npi.getValue() == b) {
                    sourceAddrNpi = npi;
                }
            }
            if (sourceAddrNpi == null) {
                sourceAddrNpi = NPI.RESERVED;
            }
            sourceAddr = bb.removeCString();
            if (sourceAddr.length() > MAX_ADDRESS_LENGTH) {
                throw new PDUException("sourceAddr field is too long");
            }
            b = bb.removeByte();
            for (TON ton : TON.values()) {
                if (ton.getValue() == b) {
                    destAddrTon = ton;
                }
            }
            if (destAddrTon == null) {
                destAddrTon = TON.RESERVED;
            }
            b = bb.removeByte();
            for (NPI npi : NPI.values()) {
                if (npi.getValue() == b) {
                    destAddrNpi = npi;
                }
            }
            if (destAddrNpi == null) {
                destAddrNpi = NPI.RESERVED;
            }
            destinationAddr = bb.removeCString();
            if (destinationAddr.length() > MAX_ADDRESS_LENGTH) {
                throw new PDUException("destinationAddr field is too long");
            }
            esmClass = new SmscEsmClass(bb.removeByte());
            protocolId = bb.removeByte();
            priorityFlag = bb.removeByte();
            scheduleDeliveryTime = bb.removeCString();
            validityPeriod = bb.removeCString();
            registeredDelivery = bb.removeByte();
            replaceIfPresentFlag = bb.removeByte();
            dataCoding = bb.removeByte();
            smDefaultMsgId = bb.removeByte();
            smLength = bb.removeByte();
            if (smLength > 0 && SmscEsmClass.SmscGSMFeatures.UDHI_INDICATOR == esmClass.getFeatures()) {
                udh = parseUDH(bb);
                smLength = (short) (smLength - udh.length());
            }
            shortMessage = bb.removeString(smLength, getCharsetName(dataCoding));
        } catch (WrongLengthException e) {
            throw new PDUException("PDU parsing error", e);
        }
        if (bb.length() > 0) {
            List<TLV> list = getOptionalParams(bb.array(), esmClass, dataCoding);
            for (TLV tlv : list) {
                switch (tlv.getTag()) {
                    case USER_MESSAGE_REFERENCE:
                        userMessageReference = (UserMessageReference) tlv;
                        break;
                    case SOURCE_PORT:
                        sourcePort = (SourcePort) tlv;
                        break;
                    case DESTINATION_PORT:
                        destinationPort = (DestinationPort) tlv;
                        break;
                    case SAR_MSG_REF_NUM:
                        sarMsgRefNum = (SarMsgRefNum) tlv;
                        break;
                    case SAR_TOTAL_SEGMENTS:
                        sarTotalSegments = (SarTotalSegments) tlv;
                        break;
                    case SAR_SEGMENT_SEQNUM:
                        sarSegmentSeqnum = (SarSegmentSeqnum) tlv;
                        break;
                    case PAYLOAD_TYPE:
                        payloadType = (PayloadType) tlv;
                        break;
                    case MESSAGE_PAYLOAD:
                        messagePayload = (MessagePayload) tlv;
                        break;
                    case PRIVACY_INDICATOR:
                        privacyIndicator = (PrivacyIndicator) tlv;
                        break;
                    case CALLBACK_NUM:
                        callbackNum = (CallbackNum) tlv;
                        break;
                    case SOURCE_SUBADDRESS:
                        sourceSubaddress = (SourceSubaddress) tlv;
                        break;
                    case DEST_SUBADDRESS:
                        destSubaddress = (DestSubaddress) tlv;
                        break;
                    case USER_RESPONSE_CODE:
                        userResponseCode = (UserResponseCode) tlv;
                        break;
                    case LANGUAGE_INDICATOR:
                        languageIndicator = (LanguageIndicator) tlv;
                        break;
                    case ITS_SESSION_INFO:
                        itsSessionInfo = (ItsSessionInfo) tlv;
                        break;
                    case NETWORK_ERROR_CODE:
                        networkErrorCode = (NetworkErrorCode) tlv;
                        break;
                    case MESSAGE_STATE:
                        messageStateTlv = (MessageStateTlv) tlv;
                        break;
                    case RECEIPTED_MESSAGE_ID:
                        receiptedMessageId = (ReceiptedMessageId) tlv;
                        break;
                    default:
                        break;
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public final DeliverSMResp getResponse() {
        DeliverSMResp resp = new DeliverSMResp();
        resp.setSequenceNumber(getSequenceNumber());
        return resp;
    }

    /**
     * @return indicate the SMS Application service associated with the message
     */
    public final String getServiceType() {
        return serviceType;
    }

    /**
     * @param serviceType indicate the SMS Application service associated with the
     *                    message
     */
    public final void setServiceType(final String serviceType) {
        this.serviceType = serviceType;
    }

    /**
     * @return Type of Number for source address
     */
    public final TON getSourceAddrTon() {
        return sourceAddrTon;
    }

    /**
     * @param sourceAddrTon Type of Number for source address
     */
    public final void setSourceAddrTon(final TON sourceAddrTon) {
        this.sourceAddrTon = sourceAddrTon;
    }

    /**
     * @return Numbering Plan Indicator for source
     */
    public final NPI getSourceAddrNpi() {
        return sourceAddrNpi;
    }

    /**
     * @param sourceAddrNpi Numbering Plan Indicator for source
     */
    public final void setSourceAddrNpi(final NPI sourceAddrNpi) {
        this.sourceAddrNpi = sourceAddrNpi;
    }

    /**
     * @return address of SME which originated this message
     */
    public final String getSourceAddr() {
        return sourceAddr;
    }

    /**
     * @param sourceAddr address of SME which originated this message
     */
    public final void setSourceAddr(final String sourceAddr) {
        this.sourceAddr = sourceAddr;
    }

    /**
     * @return Type of number of destination SME
     */
    public final TON getDestAddrTon() {
        return destAddrTon;
    }

    /**
     * @param destAddrTon Type of number of destination SME
     */
    public final void setDestAddrTon(final TON destAddrTon) {
        this.destAddrTon = destAddrTon;
    }

    /**
     * @return Numbering Plan Indicator of destination SME
     */
    public final NPI getDestAddrNpi() {
        return destAddrNpi;
    }

    /**
     * @param destAddrNpi Numbering Plan Indicator of destination SME
     */
    public final void setDestAddrNpi(final NPI destAddrNpi) {
        this.destAddrNpi = destAddrNpi;
    }

    /**
     * @return destination address of destination SME
     */
    public final String getDestinationAddr() {
        return destinationAddr;
    }

    /**
     * @param destinationAddr destination address of destination SME
     */
    public final void setDestinationAddr(final String destinationAddr) {
        this.destinationAddr = destinationAddr;
    }

    /**
     * @return Message Type
     */
    public final SmscEsmClass getEsmClass() {
        return esmClass;
    }

    /**
     * @param esmClass Message Type
     */
    public final void setEsmClass(final SmscEsmClass esmClass) {
        this.esmClass = esmClass;
    }

    /**
     * @return Protocol Identifier
     */
    public final short getProtocolId() {
        return protocolId;
    }

    /**
     * @param protocolId Protocol Identifier
     */
    public final void setProtocolId(final short protocolId) {
        this.protocolId = protocolId;
    }

    /**
     * @return the priority level of the message
     */
    public final short getPriorityFlag() {
        return priorityFlag;
    }

    /**
     * @param priorityFlag the priority level of the message
     */
    public final void setPriorityFlag(final short priorityFlag) {
        this.priorityFlag = priorityFlag;
    }

    /**
     * @return field is unused
     */
    public final String getScheduleDeliveryTime() {
        return scheduleDeliveryTime;
    }

    /**
     * @return field is unused
     */
    public final String getValidityPeriod() {
        return validityPeriod;
    }

    /**
     * @return indicator if an ESME acknowledgement is required
     */
    public final short getRegisteredDelivery() {
        return registeredDelivery;
    }

    /**
     * @param registeredDelivery indicator if an ESME acknowledgement is required
     */
    public final void setRegisteredDelivery(final short registeredDelivery) {
        this.registeredDelivery = registeredDelivery;
    }

    /**
     * @return field is unused
     */
    public final short getReplaceIfPresentFlag() {
        return replaceIfPresentFlag;
    }

    /**
     * @return indicator the encoding scheme of the short message
     */
    public final short getDataCoding() {
        return dataCoding;
    }

    /**
     * @param dataCoding indicator the encoding scheme of the short message
     */
    public final void setDataCoding(final short dataCoding) {
        this.dataCoding = dataCoding;
    }

    /**
     * @return field is unused
     */
    public final short getSmDefaultMsgId() {
        return smDefaultMsgId;
    }

    /**
     * @return length of short message user data in octets
     */
    public final short getSmLength() {
        return smLength;
    }

    /**
     * @param smLength length of short message user data in octets
     */
    public final void setSmLength(final short smLength) {
        this.smLength = smLength;
    }

    /**
     * @return short message user data
     */
    public final String getShortMessage() {
        return shortMessage;
    }

    /**
     * @param shortMessage short message user data
     */
    public final void setShortMessage(final String shortMessage) {
        this.shortMessage = shortMessage;
    }

    public UDH getUdh() {
        return udh;
    }

    /**
     * @return reference assigned by the originating SME to the message
     */
    public final UserMessageReference getUserMessageReference() {
        return userMessageReference;
    }

    /**
     * @param userMessageReference reference assigned by the originating SME to the message
     */
    public final void setUserMessageReference(
            final UserMessageReference userMessageReference) {
        this.userMessageReference = userMessageReference;
    }

    /**
     * @return application port number associated with the source address of the
     *         message
     */
    public final SourcePort getSourcePort() {
        return sourcePort;
    }

    /**
     * @param sourcePort application port number associated with the source address of
     *                   the message
     */
    public final void setSourcePort(final SourcePort sourcePort) {
        this.sourcePort = sourcePort;
    }

    /**
     * @return application port number associated with the destination address
     *         of the message
     */
    public final DestinationPort getDestinationPort() {
        return destinationPort;
    }

    /**
     * @param destinationPort application port number associated with the destination
     *                        address of the message
     */
    public final void setDestinationPort(final DestinationPort destinationPort) {
        this.destinationPort = destinationPort;
    }

    /**
     * @return the reference number for a particular concatenated short message
     */
    public final SarMsgRefNum getSarMsgRefNum() {
        return sarMsgRefNum;
    }

    /**
     * @param sarMsgRefNum the reference number for a particular concatenated short
     *                     message
     */
    public final void setSarMsgRefNum(final SarMsgRefNum sarMsgRefNum) {
        this.sarMsgRefNum = sarMsgRefNum;
    }

    /**
     * @return the total number of short messages within the concatenated short
     *         message
     */
    public final SarTotalSegments getSarTotalSegments() {
        return sarTotalSegments;
    }

    /**
     * @param sarTotalSegments the total number of short messages within the concatenated
     *                         short message
     */
    public final void setSarTotalSegments(
            final SarTotalSegments sarTotalSegments) {
        this.sarTotalSegments = sarTotalSegments;
    }

    /**
     * @return the sequence number of a particular short message fragment within
     *         the concatenated short message
     */
    public final SarSegmentSeqnum getSarSegmentSeqnum() {
        return sarSegmentSeqnum;
    }

    /**
     * @param sarSegmentSeqnum the sequence number of a particular short message fragment
     *                         within the concatenated short message
     */
    public final void setSarSegmentSeqnum(
            final SarSegmentSeqnum sarSegmentSeqnum) {
        this.sarSegmentSeqnum = sarSegmentSeqnum;
    }

    /**
     * @return user response code
     */
    public final UserResponseCode getUserResponseCode() {
        return userResponseCode;
    }

    /**
     * @param userResponseCode user response code
     */
    public final void setUserResponseCode(
            final UserResponseCode userResponseCode) {
        this.userResponseCode = userResponseCode;
    }

    /**
     * @return level of privacy associated with the message
     */
    public final PrivacyIndicator getPrivacyIndicator() {
        return privacyIndicator;
    }

    /**
     * @param privacyIndicator level of privacy associated with the message
     */
    public final void setPrivacyIndicator(
            final PrivacyIndicator privacyIndicator) {
        this.privacyIndicator = privacyIndicator;
    }

    /**
     * @return the type of payload
     */
    public final PayloadType getPayloadType() {
        return payloadType;
    }

    /**
     * @param payloadType the type of payload
     */
    public final void setPayloadType(final PayloadType payloadType) {
        this.payloadType = payloadType;
    }

    /**
     * @return the extended short message user data
     */
    public final MessagePayload getMessagePayload() {
        return messagePayload;
    }

    /**
     * @param messagePayload the extended short message user data
     */
    public final void setMessagePayload(final MessagePayload messagePayload) {
        this.messagePayload = messagePayload;
    }

    /**
     * @return callback number associated with the short message
     */
    public final CallbackNum getCallbackNum() {
        return callbackNum;
    }

    /**
     * @param callbackNum callback number associated with the short message
     */
    public final void setCallbackNum(final CallbackNum callbackNum) {
        this.callbackNum = callbackNum;
    }

    /**
     * @return the subaddress of the message originator
     */
    public final SourceSubaddress getSourceSubaddress() {
        return sourceSubaddress;
    }

    /**
     * @param sourceSubaddress the subaddress of the message originator
     */
    public final void setSourceSubaddress(
            final SourceSubaddress sourceSubaddress) {
        this.sourceSubaddress = sourceSubaddress;
    }

    /**
     * @return the subaddress of the message destination
     */
    public final DestSubaddress getDestSubaddress() {
        return destSubaddress;
    }

    /**
     * @param destSubaddress the subaddress of the message destination
     */
    public final void setDestSubaddress(final DestSubaddress destSubaddress) {
        this.destSubaddress = destSubaddress;
    }

    /**
     * @return the language of an alphanumeric text message
     */
    public final LanguageIndicator getLanguageIndicator() {
        return languageIndicator;
    }

    /**
     * @param languageIndicator the language of an alphanumeric text message
     */
    public final void setLanguageIndicator(
            final LanguageIndicator languageIndicator) {
        this.languageIndicator = languageIndicator;
    }

    /**
     * @return session control information for Interactive Teleservice
     */
    public final ItsSessionInfo getItsSessionInfo() {
        return itsSessionInfo;
    }

    /**
     * @param itsSessionInfo session control information for Interactive Teleservice
     */
    public final void setItsSessionInfo(final ItsSessionInfo itsSessionInfo) {
        this.itsSessionInfo = itsSessionInfo;
    }

    /**
     * @return the network error code
     */
    public final NetworkErrorCode getNetworkErrorCode() {
        return networkErrorCode;
    }

    /**
     * @param networkErrorCode the network error code
     */
    public final void setNetworkErrorCode(
            final NetworkErrorCode networkErrorCode) {
        this.networkErrorCode = networkErrorCode;
    }

    /**
     * @return the message state
     */
    public final MessageStateTlv getMessageStateTlv() {
        return messageStateTlv;
    }

    /**
     * @param messageStateTlv the message state
     */
    public final void setMessageStateTlv(final MessageStateTlv messageStateTlv) {
        this.messageStateTlv = messageStateTlv;
    }

    /**
     * @return SMSC message ID of receipted message
     */
    public final ReceiptedMessageId getReceiptedMessageId() {
        return receiptedMessageId;
    }

    /**
     * @param receiptedMessageId SMSC message ID of receipted message
     */
    public final void setReceiptedMessageId(
            final ReceiptedMessageId receiptedMessageId) {
        this.receiptedMessageId = receiptedMessageId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String toString() {
        return getClass().getName() + " Object {" + "\nserviceType : "
                + serviceType + "\nsourceAddrTon : " + sourceAddrTon
                + "\nsourceAddrNpi : " + sourceAddrNpi + "\nsourceAddr : "
                + sourceAddr + "\ndestAddrTon : " + destAddrTon
                + "\ndestAddrNpi : " + destAddrNpi + "\ndestinationAddr : "
                + destinationAddr + "\nesmClass : " + esmClass
                + "\nprotocolId : " + protocolId + "\npriorityFlag : "
                + priorityFlag + "\nscheduleDeliveryTime : "
                + scheduleDeliveryTime + "\nvalidityPeriod : " + validityPeriod
                + "\nregisteredDelivery : " + registeredDelivery
                + "\nreplaceIfPresentFlag : " + replaceIfPresentFlag
                + "\ndataCoding : " + dataCoding + "\nsmDefaultMsgId : "
                + smDefaultMsgId + "\nsmLength : " + smLength
                + "\nshortMessage : " + shortMessage
                + "\nuserMessageReference : " + userMessageReference
                + "\nsourcePort : " + sourcePort + "\ndestinationPort : "
                + destinationPort + "\nsarMsgRefNum : " + sarMsgRefNum
                + "\nsarTotalSegments : " + sarTotalSegments
                + "\nsarSegmentSeqnum : " + sarSegmentSeqnum
                + "\nuserResponseCode : " + userResponseCode
                + "\nprivacyIndicator : " + privacyIndicator
                + "\npayloadType : " + payloadType + "\nmessagePayload : "
                + messagePayload + "\ncallbackNum : " + callbackNum
                + "\nsourceSubaddress : " + sourceSubaddress
                + "\ndestSubaddress : " + destSubaddress
                + "\nlanguageIndicator : " + languageIndicator
                + "\nitsSessionInfo : " + itsSessionInfo
                + "\nnetworkErrorCode : " + networkErrorCode
                + "\nmessageStateTlv : " + messageStateTlv
                + "\nreceiptedMessageId : " + receiptedMessageId + "\n}";
    }

}
