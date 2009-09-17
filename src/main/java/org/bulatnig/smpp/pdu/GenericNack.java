package org.bulatnig.smpp.pdu;

/**
 * This is a generic negative acknowledgement to an SMPP PDU submitted with an
 * invalid message header. A generic_nack response is returned in the following
 * cases:<br/> • Invalid command_length<br/> If the receiving SMPP entity, on
 * decoding an SMPP PDU, detects an invalid command_length (either too short or
 * too long), it should assume that the data is corrupt. In such cases a
 * generic_nack PDU must be returned to the message originator. • Unknown
 * command_id<br/> If an unknown or invalid command_id is received, a
 * generic_nack PDU must also be returned to the originator.
 *
 * @author Bulat Nigmatullin
 */
public class GenericNack extends PDU {

    /**
     * Constructor.
     */
    public GenericNack() {
        super(CommandId.GENERIC_NACK);
    }

    /**
     * Constructor.
     *
     * @param bytes байткод PDU
     * @throws PDUException ошибка обработки PDU
     */
    public GenericNack(final byte[] bytes) throws PDUException {
        super(bytes);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected final void parseBody(final byte[] bytes)
            throws PDUException {
        if (getCommandId() != CommandId.GENERIC_NACK) {
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
