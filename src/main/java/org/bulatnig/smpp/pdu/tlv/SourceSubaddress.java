package org.bulatnig.smpp.pdu.tlv;

import org.bulatnig.smpp.util.SMPPByteBuffer;
import org.bulatnig.smpp.util.WrongLengthException;
import org.bulatnig.smpp.pdu.EsmClass;

/**
 * The source_subaddress parameter specifies a subaddress associated with the
 * originator of the message.
 *
 * @author Bulat Nigmatullin
 */
public class SourceSubaddress extends TLV {
    /**
     * Максимальная длина значения параметра.
     */
    private static final int MAX_LENGTH = 23;
    /**
     * Минимальная длина значения параметра.
     */
    private static final int MIN_LENGTH = 2;
    /**
     * Значение параметра.
     */
    private String value;

    /**
     * Constructor.
     *
     * @param ss значение параметра
     * @throws TLVException ошибка разбора TLV
     */
    public SourceSubaddress(final String ss) throws TLVException {
        super(ParameterTag.SOURCE_SUBADDRESS);
        if (ss.length() >= MIN_LENGTH && ss.length() <= MAX_LENGTH) {
            value = ss;
        } else {
            throw new TLVException("Wrong value supplied " + ss.length() + ". Expected no less than " + MIN_LENGTH + " but no more than " + MAX_LENGTH + " length");
        }
    }

    /**
     * Constructor.
     *
     * @param bytes bytecode of TLV
     * @throws TLVException ошибка разбора TLV
     */
    public SourceSubaddress(final byte[] bytes) throws TLVException {
        super(bytes);
    }

    @Override
    protected void parseValue(byte[] bytes, final EsmClass esmClass, final short dataCoding) throws TLVException {
        if (getTag() != ParameterTag.SOURCE_SUBADDRESS) {
            throw new ClassCastException();
        }
        if (bytes.length >= MIN_LENGTH && bytes.length <= MAX_LENGTH) {
            try {
                value = new SMPPByteBuffer(bytes).removeString(bytes.length);
            } catch (WrongLengthException e) {
                throw new TLVException("Buffer error during parsing value", e);
            }
        } else {
            throw new TLVException("Wrong value supplied " + bytes.length + ". Expected no less than " + MIN_LENGTH + " but no more than " + MAX_LENGTH + " length");
        }
    }

    @Override
    protected byte[] getValueBytes(final EsmClass esmClass, final short dataCoding) throws TLVException {
        SMPPByteBuffer sbb = new SMPPByteBuffer();
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
