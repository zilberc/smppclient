package org.bulatnig.smpp.pdu;

import org.bulatnig.smpp.pdu.tlv.*;
import org.bulatnig.smpp.util.SMPPByteBuffer;
import org.bulatnig.smpp.util.WrongLengthException;
import org.bulatnig.smpp.util.WrongParameterException;

import java.util.List;

/**
 * This command is used to transfer data between the SMSC and the ESME. It may
 * be used by both the ESME and SMSC.<br/>
 * <p/>
 * This command is an alternative to the submit_sm and deliver_sm commands. It
 * is introduced as a new command to be used by interactive applications such as
 * those provided via a WAP framework.<br/>
 * <p/>
 * The ESME may use this command to request the SMSC to transfer a message to an
 * MS. The SMSC may also use this command to transfer an MS originated message
 * to an ESME.<br/>
 * <p/>
 * In addition, the data_sm operation can be used to transfer the following
 * types of special messages to the ESME:-<br/> • SMSC Delivery Receipt.<br/> •
 * SME Delivery Acknowledgement. The user data of the SME delivery
 * acknowledgement is included in the short_message field of the data_sm<br/> •
 * SME Manual/User Acknowledgement. The user data of the SME delivery
 * acknowledgement is included in the short_message field of the data_sm<br/> •
 * Intermediate Notification.<br/>
 *
 * @author Bulat Nigmatullin
 */
public class DataSM extends PDU implements Responsable {

    /**
     * Максимальная длина serviceType поля.
     */
    private static final int MAX_SERVICETYPE_LENGTH = 5;
    /**
     * Максимальная длина sourceAddr и destinationAddr полей.
     */
    private static final int MAX_ADDRESS_LENGTH = 64;

    /**
     * The service_type parameter can be used to indicate the SMS Application
     * service associated with the message. Specifying the service_type allows
     * the ESME/SMSC to • to indicate the teleservice used on the air interface.
     */
    private String serviceType;
    /**
     * Type of Number for source address. If not known, set to “Unknown” (0x00).
     */
    private TON sourceAddrTon;
    /**
     * Numbering Plan Indicator for source address. If not known, set to
     * “Unknown” (0x00).
     */
    private NPI sourceAddrNpi;
    /**
     * Address of SME which originated this message.
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
     * Indicates Message Mode and Message Type.
     */
    private EsmClass esmClass;
    /**
     * Indicator for requesting a SMSC delivery receipt or an SME
     * acknowledgement.
     */
    private short registeredDelivery;
    /**
     * Indicates the encoding scheme of the payload data.
     */
    private short dataCoding;

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
     * The correct network associated with the originating device.
     */
    private SourceNetworkType sourceNetworkType;
    /**
     * The correct bearer type for the delivering the user data to the
     * destination.
     */
    private SourceBearerType sourceBearerType;
    /**
     * The telematics identifier associated with the source.
     */
    private SourceTelematicsId sourceTelematicsId;
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
     * The correct network for the destination device.
     */
    private DestNetworkType destNetworkType;
    /**
     * The correct bearer type for the delivering the user data to the
     * destination.
     */
    private DestBearerType destBearerType;
    /**
     * The telematics identifier associated with the destination.
     */
    private DestTelematicsId destTelematicsId;
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
     * Time to live as a relative time in seconds from submission.
     */
    private QosTimeToLive qosTimeToLive;
    /**
     * Defines the type of payload (e.g. WDP, WCMP, etc.).
     */
    private PayloadType payloadType;
    /**
     * Contains the message user data. Up to 64K octets can be transmitted.
     */
    private MessagePayload messagePayload;
    /**
     * Indicator for setting Delivery Pending Flag on delivery failure.
     */
    private SetDpf setDpf;
    /**
     * SMSC message ID of message being receipted. Should be present for SMSC
     * Delivery Receipts and Intermediate Notifications.
     */
    private ReceiptedMessageId receiptedMessageId;
    /**
     * Message State.Should be present for SMSC Delivery Receipts and
     * Intermediate Notifications.
     */
    private MessageStateTlv messageStateTlv;
    /**
     * Network error code. May be present for SMSC Delivery Receipts and
     * Intermediate Notifications.
     */
    private NetworkErrorCode networkErrorCode;
    /**
     * ESME assigned message reference number.
     */
    private UserMessageReference userMessageReference;
    /**
     * Indicates a level of privacy associated with the message.
     */
    private PrivacyIndicator privacyIndicator;
    /**
     * A callback number associated with the short message. This parameter can
     * be included a number of times for multiple call back addresses.
     */
    private CallbackNum callbackNum;
    /**
     * This parameter identifies the presentation and screening associated with
     * the callback number. If this parameter is present and there are multiple
     * instances of the callback_num parameter then this parameter must occur an
     * equal number of instances and the order of occurrence determines the
     * particular callback_num_pres_ind which corresponds to a particular
     * callback_num.
     */
    private CallbackNumPresInd callbackNumPresInd;
    /**
     * This parameter associates a displayable alphanumeric tag with the
     * callback number. If this parameter is present and there are multiple
     * instances of the callback_num parameter then this parameter must occur an
     * equal number of instances and the order of occurrence determines the
     * particular callback_num_atag which corresponds to a particular
     * callback_num.
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
     * Provides the receiving MS based SME with a display time associated with
     * the message.
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
     * Indicates the number of messages stored in a mail box (e.g. voice mail
     * box).
     */
    private NumberOfMessages numberOfMessages;
    /**
     * Requests an MS alert signal be invoked on message delivery.
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
     * Constructor.
     */
    public DataSM() {
        super(CommandId.DATA_SM);
    }

