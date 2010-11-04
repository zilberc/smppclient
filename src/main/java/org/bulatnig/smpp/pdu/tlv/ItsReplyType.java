package org.bulatnig.smpp.pdu.tlv;

import org.bulatnig.smpp.util.SmppByteBuffer;
import org.bulatnig.smpp.util.WrongLengthException;
import org.bulatnig.smpp.util.WrongParameterException;
import org.bulatnig.smpp.pdu.EsmClass;

/**
 * The its_reply_type parameter is a required parameter for the CDMA Interactive
 * Teleservice as defined by the Korean PCS carriers [KORITS]. It indicates and
 * controls the MS user’s reply method to an SMS delivery message received from
 * the ESME.
 *
 * @author Bulat Nigmatullin
 */
public class ItsReplyType extends TLV {
    /**
     * Длина значения переменной.
     */
    private static final int LENGTH = 1;
    /**
     * Значение переменной.
     */
    private ReplyType value;

    private short intValue;

    /**
     * Constructor.
     *
     * @param rt значение переменной
     */
    public ItsReplyType(final ReplyType rt) {
        super(ParameterTag.ITS_REPLY_TYPE);
        value = rt;
        intValue = rt.getValue();
    }

    /**
     * Constructor.
     *
     * @param intValue значение переменной
     */
    public ItsReplyType(final short intValue) {
        super(ParameterTag.ITS_REPLY_TYPE);
        defineValue(intValue);
    }
    /**
     * Constructor.
     *
     * @param bytes bytecode of TLV
     * @throws TLVException ошибка разбора TLV
     */
    public ItsReplyType(final byte[] bytes) throws TLVException {
        super(bytes);
    }

    @Override
    protected void parseValue(byte[] bytes, final EsmClass esmClass, final short dataCoding) throws TLVException {
        if (getTag() != ParameterTag.ITS_REPLY_TYPE) {
            throw new ClassCastException();
        }
        if (bytes.length == LENGTH) {
            short b;
            try {
                b = new SmppByteBuffer(bytes).removeByte();
                defineValue(b);
            } catch (WrongLengthException e) {
                throw new TLVException("Buffer error during parsing value", e);
            }
        } else {
            throw new TLVException("Value has wrong length: " + bytes.length + " but expected " + LENGTH);
        }
    }

    @Override
    protected byte[] getValueBytes(final EsmClass esmClass, final short dataCoding) throws TLVException {
        SmppByteBuffer sbb = new SmppByteBuffer();
        try {
            sbb.appendByte(intValue);
        } catch (WrongParameterException e) {
            throw new TLVException("Buffer error during parsing value", e);
        }
        return sbb.array();
    }

    private void defineValue(final short intValue) {
        for (ReplyType reply : ReplyType.values()) {
            if (reply.getValue() == intValue) {
                value = reply;
            }
        }
        if (value == null) {
            value = ReplyType.RESERVED;
        }
        this.intValue = intValue;
    }

    /**
     * @return значение параметра
     */
    public final ReplyType getValue() {
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
