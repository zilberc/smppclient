package org.bulatnig.smpp.pdu.tlv;

import org.bulatnig.smpp.pdu.EsmClass;
import org.bulatnig.smpp.util.SmppByteBuffer;

/**
 * The source_addr_subunit parameter is used to indicate where a message
 * originated in the mobile station, for example a smart card in the mobile
 * station or an external device connected to the mobile station.
 *
 * @author Bulat Nigmatullin
 */
public class SourceAddrSubunit extends TLV {
    /**
     * Длина значения параметра.
     */
    private static final int LENGTH = 1;
    /**
     * Значение параметра.
     */
    private AddrSubunit value;

    private int intValue;

    /**
     * Constructor.
     *
     * @param sas значение параметраа
     */
    public SourceAddrSubunit(final AddrSubunit sas) {
        super(ParameterTag.SOURCE_ADDR_SUBUNIT);
        value = sas;
        intValue = sas.getValue();
    }

    /**
     * Constructor.
     *
     * @param intValue значение параметра
     */
    public SourceAddrSubunit(final short intValue) {
        super(ParameterTag.SOURCE_ADDR_SUBUNIT);
        defineValue(intValue);
    }

    /**
     * Constructor.
     *
     * @param bytes bytecode of TLV
     * @throws TLVException ошибка разбора TLV
     */
    public SourceAddrSubunit(final byte[] bytes) throws TLVException {
        super(bytes);
    }

    @Override
    protected void parseValue(byte[] bytes, final EsmClass esmClass, final int dataCoding) throws TLVException {
        if (getTag() != ParameterTag.SOURCE_ADDR_SUBUNIT) {
            throw new ClassCastException();
        }
        if (bytes.length == LENGTH) {
            defineValue(new SmppByteBuffer(bytes).removeByte());
        } else {
            throw new TLVException("Value has wrong length: " + bytes.length + " but expected " + LENGTH);
        }
    }

    @Override
    protected byte[] getValueBytes(final EsmClass esmClass, final short dataCoding) throws TLVException {
        return new SmppByteBuffer().appendByte(intValue).array();
    }

    private void defineValue(final int intValue) {
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

    public final int getIntValue() {
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
