package org.bulatnig.smpp.pdu.tlv;

import org.bulatnig.smpp.pdu.EsmClass;
import org.bulatnig.smpp.util.SmppByteBuffer;
import org.bulatnig.smpp.util.WrongLengthException;

/**
 * The privacy_indicator indicates the privacy level of the message.
 * 
 * @author Bulat Nigmatullin
 * 
 */
public class PrivacyIndicator extends TLV {
	/**
	 * Длина значения параметра.
	 */
	private static final int LENGTH = 1;
	/**
	 * Значение параметра.
	 */
	private Privacy value;

    private short intValue;

	/**
	 * Constructor.
	 * 
	 * @param p
	 *            значение параметра
	 */
	public PrivacyIndicator(final Privacy p) {
		super(ParameterTag.PRIVACY_INDICATOR);
		value = p;
        intValue = p.getValue();
	}

    /**
     * Constructor.
     *
     * @param intValue значение параметра
     */
    public PrivacyIndicator(final short intValue) {
        super(ParameterTag.PRIVACY_INDICATOR);
        defineValue(intValue);
    }

	/**
	 * Constructor.
	 * 
	 * @param bytes
	 *            bytecode of TLV
     * @throws TLVException ошибка разбора TLV
	 */
	public PrivacyIndicator(final byte[] bytes) throws TLVException {
		super(bytes);
	}

    @Override
    protected void parseValue(byte[] bytes, final EsmClass esmClass, final short dataCoding) throws TLVException {
		if (getTag() != ParameterTag.PRIVACY_INDICATOR) {
			throw new ClassCastException();
		}
		if (bytes.length == LENGTH) {
            try {
                defineValue(new SmppByteBuffer(bytes).removeByte());
            } catch (WrongLengthException e) {
                throw new TLVException("Buffer error during parsing value", e);
            }
		} else {
            throw new TLVException("Value has wrong length: " + bytes.length + " but expected " + LENGTH);
		}
    }

    @Override
    protected byte[] getValueBytes(final EsmClass esmClass, final short dataCoding) throws TLVException {
        return new SmppByteBuffer().appendByte(intValue).array();
    }

    private void defineValue(final short intValue) {
        for (Privacy p : Privacy.values()) {
            if (p.getValue() == intValue) {
                value = p;
            }
        }
        if (value == null) {
            value = Privacy.RESERVED;
        }
        this.intValue = intValue;
    }

    /**
	 * @return значение параметра
	 */
	public final Privacy getValue() {
		return value;
	}

    public final short getIntValue() {
        return intValue;
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
