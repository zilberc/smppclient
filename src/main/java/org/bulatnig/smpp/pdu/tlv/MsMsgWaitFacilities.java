package org.bulatnig.smpp.pdu.tlv;

import org.bulatnig.smpp.util.SmppByteBuffer;
import org.bulatnig.smpp.util.WrongLengthException;
import org.bulatnig.smpp.util.WrongParameterException;
import org.bulatnig.smpp.pdu.EsmClass;

/**
 * The ms_msg_wait_facilities parameter allows an indication to be provided to
 * an MS that there are messages waiting for the subscriber on systems on the
 * PLMN. The indication can be an icon on the MS screen or other MMI indication.<br/>
 * 
 * The ms_msg_wait_facilities can also specify the type of message associated
 * with the message waiting indication.
 * 
 * @author Bulat Nigmatullin
 * 
 */
public class MsMsgWaitFacilities extends TLV {
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
	 * @param mmwf	значение параметра
	 */
	public MsMsgWaitFacilities(final short mmwf) {
		super(ParameterTag.MS_MSG_WAIT_FACILITIES);
		value = mmwf;
	}

	/**
	 * Constructor.
	 * 
	 * @param bytes							bytecode of TLV
     * @throws TLVException ошибка разбора TLV
	 */
	public MsMsgWaitFacilities(final byte[] bytes) throws TLVException {
		super(bytes);
	}

    @Override
    protected void parseValue(byte[] bytes, final EsmClass esmClass, final short dataCoding) throws TLVException {
		if (getTag() != ParameterTag.MS_MSG_WAIT_FACILITIES) {
			throw new ClassCastException();
		}
		if (bytes.length == LENGTH) {
            try {
                value = new SmppByteBuffer(bytes).removeByte();
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
            sbb.appendByte(value);
        } catch (WrongParameterException e) {
            throw new TLVException("Buffer error during parsing value", e);
        }
        return sbb.array();
    }

    /**
	 * @return	значение параметра
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
