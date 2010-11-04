package org.bulatnig.smpp.pdu.tlv;

import org.bulatnig.smpp.util.SmppByteBuffer;
import org.bulatnig.smpp.util.WrongLengthException;
import org.bulatnig.smpp.pdu.EsmClass;

/**
 * The callback_num_atag parameter associates an alphanumeric display with the
 * call back number.
 * 
 * @author Bulat Nigmatullin
 * 
 */
public class CallbackNumAtag extends TLV {
	/**
	 * Максимальная длина значения параметра.
	 */
	private static final int MAX_LENGTH = 65;
	
	/**
	 * Значение параметра.
	 */
	private String value;

	/**
	 * Constructor.
	 * 
	 * @param cna					значение параметра
     * @throws TLVException ошибка разбора TLV
	 */
	public CallbackNumAtag(final String cna) throws TLVException {
		super(ParameterTag.CALLBACK_NUM_ATAG);
		if (cna.length() <= MAX_LENGTH) {
			value = cna;
		} else {
            throw new TLVException("Value has wrong length: " + cna.length() + " but expected no more than " + MAX_LENGTH);
		}
	}

	/**
	 * Constructor.
	 * 
	 * @param bytes							bytecode of TLV
     * @throws TLVException ошибка разбора TLV
	 */
	public CallbackNumAtag(final byte[] bytes) throws TLVException {
		super(bytes);
	}

    @Override
    protected void parseValue(byte[] bytes, final EsmClass esmClass, final short dataCoding) throws TLVException {
		if (getTag() != ParameterTag.CALLBACK_NUM_ATAG) {
			throw new ClassCastException();
		}
		if (bytes.length <= MAX_LENGTH) {
            try {
                value = new SmppByteBuffer(bytes).removeString(bytes.length);
            } catch (WrongLengthException e) {
                throw new TLVException("Buffer error during parsing value", e);
            }
        } else {
            throw new TLVException("Value has wrong length: " + bytes.length + " but expected no more than " + MAX_LENGTH);
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
