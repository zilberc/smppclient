package org.bulatnig.smpp.pdu.tlv;

import org.bulatnig.smpp.pdu.EsmClass;
import org.bulatnig.smpp.util.SmppByteBuffer;
import org.bulatnig.smpp.util.WrongLengthException;

/**
 * The sms_signal parameter is used to provide a TDMA MS with alert tone
 * information associated with the received short message.
 * 
 * @author Bulat Nigmatullin
 * 
 */
public class SmsSignal extends TLV {
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
	 * @param telematicsId
	 *            значение параметра
	 */
	public SmsSignal(final int telematicsId) {
		super(ParameterTag.SMS_SIGNAL);
		value = telematicsId;
	}

	/**
	 * Constructor.
	 * 
	 * @param bytes
	 *            bytecode of TLV
     * @throws TLVException ошибка разбора TLV
	 */
	public SmsSignal(final byte[] bytes) throws TLVException {
		super(bytes);
	}

    @Override
    protected void parseValue(byte[] bytes, final EsmClass esmClass, final short dataCoding) throws TLVException {
		if (getTag() != ParameterTag.SMS_SIGNAL) {
			throw new ClassCastException();
		}
		if (bytes.length == LENGTH) {
            try {
                value = new SmppByteBuffer(bytes).removeShort();
            } catch (WrongLengthException e) {
                throw new TLVException("Buffer error during parsing value", e);
            }
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
