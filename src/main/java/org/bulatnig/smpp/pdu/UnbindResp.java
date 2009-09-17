package org.bulatnig.smpp.pdu;

/**
 * Unbind Response PDU.
 *
 * @author Bulat Nigmatullin
 */
public class UnbindResp extends PDU {

    /**
     * Constructor.
     */
    public UnbindResp() {
        super(CommandId.UNBIND_RESP);
    }

    /**
     * Constructor.
     *
     * @param bytes байткод PDU
     * @throws PDUException ошибка обработки PDU
     */
    public UnbindResp(final byte[] bytes) throws PDUException {
        super(bytes);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected final void parseBody(final byte[] bytes)
            throws PDUException {
        if (getCommandId() != CommandId.UNBIND_RESP) {
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
