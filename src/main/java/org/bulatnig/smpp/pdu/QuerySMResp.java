package org.bulatnig.smpp.pdu;

import org.bulatnig.smpp.util.SMPPByteBuffer;
import org.bulatnig.smpp.util.WrongLengthException;
import org.bulatnig.smpp.util.WrongParameterException;

/**
 * QuerySM Response PDU.
 *
 * @author Bulat Nigmatullin
 */
public class QuerySMResp extends PDU {
    /**
     * Максимальная длина messageId поля.
     */
    private static final int MAX_MESSAGEID_LENGTH = 5;
    /**
     * Максимальная длина finalDate поля.
     */
    private static final int FINALDATE_LENGTH = 16;

    /**
     * SMSC Message ID of the message whose state is being queried.
     */
    private String messageId;
    /**
     * Date and time when the queried message reached a final state. For
     * messages which have not yet reached a final state this field will contain
     * a single NULL octet.
     */
    private String finalDate;
    /**
     * Specifies the status of the queried short message.
     */
    private MessageState messageState;
    /**
     * Where appropriate this holds a network error code defining the reason for
     * failure of message delivery.
     */
    private short errorCode;

    /**
     * Constructor.
     */
    public QuerySMResp() {
        super(CommandId.QUERY_SM_RESP);
    }

    /**
     * Constructor.
     *
     * @param bytes байткод PDU
     * @throws PDUException ошибка обработки PDU
     */
    public QuerySMResp(final byte[] bytes) throws PDUException {
        super(bytes);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected final void parseBody(final byte[] bytes)
            throws PDUException {
        if (getCommandId() != CommandId.QUERY_SM_RESP) {
            throw new ClassCastException();
        }
        SMPPByteBuffer bb = new SMPPByteBuffer(bytes);
        try {
            messageId = bb.removeCString();
            if (messageId.length() > MAX_MESSAGEID_LENGTH) {
                throw new PDUException("messageId field is too long");
            }
            finalDate = bb.removeCString();
            if (finalDate.length() > 0 && finalDate.length() != FINALDATE_LENGTH) {
                throw new PDUException("finalDate field is invalid");
            }
            short b = bb.removeByte();
            for (MessageState ms : MessageState.values()) {
                if (ms.getValue() == b) {
                    messageState = ms;
                }
            }
            if (messageState == null) {
                throw new PDUException("Wrong message state value: " + b);
            }
            errorCode = bb.removeByte();
        } catch (WrongLengthException e) {
            throw new PDUException("PDU parsing error", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected final byte[] getBodyBytes() throws PDUException {
        SMPPByteBuffer bb = new SMPPByteBuffer();
        if (messageId != null && messageId.length() > MAX_MESSAGEID_LENGTH) {
            throw new PDUException("messageId field is too long");
        }
        bb.appendCString(messageId);
        if (finalDate != null && finalDate.length() != FINALDATE_LENGTH) {
            throw new PDUException("finalDate field is invalid");
        }
        bb.appendCString(finalDate);
        try {
            bb.appendByte(messageState.getValue());
        } catch (WrongParameterException e) {
            throw new PDUException("messageState field is invalid", e);
        }
        try {
            bb.appendByte(errorCode);
        } catch (
                WrongParameterException e) {
            throw new PDUException("errorCode field is invalid", e);
        }
        return bb.getBuffer();
    }

    /**
     * @return SMSC Message ID of the message whose state is being queried
     */
    public final String getMessageId() {
        return messageId;
    }

    /**
     * @param messageId SMSC Message ID of the message whose state is being queried
     */
    public final void setMessageId(final String messageId) {
        this.messageId = messageId;
    }

    /**
     * @return date and time when the queried message reached a final state
     */
    public final String getFinalDate() {
        return finalDate;
    }

    /**
     * @param finalDate date and time when the queried message reached a final state
     */
    public final void setFinalDate(final String finalDate) {
        this.finalDate = finalDate;
    }

    /**
     * @return the status of the queried short message
     */
    public final MessageState getMessageState() {
        return messageState;
    }

    /**
     * @param messageState the status of the queried short message
     */
    public final void setMessageState(final MessageState messageState) {
        this.messageState = messageState;
    }

    /**
     * @return error code defining the reason for failure of message delivery
     */
    public final short getErrorCode() {
        return errorCode;
    }

    /**
     * @param errorCode error code defining the reason for failure of message delivery
     */
    public final void setErrorCode(final short errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String toString() {
        return getClass().getName() + " Object {" + "\nmessageId : "
                + messageId + "\nfinalDate : " + finalDate
                + "\nmessageState : " + messageState + "\nerrorCode : "
                + errorCode + "\n}";
    }

}