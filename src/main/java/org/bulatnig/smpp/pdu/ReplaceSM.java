package org.bulatnig.smpp.pdu;

import org.bulatnig.smpp.util.SmppByteBuffer;
import org.bulatnig.smpp.util.WrongLengthException;
import org.bulatnig.smpp.util.WrongParameterException;

/**
 * This command is issued by the ESME to replace a previously submitted short
 * message that is still pending delivery. The matching mechanism is based on
 * the message_id and source address of the original message.<br/> Where the
 * original submit_sm ‘source address’ was defaulted to NULL, then the source
 * address in the replace_sm command should also be NULL
 *
 * @author Bulat Nigmatullin
 */
public class ReplaceSM extends PDU implements Responsable {
    /**
     * Максимальная длина serviceType поля.
     */
    private static final int MAX_MESSAGEID_LENGTH = 64;
    /**
     * Максимальная длина sourceAddr и destinationAddr полей.
     */
    private static final int MAX_ADDRESS_LENGTH = 20;
    /**
     * Максимальная длина serviceType поля.
     */
    private static final int SCHEDULEDELIVERYTIME_LENGTH = 16;
    /**
     * Максимальная длина serviceType поля.
     */
    private static final int VALIDITYPERIOD_LENGTH = 16;

    /**
     * SMSC message ID of the message to be replaced. This must be the message
     * ID allocated to the original short message when submitted to the SMSC by
     * the submit_sm command, and returned in the submit_sm_resp message by the
     * SMSC.
     */
    private String messageId;
    /**
     * Type of Number of mesage originator. This is used for verification
     * purposes, and must match that supplied in the corresponding submit_sm
     * request. If not known, set to NULL.
     */
    private TON sourceAddrTon;
    /**
     * Numbering Plan Identity of message originator. This is used for
     * verification purposes, and must match that supplied in the corresponding
     * submit_sm request. If not known set to NULL.
     */
    private NPI sourceAddrNpi;
    /**
     * Originating address of the short message to be replaced. This is used for
     * verification purposes, and must match that supplied in the corresponding
     * submit_sm request.
     */
    private String sourceAddr;
    /**
     * New scheduled delivery time for the short message. Set to NULL, if
     * updating of the original scheduled delivery time is not desired.
     */
    private String scheduleDeliveryTime;
    /**
     * New expiration time for the short message. Set to NULL, if updating of
     * the original expiration time is not required.
     */
    private String validityPeriod;
    /**
     * New registered delivery parameter setting.
     */
    private short registeredDelivery;
    /**
     * New pre-defined (canned) message identifier.
     */
    private short smDefaultMsgId;
    /**
     * Length of new short message in octets.
     */
    private short smLength;
    /**
     * New short message to replace existing message.
     */
    private String shortMessage;

    /**
     * Constructor.
     */
    public ReplaceSM() {
        super(CommandId.REPLACE_SM);
    }

