package org.bulatnig.smpp.pdu;

import org.bulatnig.smpp.util.SmppByteBuffer;

/**
 * An ESME bound as a Transmitter is authorised to send short messages to the
 * SMSC and to receive the corresponding SMPP responses from the SMSC. An ESME
 * indicates its desire not to receive (mobile) originated messages from other
 * SME’s (e.g. mobile stations) by binding as a Transmitter.
 *
 * @author Bulat Nigmatullin
 */
public class BindTransmitter extends PDU implements Responsable {

    /**
     * Максимальная длина systemId поля.
     */
    private static final int MAX_SYSTEMID_LENGTH = 15;
    /**
     * Максимальная длина password поля.
     */
    private static final int MAX_PASSWORD_LENGTH = 8;
    /**
     * Максимальная длина systemType поля.
     */
    private static final int MAX_SYSTEMTYPE_LENGTH = 12;
    /**
     * Максимальная длина addressRange поля.
     */
    private static final int MAX_ADDRESSRANGE_LENGTH = 40;

    /**
     * Identifies the ESME system requesting to bind as a receiver with the
     * SMSC.
     */
    private String systemId;
    /**
     * The password may be used by the SMSC for security reasons to authenticate
     * the ESME requesting to bind.
     */
    private String password;
    /**
     * Identifies the type of ESME system requesting to bind as a receiver with
     * the SMSC.
     */
    private String systemType;
    /**
     * Identifies the version of the SMPP protocol supported by the ESME.
     */
    private int interfaceVersion;
    /**
     * Type of Number (TON) for ESME address(es) served via this SMPP receiver
     * session. Set to NULL if not known.
     */
    private TON addrTon;
    /**
     * Numbering Plan Indicator (NPI) for ESME address(es) served via this SMPP
     * receiver session. Set to NULL if not known.
     */
    private NPI addrNpi;
    /**
     * A single ESME address or a range of ESME addresses served via this SMPP
     * receiver session. The parameter value is represented in UNIX regular
     * expression format (see Appendix A). Set to NULL if not known.
     */
    private String addressRange;

    /**
     * Constructor.
     */
    public BindTransmitter() {
        super(CommandId.BIND_TRANSMITTER);
    }

    /**
     * Constructor.
     *
     * @param bytes байткод PDU
     * @throws PDUException ошибка обработки PDU
     */
    public BindTransmitter(final byte[] bytes) throws PDUException {
        super(bytes);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected final void parseBody(final byte[] bytes)
            throws PDUException {
        if (getCommandId() != CommandId.BIND_TRANSMITTER) {
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
        systemType = bb.removeCString();
        if (systemType.length() > MAX_SYSTEMTYPE_LENGTH) {
            throw new PDUException("systemType field is too long");
        }
        interfaceVersion = bb.removeByte();
        int b = bb.removeByte();
        for (TON ton : TON.values()) {
            if (ton.getValue() == b) {
                addrTon = ton;
            }
        }
        if (addrTon == null) {
            addrTon = TON.RESERVED;
        }
        b = bb.removeByte();
        for (NPI npi : NPI.values()) {
            if (npi.getValue() == b) {
                addrNpi = npi;
            }
        }
        if (addrNpi == null) {
            addrNpi = NPI.RESERVED;
        }
        addressRange = bb.removeCString();
        if (addressRange.length() > MAX_ADDRESSRANGE_LENGTH) {
            throw new PDUException("addressRange field is too long");
        }
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
        if (systemType != null && systemType.length() > MAX_SYSTEMTYPE_LENGTH) {
            throw new PDUException("systemType field is too long");
        }
        bb.appendCString(systemType);
        bb.appendByte(interfaceVersion);
        bb.appendByte(addrTon != null ? addrTon.getValue() : TON.UNKNOWN.getValue());
        bb.appendByte(addrNpi != null ? addrNpi.getValue() : NPI.UNKNOWN.getValue());
        if (addressRange != null && addressRange.length() > MAX_ADDRESSRANGE_LENGTH) {
            throw new PDUException("addressRange field is too long");
        }
        bb.appendCString(addressRange);
        return bb.array();
    }

    /**
     * {@inheritDoc}
     */
    public final BindTransmitterResp getResponse() {
        BindTransmitterResp resp = new BindTransmitterResp();
        resp.setSequenceNumber(getSequenceNumber());
        return resp;
    }

    /**
     * @return ESME identifier
     */
    public final String getSystemId() {
        return systemId;
    }

    /**
     * @param systemId ESME identifier
     */
    public final void setSystemId(final String systemId) {
        this.systemId = systemId;
    }

    /**
     * @return ESME password
     */
    public final String getPassword() {
        return password;
    }

    /**
     * @param password ESME password
     */
    public final void setPassword(final String password) {
        this.password = password;
    }

    /**
     * @return ESME type identifier
     */
    public final String getSystemType() {
        return systemType;
    }

    /**
     * @param systemType ESME type identifier
     */
    public final void setSystemType(final String systemType) {
        this.systemType = systemType;
    }

    /**
     * @return ESME SMPP protocol version
     */
    public final int getInterfaceVersion() {
        return interfaceVersion;
    }

    /**
     * @param interfaceVersion ESME SMPP protocol version
     */
    public final void setInterfaceVersion(final short interfaceVersion) {
        this.interfaceVersion = interfaceVersion;
    }

    /**
     * @return Type of Number (TON) for ESME address(es)
     */
    public final TON getAddrTon() {
        return addrTon;
    }

    /**
     * @param addrTon Type of Number (TON) for ESME address(es)
     */
    public final void setAddrTon(final TON addrTon) {
        this.addrTon = addrTon;
    }

    /**
     * @return Numbering Plan Indicator (NPI) for ESME address(es)
     */
    public final NPI getAddrNpi() {
        return addrNpi;
    }

    /**
     * @param addrNpi Numbering Plan Indicator (NPI) for ESME address(es)
     */
    public final void setAddrNpi(final NPI addrNpi) {
        this.addrNpi = addrNpi;
    }

    /**
     * @return ESME addresses served via this SMPP receiver session
     */
    public final String getAddressRange() {
        return addressRange;
    }

    /**
     * @param addressRange ESME addresses served via this SMPP receiver session
     */
    public final void setAddressRange(final String addressRange) {
        this.addressRange = addressRange;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String toString() {
        return getClass().getName() + " Object {" + "\nsystemId : " + systemId
                + "\npassword : " + password + "\nsystemType : " + systemType
                + "\ninterfaceVersion : " + interfaceVersion + "\naddrTon : "
                + addrTon + "\naddrNpi : " + addrNpi + "\naddressRange : "
                + addressRange + "\n}";
    }

}
