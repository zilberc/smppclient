package org.bulatnig.smpp.pdu.tlv;

import org.bulatnig.smpp.pdu.EsmClass;
import org.bulatnig.smpp.util.SmppByteBuffer;

/**
 * The ussd_service_op parameter is required to define the USSD service
 * operation when SMPP is being used as an interface to a (GSM) USSD system.
 *
 * @author Bulat Nigmatullin
 */
public class UssdServiceOp extends TLV {
    /**
     * Длина значения параметра.
     */
    private static final int LENGTH = 1;
    /**
     * Значение параметра.
     */
    private ServiceOperation value;

    private int intValue;

    /**
     * Constructor.
     *
     * @param so значение параметра
     */
    public UssdServiceOp(final ServiceOperation so) {
        super(ParameterTag.USSD_SERVICE_OP);
        value = so;
        intValue = so.getValue();
    }

    /**
     * Constructor.
     *
     * @param intValue значение параметра
     */
    public UssdServiceOp(final short intValue) {
        super(ParameterTag.USSD_SERVICE_OP);
        defineValue(intValue);
    }

    /**
     * Constructor.
     *
     * @param bytes bytecode of TLV
     * @throws TLVException ошибка разбора TLV
     */
    public UssdServiceOp(final byte[] bytes) throws TLVException {
        super(bytes);
    }

    @Override
    protected void parseValue(byte[] bytes, final EsmClass esmClass, final int dataCoding) throws TLVException {
        if (getTag() != ParameterTag.USSD_SERVICE_OP) {
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
        for (ServiceOperation so : ServiceOperation.values()) {
            if (so.getValue() == intValue) {
                value = so;
            }
        }
        if (value == null) {
            value = ServiceOperation.RESERVED;
        }
        this.intValue = intValue;
    }

    /**
     * @return значение параметра
     */
    public final ServiceOperation getValue() {
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
