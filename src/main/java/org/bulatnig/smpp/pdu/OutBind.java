package org.bulatnig.smpp.pdu;

import org.bulatnig.smpp.util.SmppByteBuffer;
import org.bulatnig.smpp.util.WrongLengthException;

/**
 * This operation is used by the SMSC to signal an ESME to originate a
 * bind_receiver request to the SMSC.
 *
 * @author Bulat Nigmatullin
 */
public class OutBind extends PDU {

    /**
     * Максимальная длина systemId поля.
     */
    private static final int MAX_SYSTEMID_LENGTH = 15;
    /**
     * Максимальная длина password поля.
     */
    private static final int MAX_PASSWORD_LENGTH = 8;

    /**
     * SMSC identifier. Identifies the SMSC to the ESME.
     */
    private String systemId;
    /**
     * The password may be used by the ESME for security reasons to authenticate
     * the SMSC originating the outbind.
     */
    private String password;

    /**
     * Constructor.
     */
    public OutBind() {
        super(CommandId.OUTBIND);
    }

    /**
     * Constructor.
     *
     * @param bytes байткод PDU
     * @throws PDUException ошибка обработки PDU
     */
    public OutBind(final byte[] bytes) throws PDUException {
        super(bytes);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected final byte[] getBodyBytes() throws PDUException {
        SmppByteBuffer bb = new SmppByteBuffer();
        if (systemId != null && systemId.length() > MAX_SYSTEMID_LENGTH) {
            throw new PDUException("systemId field is too long");
        }
        bb.appendCString(systemId);
        if (password != null && password.length() > MAX_PASSWORD_LENGTH) {
            throw new PDUException("password field is too long");
        }
        bb.appendCString(password);
        return bb.array();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected final void parseBody(final byte[] bytes)
            throws PDUException {
        if (getCommandId() != CommandId.OUTBIND) {
            throw new ClassCastException();
        }
        SmppByteBuffer bb = new SmppByteBuffer(bytes);
        systemId = bb.removeCString();
        if (systemId.length() > MAX_SYSTEMID_LENGTH) {
            throw new PDUException("systemId field is too long");
        }
        password = bb.removeCString();
        if (password.length() > MAX_PASSWORD_LENGTH) {
            throw new PDUException("password field is too long");
        }
    }

    /**
     * @return SMSC identifier
     */
    public final String getSystemId() {
        return systemId;
    }

    /**
     * @param systemId SMSC identifier
     */
    public final void setSystemId(final String systemId) {
        this.systemId = systemId;
    }

    /**
     * @return the password
     */
    public final String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public final void setPassword(final String password) {
        this.password = password;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String toString() {
        return getClass().getName() + " Object {" + "\nsystemId : " + systemId
                + "\npassword : " + password + "\n}";
    }

}
