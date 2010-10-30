package org.bulatnig.smpp.pdu.tlv;

import org.bulatnig.smpp.util.SMPPByteBuffer;
import org.bulatnig.smpp.util.WrongLengthException;
import org.bulatnig.smpp.util.WrongParameterException;
import org.bulatnig.smpp.pdu.EsmClass;

/**
 * The dest_network_type parameter is used to indicate a network type associated
 * with the destination address of a message. In the case that the receiving
 * system (e.g. SMSC) does not support the indicated network type, it may treat
 * this a failure and return a response PDU reporting a failure.
 *
 * @author Bulat Nigmatullin
 */
public class DestNetworkType extends TLV {
    /**
     * Длина значения параметра.
     */
    private static final int LENGTH = 1;
    /**
     * Значение параметра.
     */
    private NetworkType value;

    private short intValue;

    /**
     * Constructor.
     *
     * @param nt значение параметра
     */
    public DestNetworkType(final NetworkType nt) {
        super(ParameterTag.DEST_NETWORK_TYPE);
        value = nt;
        intValue = nt.getValue();
    }

    /**
     * Constructor.
     *
     * @param intValue значение параметра
     */
    public DestNetworkType(final short intValue) {
        super(ParameterTag.DEST_NETWORK_TYPE);
        defineValue(intValue);
    }

    /**
     * Constructor.
     *
     * @param bytes bytecode of TLV
     * @throws TLVException ошибка разбора TLV
     */
    public DestNetworkType(final byte[] bytes) throws TLVException {
        super(bytes);
    }

    @Override
    protected void parseValue(byte[] bytes, final EsmClass esmClass, final short dataCoding) throws TLVException {
        if (getTag() != ParameterTag.DEST_NETWORK_TYPE) {
            throw new ClassCastException();
        }
        if (bytes.length == LENGTH) {
            try {
                defineValue(new SMPPByteBuffer(bytes).removeByte());
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
            sbb.appendByte(intValue);
        } catch (WrongParameterException e) {
            throw new TLVException("Buffer error during parsing value", e);
        }
        return sbb.getBuffer();
    }

    private void defineValue(final short intValue) {
        for (NetworkType nt : NetworkType.values()) {
            if (nt.getValue() == intValue) {
                value = nt;
            }
        }
        if (value == null) {
            value = NetworkType.RESERVED;
        }
        this.intValue = intValue;
    }

    /**
     * @return значение параметра
     */
    public final NetworkType getValue() {
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