package org.bulatnig.smpp.pdu.tlv;

import org.bulatnig.smpp.pdu.EsmClass;
import org.bulatnig.smpp.util.SmppByteBuffer;
import org.bulatnig.smpp.util.WrongLengthException;

/**
 * The source_port parameter is used to indicate the application port number
 * associated with the source address of the message.
 * 
 * @author Bulat Nigmatullin
 * 
 */
public class SourcePort extends TLV {
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
	 * @param sourcePort
	 *            значение параметра
	 */
	public SourcePort(final int sourcePort) {
		super(ParameterTag.SOURCE_PORT);
		value = sourcePort;
	}

	/**
	 * Constructor.
	 * 
	 * @param bytes
	 *            bytecode of TLV
     * @throws TLVException ошибка разбора TLV
	 */
	public SourcePort(final byte[] bytes) throws TLVException {
		super(bytes);
	}

    @Override
    protected void parseValue(byte[] bytes, final EsmClass esmClass, final short dataCoding) throws TLVException {
		if (getTag() != ParameterTag.SOURCE_PORT) {
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
