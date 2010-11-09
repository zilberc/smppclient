package org.bulatnig.smpp.pdu.tlv;

import org.bulatnig.smpp.pdu.*;
import org.bulatnig.smpp.pdu.udh.*;
import org.bulatnig.smpp.util.SmppByteBuffer;
import org.bulatnig.smpp.util.WrongLengthException;
import org.bulatnig.smpp.util.WrongParameterException;

/**
 * Optional Parameters are fields, which may be present in a message. Optional
 * Parameters provide a mechanism for the future introduction of new parameters,
 * as and when defined in future versions of the SMPP protocol.<br/>
 * <p/>
 * Optional Parameters must always appear at the end of a PDU , in the "Optional
 * Parameters" section of the SMPP PDU. However, they may be included in any
 * convenient order within the "Optional Parameters" section of the SMPP PDU and
 * need not be encoded in the order presented in this document.<br/>
 * <p/>
 * For a particular SMPP PDU, the ESME or SMSC may include some, all or none of
 * the defined optional parameters as required for the particular application
 * context. For example a paging system may only need to include the “callback
 * number” related optional parameters in a submit_sm operation.
 *
 * @author Bulat Nigmatullin
 */
public abstract class TLV {

    private static final TLVHelper helper = TLVHelperImpl.INSTANCE;

    private static final UDHFactory udhFactory = UDHFactoryImpl.INSTANCE;

    /**
     * The Tag field is used to uniquely identify the particular optional
     * parameter in question.<br>
     * <p/>
     * The optional parameter Tag field is always 2 octets in length.
     */
    private ParameterTag tag;

    /**
     * Constructor.
     *
     * @param t the type of TLV
     */
    protected TLV(final ParameterTag t) {
        tag = t;
    }

    /**
     * Constructor.
     *
     * @param bytes bytecode of TLV
     * @throws TLVException ошибка разбора TLV
     */
    protected TLV(final byte[] bytes) throws TLVException {
        this(bytes, new SmscEsmClass(), (short) 0);
    }

    /**
     * Constructor.
     *
     * @param bytes      bytecode of TLV
     * @param esmClass   PDU esm_class
     * @param dataCoding PDU data_coding
     * @throws TLVException ошибка разбора TLV
     */
    protected TLV(final byte[] bytes, final EsmClass esmClass, final int dataCoding) throws TLVException {
        SmppByteBuffer bb = new SmppByteBuffer(bytes);
        parseHead(bb);
        int length;
        length = bb.removeShort();
        if (bb.length() == length) {
            parseValue(bb.array(), esmClass, dataCoding);
        } else {
            throw new TLVException("TLV has wrong length. Found: " + bb.length() + " expected " + length);
        }
    }

    /**
     * Обрабатывает заголовок, который является обязательной частью любого TLV.
     *
     * @param bb TLV SmppByteBuffer
     * @return тело TLV
     * @throws TLVException ошибка разбора TLV
     */
    private SmppByteBuffer parseHead(final SmppByteBuffer bb) throws TLVException {
        int tagValue;
        tagValue = bb.removeShort();
        try {
            tag = helper.getParameterTag(tagValue);
        } catch (ParameterTagNotFoundException e) {
            throw new TLVNotFoundException("TLV param tag not found by tag value: " + tagValue, e);
        }
        return bb;
    }

    /**
     * Get TLV bytes with default esm_class and 0 data_coding.
     *
     * @return TLV bytes
     * @throws TLVException tlv parsing failed
     */
    public final byte[] getBytes() throws TLVException {
        return getBytes(new EsmeEsmClass(), (short) 0);
    }

    /**
     * @param esmClass   PDU esm_class
     * @param dataCoding PDU data_coding
     * @return bytecode of TLV
     * @throws TLVException ошибка обработки TLV
     */
    public final byte[] getBytes(final EsmClass esmClass, final short dataCoding) throws TLVException {
        SmppByteBuffer bb = new SmppByteBuffer();
        bb.appendShort(tag.getValue());
        byte[] value = getValueBytes(esmClass, dataCoding);
        bb.appendShort(value.length);
        bb.appendBytes(value);
        return bb.array();
    }

    /**
     * @return the type of TLV
     */
    public final ParameterTag getTag() {
        return tag;
    }

    /**
     * Returns charset name corresponding to defined data coding;
     *
     * @param dataCoding PDU data_coding parameter
     * @return charset name
     */
    protected String getCharsetName(int dataCoding) {
        return DataCodingHelper.INSTANCE.getCharsetName(dataCoding);
    }

    /**
     * Парсит значение TLV. Вызывается в процессе создания TLV в конструкторе.
     *
     * @param bytes      байткод значения TLV
     * @param esmClass   PDU esm_class
     * @param dataCoding PDU data_coding
     * @throws TLVException ошибка разбора TLV
     */
    protected abstract void parseValue(final byte[] bytes, final EsmClass esmClass, final int dataCoding) throws TLVException;

    /**
     * Возвращает байты соответствующие значению TLV.
     * Вызывается в getBytes() методе класса TLV.
     *
     * @param esmClass   PDU esm_class parameter value
     * @param dataCoding PDU data_coding value
     * @return байткод содержащий значение параметра
     * @throws TLVException ошибка обработки TLV
     */
    protected abstract byte[] getValueBytes(final EsmClass esmClass, final short dataCoding) throws TLVException;


    protected final UDH parseUDH(SmppByteBuffer byteBuffer) throws TLVException {
        try {
            int length = byteBuffer.readBytes(1).removeByte();
            return udhFactory.parseUDH(byteBuffer.removeBytes(length + 1).array());
        } catch (WrongLengthException e) {
            throw new TLVException("Buffer have not enough length to read UDH header", e);
        } catch (WrongParameterException e) {
            throw new TLVException("Buffer have not enough length to read UDH", e);
        } catch (UDHException e) {
            // omit it
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return getClass().getName() + " Object {" + "\ntag : " + tag
                + "\n}";
    }

}
