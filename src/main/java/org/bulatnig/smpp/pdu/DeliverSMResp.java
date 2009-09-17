package org.bulatnig.smpp.pdu;

import org.bulatnig.smpp.util.SMPPByteBuffer;
import org.bulatnig.smpp.util.WrongLengthException;

/**
 * DeliverSM Response PDU.
 *
 * @author Bulat Nigmatullin
 */
public class DeliverSMResp extends PDU {
    /**
     * field is unused.
     */
    private String messageId;

    /**
     * Constructor.
     */
    public DeliverSMResp() {
        super(CommandId.DELIVER_SM_RESP);
    }

    /**
     * Constructor.
     *
     * @param bytes байткод PDU
     * @throws PDUException ошибка обработки PDU
     */
    public DeliverSMResp(final byte[] bytes) throws PDUException {
        super(bytes);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected final byte[] getBodyBytes() {
        SMPPByteBuffer bb = new SMPPByteBuffer();
        bb.appendCString(messageId);
        return bb.getBuffer();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected final void parseBody(final byte[] bytes)
            throws PDUException {
        if (getCommandId() != CommandId.DELIVER_SM_RESP) {
            throw new ClassCastException();
        }
        SMPPByteBuffer bb = new SMPPByteBuffer(bytes);
        try {
            messageId = bb.removeCString();
        } catch (WrongLengthException e) {
            throw new PDUException("PDU parsing error", e);
        }
    }

    /**
     * @return field is unused
     */
    public final String getMessageId() {
        return messageId;
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
