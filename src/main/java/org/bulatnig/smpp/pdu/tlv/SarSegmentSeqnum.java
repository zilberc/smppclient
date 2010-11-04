package org.bulatnig.smpp.pdu.tlv;

import org.bulatnig.smpp.pdu.EsmClass;
import org.bulatnig.smpp.util.SmppByteBuffer;
import org.bulatnig.smpp.util.WrongLengthException;

/**
 * The sar_segment_seqnum parameter is used to indicate the sequence number of a
 * particular short message within the concatenated short message.
 *
 * @author Bulat Nigmatullin
 */
public class SarSegmentSeqnum extends TLV {
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
     * @param telematicsId значение параметра
     */
    public SarSegmentSeqnum(final short telematicsId) {
        super(ParameterTag.SAR_SEGMENT_SEQNUM);
        value = telematicsId;
    }

    /**
     * Constructor.
     *
     * @param bytes bytecode of TLV
     * @throws TLVException ошибка разбора TLV
     */
    public SarSegmentSeqnum(final byte[] bytes) throws TLVException {
        super(bytes);
    }

    @Override
    protected void parseValue(byte[] bytes, final EsmClass esmClass, final int dataCoding) throws TLVException {
        if (getTag() != ParameterTag.SAR_SEGMENT_SEQNUM) {
            throw new ClassCastException();
        }
        if (bytes.length != LENGTH) {
            throw new TLVException("Value has wrong length: " + bytes.length + " but expected " + LENGTH);
        } else {
            value = new SmppByteBuffer(bytes).removeByte();
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
