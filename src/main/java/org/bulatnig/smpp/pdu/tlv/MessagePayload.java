package org.bulatnig.smpp.pdu.tlv;

import org.bulatnig.smpp.pdu.EsmClass;
import org.bulatnig.smpp.pdu.SmscEsmClass;
import org.bulatnig.smpp.pdu.udh.UDH;
import org.bulatnig.smpp.util.SmppByteBuffer;
import org.bulatnig.smpp.util.WrongLengthException;

/**
 * The message_payload parameter contains the user data.
 *
 * @author Bulat Nigmatullin
 */
public class MessagePayload extends TLV {

    private UDH udh;

    /**
     * Значение параметра.
     */
    private String value;

    /**
     * Constructor.
     *
     * @param payload значение параметра
     */
    public MessagePayload(final String payload) {
        super(ParameterTag.MESSAGE_PAYLOAD);
        value = payload;
    }

    /**
     * Constructor.
     *
     * @param bytes bytecode of TLV
     * @param esmClass   PDU esm_class
     * @param dataCoding PDU data_coding
     * @throws TLVException ошибка разбора TLV
     */
    public MessagePayload(final byte[] bytes, final EsmClass esmClass, int dataCoding)
            throws TLVException {
        super(bytes, esmClass, dataCoding);
    }

    @Override
    protected void parseValue(byte[] bytes, final EsmClass esmClass, final int dataCoding) throws TLVException {
        if (getTag() != ParameterTag.MESSAGE_PAYLOAD) {
            throw new ClassCastException();
        }
        SmppByteBuffer byteBuffer = new SmppByteBuffer(bytes);
        if (esmClass instanceof SmscEsmClass &&
                SmscEsmClass.SmscGSMFeatures.UDHI_INDICATOR == ((SmscEsmClass)esmClass).getFeatures()) {
            udh = parseUDH(byteBuffer);
        }
        try {
            value = byteBuffer.removeString(byteBuffer.length(), getCharsetName(dataCoding));
        } catch (WrongLengthException e) {
            throw new TLVException("Buffer error during parsing value", e);
        }
    }

    @Override
    protected byte[] getValueBytes(final EsmClass esmClass, final short dataCoding) throws TLVException {
        SmppByteBuffer sbb = new SmppByteBuffer();
        sbb.appendString(value, getCharsetName(dataCoding));
        return sbb.array();
    }

    public UDH getUdh() {
        return udh;
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
