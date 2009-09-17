package org.bulatnig.smpp.pdu;

/**
 * This message can be sent by either the ESME or SMSC and is used to provide a
 * confidencecheck of the communication path between an ESME and an SMSC. On
 * receipt of this request the receiving party should respond with an
 * enquire_link_resp, thus verifying that the application level connection
 * between the SMSC and the ESME is functioning. The ESME may also respond by
 * sending any valid SMPP primitive.
 *
 * @author Bulat Nigmatullin
 */
public class EnquireLink extends PDU implements Responsable {

    /**
     * Constructor.
     */
    public EnquireLink() {
        super(CommandId.ENQUIRE_LINK);
    }

    /**
     * Constructor.
     *
     * @param bytes байткод PDU
     * @throws PDUException ошибка обработки PDU
     */
    public EnquireLink(final byte[] bytes) throws PDUException {
        super(bytes);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected final void parseBody(final byte[] bytes)
            throws PDUException {
        if (getCommandId() != CommandId.ENQUIRE_LINK) {
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
    public final EnquireLinkResp getResponse() {
        EnquireLinkResp resp = new EnquireLinkResp();
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
