package org.bulatnig.smpp.pdu;

import org.bulatnig.smpp.SMPPObject;
import org.bulatnig.smpp.pdu.tlv.TLV;
import org.bulatnig.smpp.pdu.tlv.TLVException;
import org.bulatnig.smpp.pdu.tlv.TLVFactory;
import org.bulatnig.smpp.pdu.tlv.TLVFactoryImpl;
import org.bulatnig.smpp.pdu.udh.UDHException;
import org.bulatnig.smpp.pdu.udh.UDH;
import org.bulatnig.smpp.pdu.udh.UDHFactory;
import org.bulatnig.smpp.pdu.udh.UDHFactoryImpl;
import org.bulatnig.smpp.util.SMPPByteBuffer;
import org.bulatnig.smpp.util.WrongLengthException;
import org.bulatnig.smpp.util.WrongParameterException;

import java.util.List;

/**
 * PDU общего вида.
 *
 * @author Bulat Nigmatullin
 */
public abstract class PDU extends SMPPObject {

    /**
     * Длина заголовка.
     */
    public static final int HEADER_LENGTH = 16;

    /**
     * Helper.
     */
    private static final PDUHelper helper = PDUHelperImpl.INSTANCE;
    /**
     * TLV Factory.
     */
    private static final TLVFactory tlvFactory = TLVFactoryImpl.INSTANCE;

    private static final UDHFactory udhFactory = UDHFactoryImpl.INSTANCE;

    /**
     * длина всего PDU в октетах(пара цифр).
     */
    private long commandLength;
    /**
     * тип PDU (идентификатор команды).
     */
    private CommandId commandId;
    /**
     * статус PDU (0 если все ок, иначе код ошибки).
     */
    private CommandStatus commandStatus = CommandStatus.ESME_ROK;
    /**
     * численное значения статуса PDU.
     */
    private long commandStatusValue = 0;
    /**
     * ID PDU в рамках сессии.
     */
    private long sequenceNumber;

    /**
     * Конструктор.
     *
     * @param ci идентификатор PDU
     */
    protected PDU(final CommandId ci) {
        commandId = ci;
    }

    /**
     * Конструктор.
     *
     * @param bytes байткод PDU
     * @throws PDUException ошибка разбора PDU
     */
    protected PDU(final byte[] bytes) throws PDUException {
        SMPPByteBuffer bb = new SMPPByteBuffer(bytes);
        try {
            parseHeader(bb.removeBytes(HEADER_LENGTH));
        } catch (WrongParameterException e) {
            throw new PDUException("FATAL ERROR wrong header length supplied", e);
        } catch (WrongLengthException e) {
            throw new PDUException("PDU has not enough length to read header", e);
        }
        if (bytes.length == commandLength) {
            parseBody(bb.getBuffer());
        } else {
            throw new PDUException("PDU has wrong length " + bytes.length + " but should be " + commandLength);
        }
    }

    /**
     * Обрабатывает заголовок, который является обязательной частью любого PDU.
     *
     * @param bb заголовок PDU
     * @throws PDUException ошибка разбора PDU
     */
    private void parseHeader(final SMPPByteBuffer bb) throws PDUException {
        try {
            commandLength = bb.removeInt();
            commandId = helper.getCommandId(bb.removeInt());
            commandStatusValue = bb.removeInt();
            try {
                commandStatus = helper.getCommandStatus(commandStatusValue);
            } catch (CommandStatusNotFoundException e) {
                commandStatus = CommandStatus.RESERVED;
            }
            sequenceNumber = bb.removeInt();
        } catch (WrongLengthException e) {
            throw new PDUException("PDU has wrong header parameters", e);
        }
    }

    /**
     * Обрабатывает тело PDU.
     *
     * @param bytes байткод PDU
     * @throws PDUException ошибка разбора PDU
     */
    protected abstract void parseBody(final byte[] bytes) throws PDUException;

    /**
     * Возвращает длину PDU. Осторожно! Может отображать неактуальную информацию.
     * Данные достоверны только немедленно после создания PDU или после выполнения
     * метода getBytes(). С изменеием значения остальных полей,
     * значение этого поля не обновляется.
     *
     * @return длина PDU
     */
    public final long getCommandLength() {
        return commandLength;
    }

    /**
     * Возвращает тип PDU (идентификатор команды).
     *
     * @return идентификатор команды
     */
    public final CommandId getCommandId() {
        return commandId;
    }

    /**
     * Присваивает PDU статус.
     *
     * @param commandStatusVal статус PDU
     */
    public final void setCommandStatus(final CommandStatus commandStatusVal) {
        commandStatus = commandStatusVal;
        commandStatusValue = commandStatus.getValue();
    }

