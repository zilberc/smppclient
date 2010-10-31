package org.bulatnig.smpp.pdu;

import org.bulatnig.smpp.util.SmppByteBuffer;
import org.bulatnig.smpp.util.WrongLengthException;

/**
 * SubmitSM Response PDU.
 *
 * @author Bulat Nigmatullin
 */
public class SubmitSMResp extends PDU {

    /**
     * Максимальная длина messageId поля.
     */
    private static final int MAX_MESSAGEID_LENGTH = 65;

    /**
     * This field contains the SMSC message ID of the submitted message. It may
     * be used at a later stage to query the status of a message, cancel or
     * replace the message.
     */
    private String messageId;

    /**
     * Constructor.
     */
    public SubmitSMResp() {
        super(CommandId.SUBMIT_SM_RESP);
    }

    /**
     * Constructor.
     *
     * @param bytes байткод PDU
     * @throws PDUException ошибка обработки PDU
     */
    public SubmitSMResp(final byte[] bytes) throws PDUException {
        super(bytes);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected final byte[] getBodyBytes() throws PDUException {
        if (getCommandStatus() == CommandStatus.ESME_ROK) {
            SmppByteBuffer bb = new SmppByteBuffer();
            if (messageId != null && messageId.length() > MAX_MESSAGEID_LENGTH) {
                throw new PDUException("messageId field is invalid");
            }
            bb.appendCString(messageId);
            return bb.getBuffer();
        } else {
            return new byte[0];
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected final void parseBody(final byte[] bytes)
            throws PDUException {
        if (getCommandId() != CommandId.SUBMIT_SM_RESP) {
            throw new ClassCastException();
        }
        if (getCommandStatus() == CommandStatus.ESME_ROK) {
            SmppByteBuffer bb = new SmppByteBuffer(bytes);
            if (bb.length() > 0) {
                try {
                    messageId = bb.removeCString();
                } catch (WrongLengthException e) {
                    throw new PDUException("PDU parsing error", e);
                }
                if (messageId.length() > MAX_MESSAGEID_LENGTH) {
                    throw new PDUException("messageId field is too long");
                }
            }
        }
    }

    /**
     * @return the SMSC message ID of the submitted message
     */
    public final String getMessageId() {
        return messageId;
    }

    /**
     * @param messageId the SMSC message ID of the submitted message
     */
    public final void setMessageId(final String messageId) {
        this.messageId = messageId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String toString() {
        return getClass().getName() + " Object {" + "\nmessageId : "
                + messageId + "\n}";
    }

}
