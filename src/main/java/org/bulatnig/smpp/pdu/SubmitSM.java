package org.bulatnig.smpp.pdu;

import org.bulatnig.smpp.pdu.tlv.*;
import org.bulatnig.smpp.util.SmppByteBuffer;
import org.bulatnig.smpp.util.WrongLengthException;
import org.bulatnig.smpp.util.WrongParameterException;

import java.util.List;

/**
 * This operation is used by an ESME to submit a short message to the SMSC for
 * onward transmission to a specified short message entity (SME). The submit_sm
 * PDU does not support the transaction message mode.
 *
 * @author Bulat Nigmatullin
 */
public class SubmitSM extends PDU implements Responsable {
    /**
     * Максимальная длина serviceType поля.
     */
    private static final int MAX_SERVICETYPE_LENGTH = 5;
    /**
     * Максимальная длина sourceAddr и destinationAddr полей.
     */
    private static final int MAX_ADDRESS_LENGTH = 20;
    /**
     * Максимальная длина serviceType поля.
     */
    private static final int SCHEDULEDELIVERYTIME_LENGTH = 16;
    /**
     * Максимальная длина serviceType поля.
     */
    private static final int VALIDITYPERIOD_LENGTH = 16;

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
    private TON sourceAddrTon;
    /**
     * Numbering Plan Indicator for source address. If not known, set to NULL
     * (Unknown).
     */
    private NPI sourceAddrNpi;
    /**
     * Address of SME which originated this message. If not known, set to NULL
     * (Unknown).
     */
    private String sourceAddr;
    /**
     * Type of Number for destination.
     */
    private TON destAddrTon;
    /**
     * Numbering Plan Indicator for destination.
     */
    private NPI destAddrNpi;
    /**
     * Destination address of this short message. For mobile terminated
     * messages, this is the directory number of the recipient MS.
     */
    private String destinationAddr;
    /**
     * Indicates Message Mode & Message Type.
     */
    private EsmeEsmClass esmClass;
    /**
     * Protocol Identifier. Network specific field.
     */
    private short protocolId;
    /**
     * Designates the priority level of the message.
     */
    private short priorityFlag;
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
    private short registeredDelivery;
    /**
     * Flag indicating if submitted message should replace an existing message.
     */
    private short replaceIfPresentFlag;
    /**
     * Defines the encoding scheme of the short message user data.
     */
    private short dataCoding;
    /**
     * Indicates the short message to send from a list of predefined (‘canned’)
     * short messages stored on the SMSC. If not using an SMSC canned message,
     * set to NULL.
     */
    private short smDefaultMsgId;
    /**
     * Length in octets of the short_message user data.
     */
    private short smLength;
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
    private String shortMessage;

