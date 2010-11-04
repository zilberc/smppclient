package org.bulatnig.smpp.pdu.tlv;

import org.bulatnig.smpp.util.SmppByteBuffer;
import org.bulatnig.smpp.util.WrongLengthException;
import org.bulatnig.smpp.pdu.EsmClass;

/**
 * The additional_status_info_text parameter gives an ASCII textual description of the meaning
 * of a response PDU. It is to be used by an implementation to allow easy diagnosis of problems.
 *
 * @author Bulat Nigmatullin
 */
public class AdditionalStatusInfoText extends TLV {
    /**
     * Максимальная длина значения параметра.
     */
    private static final int MAX_LENGTH = 256;
    /**
     * Значение параметра.
     */
    private String value;

    /**
     * Constructor.
     *
     * @param asit значение параметра
     * @throws TLVException ошибка разбора TLV
     */
    public AdditionalStatusInfoText(final String asit)
            throws TLVException {
        super(ParameterTag.ADDITIONAL_STATUS_INFO_TEXT);
        if (asit.length() < MAX_LENGTH) {
            value = asit;
        } else {
            throw new TLVException("Value has wrong length: " + asit.length() + " but expected less than " + MAX_LENGTH);
        }
    }

    /**
     * Constructor.
     *
     * @param bytes bytecode of TLV
     * @throws TLVException ошибка разбора TLV
     */
    public AdditionalStatusInfoText(final byte[] bytes) throws TLVException {
        super(bytes);
    }

    @Override
    protected void parseValue(byte[] bytes, final EsmClass esmClass, final short dataCoding) throws TLVException {
        if (getTag() != ParameterTag.ADDITIONAL_STATUS_INFO_TEXT) {
            throw new ClassCastException();
        }
        if (bytes.length <= MAX_LENGTH) {
            try {
                value = new SmppByteBuffer(bytes).removeCString();
            } catch (WrongLengthException e) {
                throw new TLVException("Buffer error during parsing value", e);
            }
        } else {
            throw new TLVException("Value has wrong length: " + bytes.length + " but expected more than 0 and no more than " + MAX_LENGTH);
        }
    }

    @Override
    protected byte[] getValueBytes(final EsmClass esmClass, final short dataCoding) throws TLVException {
        SmppByteBuffer sbb = new SmppByteBuffer();
        sbb.appendCString(value);
        return sbb.array();
    }

    /**
     * @return значение параметра
     */
    public final String getValue() {
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
