package org.bulatnig.smpp.pdu;

/**
 * CancelSM Response PDU.
 * 
 * @author Bulat Nigmatullin
 * 
 */
public class CancelSMResp extends PDU {

	/**
	 * Constructor.
	 */
	public CancelSMResp() {
		super(CommandId.CANCEL_SM_RESP);
	}

	/**
	 * Constructor.
	 * 
	 * @param bytes
	 *            байткод PDU
     * @throws PDUException ошибка обработки PDU
	 */
	public CancelSMResp(final byte[] bytes) throws PDUException {
		super(bytes);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected final void parseBody(final byte[] bytes)
			throws PDUException {
		if (getCommandId() != CommandId.CANCEL_SM_RESP) {
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
