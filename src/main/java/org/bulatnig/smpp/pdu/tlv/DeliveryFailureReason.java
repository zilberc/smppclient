package org.bulatnig.smpp.pdu.tlv;

import org.bulatnig.smpp.pdu.EsmClass;
import org.bulatnig.smpp.util.SmppByteBuffer;

/**
 * The delivery_failure_reason parameter is used in the data_sm_resp operation
 * to indicate the outcome of the message delivery attempt (only applicable for
 * transaction message mode). If a delivery failure due to a network error is
 * indicated, the ESME may check the network_error_code parameter (if present)
 * for the actual network error code.<br/>
 * <p/>
 * The delivery_failure_reason parameter is not included if the delivery attempt
 * was successful.
 *
 * @author Bulat Nigmatullin
 */
public class DeliveryFailureReason extends TLV {
    /**
     * Длина значения параметра.
     */
    private static final int LENGTH = 1;
    /**
     * Значение параметра.
     */
    private FailureReason value;

    private int intValue;

    /**
     * Constructor.
     *
     * @param reason значение параметра
     */
    public DeliveryFailureReason(final FailureReason reason) {
        super(ParameterTag.DELIVERY_FAILURE_REASON);
        value = reason;
        intValue = reason.getValue();
    }

    /**
     * Constructor.
     *
     * @param intValue значение параметра
     */
    public DeliveryFailureReason(final short intValue) {
        super(ParameterTag.DELIVERY_FAILURE_REASON);
        defineValue(intValue);
    }

    /**
     * Constructor.
     *
     * @param bytes bytecode of TLV
     * @throws TLVException ошибка разбора TLV
     */
    public DeliveryFailureReason(final byte[] bytes) throws TLVException {
        super(bytes);
    }

    @Override
    protected void parseValue(byte[] bytes, final EsmClass esmClass, final int dataCoding) throws TLVException {
        if (getTag() != ParameterTag.DELIVERY_FAILURE_REASON) {
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
        for (FailureReason fr : FailureReason.values()) {
            if (fr.getValue() == intValue) {
                value = fr;
            }
        }
        if (value == null) {
            value = FailureReason.RESERVED;
        }
        this.intValue = intValue;
    }

    /**
     * @return значение параметра
     */
    public final FailureReason getValue() {
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
