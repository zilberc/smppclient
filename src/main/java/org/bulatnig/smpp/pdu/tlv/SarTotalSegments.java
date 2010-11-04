package org.bulatnig.smpp.pdu.tlv;

import org.bulatnig.smpp.pdu.EsmClass;
import org.bulatnig.smpp.util.SmppByteBuffer;

/**
 * The sar_total_segments parameter is used to indicate the total number of
 * short messages within the concatenated short message.
 *
 * @author Bulat Nigmatullin
 */
public class SarTotalSegments extends TLV {
    /**
     * Длина значения параметра.
     */
    private static final int LENGTH = 1;
    /**
     * Значение параметра.
     */
    private int value;

    /**
     * Constructor.
     *
     * @param sarTotalSegments значение параметра
     */
    public SarTotalSegments(final short sarTotalSegments) {
        super(ParameterTag.SAR_TOTAL_SEGMENTS);
        value = sarTotalSegments;
    }

    /**
     * Constructor.
     *
     * @param bytes bytecode of TLV
     * @throws TLVException ошибка разбора TLV
     */
    public SarTotalSegments(final byte[] bytes) throws TLVException {
        super(bytes);
    }

    @Override
    protected void parseValue(byte[] bytes, final EsmClass esmClass, final int dataCoding) throws TLVException {
        if (getTag() != ParameterTag.SAR_TOTAL_SEGMENTS) {
            throw new ClassCastException();
        }
        if (bytes.length == LENGTH) {
            value = new SmppByteBuffer(bytes).removeByte();
        } else {
            throw new TLVException("Value has wrong length: " + bytes.length + " but expected " + LENGTH);
        }
    }

    @Override
    protected byte[] getValueBytes(final EsmClass esmClass, final short dataCoding) throws TLVException {
        return new SmppByteBuffer().appendByte(value).array();
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