    /**
     * Constructor.
     *
     * @param bytes байткод PDU
     * @throws PDUException ошибка обработки PDU
     */
    public ReplaceSM(final byte[] bytes) throws PDUException {
        super(bytes);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected final void parseBody(final byte[] bytes)
            throws PDUException {
        if (getCommandId() != CommandId.REPLACE_SM) {
            throw new ClassCastException();
        }
        SmppByteBuffer bb = new SmppByteBuffer(bytes);
        try {
            messageId = bb.removeCString();
            if (messageId.length() > MAX_MESSAGEID_LENGTH) {
                throw new PDUException("messageId field is too long");
            }
            short b = bb.removeByte();
            for (TON ton : TON.values()) {
                if (ton.getValue() == b) {
                    sourceAddrTon = ton;
                }
            }
            if (sourceAddrTon == null) {
                sourceAddrTon = TON.RESERVED;
            }
            b = bb.removeByte();
            for (NPI npi : NPI.values()) {
                if (npi.getValue() == b) {
                    sourceAddrNpi = npi;
                }
            }
            if (sourceAddrNpi == null) {
                sourceAddrNpi = NPI.RESERVED;
            }
            sourceAddr = bb.removeCString();
            if (sourceAddr.length() > MAX_ADDRESS_LENGTH) {
                throw new PDUException("sourceAddr field is too long");
            }
            scheduleDeliveryTime = bb.removeCString();
            if (scheduleDeliveryTime.length() > 0 && scheduleDeliveryTime.length() != SCHEDULEDELIVERYTIME_LENGTH) {
                throw new PDUException("scheduleDeliveryTime field is invalid");
            }
            validityPeriod = bb.removeCString();
            if (validityPeriod.length() > 0 && validityPeriod.length() != VALIDITYPERIOD_LENGTH) {
                throw new PDUException("validityPeriod field is invalid");
            }
            registeredDelivery = bb.removeByte();
            smDefaultMsgId = bb.removeByte();
            smLength = bb.removeByte();
            shortMessage = bb.removeCString();
        } catch (WrongLengthException e) {
            throw new PDUException("PDU parsing error", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected final byte[] getBodyBytes() throws PDUException {
        SmppByteBuffer bb = new SmppByteBuffer();
        if (messageId != null && messageId.length() > MAX_MESSAGEID_LENGTH) {
            throw new PDUException("serviceType field is too long");
        }
        bb.appendCString(messageId);
        try {
            bb.appendByte(sourceAddrTon != null ? sourceAddrTon.getValue() : TON.UNKNOWN.getValue());
        } catch (
                WrongParameterException e) {
            throw new PDUException("sourceAddrTon field is invalid", e);
        }
        try {
            bb.appendByte(sourceAddrNpi != null ? sourceAddrNpi.getValue() : NPI.UNKNOWN.getValue());
        } catch (WrongParameterException e) {
            throw new PDUException("sourceAddrNpi field is invalid", e);
        }
        if (sourceAddr != null && sourceAddr.length() > MAX_ADDRESS_LENGTH) {
            throw new PDUException("sourceAddr field is too long");
        }
        bb.appendCString(sourceAddr);
        if (scheduleDeliveryTime != null && scheduleDeliveryTime.length() != SCHEDULEDELIVERYTIME_LENGTH) {
            throw new PDUException("scheduleDeliveryTime field is invalid");
        }
        bb.appendCString(scheduleDeliveryTime);
        if (validityPeriod != null && validityPeriod.length() != VALIDITYPERIOD_LENGTH) {
            throw new PDUException("validityPeriod field is invalid");
        }
        bb.appendCString(validityPeriod);
        try {
            bb.appendByte(registeredDelivery);
        } catch (WrongParameterException e) {
            throw new PDUException("registeredDelivery field is invalid", e);
        }
        try {
            bb.appendByte(smDefaultMsgId);
        } catch (WrongParameterException e) {
            throw new PDUException("smDefaultMsgId field is invalid", e);
        }
        try {
            bb.appendByte(smLength);
        } catch (WrongParameterException e) {
            throw new PDUException("smLength field is invalid", e);
        }
        bb.appendCString(shortMessage);
        return bb.array();
    }

    /**
     * {@inheritDoc}
     */
    public final ReplaceSMResp getResponse() {
        ReplaceSMResp resp = new ReplaceSMResp();
        resp.setSequenceNumber(getSequenceNumber());
        return resp;
    }

    /**
     * @return SMSC message ID of the message to be replaced
     */
    public final String getMessageId() {
        return messageId;
    }

    /**
     * @param messageId SMSC message ID of the message to be replaced
     */
    public final void setMessageId(final String messageId) {
        this.messageId = messageId;
    }

    /**
     * @return Type of Number of mesage originator
     */
    public final TON getSourceAddrTon() {
        return sourceAddrTon;
    }

    /**
     * @param sourceAddrTon Type of Number of mesage originator
     */
    public final void setSourceAddrTon(final TON sourceAddrTon) {
        this.sourceAddrTon = sourceAddrTon;
    }

    /**
     * @return Numbering Plan Identity of message originator
     */
    public final NPI getSourceAddrNpi() {
        return sourceAddrNpi;
    }

    /**
     * @param sourceAddrNpi Numbering Plan Identity of message originator
     */
    public final void setSourceAddrNpi(final NPI sourceAddrNpi) {
        this.sourceAddrNpi = sourceAddrNpi;
    }

    /**
     * @return originating address of the short message to be replaced
     */
    public final String getSourceAddr() {
        return sourceAddr;
    }

    /**
     * @param sourceAddr originating address of the short message to be replaced
     */
    public final void setSourceAddr(final String sourceAddr) {
        this.sourceAddr = sourceAddr;
    }

    /**
     * @return new scheduled delivery time for the short message
     */
    public final String getScheduleDeliveryTime() {
        return scheduleDeliveryTime;
    }

    /**
     * @param scheduleDeliveryTime new scheduled delivery time for the short message
     */
    public final void setScheduleDeliveryTime(final String scheduleDeliveryTime) {
        this.scheduleDeliveryTime = scheduleDeliveryTime;
    }

    /**
     * @return new expiration time for the short message
     */
    public final String getValidityPeriod() {
        return validityPeriod;
    }

    /**
     * @param validityPeriod new expiration time for the short message
     */
    public final void setValidityPeriod(final String validityPeriod) {
        this.validityPeriod = validityPeriod;
    }

    /**
     * @return new registered delivery parameter setting
     */
    public final short getRegisteredDelivery() {
        return registeredDelivery;
    }

    /**
     * @param registeredDelivery new registered delivery parameter setting
     */
    public final void setRegisteredDelivery(final short registeredDelivery) {
        this.registeredDelivery = registeredDelivery;
    }

    /**
     * @return new pre-defined (canned) message identifier
     */
    public final short getSmDefaultMsgId() {
        return smDefaultMsgId;
    }

    /**
     * @param smDefaultMsgId new pre-defined (canned) message identifier
     */
    public final void setSmDefaultMsgId(final short smDefaultMsgId) {
        this.smDefaultMsgId = smDefaultMsgId;
    }

    /**
     * @return length of new short message in octets
     */
    public final short getSmLength() {
        return smLength;
    }

    /**
     * @param smLength length of new short message in octets
     */
    public final void setSmLength(final short smLength) {
        this.smLength = smLength;
    }

    /**
     * @return new short message to replace existing message
     */
    public final String getShortMessage() {
        return shortMessage;
    }

    /**
     * @param shortMessage new short message to replace existing message
     */
    public final void setShortMessage(final String shortMessage) {
        this.shortMessage = shortMessage;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String toString() {
        return getClass().getName() + " Object {" + "\nmessageId : "
                + messageId + "\nsourceAddrTon : " + sourceAddrTon
                + "\nsourceAddrNpi : " + sourceAddrNpi + "\nsourceAddr : "
                + sourceAddr + "\nscheduleDeliveryTime : "
                + scheduleDeliveryTime + "\nvalidityPeriod : " + validityPeriod
                + "\nregisteredDelivery : " + registeredDelivery
                + "\nsmDefaultMsgId : " + smDefaultMsgId + "\nsmLength : "
                + smLength + "\nshortMessage : " + shortMessage + "\n}";
    }

}
