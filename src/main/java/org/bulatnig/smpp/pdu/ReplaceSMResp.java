package org.bulatnig.smpp.pdu;

/**
 * ReplaceSM Response PDU.
 *
 * @author Bulat Nigmatullin
 */
public class ReplaceSMResp extends PDU {

    /**
     * Constructor.
     */
    public ReplaceSMResp() {
        super(CommandId.REPLACE_SM_RESP);
    }

    /**
     * Constructor.
     *
     * @param bytes байткод PDU
     * @throws PDUException ошибка обработки PDU
     */
    public ReplaceSMResp(final byte[] bytes) throws PDUException {
        super(bytes);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected final void parseBody(final byte[] bytes)
            throws PDUException {
        if (getCommandId() != CommandId.REPLACE_SM_RESP) {
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
