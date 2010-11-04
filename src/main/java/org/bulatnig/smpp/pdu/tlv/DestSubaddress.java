package org.bulatnig.smpp.pdu.tlv;

import org.bulatnig.smpp.util.SmppByteBuffer;
import org.bulatnig.smpp.util.WrongLengthException;
import org.bulatnig.smpp.pdu.EsmClass;

/**
 * The dest_subaddress parameter specifies a subaddress associated with the
 * destination of the message.
 * 
 * @author Bulat Nigmatullin
 * 
 */
public class DestSubaddress extends TLV {
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
	 * @param ds
	 *            значение параметра
     * @throws TLVException ошибка разбора TLV
	 */
	public DestSubaddress(final String ds) throws TLVException {
		super(ParameterTag.DEST_SUBADDRESS);
		if (ds.length() >= MIN_LENGTH && ds.length() <= MAX_LENGTH) {
			value = ds;
		} else {
            throw new TLVException("Wrong value supplied " + ds.length() + ". Expected no less than " + MIN_LENGTH + " but no more than " + MAX_LENGTH + " length");
		}
	}

	/**
	 * Constructor.
	 * 
	 * @param bytes
	 *            bytecode of TLV
     * @throws TLVException ошибка разбора TLV
	 */
	public DestSubaddress(final byte[] bytes) throws TLVException {
		super(bytes);
	}

    @Override
    protected void parseValue(byte[] bytes, final EsmClass esmClass, final short dataCoding) throws TLVException {
		if (getTag() != ParameterTag.DEST_SUBADDRESS) {
			throw new ClassCastException();
		}
		if (bytes.length >= MIN_LENGTH && bytes.length <= MAX_LENGTH) {
            try {
                value = new SmppByteBuffer(bytes).removeString(bytes.length);
            } catch (WrongLengthException e) {
                throw new TLVException("Buffer error during parsing value", e);
            }
        } else {
            throw new TLVException("Wrong value supplied " + bytes.length + ". Expected no less than " + MIN_LENGTH + " but no more than " + MAX_LENGTH + " length");
		}
    }

    @Override
    protected byte[] getValueBytes(final EsmClass esmClass, final short dataCoding) throws TLVException {
        SmppByteBuffer sbb = new SmppByteBuffer();
        sbb.appendString(value);
        return sbb.array();
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
