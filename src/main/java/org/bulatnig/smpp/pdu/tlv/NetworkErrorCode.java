package org.bulatnig.smpp.pdu.tlv;

import org.bulatnig.smpp.util.SmppByteBuffer;
import org.bulatnig.smpp.util.WrongLengthException;
import org.bulatnig.smpp.pdu.EsmClass;

/**
 * The network_error_code parameter is used to indicate the actual network error
 * code for a delivery failure. The network error code is technology specific.
 *
 * @author Bulat Nigmatullin
 */
public class NetworkErrorCode extends TLV {
    /**
     * Длина значения параметра.
     */
    private static final int LENGTH = 3;
    /**
     * Значение параметра.
     */
    private String value;

    /**
     * Constructor.
     *
     * @param errorCode значение параметра
     * @throws TLVException ошибка разбора TLV
     */
    public NetworkErrorCode(final String errorCode) throws TLVException {
        super(ParameterTag.NETWORK_ERROR_CODE);
        if (errorCode.length() == LENGTH) {
            value = errorCode;
        } else {
            throw new TLVException("Value has wrong length: " + errorCode.length() + " but expected " + LENGTH);
        }
    }

    /**
     * Constructor.
     *
     * @param bytes bytecode of TLV
     * @throws TLVException ошибка разбора TLV
     */
    public NetworkErrorCode(final byte[] bytes) throws TLVException {
        super(bytes);
    }

    @Override
    protected void parseValue(byte[] bytes, final EsmClass esmClass, final short dataCoding) throws TLVException {
        if (getTag() != ParameterTag.NETWORK_ERROR_CODE) {
            throw new ClassCastException();
        }
        if (bytes.length == LENGTH) {
            try {
                value = new SmppByteBuffer(bytes).removeString(bytes.length);
            } catch (WrongLengthException e) {
                throw new TLVException("Buffer error during parsing value", e);
            }
        } else {
            throw new TLVException("Value has wrong length: " + bytes.length + " but expected " + LENGTH);
        }
    }

    @Override
    protected byte[] getValueBytes(final EsmClass esmClass, final short dataCoding) throws TLVException {
        SmppByteBuffer sbb = new SmppByteBuffer();
        sbb.appendString(value);
        return sbb.getBuffer();
    }

    /**
     * @return значение параметра
     */
    public final String getValue() {
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