    /**
     * Constructor.
     *
     * @param bytes байткод PDU
     * @throws PDUException ошибка обработки PDU
     */
    public DataSM(final byte[] bytes) throws PDUException {
        super(bytes);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected final byte[] getBodyBytes() throws PDUException {
        SMPPByteBuffer bb = new SMPPByteBuffer();
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
        } catch (
                WrongParameterException e) {
            throw new PDUException("sourceAddrTon field is invalid", e);
        }
        if (sourceAddr != null && sourceAddr.length() > MAX_ADDRESS_LENGTH) {
            throw new PDUException("sourceAddr field is too long");
        }
        bb.appendCString(sourceAddr);
        try {
            bb.appendByte(destAddrTon != null ? destAddrTon.getValue() : TON.UNKNOWN.getValue());
        } catch (WrongParameterException e) {
            throw new PDUException("sourceAddrTon field is invalid", e);
        }
        try {
            bb.appendByte(destAddrNpi != null ? destAddrNpi.getValue() : NPI.UNKNOWN.getValue());
        } catch (WrongParameterException e) {
            throw new PDUException("sourceAddrTon field is invalid", e);
        }
        if (destinationAddr != null && destinationAddr.length() > MAX_ADDRESS_LENGTH) {
            throw new PDUException("destinationAddr field is too long");
        }
        bb.appendCString(destinationAddr);
        try {
            bb.appendByte(esmClass != null ? esmClass.getValue() : 0);
        } catch (WrongParameterException e) {
            throw new PDUException("sourceAddrTon field is invalid", e);
        }
        try {
            bb.appendByte(registeredDelivery);
        } catch (WrongParameterException e) {
            throw new PDUException("sourceAddrTon field is invalid", e);
        }
        try {
            bb.appendByte(dataCoding);
        } catch (WrongParameterException e) {
            throw new PDUException("sourceAddrTon field is invalid", e);
        }
        try {
            if (sourcePort != null) {
                bb.appendBytes(sourcePort.getBytes(), sourcePort.getBytes().length);
            }
            if (sourceAddrSubunit != null) {
                bb.appendBytes(sourceAddrSubunit.getBytes(), sourceAddrSubunit
                        .getBytes().length);
            }
            if (sourceNetworkType != null) {
                bb.appendBytes(sourceNetworkType.getBytes(), sourceNetworkType
                        .getBytes().length);
            }
            if (sourceBearerType != null) {
                bb.appendBytes(sourceBearerType.getBytes(), sourceBearerType
                        .getBytes().length);
            }
            if (sourceTelematicsId != null) {
                bb.appendBytes(sourceTelematicsId.getBytes(), sourceTelematicsId
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
            if (destNetworkType != null) {
                bb.appendBytes(destNetworkType.getBytes(), destNetworkType
                        .getBytes().length);
            }
            if (destBearerType != null) {
                bb.appendBytes(destBearerType.getBytes(),
                        destBearerType.getBytes().length);
            }
            if (destTelematicsId != null) {
                bb.appendBytes(destTelematicsId.getBytes(), destTelematicsId
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
            if (qosTimeToLive != null) {
                bb.appendBytes(qosTimeToLive.getBytes(),
                        qosTimeToLive.getBytes().length);
            }
            if (payloadType != null) {
                bb.appendBytes(payloadType.getBytes(),
                        payloadType.getBytes().length);
            }
            if (messagePayload != null) {
                bb.appendBytes(messagePayload.getBytes(),
                        messagePayload.getBytes().length);
            }
            if (setDpf != null) {
                bb.appendBytes(setDpf.getBytes(), setDpf.getBytes().length);
            }
            if (receiptedMessageId != null) {
                bb.appendBytes(receiptedMessageId.getBytes(), receiptedMessageId
                        .getBytes().length);
            }
            if (messageStateTlv != null) {
                bb.appendBytes(messageStateTlv.getBytes(),
                        messageStateTlv.getBytes().length);
            }
            if (networkErrorCode != null) {
                bb.appendBytes(networkErrorCode.getBytes(), networkErrorCode
                        .getBytes().length);
            }
            if (userMessageReference != null) {
                bb.appendBytes(userMessageReference.getBytes(),
                        userMessageReference.getBytes().length);
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
        } catch (TLVException e) {
            throw new PDUException("TLVs parsing failed", e);
        }
        return bb.getBuffer();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected final void parseBody(final byte[] bytes) throws PDUException {
        if (getCommandId() != CommandId.DATA_SM) {
            throw new ClassCastException();
        }
        SMPPByteBuffer bb = new SMPPByteBuffer(bytes);
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
            registeredDelivery = bb.removeByte();
            dataCoding = bb.removeByte();
        } catch (WrongLengthException e) {
            throw new PDUException("PDU parsing error", e);
        }
        if (bb.length() > 0) {
            List<TLV> list = getOptionalParams(bb.getBuffer(), esmClass, dataCoding);
            for (TLV tlv : list) {
                switch (tlv.getTag()) {
                    case SOURCE_PORT:
                        sourcePort = (SourcePort) tlv;
                        break;
                    case SOURCE_ADDR_SUBUNIT:
                        sourceAddrSubunit = (SourceAddrSubunit) tlv;
                        break;
                    case SOURCE_NETWORK_TYPE:
                        sourceNetworkType = (SourceNetworkType) tlv;
                        break;
                    case SOURCE_BEARER_TYPE:
                        sourceBearerType = (SourceBearerType) tlv;
                        break;
                    case SOURCE_TELEMATICS_ID:
                        sourceTelematicsId = (SourceTelematicsId) tlv;
                        break;
                    case DESTINATION_PORT:
                        destinationPort = (DestinationPort) tlv;
                        break;
                    case DEST_ADDR_SUBUNIT:
                        destAddrSubunit = (DestAddrSubunit) tlv;
                        break;
                    case DEST_NETWORK_TYPE:
                        destNetworkType = (DestNetworkType) tlv;
                        break;
                    case DEST_BEARER_TYPE:
                        destBearerType = (DestBearerType) tlv;
                        break;
                    case DEST_TELEMATICS_ID:
                        destTelematicsId = (DestTelematicsId) tlv;
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
                    case QOS_TIME_TO_LIVE:
                        qosTimeToLive = (QosTimeToLive) tlv;
                        break;
                    case PAYLOAD_TYPE:
                        payloadType = (PayloadType) tlv;
                        break;
                    case MESSAGE_PAYLOAD:
                        messagePayload = (MessagePayload) tlv;
                        break;
                    case SET_DPF:
                        setDpf = (SetDpf) tlv;
                        break;
                    case RECEIPTED_MESSAGE_ID:
                        receiptedMessageId = (ReceiptedMessageId) tlv;
                        break;
                    case MESSAGE_STATE:
                        messageStateTlv = (MessageStateTlv) tlv;
                        break;
                    case NETWORK_ERROR_CODE:
                        networkErrorCode = (NetworkErrorCode) tlv;
                        break;
                    case USER_MESSAGE_REFERENCE:
                        userMessageReference = (UserMessageReference) tlv;
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
                    default:
                        break;
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public final DataSMResp getResponse
            () {
        DataSMResp resp = new DataSMResp();
        resp.setSequenceNumber(getSequenceNumber());
        return resp;
    }

    /**
     * @return indicator the SMS Application service
     */
    public final String getServiceType
            () {
        return serviceType;
    }

    /**
     * @param serviceType indicator the SMS Application service
     */
    public final void setServiceType
            (
                    final String serviceType) {
        this.serviceType = serviceType;
    }

    /**
     * @return Type of Number for source address
     */
    public final TON getSourceAddrTon
            () {
        return sourceAddrTon;
    }

    /**
     * @param sourceAddrTon Type of Number for source address
     */
    public final void setSourceAddrTon
            (
                    final TON sourceAddrTon) {
        this.sourceAddrTon = sourceAddrTon;
    }

    /**
     * @return Numbering Plan Indicator for source address
     */
    public final NPI getSourceAddrNpi
            () {
        return sourceAddrNpi;
    }

    /**
     * @param sourceAddrNpi Numbering Plan Indicator for source address
     */
    public final void setSourceAddrNpi
            (
                    final NPI sourceAddrNpi) {
        this.sourceAddrNpi = sourceAddrNpi;
    }

    /**
     * @return address of SME which originated this message
     */
    public final String getSourceAddr
            () {
        return sourceAddr;
    }

    /**
     * @param sourceAddr address of SME which originated this message
     */
    public final void setSourceAddr
            (
                    final String sourceAddr) {
        this.sourceAddr = sourceAddr;
    }

    /**
     * @return Type of Number for destination
     */
    public final TON getDestAddrTon
            () {
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
    public final NPI getDestAddrNpi
            () {
        return destAddrNpi;
    }

    /**
     * @param destAddrNpi Numbering Plan Indicator for destination
     */
    public final void setDestAddrNpi
            (
                    final NPI destAddrNpi) {
        this.destAddrNpi = destAddrNpi;
    }

    /**
     * @return destination address of this short message
     */
    public final String getDestinationAddr
            () {
        return destinationAddr;
    }

    /**
     * @param destinationAddr destination address of this short message
     */
    public final void setDestinationAddr
            (
                    final String destinationAddr) {
        this.destinationAddr = destinationAddr;
    }

    /**
     * @return indicates Message Mode and Message Type
     */
    public final EsmClass getEsmClass() {
        return esmClass;
    }

    /**
     * @param esmClass indicates Message Mode and Message Type
     */
    public final void setEsmClass(final EsmClass esmClass) {
        this.esmClass = esmClass;
    }

    /**
     * @return indicator for requesting a SMSC delivery receipt or an SME
     *         acknowledgement
     */
    public final short getRegisteredDelivery
            () {
        return registeredDelivery;
    }

    /**
     * @param registeredDelivery indicator for requesting a SMSC delivery receipt or an SME
     *                           acknowledgement
     */
    public final void setRegisteredDelivery
            (
                    final short registeredDelivery) {
        this.registeredDelivery = registeredDelivery;
    }

    /**
     * @return the encodingscheme of the payload data indicator
     */
    public final short getDataCoding
            () {
        return dataCoding;
    }

    /**
     * @param dataCoding the encodingscheme of the payload data indicator
     */
    public final void setDataCoding
            (
                    final short dataCoding) {
        this.dataCoding = dataCoding;
    }

    /**
     * @return application port number associated with the source address
     */
    public final SourcePort getSourcePort
            () {
        return sourcePort;
    }

    /**
     * @param sourcePort application port number associated with the source address
     */
    public final void setSourcePort
            (
                    final SourcePort sourcePort) {
        this.sourcePort = sourcePort;
    }

    /**
     * @return subcomponent in the destination device which created the user
     *         data
     */
    public final SourceAddrSubunit getSourceAddrSubunit
            () {
        return sourceAddrSubunit;
    }

    /**
     * @param sourceAddrSubunit subcomponent in the destination device which created the user
     *                          data
     */
    public final void setSourceAddrSubunit
            (
                    final SourceAddrSubunit sourceAddrSubunit) {
        this.sourceAddrSubunit = sourceAddrSubunit;
    }

    /**
     * @return the correct network associated with the originating device
     */
    public final SourceNetworkType getSourceNetworkType
            () {
        return sourceNetworkType;
    }

    /**
     * @param sourceNetworkType the correct network associated with the originating device
     */
    public final void setSourceNetworkType
            (
                    final SourceNetworkType sourceNetworkType) {
        this.sourceNetworkType = sourceNetworkType;
    }

    /**
     * @return the correct bearer type for the delivering the user data to the
     *         destination
     */
    public final SourceBearerType getSourceBearerType
            () {
        return sourceBearerType;
    }

    /**
     * @param sourceBearerType the correct bearer type for the delivering the user data to
     *                         the destination
     */
    public final void setSourceBearerType
            (
                    final SourceBearerType sourceBearerType) {
        this.sourceBearerType = sourceBearerType;
    }

    /**
     * @return the telematics identifier associated with the source
     */
    public final SourceTelematicsId getSourceTelematicsId
            () {
        return sourceTelematicsId;
    }

    /**
     * @param sourceTelematicsId the telematics identifier associated with the source
     */
    public final void setSourceTelematicsId
            (
                    final SourceTelematicsId sourceTelematicsId) {
        this.sourceTelematicsId = sourceTelematicsId;
    }

    /**
     * @return application port number associated with the destination address
     */
    public final DestinationPort getDestinationPort
            () {
        return destinationPort;
    }

    /**
     * @param destinationPort application port number associated with the destination
     *                        address
     */
    public final void setDestinationPort
            (
                    final DestinationPort destinationPort) {
        this.destinationPort = destinationPort;
    }

    /**
     * @return subcomponent in the destination device for which the user data is
     *         intended
     */
    public final DestAddrSubunit getDestAddrSubunit
            () {
        return destAddrSubunit;
    }

    /**
     * @param destAddrSubunit subcomponent in the destination device for which the user data
     *                        is intended
     */
    public final void setDestAddrSubunit
            (
                    final DestAddrSubunit destAddrSubunit) {
        this.destAddrSubunit = destAddrSubunit;
    }

    /**
     * @return the correct network for the destination device
     */
    public final DestNetworkType getDestNetworkType
            () {
        return destNetworkType;
    }

    /**
     * @param destNetworkType the correct network for the destination device
     */
    public final void setDestNetworkType
            (
                    final DestNetworkType destNetworkType) {
        this.destNetworkType = destNetworkType;
    }

    /**
     * @return the correct bearer type for the delivering the user data to the
     *         destination
     */
    public final DestBearerType getDestBearerType
            () {
        return destBearerType;
    }

    /**
     * @param destBearerType the correct bearer type for the delivering the user data to
     *                       the destination
     */
    public final void setDestBearerType
            (
                    final DestBearerType destBearerType) {
        this.destBearerType = destBearerType;
    }

    /**
     * @return the telematics identifier associated with the destination
     */
    public final DestTelematicsId getDestTelematicsId
            () {
        return destTelematicsId;
    }

    /**
     * @param destTelematicsId the telematics identifier associated with the destination
     */
    public final void setDestTelematicsId
            (
                    final DestTelematicsId destTelematicsId) {
        this.destTelematicsId = destTelematicsId;
    }

    /**
     * @return the reference number for a particular concatenated short message
     */
    public final SarMsgRefNum getSarMsgRefNum
            () {
        return sarMsgRefNum;
    }

    /**
     * @param sarMsgRefNum the reference number for a particular concatenated short
     *                     message
     */
    public final void setSarMsgRefNum
            (
                    final SarMsgRefNum sarMsgRefNum) {
        this.sarMsgRefNum = sarMsgRefNum;
    }

    /**
     * @return the total number ofshort messages within the concatenated short
     *         message
     */
    public final SarTotalSegments getSarTotalSegments
            () {
        return sarTotalSegments;
    }

    /**
     * @param sarTotalSegments the total number ofshort messages within the concatenated
     *                         short message
     */
    public final void setSarTotalSegments
            (
                    final SarTotalSegments sarTotalSegments) {
        this.sarTotalSegments = sarTotalSegments;
    }

    /**
     * @return the sequence number of a particular short message fragment within
     *         the concatenated short message
     */
    public final SarSegmentSeqnum getSarSegmentSeqnum
            () {
        return sarSegmentSeqnum;
    }

    /**
     * @param sarSegmentSeqnum the sequence number of a particular short message fragment
     *                         within the concatenated short message
     */
    public final void setSarSegmentSeqnum
            (
                    final SarSegmentSeqnum sarSegmentSeqnum) {
        this.sarSegmentSeqnum = sarSegmentSeqnum;
    }

    /**
     * @return indicator that there are more messages to follow for the
     *         destination SME
     */
    public final MoreMessagesToSend getMoreMessagesToSend
            () {
        return moreMessagesToSend;
    }

    /**
     * @param moreMessagesToSend indicator that there are more messages to follow for the
     *                           destination SME
     */
    public final void setMoreMessagesToSend
            (
                    final MoreMessagesToSend moreMessagesToSend) {
        this.moreMessagesToSend = moreMessagesToSend;
    }

    /**
     * @return time to live as a relative time in seconds from submission
     */
    public final QosTimeToLive getQosTimeToLive
            () {
        return qosTimeToLive;
    }

    /**
     * @param qosTimeToLive time to live as a relative time in seconds from submission
     */
    public final void setQosTimeToLive
            (
                    final QosTimeToLive qosTimeToLive) {
        this.qosTimeToLive = qosTimeToLive;
    }

    /**
     * @return defines the type of payload
     */
    public final PayloadType getPayloadType
            () {
        return payloadType;
    }

    /**
     * @param payloadType defines the type of payload
     */
    public final void setPayloadType
            (
                    final PayloadType payloadType) {
        this.payloadType = payloadType;
    }

    /**
     * @return contains the message user data
     */
    public final MessagePayload getMessagePayload
            () {
        return messagePayload;
    }

    /**
     * @param messagePayload contains the message user data
     */
    public final void setMessagePayload
            (
                    final MessagePayload messagePayload) {
        this.messagePayload = messagePayload;
    }

    /**
     * @return indicator for setting Delivery Pending Flag on delivery failure
     */
    public final SetDpf getSetDpf
            () {
        return setDpf;
    }

    /**
     * @param setDpf indicator for setting Delivery Pending Flag on delivery
     *               failure
     */
    public final void setSetDpf
            (
                    final SetDpf setDpf) {
        this.setDpf = setDpf;
    }

    /**
     * @return SMSC message ID of message being receipted
     */
    public final ReceiptedMessageId getReceiptedMessageId
            () {
        return receiptedMessageId;
    }

    /**
     * @param receiptedMessageId SMSC message ID of message being receipted
     */
    public final void setReceiptedMessageId
            (
                    final ReceiptedMessageId receiptedMessageId) {
        this.receiptedMessageId = receiptedMessageId;
    }

    /**
     * @return message state
     */
    public final MessageStateTlv getMessageStateTlv
            () {
        return messageStateTlv;
    }

    /**
     * @param messageStateTlv message state
     */
    public final void setMessageStateTlv
            (
                    final MessageStateTlv messageStateTlv) {
        this.messageStateTlv = messageStateTlv;
    }

    /**
     * @return network error code
     */
    public final NetworkErrorCode getNetworkErrorCode
            () {
        return networkErrorCode;
    }

    /**
     * @param networkErrorCode network error code
     */
    public final void setNetworkErrorCode
            (
                    final NetworkErrorCode networkErrorCode) {
        this.networkErrorCode = networkErrorCode;
    }

    /**
     * @return ESME assigned message reference number
     */
    public final UserMessageReference getUserMessageReference
            () {
        return userMessageReference;
    }

    /**
     * @param userMessageReference ESME assigned message reference number
     */
    public final void setUserMessageReference
            (
                    final UserMessageReference userMessageReference) {
        this.userMessageReference = userMessageReference;
    }

    /**
     * @return indicates a level of privacy associated with the message
     */
    public final PrivacyIndicator getPrivacyIndicator
            () {
        return privacyIndicator;
    }

    /**
     * @param privacyIndicator indicates a level of privacy associated with the message
     */
    public final void setPrivacyIndicator
            (
                    final PrivacyIndicator privacyIndicator) {
        this.privacyIndicator = privacyIndicator;
    }

    /**
     * @return a callback number associated with the short message
     */
    public final CallbackNum getCallbackNum
            () {
        return callbackNum;
    }

    /**
     * @param callbackNum a callback number associated with the short message
     */
    public final void setCallbackNum
            (
                    final CallbackNum callbackNum) {
        this.callbackNum = callbackNum;
    }

    /**
     * @return identifier the presentation and screening associated with the
     *         callback number
     */
    public final CallbackNumPresInd getCallbackNumPresInd
            () {
        return callbackNumPresInd;
    }

    /**
     * @param callbackNumPresInd identifier the presentation and screening associated with the
     *                           callback number
     */
    public final void setCallbackNumPresInd
            (
                    final CallbackNumPresInd callbackNumPresInd) {
        this.callbackNumPresInd = callbackNumPresInd;
    }

    /**
     * @return parameter that associates a displayable alphanumeric tag with the
     *         callback number
     */
    public final CallbackNumAtag getCallbackNumAtag
            () {
        return callbackNumAtag;
    }

    /**
     * @param callbackNumAtag parameter that associates a displayable alphanumeric tag with
     *                        the callback number
     */
    public final void setCallbackNumAtag
            (
                    final CallbackNumAtag callbackNumAtag) {
        this.callbackNumAtag = callbackNumAtag;
    }

    /**
     * @return the subaddress of the message originator
     */
    public final SourceSubaddress getSourceSubaddress
            () {
        return sourceSubaddress;
    }

    /**
     * @param sourceSubaddress the subaddress of the message originator
     */
    public final void setSourceSubaddress
            (
                    final SourceSubaddress sourceSubaddress) {
        this.sourceSubaddress = sourceSubaddress;
    }

    /**
     * @return the subaddress of the message destination
     */
    public final DestSubaddress getDestSubaddress
            () {
        return destSubaddress;
    }

    /**
     * @param destSubaddress the subaddress of the message destination
     */
    public final void setDestSubaddress
            (
                    final DestSubaddress destSubaddress) {
        this.destSubaddress = destSubaddress;
    }

    /**
     * @return user response code
     */
    public final UserResponseCode getUserResponseCode
            () {
        return userResponseCode;
    }

    /**
     * @param userResponseCode user response code
     */
    public final void setUserResponseCode
            (
                    final UserResponseCode userResponseCode) {
        this.userResponseCode = userResponseCode;
    }

    /**
     * @return receiving MS based SME with a display time associated with the
     *         message
     */
    public final DisplayTime getDisplayTime
            () {
        return displayTime;
    }

    /**
     * @param displayTime receiving MS based SME with a display time associated with the
     *                    message
     */
    public final void setDisplayTime
            (
                    final DisplayTime displayTime) {
        this.displayTime = displayTime;
    }

    /**
     * @return the alerting mechanism when the message is received by an MS
     */
    public final SmsSignal getSmsSignal
            () {
        return smsSignal;
    }

    /**
     * @param smsSignal the alerting mechanism when the message is received by an MS
     */
    public final void setSmsSignal
            (
                    final SmsSignal smsSignal) {
        this.smsSignal = smsSignal;
    }

    /**
     * @return validity information for this message to the recipient MS
     */
    public final MsValidity getMsValidity
            () {
        return msValidity;
    }

    /**
     * @param msValidity validity information for this message to the recipient MS
     */
    public final void setMsValidity
            (
                    final MsValidity msValidity) {
        this.msValidity = msValidity;
    }

    /**
     * @return controller the indication and specifies the message type (of the
     *         message associated with the MWI) at the mobile station
     */
    public final MsMsgWaitFacilities getMsMsgWaitFacilities
            () {
        return msMsgWaitFacilities;
    }

    /**
     * @param msMsgWaitFacilities controller the indication and specifies the message type (of
     *                            the message associated with the MWI) at the mobile station
     */
    public final void setMsMsgWaitFacilities
            (
                    final MsMsgWaitFacilities msMsgWaitFacilities) {
        this.msMsgWaitFacilities = msMsgWaitFacilities;
    }

    /**
     * @return number of messages stored in a mail box
     */
    public final NumberOfMessages getNumberOfMessages
            () {
        return numberOfMessages;
    }

    /**
     * @param numberOfMessages number of messages stored in a mail box
     */
    public final void setNumberOfMessages
            (
                    final NumberOfMessages numberOfMessages) {
        this.numberOfMessages = numberOfMessages;
    }

    /**
     * @return requests an MS alert signal be invoked on message delivery
     */
    public final AlertOnMessageDelivery getAlertOnMessageDelivery
            () {
        return alertOnMessageDelivery;
    }

    /**
     * @param alertOnMessageDelivery requests an MS alert signal be invoked on message delivery
     */
    public final void setAlertOnMessageDelivery
            (
                    final AlertOnMessageDelivery alertOnMessageDelivery) {
        this.alertOnMessageDelivery = alertOnMessageDelivery;
    }

    /**
     * @return the language of an alphanumeric text message indicator
     */
    public final LanguageIndicator getLanguageIndicator
            () {
        return languageIndicator;
    }

    /**
     * @param languageIndicator the language of an alphanumeric text message indicator
     */
    public final void setLanguageIndicator
            (
                    final LanguageIndicator languageIndicator) {
        this.languageIndicator = languageIndicator;
    }

    /**
     * @return the MS user’s reply method
     */
    public final ItsReplyType getItsReplyType
            () {
        return itsReplyType;
    }

    /**
     * @param itsReplyType the MS user’s reply method
     */
    public final void setItsReplyType
            (
                    final ItsReplyType itsReplyType) {
        this.itsReplyType = itsReplyType;
    }

    /**
     * @return session control information for Interactive Teleservice
     */
    public final ItsSessionInfo getItsSessionInfo
            () {
        return itsSessionInfo;
    }

    /**
     * @param itsSessionInfo session control information for Interactive Teleservice
     */
    public final void setItsSessionInfo
            (
                    final ItsSessionInfo itsSessionInfo) {
        this.itsSessionInfo = itsSessionInfo;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String toString
            () {
        return getClass().getName() + " Object {" + "\nserviceType : "
                + serviceType + "\nsourceAddrTon : " + sourceAddrTon
                + "\nsourceAddrNpi : " + sourceAddrNpi + "\nsourceAddr : "
                + sourceAddr + "\ndestAddrTon : " + destAddrTon
                + "\ndestAddrNpi : " + destAddrNpi + "\ndestinationAddr : "
                + destinationAddr + "\nesmClass : " + esmClass
                + "\nregisteredDelivery : " + registeredDelivery
                + "\ndataCoding : " + dataCoding + "\nsourcePort : "
                + sourcePort + "\nsourceAddrSubunit : " + sourceAddrSubunit
                + "\nsourceNetworkType : " + sourceNetworkType
                + "\nsourceBearerType : " + sourceBearerType
                + "\ndestTelematicsId : " + destTelematicsId
                + "\nsarMsgRefNum : " + sarMsgRefNum + "\nsarTotalSegments : "
                + sarTotalSegments + "\nsarSegmentSeqnum : " + sarSegmentSeqnum
                + "\nmoreMessagesToSend : " + moreMessagesToSend
                + "\nqosTimeToLive : " + qosTimeToLive + "\npayloadType : "
                + payloadType + "\nmessagePayload : " + messagePayload
                + "\nsetDpf : " + setDpf + "\nreceiptedMessageId : "
                + receiptedMessageId + "\nmessageStateTlv : " + messageStateTlv
                + "\nnetworkErrorCode : " + networkErrorCode
                + "\nuserMessageReference : " + userMessageReference
                + "\nprivacyIndicator : " + privacyIndicator
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
                + itsSessionInfo + "\n}";
    }

}
