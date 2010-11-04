package org.bulatnig.smpp.pdu.tlv;

import org.bulatnig.smpp.util.SmppByteBuffer;
import org.bulatnig.smpp.util.WrongLengthException;
import org.bulatnig.smpp.util.WrongParameterException;
import org.bulatnig.smpp.pdu.EsmClass;

/**
 * The ms_availability_status parameter is used in the alert_notification
 * operation to indicate the availability state of the MS to the ESME.<br/>
 * 
 * If the SMSC does not include the parameter in the alert_notification
 * operation, the ESME should assume that the MS is in an “available” state.
 * 
 * @author Bulat Nigmatullin
 * 
 */
public class MsAvailabilityStatus extends TLV {
	/**
	 * Длина значения параметра.
	 */
	private static final int LENGTH = 1;
	/**
	 * Значение параметра.
	 */
	private AvailabilityStatus value;

    private short intValue;

	/**
	 * Constructor.
	 * 
	 * @param status
	 *            значение параметра
	 */
	public MsAvailabilityStatus(final AvailabilityStatus status) {
		super(ParameterTag.MS_AVAILABILITY_STATUS);
		value = status;
        intValue = status.getValue();
	}

    /**
     * Constructor.
     *
     * @param intValue значение параметра
     */
    public MsAvailabilityStatus(final short intValue) {
        super(ParameterTag.MS_AVAILABILITY_STATUS);
        defineValue(intValue);
    }

	/**
	 * Constructor.
	 * 
	 * @param bytes
	 *            bytecode of TLV
     * @throws TLVException ошибка разбора TLV
	 */
	public MsAvailabilityStatus(final byte[] bytes) throws TLVException {
		super(bytes);
	}

    @Override
    protected void parseValue(byte[] bytes, final EsmClass esmClass, final short dataCoding) throws TLVException {
		if (getTag() != ParameterTag.MS_AVAILABILITY_STATUS) {
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
        for (AvailabilityStatus as : AvailabilityStatus.values()) {
            if (as.getValue() == intValue) {
                value = as;
            }
        }
        if (value == null) {
            value = AvailabilityStatus.RESERVED;
        }
        this.intValue = intValue;
    }

    /**
	 * @return значение параметра
	 */
	public final AvailabilityStatus getValue() {
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
