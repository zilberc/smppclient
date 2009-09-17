package org.bulatnig.smpp.pdu;

/**
 * The purpose of the SMPP unbind operation is to deregister an instance of an
 * ESME from the SMSC and inform the SMSC that the ESME no longer wishes to use
 * this network connection for the submission or delivery of messages.<br/>
 * <p/>
 * Thus, the unbind operation may be viewed as a form of SMSC logoff request to
 * close the current SMPP session.
 *
 * @author Bulat Nigmatullin
 */
public class Unbind extends PDU implements Responsable {

    /**
     * Constructor.
     */
    public Unbind() {
        super(CommandId.UNBIND);
    }

    /**
     * Constructor.
     *
     * @param bytes байткод PDU
     * @throws PDUException ошибка обработки PDU
     */
    public Unbind(final byte[] bytes) throws PDUException {
        super(bytes);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected final void parseBody(final byte[] bytes)
            throws PDUException {
        if (getCommandId() != CommandId.UNBIND) {
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
    public final UnbindResp getResponse() {
        UnbindResp resp = new UnbindResp();
        resp.setSequenceNumber(getSequenceNumber());
        return resp;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String toString() {
        return getClass().getName() + " Object {}";
	}

}
