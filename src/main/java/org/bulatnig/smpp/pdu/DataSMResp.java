package org.bulatnig.smpp.pdu;

import org.bulatnig.smpp.pdu.tlv.*;
import org.bulatnig.smpp.util.SmppByteBuffer;
import org.bulatnig.smpp.util.WrongLengthException;

import java.util.List;

/**
 * DataSM Response PDU.
 *
 * @author Bulat Nigmatullin
 */
public class DataSMResp extends PDU {

    /**
     * Максимальная длина messageId поля.
     */
    private static final int MAX_MESSAGEID_LENGTH = 16;

    /**
     * This field contains the SMSC assigned message ID of the short message.
     */
    private String messageId;

    /**
     * Include to indicate reason for delivery failure.
     */
    private DeliveryFailureReason deliveryFailureReason;
    /**
     * Error code specific to a wireless network.
     */
    private NetworkErrorCode networkErrorCode;
    /**
     * ASCII text giving a description of the meaning of the response.
     */
    private AdditionalStatusInfoText additionalStatusInfoText;
    /**
     * Indicates whether the Delivery Pending Flag was set.
     */
    private DpfResult dpfResult;

    /**
     * Constructor.
     */
    public DataSMResp() {
        super(CommandId.DATA_SM_RESP);
    }

    /**
     * Constructor.
     *
     * @param bytes байткод PDU
     * @throws PDUException ошибка обработки PDU
     */
    public DataSMResp(final byte[] bytes) throws PDUException {
        super(bytes);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected final byte[] getBodyBytes() throws PDUException {
        SmppByteBuffer bb = new SmppByteBuffer();
        if (messageId != null && messageId.length() > MAX_MESSAGEID_LENGTH) {
            throw new PDUException("messageId field is invalid");
        }
        bb.appendCString(messageId);
        try {
            if (deliveryFailureReason != null) {
                bb.appendBytes(deliveryFailureReason.getBytes());
            }
            if (networkErrorCode != null) {
                bb.appendBytes(networkErrorCode.getBytes());
            }
            if (additionalStatusInfoText != null) {
                bb.appendBytes(additionalStatusInfoText.getBytes());
            }
            if (dpfResult != null) {
                bb.appendBytes(dpfResult.getBytes());
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
        if (getCommandId() != CommandId.DATA_SM_RESP) {
            throw new ClassCastException();
        }
        SmppByteBuffer bb = new SmppByteBuffer(bytes);
        try {
            messageId = bb.removeCString();
        } catch (WrongLengthException e) {
            throw new PDUException("PDU parsing error", e);
        }
        if (messageId.length() > MAX_MESSAGEID_LENGTH) {
            throw new PDUException("messageId field is too long");
        }
        if (bb.length() > 0) {
            List<TLV> list = getOptionalParams(bb.array());
            for (TLV tlv : list) {
                switch (tlv.getTag()) {
                    case DELIVERY_FAILURE_REASON:
                        deliveryFailureReason = (DeliveryFailureReason) tlv;
                        break;
                    case NETWORK_ERROR_CODE:
                        networkErrorCode = (NetworkErrorCode) tlv;
                        break;
                    case ADDITIONAL_STATUS_INFO_TEXT:
                        additionalStatusInfoText = (AdditionalStatusInfoText) tlv;
                        break;
                    case DPF_RESULT:
                        dpfResult = (DpfResult) tlv;
                        break;
                    default:
                        break;
                }
            }
        }
    }

    /**
     * @return SMSC assigned message ID
     */
    public final String getMessageId() {
        return messageId;
    }

    /**
     * @param messageId SMSC assigned message ID
     */
    public final void setMessageId(final String messageId) {
        this.messageId = messageId;
    }

    /**
     * @return reason for delivery failure
     */
    public final DeliveryFailureReason getDeliveryFailureReason() {
        return deliveryFailureReason;
    }

    /**
     * @param deliveryFailureReason reason for delivery failure
     */
    public final void setDeliveryFailureReason(
            final DeliveryFailureReason deliveryFailureReason) {
        this.deliveryFailureReason = deliveryFailureReason;
    }

    /**
     * @return error code specific to a wireless network
     */
    public final NetworkErrorCode getNetworkErrorCode() {
        return networkErrorCode;
    }

    /**
     * @param networkErrorCode error code specific to a wireless network
     */
    public final void setNetworkErrorCode(
            final NetworkErrorCode networkErrorCode) {
        this.networkErrorCode = networkErrorCode;
    }

    /**
     * @return description of the meaning of the response
     */
    public final AdditionalStatusInfoText getAdditionalStatusInfoText() {
        return additionalStatusInfoText;
    }

    /**
     * @param additionalStatusInfoText description of the meaning of the response
     */
    public final void setAdditionalStatusInfoText(
            final AdditionalStatusInfoText additionalStatusInfoText) {
        this.additionalStatusInfoText = additionalStatusInfoText;
    }

    /**
     * @return whether the Delivery Pending Flag was set
     */
    public final DpfResult getDpfResult() {
        return dpfResult;
    }

    /**
     * @param dpfResult whether the Delivery Pending Flag was set
     */
    public final void setDpfResult(final DpfResult dpfResult) {
        this.dpfResult = dpfResult;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String toString() {
        return getClass().getName() + " Object {" + "\nmessageId : "
                + messageId + "\ndeliveryFailureReason : "
                + deliveryFailureReason + "\nnetworkErrorCode : "
                + networkErrorCode + "\nadditionalStatusInfoText : "
                + additionalStatusInfoText + "\ndpfResult : " + dpfResult
                + "\n}";
    }

}
