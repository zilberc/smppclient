package org.bulatnig.smpp.pdu.tlv;

import org.bulatnig.smpp.util.SMPPByteBuffer;
import org.bulatnig.smpp.util.WrongLengthException;
import org.bulatnig.smpp.util.WrongParameterException;
import org.bulatnig.smpp.pdu.EsmClass;

/**
 * The more_messages_to_send parameter is used by the ESME in the submit_sm and
 * data_sm operations to indicate to the SMSC that there are further messages
 * for the same destination SME. The SMSC may use this setting for network
 * resource optimization.
 * 
 * @author Bulat Nigmatullin
 * 
 */
public class MoreMessagesToSend extends TLV {
	/**
	 * Длина значения параметра.
	 */
	private static final int LENGTH = 1;
	/**
	 * Значение параметра.
	 */
	private short value;

	/**
	 * Constructor.
	 * 
	 * @param mmts	значение параметра
	 */
	public MoreMessagesToSend(final short mmts) {
		super(ParameterTag.MORE_MESSAGES_TO_SEND);
		value = mmts;
	}

	/**
	 * Constructor.
	 * 
	 * @param bytes
	 *            bytecode of TLV
     * @throws TLVException ошибка разбора TLV
	 */
	public MoreMessagesToSend(final byte[] bytes) throws TLVException {
		super(bytes);
	}

    @Override
    protected void parseValue(byte[] bytes, final EsmClass esmClass, final short dataCoding) throws TLVException {
		if (getTag() != ParameterTag.MORE_MESSAGES_TO_SEND) {
			throw new ClassCastException();
		}
		if (bytes.length == LENGTH) {
            try {
                value = new SMPPByteBuffer(bytes).removeByte();
            } catch (WrongLengthException e) {
                throw new TLVException("Buffer error during parsing value", e);
            }
		} else {
            throw new TLVException("Value has wrong length: " + bytes.length + " but expected " + LENGTH);
		}
    }

    @Override
    protected byte[] getValueBytes(final EsmClass esmClass, final short dataCoding) throws TLVException {
        SMPPByteBuffer sbb = new SMPPByteBuffer();
        try {
            sbb.appendByte(value);
        } catch (WrongParameterException e) {
            throw new TLVException("Buffer error during parsing value", e);
        }
        return sbb.getBuffer();
    }

    /**
	 * @return значение параметра
	 */
	public final short getValue() {
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