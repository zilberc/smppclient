package org.bulatnig.smpp.pdu.tlv;

import org.bulatnig.smpp.util.SmppByteBuffer;
import org.bulatnig.smpp.util.WrongLengthException;
import org.bulatnig.smpp.pdu.EsmClass;

/**
 * The receipted_message_id parameter indicates the ID of the message being
 * receipted in an SMSC Delivery Receipt. This is the opaque SMSC message
 * identifier that was returned in the message_id parameter of the SMPP response
 * PDU that acknowledged the submission of the original message.
 *
 * @author Bulat Nigmatullin
 */
public class ReceiptedMessageId extends TLV {
    /**
     * Длина значения параметра.
     */
    private static final int MAX_LENGTH = 65;
    /**
     * Значение параметра.
     */
    private String value;

    /**
     * Constructor.
     *
     * @param rmi значение параметра
     * @throws TLVException ошибка разбора TLV
     */
    public ReceiptedMessageId(final String rmi) throws TLVException {
        super(ParameterTag.RECEIPTED_MESSAGE_ID);
        if (rmi.length() < MAX_LENGTH) {
            value = rmi;
        } else {
            throw new TLVException("Wrong value supplied " + rmi.length() + ". Expected less than " + MAX_LENGTH);
        }
    }

    /**
     * Constructor.
     *
     * @param bytes bytecode of TLV
     * @throws TLVException ошибка разбора TLV
     */
    public ReceiptedMessageId(final byte[] bytes) throws TLVException {
        super(bytes);
    }

    @Override
    protected void parseValue(byte[] bytes, final EsmClass esmClass, final short dataCoding) throws TLVException {
        if (getTag() != ParameterTag.RECEIPTED_MESSAGE_ID) {
            throw new ClassCastException();
        }
        if (bytes.length < MAX_LENGTH) {
            try {
                value = new SmppByteBuffer(bytes).removeCString();
            } catch (WrongLengthException e) {
                throw new TLVException("Buffer error during parsing value", e);
            }
        } else {
            throw new TLVException("Value has wrong length: " + bytes.length + " but expected no more than " + MAX_LENGTH);
        }
    }

    @Override
    protected byte[] getValueBytes(final EsmClass esmClass, final short dataCoding) throws TLVException {
        SmppByteBuffer sbb = new SmppByteBuffer();
        sbb.appendCString(value);
        return sbb.getBuffer();
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
