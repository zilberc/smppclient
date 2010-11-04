package org.bulatnig.smpp.pdu.tlv;

import org.bulatnig.smpp.pdu.EsmClass;
import org.bulatnig.smpp.util.SmppByteBuffer;
import org.bulatnig.smpp.util.WrongLengthException;

/**
 * The source_bearer_type parameter indicates the wireless bearer over which the
 * message originated.
 * 
 * @author Bulat Nigmatullin
 * 
 */
public class SourceBearerType extends TLV {
	/**
	 * Длина значения параметра.
	 */
	private static final int LENGTH = 1;
	/**
	 * Значение параметра.
	 */
	private BearerType value;

    private short intValue;

	/**
	 * Constructor.
	 * 
	 * @param bt
	 *            значение параметра
	 */
	public SourceBearerType(final BearerType bt) {
		super(ParameterTag.SOURCE_BEARER_TYPE);
		value = bt;
        intValue = bt.getValue();
	}

    /**
     * Constructor.
     *
     * @param intValue значение параметра
     */
    public SourceBearerType(final short intValue) {
        super(ParameterTag.SOURCE_BEARER_TYPE);
        defineValue(intValue);
    }

	/**
	 * Constructor.
	 * 
	 * @param bytes
	 *            bytecode of TLV
     * @throws TLVException ошибка разбора TLV
	 */
	public SourceBearerType(final byte[] bytes) throws TLVException {
		super(bytes);
	}

    @Override
    protected void parseValue(byte[] bytes, final EsmClass esmClass, final short dataCoding) throws TLVException {
		if (getTag() != ParameterTag.SOURCE_BEARER_TYPE) {
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
        for (BearerType bt : BearerType.values()) {
            if (bt.getValue() == intValue) {
                value = bt;
            }
        }
        if (value == null) {
            value = BearerType.RESERVED;
        }
        this.intValue = intValue;
    }

    /**
	 * @return значение параметра
	 */
	public final BearerType getValue() {
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
