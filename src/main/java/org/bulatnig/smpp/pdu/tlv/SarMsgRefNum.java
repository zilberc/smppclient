package org.bulatnig.smpp.pdu.tlv;

import org.bulatnig.smpp.pdu.EsmClass;
import org.bulatnig.smpp.util.SmppByteBuffer;

/**
 * The sar_msg_ref_num parameter is used to indicate the reference number for a
 * particular concatenated short message.
 *
 * @author Bulat Nigmatullin
 */
public class SarMsgRefNum extends TLV {
    /**
     * Длина значения параметра.
     */
    private static final int LENGTH = 2;
    /**
     * Значение параметра.
     */
    private int value;

    /**
     * Costructor.
     *
     * @param smrn значение параметра
     */
    public SarMsgRefNum(final int smrn) {
        super(ParameterTag.SAR_MSG_REF_NUM);
        value = smrn;
    }

    /**
     * Constructor.
     *
     * @param bytes bytecode of TLV
     * @throws TLVException ошибка разбора TLV
     */
    public SarMsgRefNum(final byte[] bytes) throws TLVException {
        super(bytes);
    }

    @Override
    protected void parseValue(byte[] bytes, final EsmClass esmClass, final int dataCoding) throws TLVException {
        if (getTag() != ParameterTag.SAR_MSG_REF_NUM) {
            throw new ClassCastException();
        }
        if (bytes.length == LENGTH) {
            value = new SmppByteBuffer(bytes).removeShort();
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