    /**
     * ESME assigned message reference number.
     */
    private UserMessageReference userMessageReference;
    /**
     * Indicates the application port number associated with the source address
     * of the message. This parameter should be present for WAP applications.
     */
    private SourcePort sourcePort;
    /**
     * The subcomponent in the destination device which created the user data.
     */
    private SourceAddrSubunit sourceAddrSubunit;
    /**
     * Indicates the application port number associated with the destination
     * address of the message. This parameter should be present for WAP
     * applications.
     */
    private DestinationPort destinationPort;
    /**
     * The subcomponent in the destination device for which the user data is
     * intended.
     */
    private DestAddrSubunit destAddrSubunit;
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
     * Indicates that there are more messages to follow for the destination SME.
     */
    private MoreMessagesToSend moreMessagesToSend;
    /**
     * Defines the type of payload (e.g. WDP, WCMP, etc.).
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
     * Indicates the level of privacy associated with the message.
     */
    private PrivacyIndicator privacyIndicator;
    /**
     * A callback number associated with the short message. This parameter can
     * be included a number of times for multiple callback addresses.
     */
    private CallbackNum callbackNum;
    /**
     * Defines the callback number presentation and screening. If this parameter
     * is present and there are multiple instances of the callback_num parameter
     * then this parameter must occur an equal number of instances and the order
     * of occurrence determines the particular callback_num_pres_ind which
     * corresponds to a particular callback_num.
     */
    private CallbackNumPresInd callbackNumPresInd;
    /**
     * Associates a displayable alphanumeric tag with the callback number. If
     * this parameter is present and there are multiple instances of the
     * callback_num parameter then this parameter must occur an equal number of
     * instances and the order of occurrence determines the particular
     * callback_num_atag which corresponds to a particular callback_num.
     */
    private CallbackNumAtag callbackNumAtag;
    /**
     * The subaddress of the message originator.
     */
    private SourceSubaddress sourceSubaddress;
    /**
     * The subaddress of the message destination.
     */
    private DestSubaddress destSubaddress;
    /**
     * A user response code. The actual response codes are implementation
     * specific.
     */
    private UserResponseCode userResponseCode;
    /**
     * Provides the receiving MS with a display time associated with the
     * message.
     */
    private DisplayTime displayTime;
    /**
     * Indicates the alerting mechanism when the message is received by an MS.
     */
    private SmsSignal smsSignal;
    /**
     * Indicates validity information for this message to the recipient MS.
     */
    private MsValidity msValidity;
    /**
     * This parameter controls the indication and specifies the message type (of
     * the message associated with the MWI) at the mobile station.
     */
    private MsMsgWaitFacilities msMsgWaitFacilities;
    /**
     * Indicates the number of messages stored in a mail box.
     */
    private NumberOfMessages numberOfMessages;
    /**
     * Request an MS alert signal be invoked on message delivery.
     */
    private AlertOnMessageDelivery alertOnMessageDelivery;
    /**
     * Indicates the language of an alphanumeric text message.
     */
    private LanguageIndicator languageIndicator;
    /**
     * The MS user’s reply method to an SMS delivery message received from the
     * network is indicated and controlled by this parameter.
     */
    private ItsReplyType itsReplyType;
    /**
     * Session control information for Interactive Teleservice.
     */
    private ItsSessionInfo itsSessionInfo;
    /**
     * This parameter is used to identify the required USSD Service type when
     * interfacing to a USSD system.
     */
    private UssdServiceOp ussdServiceOp;

    /**
     * Constructor.
     */
    public SubmitSM() {
        super(CommandId.SUBMIT_SM);
    }

