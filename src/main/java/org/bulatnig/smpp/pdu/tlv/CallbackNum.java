package org.bulatnig.smpp.pdu.tlv;

import org.bulatnig.smpp.util.SmppByteBuffer;
import org.bulatnig.smpp.util.WrongLengthException;
import org.bulatnig.smpp.pdu.EsmClass;

/**
 * The callback_num parameter associates a call back number with the message. In
 * TDMA networks, it is possible to send and receive multiple callback numbers
 * to/from TDMA mobile stations.
 * 
 * @author Bulat Nigmatullin
 * 
 */
public class CallbackNum extends TLV {
	/**
	 * Максимальная длина значения параметра.
	 */
	private static final int MAX_LENGTH = 19;
	/**
	 * Минимальная длина значения параметра.
	 */
	private static final int MIN_LENGTH = 4;
	/**
	 * Значение параметра.
	 */
	private String value;

	/**
	 * Constructor.
	 * 
	 * @param cn
	 *            значение параметра
     * @throws TLVException ошибка разбора TLV
	 */
	public CallbackNum(final String cn) throws TLVException {
		super(ParameterTag.CALLBACK_NUM);
		if (cn.length() >= MIN_LENGTH && cn.length() <= MAX_LENGTH) {
			value = cn;
		} else {
            throw new TLVException("Wrong value supplied " + cn.length() + ". Expected no less than " + MIN_LENGTH + " but no more than " + MAX_LENGTH + " length");
		}
	}

	/**
	 * Constructor.
	 *
	 * @param bytes							bytecode of TLV
     * @throws TLVException ошибка разбора TLV
	 */
	public CallbackNum(final byte[] bytes) throws TLVException {
		super(bytes);
	}

    @Override
    protected void parseValue(byte[] bytes, final EsmClass esmClass, final short dataCoding) throws TLVException {
		if (getTag() != ParameterTag.CALLBACK_NUM) {
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
	 * @return	значение параметра
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
