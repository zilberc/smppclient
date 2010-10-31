package org.bulatnig.smpp.pdu.tlv;

import org.bulatnig.smpp.util.SmppByteBuffer;
import org.bulatnig.smpp.util.WrongLengthException;
import org.bulatnig.smpp.util.WrongParameterException;
import org.bulatnig.smpp.pdu.EsmClass;

/**
 * The dest_bearer_type parameter is used to request the desired bearer for
 * delivery of the message to the destination address. In the case that the
 * receiving system (e.g. SMSC) does not support the indicated bearer type, it
 * may treat this a failure and return a response PDU reporting a failure.
 *
 * @author Bulat Nigmatullin
 */
public class DestBearerType extends TLV {
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
     * @param bt значение параметра
     */
    public DestBearerType(final BearerType bt) {
        super(ParameterTag.DEST_BEARER_TYPE);
        value = bt;
        intValue = bt.getValue();
    }

    /**
     * Constructor.
     *
     * @param intValue значение параметра
     */
    public DestBearerType(final short intValue) {
        super(ParameterTag.DEST_BEARER_TYPE);
        defineValue(intValue);
    }

    /**
     * Constructor.
     *
     * @param bytes bytecode of TLV
     * @throws TLVException ошибка разбора TLV
     */
    public DestBearerType(final byte[] bytes) throws TLVException {
        super(bytes);
    }

    @Override
    protected void parseValue(byte[] bytes, final EsmClass esmClass, final short dataCoding) throws TLVException {
        if (getTag() != ParameterTag.DEST_BEARER_TYPE) {
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
        return sbb.getBuffer();
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