    /**
     * Возвращает статус PDU.
     *
     * @return статус PDU
     */
    public final CommandStatus getCommandStatus() {
        return commandStatus;
    }

    /**
     * Возвращает численное значение статуса PDU.
     *
     * @return численное значение статуса PDU
     */
    public long getCommandStatusValue() {
        return commandStatusValue;
    }

    /**
     * Присваивает PDU ID.
     *
     * @param sequenceNumberVal ID PDU в рамках сессии
     */
    public final void setSequenceNumber(final long sequenceNumberVal) {
        sequenceNumber = sequenceNumberVal;
    }

    /**
     * Возвращает ID PDU в рамках сессии.
     *
     * @return PDU ID
     */
    public final long getSequenceNumber() {
        return sequenceNumber;
    }

    /**
     * Возвращает массив байтов, из которых состоит PDU.
     *
     * @return массив байтов
     * @throws PDUException ошибка обработки PDU
     */
    public final byte[] getBytes() throws PDUException {
        byte[] body = getBodyBytes();
        commandLength = HEADER_LENGTH + body.length;
        if (body.length > 0) {
            SMPPByteBuffer bb = getHeader();
            bb.appendBytes(body, body.length);
            return bb.getBuffer();
        } else {
            return getHeader().getBuffer();
        }
    }

    /**
     * Возвращает байты заголовка PDU.
     *
     * @return байты заголовка
     * @throws PDUException ошибка обработки PDU
     */
    private SMPPByteBuffer getHeader() throws PDUException {
        SMPPByteBuffer bb = new SMPPByteBuffer();
        try {
            bb.appendInt(commandLength);
        } catch (WrongParameterException e) {
            throw new PDUException("commandLength field is invalid", e);
        }
        try {
            bb.appendInt(commandId.getValue());
        } catch (WrongParameterException e) {
            throw new PDUException("commandId field is invalid", e);
        }
        try {
            bb.appendInt(commandStatusValue);
        } catch (WrongParameterException e) {
            throw new PDUException("commandStatus field is invalid", e);
        }
        try {
            bb.appendInt(sequenceNumber);
        } catch (WrongParameterException e) {
            throw new PDUException("sequenceNumber field is invalid", e);
        }
        return bb;
    }

    /**
     * Возвращает байты тела PDU.
     *
     * @return байты тела
     * @throws PDUException ошибка обработки PDU
     */
    protected abstract byte[] getBodyBytes() throws PDUException;

    /**
     * Returns list of optional parameters with default esm_class and 0 data_coding.
     *
     * @param bb TLVs byte buffer
     * @return list of TLVs
     * @throws PDUException error during parsing TLVs
     */
    protected final List<TLV> getOptionalParams(final byte[] bb) throws PDUException {
        return getOptionalParams(bb, new SmscEsmClass(), (short) 0);
    }

    /**
     * Returns list of optional parameters.
     *
     * @param bb         optional paratemeters byte buffer
     * @param esmClass   PDU esm_class value
     * @param dataCoding PDU data_coding value
     * @return list of TLVs
     * @throws PDUException error during parsing TLVs
     */
    protected final List<TLV> getOptionalParams(final byte[] bb, EsmClass esmClass, short dataCoding) throws PDUException {
        try {
            return tlvFactory.parseTLVs(bb, esmClass, dataCoding);
        } catch (TLVException e) {
            throw new PDUException("PDU TLVs parsing error", e);
        }
    }

    protected final UDH parseUDH(SMPPByteBuffer byteBuffer) throws PDUException {
        try {
            short length = byteBuffer.readBytes(1).removeByte();
            return udhFactory.parseUDH(byteBuffer.removeBytes(length + 1).getBuffer());
        } catch (WrongLengthException e) {
            throw new PDUException("Buffer have not enough length to read UDH header", e);
        } catch (WrongParameterException e) {
            throw new PDUException("Buffer have not enough length to read UDH", e);
        } catch (UDHException e) {
            e.printStackTrace();
            // omit it
            return null;
        }
    }

    /**
     * Returns charset name corresponding to defined data coding;
     *
     * @param dataCoding PDU data_coding parameter
     * @return charset name
     */
    protected String getCharsetName(short dataCoding) {
        return DataCodingHelper.INSTANCE.getCharsetName(dataCoding);
    }

    @Override
    public String toString() {
        return "PDU{" +
                "commandLength=" + commandLength +
                ", commandId=" + commandId +
                ", commandStatus=" + commandStatus +
                ", commandStatusValue=" + commandStatusValue +
                ", sequenceNumber=" + sequenceNumber +
                '}';
    }
}
