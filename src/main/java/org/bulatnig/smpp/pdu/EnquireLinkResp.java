package org.bulatnig.smpp.pdu;

/**
 * EnquireLink Response PDU.
 *
 * @author Bulat Nigmatullin
 */
public class EnquireLinkResp extends PDU {

    /**
     * Constructor.
     */
    public EnquireLinkResp() {
        super(CommandId.ENQUIRE_LINK_RESP);
    }

    /**
     * Constructor.
     *
     * @param bytes байткод PDU
     * @throws PDUException ошибка обработки PDU
     */
    public EnquireLinkResp(final byte[] bytes) throws PDUException {
        super(bytes);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected final void parseBody(final byte[] bytes)
            throws PDUException {
        if (getCommandId() != CommandId.ENQUIRE_LINK_RESP) {
            throw new ClassCastException();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected byte[] getBodyBytes() throws PDUException {
        return new byte[0];
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String toString() {
        return getClass().getName() + " Object {}";
	}

}
