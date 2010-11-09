package org.bulatnig.smpp.pdu.tlv;

import org.bulatnig.smpp.pdu.EsmClass;
import org.bulatnig.smpp.util.SmppByteBuffer;

/**
 * The its_session_info parameter is a required parameter for the CDMA
 * Interactive Teleservice as defined by the Korean PCS carriers [KORITS]. It
 * contains control information for the interactive session between an MS and an
 * ESME.
 *
 * @author Bulat Nigmatullin
 */
public class ItsSessionInfo extends TLV {
    /**
     * Длина значения параметра.
     */
    private static final int LENGTH = 2;
    /**
     * Значение параметра.
     */
    private int value;

    /**
     * Constructor.
     *
     * @param sessionInfo значение параметра
     */
    public ItsSessionInfo(final int sessionInfo) {
        super(ParameterTag.ITS_SESSION_INFO);
        value = sessionInfo;
    }

    /**
     * Constructor.
     *
     * @param bytes bytecode of TLV
     * @throws TLVException ошибка разбора TLV
     */
    public ItsSessionInfo(final byte[] bytes) throws TLVException {
        super(bytes);
    }

    @Override
    protected void parseValue(byte[] bytes, final EsmClass esmClass, final int dataCoding) throws TLVException {
        if (getTag() != ParameterTag.ITS_SESSION_INFO) {
            throw new ClassCastException();
        }
        if (bytes.length == LENGTH) {
            value = new SmppByteBuffer(bytes).removeShort();
        } else {
            throw new TLVException("Value has wrong length: " + bytes.length + " but expected " + LENGTH);
        }
    }

    @Override
    protected byte[] getValueBytes(final EsmClass esmClass, final short dataCoding) throws TLVException {
        return new SmppByteBuffer().appendShort(value).array();
    }

    /**
     * @return значение параметра
     */
    public final int getValue() {
        return value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String toString() {
        return getClass().getName() + " Object {" + "\nvalue : " + value
                + "\n}";
    }

}
