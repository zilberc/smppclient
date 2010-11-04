package org.bulatnig.smpp.pdu.tlv;

import org.bulatnig.smpp.pdu.EsmClass;
import org.bulatnig.smpp.util.SmppByteBuffer;

/**
 * The dpf_result parameter is used in the data_sm_resp PDU to indicate if
 * delivery pending flag (DPF) was set for a delivery failure of the short
 * message..<br/>
 * <p/>
 * If the dpf_result parameter is not included in the data_sm_resp PDU, the ESME
 * should assume that DPF is not set.<br/>
 * <p/>
 * Currently this parameter is only applicable for the Transaction message mode.
 *
 * @author Bulat Nigmatullin
 */
public class DpfResult extends TLV {
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
     * @param dpfResult значение параметра
     */
    public DpfResult(final short dpfResult) {
        super(ParameterTag.DPF_RESULT);
        value = dpfResult;
    }

    /**
     * Constructor.
     *
     * @param bytes bytecode of TLV
     * @throws TLVException ошибка разбора TLV
     */
    public DpfResult(final byte[] bytes) throws TLVException {
        super(bytes);
    }

    @Override
    protected void parseValue(byte[] bytes, final EsmClass esmClass, final int dataCoding) throws TLVException {
        if (getTag() != ParameterTag.DPF_RESULT) {
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
