package org.bulatnig.smpp.pdu.tlv;

import org.bulatnig.smpp.util.SmppByteBuffer;
import org.bulatnig.smpp.util.SmppByteBufferException;
import org.bulatnig.smpp.util.WrongLengthException;
import org.bulatnig.smpp.pdu.EsmClass;

import java.util.ArrayList;
import java.util.List;

/**
 * Простая реализация TLVFactory интерфейса.
 * <p/>
 * User: Bulat Nigmatullin
 * Date: Oct 30, 2008
 * Time: 7:30:46 AM
 */
public enum TLVFactoryImpl implements TLVFactory {
    INSTANCE;

    private TLVHelper helper = TLVHelperImpl.INSTANCE;

    /**
     * {@inheritDoc}
     */
    public TLV parseTLV(byte[] bytes, EsmClass esmClass, short dataCoding) throws TLVException {
        TLV tlv;
        SmppByteBuffer param = new SmppByteBuffer(bytes);
        int paramTag;
        try {
            paramTag = param.removeShort();
        } catch (WrongLengthException e) {
            throw new TLVException("TLV have not enougth length to read parameter tag", e);
        }
        ParameterTag tag;
        try {
            tag = helper.getParameterTag(paramTag);
        } catch (ParameterTagNotFoundException e) {
            throw new TLVNotFoundException("TLV not found by parameter tag: " + paramTag, e);
        }
        switch (tag) {
            case ADDITIONAL_STATUS_INFO_TEXT:
                tlv = new AdditionalStatusInfoText(bytes);
                break;
            case ALERT_ON_MESSAGE_DELIVERY:
                tlv = new AlertOnMessageDelivery(bytes);
                break;
            case CALLBACK_NUM:
                tlv = new CallbackNum(bytes);
                break;
            case CALLBACK_NUM_ATAG:
                tlv = new CallbackNumAtag(bytes);
                break;
            case CALLBACK_NUM_PRES_IND:
                tlv = new CallbackNumPresInd(bytes);
                break;
            case DELIVERY_FAILURE_REASON:
                tlv = new DeliveryFailureReason(bytes);
                break;
            case DEST_ADDR_SUBUNIT:
                tlv = new DestAddrSubunit(bytes);
                break;
            case DEST_BEARER_TYPE:
                tlv = new DestBearerType(bytes);
                break;
            case DEST_NETWORK_TYPE:
                tlv = new DestNetworkType(bytes);
                break;
            case DEST_SUBADDRESS:
                tlv = new DestSubaddress(bytes);
                break;
            case DEST_TELEMATICS_ID:
                tlv = new DestTelematicsId(bytes);
                break;
            case DISPLAY_TIME:
                tlv = new DisplayTime(bytes);
                break;
            case DPF_RESULT:
                tlv = new DpfResult(bytes);
                break;
            case ITS_REPLY_TYPE:
                tlv = new ItsReplyType(bytes);
                break;
            case ITS_SESSION_INFO:
                tlv = new ItsSessionInfo(bytes);
                break;
            case LANGUAGE_INDICATOR:
                tlv = new LanguageIndicator(bytes);
                break;
            case MESSAGE_PAYLOAD:
                tlv = new MessagePayload(bytes, esmClass, dataCoding);
                break;
            case MESSAGE_STATE:
                tlv = new MessageStateTlv(bytes);
                break;
            case MORE_MESSAGES_TO_SEND:
                tlv = new MoreMessagesToSend(bytes);
                break;
            case MS_AVAILABILITY_STATUS:
                tlv = new MsAvailabilityStatus(bytes);
                break;
            case MS_MSG_WAIT_FACILITIES:
                tlv = new MsMsgWaitFacilities(bytes);
                break;
            case MS_VALIDITY:
                tlv = new MsValidity(bytes);
                break;
            case NETWORK_ERROR_CODE:
                tlv = new NetworkErrorCode(bytes);
                break;
            case NUMBER_OF_MESSAGES:
                tlv = new NumberOfMessages(bytes);
                break;
            case PAYLOAD_TYPE:
                tlv = new PayloadType(bytes);
                break;
            case PRIVACY_INDICATOR:
                tlv = new PrivacyIndicator(bytes);
                break;
            case QOS_TIME_TO_LIVE:
                tlv = new QosTimeToLive(bytes);
                break;
            case RECEIPTED_MESSAGE_ID:
                tlv = new ReceiptedMessageId(bytes);
                break;
            case SAR_MSG_REF_NUM:
                tlv = new SarMsgRefNum(bytes);
                break;
            case SAR_SEGMENT_SEQNUM:
                tlv = new SarSegmentSeqnum(bytes);
                break;
            case SAR_TOTAL_SEGMENTS:
                tlv = new SarTotalSegments(bytes);
                break;
            case SC_INTERFACE_VERSION:
                tlv = new ScInterfaceVersion(bytes);
                break;
            case SET_DPF:
                tlv = new SetDpf(bytes);
                break;
            case SMS_SIGNAL:
                tlv = new SmsSignal(bytes);
                break;
            case SOURCE_ADDR_SUBUNIT:
                tlv = new SourceAddrSubunit(bytes);
                break;
            case SOURCE_BEARER_TYPE:
                tlv = new SourceBearerType(bytes);
                break;
            case SOURCE_NETWORK_TYPE:
                tlv = new SourceNetworkType(bytes);
                break;
            case SOURCE_PORT:
                tlv = new SourcePort(bytes);
                break;
            case SOURCE_SUBADDRESS:
                tlv = new SourceSubaddress(bytes);
                break;
            case SOURCE_TELEMATICS_ID:
                tlv = new SourceTelematicsId(bytes);
                break;
            case USER_MESSAGE_REFERENCE:
                tlv = new UserMessageReference(bytes);
                break;
            case USER_RESPONSE_CODE:
                tlv = new UserResponseCode(bytes);
                break;
            case USSD_SERVICE_OP:
                tlv = new UssdServiceOp(bytes);
                break;
            default:
                throw new TLVNotFoundException("TLV not handled. TLV Parameter Tag: " + tag);
        }
        return tlv;
    }

    /**
     * {@inheritDoc}
     */
    public List<TLV> parseTLVs(byte[] bytes, EsmClass esmClass, short dataCoding) throws TLVException {
        List<TLV> list = new ArrayList<TLV>();
        SmppByteBuffer params = new SmppByteBuffer(bytes);
        SmppByteBuffer buffer;
        int length;
        try {
            while (params.length() > 0) {
                buffer = new SmppByteBuffer();
                buffer.appendShort(params.removeShort());
                length = params.removeShort();
                buffer.appendShort(length);
                if (length > 0) {
                    buffer.appendBytes(params.removeBytes(length).array());
                }
                try {
                    list.add(parseTLV(buffer.array(), esmClass, dataCoding));
                } catch (TLVNotFoundException e) {
                    // omit it
                }
            }
        } catch (SmppByteBufferException e) {
            throw new TLVException("SmppByteBuffer error during tlv parsing", e);
        }
        return list;
    }
}