    /**
     * Constructor.
     *
     * @param bytes байткод PDU
     * @throws PDUException ошибка обработки PDU
     */
    public SubmitSM(final byte[] bytes) throws PDUException {
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
        } catch (WrongParameterException e) {
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
        if (scheduleDeliveryTime != null && scheduleDeliveryTime.length() != SCHEDULEDELIVERYTIME_LENGTH) {
            throw new PDUException("scheduleDeliveryTime field is invalid");
        }
        bb.appendCString(scheduleDeliveryTime);
        if (validityPeriod != null && validityPeriod.length() != VALIDITYPERIOD_LENGTH) {
            throw new PDUException("validityPeriod field is invalid");
        }
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
        } catch (
                WrongParameterException e) {
            throw new PDUException("smLength field is invalid", e);
        }
        bb.appendString(shortMessage, getCharsetName(dataCoding));
        try {
            if (userMessageReference != null) {
                bb.appendBytes(userMessageReference.getBytes(),
                        userMessageReference.getBytes().length);
            }
            if (sourcePort != null) {
                bb.appendBytes(sourcePort.getBytes(), sourcePort.getBytes().length);
            }
            if (sourceAddrSubunit != null) {
                bb.appendBytes(sourceAddrSubunit.getBytes(), sourceAddrSubunit
                        .getBytes().length);
            }
            if (destinationPort != null) {
                bb.appendBytes(destinationPort.getBytes(), destinationPort
                        .getBytes().length);
            }
            if (destAddrSubunit != null) {
                bb.appendBytes(destAddrSubunit.getBytes(), destAddrSubunit
                        .getBytes().length);
            }
            if (sarMsgRefNum != null) {
                bb.appendBytes(sarMsgRefNum.getBytes(),
                        sarMsgRefNum.getBytes().length);
            }
            if (sarTotalSegments != null) {
                bb.appendBytes(sarTotalSegments.getBytes(), sarTotalSegments
                        .getBytes().length);
            }
            if (sarSegmentSeqnum != null) {
                bb.appendBytes(sarSegmentSeqnum.getBytes(), sarSegmentSeqnum
                        .getBytes().length);
            }
            if (moreMessagesToSend != null) {
                bb.appendBytes(moreMessagesToSend.getBytes(), moreMessagesToSend
                        .getBytes().length);
            }
            if (payloadType != null) {
                bb.appendBytes(payloadType.getBytes(),
                        payloadType.getBytes().length);
            }
            if (messagePayload != null) {
                bb.appendBytes(messagePayload.getBytes(),
                        messagePayload.getBytes().length);
            }
            if (privacyIndicator != null) {
                bb.appendBytes(privacyIndicator.getBytes(), privacyIndicator
                        .getBytes().length);
            }
            if (callbackNum != null) {
                bb.appendBytes(callbackNum.getBytes(),
                        callbackNum.getBytes().length);
            }
            if (callbackNumPresInd != null) {
                bb.appendBytes(callbackNumPresInd.getBytes(), callbackNumPresInd
                        .getBytes().length);
            }
            if (callbackNumAtag != null) {
                bb.appendBytes(callbackNumAtag.getBytes(), callbackNumAtag
                        .getBytes().length);
            }
            if (sourceSubaddress != null) {
                bb.appendBytes(sourceSubaddress.getBytes(), sourceSubaddress
                        .getBytes().length);
            }
            if (destSubaddress != null) {
                bb.appendBytes(destSubaddress.getBytes(),
                        destSubaddress.getBytes().length);
            }
            if (userResponseCode != null) {
                bb.appendBytes(userResponseCode.getBytes(), userResponseCode
                        .getBytes().length);
            }
            if (displayTime != null) {
                bb.appendBytes(displayTime.getBytes(),
                        displayTime.getBytes().length);
            }
            if (smsSignal != null) {
                bb.appendBytes(smsSignal.getBytes(), smsSignal.getBytes().length);
            }
            if (msValidity != null) {
                bb.appendBytes(msValidity.getBytes(), msValidity.getBytes().length);
            }
            if (msMsgWaitFacilities != null) {
                bb.appendBytes(msMsgWaitFacilities.getBytes(), msMsgWaitFacilities
                        .getBytes().length);
            }
            if (numberOfMessages != null) {
                bb.appendBytes(numberOfMessages.getBytes(), numberOfMessages
                        .getBytes().length);
            }
            if (alertOnMessageDelivery != null) {
                bb.appendBytes(alertOnMessageDelivery.getBytes(),
                        alertOnMessageDelivery.getBytes().length);
            }
            if (languageIndicator != null) {
                bb.appendBytes(languageIndicator.getBytes(), languageIndicator
                        .getBytes().length);
            }
            if (itsReplyType != null) {
                bb.appendBytes(itsReplyType.getBytes(),
                        itsReplyType.getBytes().length);
            }
            if (itsSessionInfo != null) {
                bb.appendBytes(itsSessionInfo.getBytes(),
                        itsSessionInfo.getBytes().length);
            }
            if (ussdServiceOp != null) {
                bb.appendBytes(ussdServiceOp.getBytes(),
                        ussdServiceOp.getBytes().length);
            }
        } catch (TLVException e) {
            throw new PDUException("TLVs parsing failed", e);
        }
        return bb.getBuffer();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected final void parseBody(final byte[] bytes)
            throws PDUException {
        if (getCommandId() != CommandId.SUBMIT_SM) {
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
            esmClass = new EsmeEsmClass(bb.removeByte());
            protocolId = bb.removeByte();
            priorityFlag = bb.removeByte();
            scheduleDeliveryTime = bb.removeCString();
            if (scheduleDeliveryTime.length() > 0 && scheduleDeliveryTime.length() != SCHEDULEDELIVERYTIME_LENGTH) {
                throw new PDUException("scheduleDeliveryTime field is invalid");
            }
            validityPeriod = bb.removeCString();
            if (validityPeriod.length() > 0 && validityPeriod.length() != VALIDITYPERIOD_LENGTH) {
                throw new PDUException("validityPeriod field is invalid");
            }
            registeredDelivery = bb.removeByte();
            replaceIfPresentFlag = bb.removeByte();
            dataCoding = bb.removeByte();
            smDefaultMsgId = bb.removeByte();
            smLength = bb.removeByte();
            shortMessage = bb.removeString(smLength, getCharsetName(dataCoding));
        } catch (WrongLengthException e) {
            throw new PDUException("PDU parsing error", e);
        }
        if (bb.length() > 0) {
            List<TLV> list = getOptionalParams(bb.getBuffer(), esmClass, dataCoding);
            for (TLV tlv : list) {
                switch (tlv.getTag()) {
                    case USER_MESSAGE_REFERENCE:
                        userMessageReference = (UserMessageReference) tlv;
                        break;
                    case SOURCE_PORT:
                        sourcePort = (SourcePort) tlv;
                        break;
                    case SOURCE_ADDR_SUBUNIT:
                        sourceAddrSubunit = (SourceAddrSubunit) tlv;
                        break;
                    case DESTINATION_PORT:
                        destinationPort = (DestinationPort) tlv;
                        break;
                    case DEST_ADDR_SUBUNIT:
                        destAddrSubunit = (DestAddrSubunit) tlv;
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
                    case MORE_MESSAGES_TO_SEND:
                        moreMessagesToSend = (MoreMessagesToSend) tlv;
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
                    case CALLBACK_NUM_PRES_IND:
                        callbackNumPresInd = (CallbackNumPresInd) tlv;
                        break;
                    case CALLBACK_NUM_ATAG:
                        callbackNumAtag = (CallbackNumAtag) tlv;
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
                    case DISPLAY_TIME:
                        displayTime = (DisplayTime) tlv;
                        break;
                    case SMS_SIGNAL:
                        smsSignal = (SmsSignal) tlv;
                        break;
                    case MS_VALIDITY:
                        msValidity = (MsValidity) tlv;
                        break;
                    case MS_MSG_WAIT_FACILITIES:
                        msMsgWaitFacilities = (MsMsgWaitFacilities) tlv;
                        break;
                    case NUMBER_OF_MESSAGES:
                        numberOfMessages = (NumberOfMessages) tlv;
                        break;
                    case ALERT_ON_MESSAGE_DELIVERY:
                        alertOnMessageDelivery = (AlertOnMessageDelivery) tlv;
                        break;
                    case LANGUAGE_INDICATOR:
                        languageIndicator = (LanguageIndicator) tlv;
                        break;
                    case ITS_REPLY_TYPE:
                        itsReplyType = (ItsReplyType) tlv;
                        break;
                    case ITS_SESSION_INFO:
                        itsSessionInfo = (ItsSessionInfo) tlv;
                        break;
                    case USSD_SERVICE_OP:
                        ussdServiceOp = (UssdServiceOp) tlv;
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
    public final SubmitSMResp getResponse() {
        SubmitSMResp resp = new SubmitSMResp();
        resp.setSequenceNumber(getSequenceNumber());
        return resp;
    }

    /**
     * @return the SMS Application service associated with the message
     */
    public final String getServiceType() {
        return serviceType;
    }

    /**
     * @param serviceType the SMS Application service associated with the message
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
     * @return Numbering Plan Indicator for source address
     */
    public final NPI getSourceAddrNpi() {
        return sourceAddrNpi;
    }

    /**
     * @param sourceAddrNpi Numbering Plan Indicator for source address
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
     * @return Type of Number for destination
     */
    public final TON getDestAddrTon() {
        return destAddrTon;
    }

    /**
     * @param destAddrTon Type of Number for destination
     */
    public final void setDestAddrTon(final TON destAddrTon) {
        this.destAddrTon = destAddrTon;
    }

    /**
     * @return Numbering Plan Indicator for destination
     */
    public final NPI getDestAddrNpi() {
        return destAddrNpi;
    }

    /**
     * @param destAddrNpi Numbering Plan Indicator for destination
     */
    public final void setDestAddrNpi(final NPI destAddrNpi) {
        this.destAddrNpi = destAddrNpi;
    }

    /**
     * @return destination address of this short message
     */
    public final String getDestinationAddr() {
        return destinationAddr;
    }

    /**
     * @param destinationAddr destination address of this short message
     */
    public final void setDestinationAddr(final String destinationAddr) {
        this.destinationAddr = destinationAddr;
    }

    /**
     * @return Message Mode & Message Type
     */
    public final EsmeEsmClass getEsmClass() {
        return esmClass;
    }

    /**
     * @param esmClass Message Mode & Message Type
     */
    public final void setEsmClass(final EsmeEsmClass esmClass) {
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
     * @return the schedule delivery time
     */
    public final String getScheduleDeliveryTime() {
        return scheduleDeliveryTime;
    }

    /**
     * @param scheduleDeliveryTime the schedule delivery time
     */
    public final void setScheduleDeliveryTime(final String scheduleDeliveryTime) {
        this.scheduleDeliveryTime = scheduleDeliveryTime;
    }

    /**
     * @return the validity period of this message
     */
    public final String getValidityPeriod() {
        return validityPeriod;
    }

    /**
     * @param validityPeriod the validity period of this message
     */
    public final void setValidityPeriod(final String validityPeriod) {
        this.validityPeriod = validityPeriod;
    }

    /**
     * @return indicator to signify if an SMSC delivery receipt or an SME
     *         acknowledgement is required
     */
    public final short getRegisteredDelivery() {
        return registeredDelivery;
    }

    /**
     * @param registeredDelivery indicator to signify if an SMSC delivery receipt or an SME
     *                           acknowledgement is required
     */
    public final void setRegisteredDelivery(final short registeredDelivery) {
        this.registeredDelivery = registeredDelivery;
    }

    /**
     * @return if submitted message should replace an existing message indicator
     */
    public final short getReplaceIfPresentFlag() {
        return replaceIfPresentFlag;
    }

    /**
     * @param replaceIfPresentFlag if submitted message should replace an existing message
     *                             indicator
     */
    public final void setReplaceIfPresentFlag(final short replaceIfPresentFlag) {
        this.replaceIfPresentFlag = replaceIfPresentFlag;
    }

    /**
     * @return encoding scheme of the short message user data
     */
    public final short getDataCoding() {
        return dataCoding;
    }

    /**
     * @param dataCoding encoding scheme of the short message user data
     */
    public final void setDataCoding(final short dataCoding) {
        this.dataCoding = dataCoding;
    }

    /**
     * @return the short message to send from a list of predefined (‘canned’)
     *         short messages stored on the SMSC indicator
     */
    public final short getSmDefaultMsgId() {
        return smDefaultMsgId;
    }

    /**
     * @param smDefaultMsgId the short message to send from a list of predefined (‘canned’)
     *                       short messages stored on the SMSC indicator
     */
    public final void setSmDefaultMsgId(final short smDefaultMsgId) {
        this.smDefaultMsgId = smDefaultMsgId;
    }

    /**
     * @return length in octets of the short_message user data
     */
    public final short getSmLength() {
        return smLength;
    }

    /**
     * @param smLength length in octets of the short_message user data
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

    /**
     * @return ESME assigned message reference number
     */
    public final UserMessageReference getUserMessageReference() {
        return userMessageReference;
    }

    /**
     * @param userMessageReference ESME assigned message reference number
     */
    public final void setUserMessageReference(
            final UserMessageReference userMessageReference) {
        this.userMessageReference = userMessageReference;
    }

    /**
     * @return the application port number associated with the source address of
     *         the message
     */
    public final SourcePort getSourcePort() {
        return sourcePort;
    }

    /**
     * @param sourcePort the application port number associated with the source address
     *                   of the message
     */
    public final void setSourcePort(final SourcePort sourcePort) {
        this.sourcePort = sourcePort;
    }

    /**
     * @return the subcomponent in the destination device which created the user
     *         data
     */
    public final SourceAddrSubunit getSourceAddrSubunit() {
        return sourceAddrSubunit;
    }

    /**
     * @param sourceAddrSubunit the subcomponent in the destination device which created the
     *                          user data
     */
    public final void setSourceAddrSubunit(
            final SourceAddrSubunit sourceAddrSubunit) {
        this.sourceAddrSubunit = sourceAddrSubunit;
    }

    /**
     * @return the application port number associated with the destination
     *         address of the message
     */
    public final DestinationPort getDestinationPort() {
        return destinationPort;
    }

    /**
     * @param destinationPort the application port number associated with the destination
     *                        address of the message
     */
    public final void setDestinationPort(final DestinationPort destinationPort) {
        this.destinationPort = destinationPort;
    }

    /**
     * @return the subcomponent in the destination device for which the user
     *         data is intended
     */
    public final DestAddrSubunit getDestAddrSubunit() {
        return destAddrSubunit;
    }

    /**
     * @param destAddrSubunit the subcomponent in the destination device for which the user
     *                        data is intended
     */
    public final void setDestAddrSubunit(final DestAddrSubunit destAddrSubunit) {
        this.destAddrSubunit = destAddrSubunit;
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
     * @return there are more messages to follow for the destination SME
     *         indicator
     */
    public final MoreMessagesToSend getMoreMessagesToSend() {
        return moreMessagesToSend;
    }

    /**
     * @param moreMessagesToSend there are more messages to follow for the destination SME
     *                           indicator
     */
    public final void setMoreMessagesToSend(
            final MoreMessagesToSend moreMessagesToSend) {
        this.moreMessagesToSend = moreMessagesToSend;
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
     * @return the level of privacy associated with the message
     */
    public final PrivacyIndicator getPrivacyIndicator() {
        return privacyIndicator;
    }

    /**
     * @param privacyIndicator the level of privacy associated with the message
     */
    public final void setPrivacyIndicator(
            final PrivacyIndicator privacyIndicator) {
        this.privacyIndicator = privacyIndicator;
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
     * @return the callback number presentation and screening
     */
    public final CallbackNumPresInd getCallbackNumPresInd() {
        return callbackNumPresInd;
    }

    /**
     * @param callbackNumPresInd the callback number presentation and screening
     */
    public final void setCallbackNumPresInd(
            final CallbackNumPresInd callbackNumPresInd) {
        this.callbackNumPresInd = callbackNumPresInd;
    }

    /**
     * @return displayable alphanumeric tag with the callback number associator
     */
    public final CallbackNumAtag getCallbackNumAtag() {
        return callbackNumAtag;
    }

    /**
     * @param callbackNumAtag displayable alphanumeric tag with the callback number
     *                        associator
     */
    public final void setCallbackNumAtag(final CallbackNumAtag callbackNumAtag) {
        this.callbackNumAtag = callbackNumAtag;
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
     * @return the user response code
     */
    public final UserResponseCode getUserResponseCode() {
        return userResponseCode;
    }

    /**
     * @param userResponseCode the user response code
     */
    public final void setUserResponseCode(
            final UserResponseCode userResponseCode) {
        this.userResponseCode = userResponseCode;
    }

    /**
     * @return the receiving MS with a display time associated with the message
     */
    public final DisplayTime getDisplayTime() {
        return displayTime;
    }

    /**
     * @param displayTime the receiving MS with a display time associated with the
     *                    message
     */
    public final void setDisplayTime(final DisplayTime displayTime) {
        this.displayTime = displayTime;
    }

    /**
     * @return the alerting mechanism when the message is received by an MS
     */
    public final SmsSignal getSmsSignal() {
        return smsSignal;
    }

    /**
     * @param smsSignal the alerting mechanism when the message is received by an MS
     */
    public final void setSmsSignal(final SmsSignal smsSignal) {
        this.smsSignal = smsSignal;
    }

    /**
     * @return validity information for this message to the recipient MS
     */
    public final MsValidity getMsValidity() {
        return msValidity;
    }

    /**
     * @param msValidity validity information for this message to the recipient MS
     */
    public final void setMsValidity(final MsValidity msValidity) {
        this.msValidity = msValidity;
    }

    /**
     * @return the indication and the message type at the mobile station
     */
    public final MsMsgWaitFacilities getMsMsgWaitFacilities() {
        return msMsgWaitFacilities;
    }

    /**
     * @param msMsgWaitFacilities the indication and the message type at the mobile station
     */
    public final void setMsMsgWaitFacilities(
            final MsMsgWaitFacilities msMsgWaitFacilities) {
        this.msMsgWaitFacilities = msMsgWaitFacilities;
    }

    /**
     * @return the number of messages stored in a mail box
     */
    public final NumberOfMessages getNumberOfMessages() {
        return numberOfMessages;
    }

    /**
     * @param numberOfMessages the number of messages stored in a mail box
     */
    public final void setNumberOfMessages(
            final NumberOfMessages numberOfMessages) {
        this.numberOfMessages = numberOfMessages;
    }

    /**
     * @return request an MS alert signal be invoked on message delivery
     */
    public final AlertOnMessageDelivery getAlertOnMessageDelivery() {
        return alertOnMessageDelivery;
    }

    /**
     * @param alertOnMessageDelivery request an MS alert signal be invoked on message delivery
     */
    public final void setAlertOnMessageDelivery(
            final AlertOnMessageDelivery alertOnMessageDelivery) {
        this.alertOnMessageDelivery = alertOnMessageDelivery;
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
     * @return MS user’s reply method
     */
    public final ItsReplyType getItsReplyType() {
        return itsReplyType;
    }

    /**
     * @param itsReplyType MS user’s reply method
     */
    public final void setItsReplyType(final ItsReplyType itsReplyType) {
        this.itsReplyType = itsReplyType;
    }

    /**
     * @return session control information
     */
    public final ItsSessionInfo getItsSessionInfo() {
        return itsSessionInfo;
    }

    /**
     * @param itsSessionInfo session control information
     */
    public final void setItsSessionInfo(final ItsSessionInfo itsSessionInfo) {
        this.itsSessionInfo = itsSessionInfo;
    }

    /**
     * @return the required USSD Service type
     */
    public final UssdServiceOp getUssdServiceOp() {
        return ussdServiceOp;
    }

    /**
     * @param ussdServiceOp the required USSD Service type
     */
    public final void setUssdServiceOp(final UssdServiceOp ussdServiceOp) {
        this.ussdServiceOp = ussdServiceOp;
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
                + "\nsourcePort : " + sourcePort + "\nsourceAddrSubunit : "
                + sourceAddrSubunit + "\ndestinationPort : " + destinationPort
                + "\ndestAddrSubunit : " + destAddrSubunit
                + "\nsarMsgRefNum : " + sarMsgRefNum + "\nsarTotalSegments : "
                + sarTotalSegments + "\nsarSegmentSeqnum : " + sarSegmentSeqnum
                + "\nmoreMessagesToSend : " + moreMessagesToSend
                + "\npayloadType : " + payloadType + "\nmessagePayload : "
                + messagePayload + "\nprivacyIndicator : " + privacyIndicator
                + "\ncallbackNum : " + callbackNum + "\ncallbackNumPresInd : "
                + callbackNumPresInd + "\ncallbackNumAtag : " + callbackNumAtag
                + "\nsourceSubaddress : " + sourceSubaddress
                + "\ndestSubaddress : " + destSubaddress
                + "\nuserResponseCode : " + userResponseCode
                + "\ndisplayTime : " + displayTime + "\nsmsSignal : "
                + smsSignal + "\nmsValidity : " + msValidity
                + "\nmsMsgWaitFacilities : " + msMsgWaitFacilities
                + "\nnumberOfMessages : " + numberOfMessages
                + "\nalertOnMessageDelivery : " + alertOnMessageDelivery
                + "\nlanguageIndicator : " + languageIndicator
                + "\nitsReplyType : " + itsReplyType + "\nitsSessionInfo : "
                + itsSessionInfo + "\nussdServiceOp : " + ussdServiceOp + "\n}";
    }

}
