package org.bulatnig.smpp.pdu.tlv;

import org.bulatnig.smpp.pdu.EsmClass;
import org.bulatnig.smpp.util.SmppByteBuffer;

/**
 * An ESME may use the set_dpf parameter to request the setting of a delivery
 * pending flag (DPF) for certain delivery failure scenarios, such as<br/> - MS
 * is unavailable for message delivery (as indicated by the HLR)<br/>
 * <p/>
 * The SMSC should respond to such a request with an alert_notification PDU when
 * it detects that the destination MS has become available.<br/>
 * <p/>
 * The delivery failure scenarios under which DPF is set is SMSC implementation
 * and network implementation specific. If a delivery pending flag is set by the
 * SMSC or network (e.g. HLR), then the SMSC should indicate this to the ESME in
 * the data_sm_resp message via the dpf_result parameter.
 *
 * @author Bulat Nigmatullin
 */
public class SetDpf extends TLV {
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
     * @param dpf значение параметра
     */
    public SetDpf(final short dpf) {
        super(ParameterTag.SET_DPF);
        value = dpf;
    }

    /**
     * Constructor.
     *
     * @param bytes bytecode of TLV
     * @throws TLVException ошибка разбора TLV
     */
    public SetDpf(final byte[] bytes) throws TLVException {
        super(bytes);
    }

    @Override
    protected void parseValue(byte[] bytes, final EsmClass esmClass, final int dataCoding) throws TLVException {
        if (getTag() != ParameterTag.SET_DPF) {
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
