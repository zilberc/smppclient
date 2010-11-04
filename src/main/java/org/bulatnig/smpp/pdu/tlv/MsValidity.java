package org.bulatnig.smpp.pdu.tlv;

import org.bulatnig.smpp.util.SmppByteBuffer;
import org.bulatnig.smpp.util.WrongLengthException;
import org.bulatnig.smpp.util.WrongParameterException;
import org.bulatnig.smpp.pdu.EsmClass;

/**
 * The ms_validity parameter is used to provide an MS with validity information
 * associated with the received short message.
 * 
 * @author Bulat Nigmatullin
 * 
 */
public class MsValidity extends TLV {
	/**
	 * Длина значения параметра.
	 */
	private static final int LENGTH = 1;
	/**
	 * Значение параметра.
	 */
	private Validity value;

    private short intValue;

	/**
	 * Constructor.
	 * 
	 * @param validity
	 *            значение параметра
	 */
	public MsValidity(final Validity validity) {
		super(ParameterTag.MS_VALIDITY);
		value = validity;
        intValue = validity.getValue();
	}

    /**
     * Constructor.
     *
     * @param intValue значение параметра
     */
    public MsValidity(final short intValue) {
        super(ParameterTag.MS_VALIDITY);
        defineValue(intValue);
    }

	/**
	 * Constructor.
	 * 
	 * @param bytes
	 *            bytecode of TLV
     * @throws TLVException ошибка разбора TLV
	 */
	public MsValidity(final byte[] bytes) throws TLVException {
		super(bytes);
	}

    @Override
    protected void parseValue(byte[] bytes, final EsmClass esmClass, final short dataCoding) throws TLVException {
		if (getTag() != ParameterTag.MS_VALIDITY) {
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
        SmppByteBuffer sbb = new SmppByteBuffer();
        try {
            sbb.appendByte(intValue);
        } catch (WrongParameterException e) {
            throw new TLVException("Buffer error during parsing value", e);
        }
        return sbb.array();
    }

    private void defineValue(final short intValue) {
        for (Validity v : Validity.values()) {
            if (v.getValue() == intValue) {
                value = v;
            }
        }
        if (value == null) {
            value = Validity.RESERVED;
        }
        this.intValue = intValue;
    }

    /**
	 * @return значение параметра
	 */
	public final Validity getValue() {
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
