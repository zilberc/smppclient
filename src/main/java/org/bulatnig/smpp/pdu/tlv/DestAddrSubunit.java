package org.bulatnig.smpp.pdu.tlv;

import org.bulatnig.smpp.util.SMPPByteBuffer;
import org.bulatnig.smpp.util.WrongLengthException;
import org.bulatnig.smpp.util.WrongParameterException;
import org.bulatnig.smpp.pdu.EsmClass;

/**
 * The dest_addr_subunit parameter is used to route messages when received by a
 * mobile station, for example to a smart card in the mobile station or to an
 * external device connected to the mobile station.
 *
 * @author Bulat Nigmatullin
 */
public class DestAddrSubunit extends TLV {
    /**
     * Длина значения параметра.
     */
    private static final int LENGTH = 1;
    /**
     * Значение параметра.
     */
    private AddrSubunit value;

    private short intValue;

    /**
     * Constructor.
     *
     * @param dasv значение параметра
     */
    public DestAddrSubunit(final AddrSubunit dasv) {
        super(ParameterTag.DEST_ADDR_SUBUNIT);
        value = dasv;
        intValue = dasv.getValue();
    }

    /**
     * Constructor.
     *
     * @param intValue значение параметра
     */
    public DestAddrSubunit(final short intValue) {
        super(ParameterTag.DEST_ADDR_SUBUNIT);
        defineValue(intValue);
    }

    /**
     * Constructor.
     *
     * @param bytes bytecode of TLV
     * @throws TLVException ошибка разбора TLV
     */
    public DestAddrSubunit(final byte[] bytes) throws TLVException {
        super(bytes);
    }

    @Override
    protected void parseValue(byte[] bytes, final EsmClass esmClass, final short dataCoding) throws TLVException {
        if (getTag() != ParameterTag.DEST_ADDR_SUBUNIT) {
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
        for (AddrSubunit as : AddrSubunit.values()) {
            if (as.getValue() == intValue) {
                value = as;
            }
        }
        if (value == null) {
            value = AddrSubunit.RESERVED;
        }
        this.intValue = intValue;
    }

    /**
     * @return значение параметра
     */
    public final AddrSubunit getValue() {
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
