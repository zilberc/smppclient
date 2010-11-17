package org.bulatnig.smpp.pdu;

import org.bulatnig.smpp.util.SmppByteBuffer;

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
        SmppByteBuffer bb = new SmppByteBuffer();
        bb.appendCString(messageId);
        return bb.array();
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
        SmppByteBuffer bb = new SmppByteBuffer(bytes);
        messageId = bb.removeCString();
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
