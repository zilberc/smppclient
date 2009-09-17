package org.bulatnig.smpp.pdu;

import org.bulatnig.smpp.util.SMPPByteBuffer;
import org.bulatnig.smpp.util.WrongLengthException;
import org.bulatnig.smpp.util.WrongParameterException;

/**
 * This command is issued by the ESME to cancel one or more previously submitted
 * short messages that are still pending delivery. The command may specify a
 * particular message to cancel, or all messages for a particular source,
 * destination and service_type are to be cancelled.<br/> • If the message_id
 * is set to the ID of a previously submitted message, then provided the source
 * address supplied by the ESME matches that of the stored message, that message
 * will be cancelled.<br/> • If the message_id is NULL, all outstanding
 * undelivered messages with matching source and destination addresses given in
 * the PDU are cancelled. If provided, service_type is included in this
 * matching.<br/>
 * <p/>
 * Where the original submit_sm, data_sm or submit_multi ‘source address’ was
 * defaulted to NULL, then the source address in the cancel_sm command should
 * also be NULL.
 *
 * @author Bulat Nigmatullin
 */
public class CancelSM extends PDU implements Responsable {
    /**
     * Максимальная длина serviceType поля.
     */
    private static final int MAX_SERVICETYPE_LENGTH = 5;
    /**
     * Максимальная длина messageId поля.
     */
    private static final int MAX_MESSAGEID_LENGTH = 65;
    /**
     * Максимальная длина sourceAddr и destinationAddr полей.
     */
    private static final int MAX_ADDRESS_LENGTH = 20;

    /**
     * Set to indicate SMS Application service, if cancellation of a group of
     * application service messages is desired. Otherwise set to NULL.
     */
    private String serviceType;
    /**
     * Message ID of the message to be cancelled. This must be the SMSC assigned
     * Message ID of the original message. Set to NULL if cancelling a group of
     * messages.
     */
    private String messageId;
    /**
     * Type of Number of message originator. This is used for verification
     * purposes, and must match that supplied in the original message submission
     * request PDU. If not known, set to NULL.
     */
    private TON sourceAddrTon;
    /**
     * Numbering Plan Identity of message originator. This is used for
     * verification purposes, and must match that supplied in the original
     * message submission request PDU. If not known, set to NULL.
     */
    private NPI sourceAddrNpi;
    /**
     * Source address of message(s) to be cancelled. This is used for
     * verification purposes, and must match that supplied in the original
     * message submission request PDU(s).
     */
    private String sourceAddr;
    /**
     * Type of number of destination SME address of the message(s) to be
     * cancelled. This is used for verification purposes, and must match that
     * supplied in the original message submission request PDU (e.g. submit_sm).
     * May be set to NULL when the message_id is provided.
     */
    private TON destAddrTon;
    /**
     * Numbering Plan Indicator of destination SME address of the message(s)) to
     * be cancelled. This is used for verification purposes, and must match that
     * supplied in the original message ubmission request PDU. May be set to
     * NULL when the message_id is provided.
     */
    private NPI destAddrNpi;
    /**
     * Destination address of message(s) to be cancelled. This is used for
     * verification purposes, and must match that supplied in the original
     * message submission request PDU. May be set to NULL when the message_id is
     * provided.
     */
    private String destinationAddr;

    /**
     * Constructor.
     */
    public CancelSM() {
        super(CommandId.CANCEL_SM);
    }

    /**
     * Constructor.
     *
     * @param bytes байткод PDU
     * @throws PDUException ошибка обработки PDU
     */
    public CancelSM(final byte[] bytes) throws PDUException {
        super(bytes);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected final void parseBody(final byte[] bytes)
            throws PDUException {
        if (getCommandId() != CommandId.CANCEL_SM) {
            throw new ClassCastException();
        }
        SMPPByteBuffer bb = new SMPPByteBuffer(bytes);
        try {
            serviceType = bb.removeCString();
            if (serviceType.length() > MAX_SERVICETYPE_LENGTH) {
                throw new PDUException("serviceType field is too long");
            }
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
            b = bb.removeByte();
            for (TON ton : TON.values()) {
                if (ton.getValue() == b) {
                    destAddrTon = ton;
                }
            }
            if (destAddrTon == null) {
                destAddrTon = TON.RESERVED;
            }
            b = bb.removeByte();
            for (NPI npi : NPI.values()) {
                if (npi.getValue() == b) {
                    destAddrNpi = npi;
                }
            }
            if (destAddrNpi == null) {
                destAddrNpi = NPI.RESERVED;
            }
            destinationAddr = bb.removeCString();
            if (destinationAddr.length() > MAX_ADDRESS_LENGTH) {
                throw new PDUException("destinationAddr field is too long");
            }
        } catch (WrongLengthException e) {
                throw new PDUException("PDU parsing error", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected final byte[] getBodyBytes() throws PDUException {
        SMPPByteBuffer bb = new SMPPByteBuffer();
        if (serviceType != null && serviceType.length() > MAX_SERVICETYPE_LENGTH) {
            throw new PDUException("serviceType field is too long");
        }
        bb.appendCString(serviceType);
        if (messageId != null && messageId.length() > MAX_MESSAGEID_LENGTH) {
            throw new PDUException("messageId field is too long");
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
        try {
            bb.appendByte(destAddrTon != null ? destAddrTon.getValue() : TON.UNKNOWN.getValue());
        } catch (WrongParameterException e) {
            throw new PDUException("destAddrTon field is invalid", e);
        }
        try {
            bb.appendByte(destAddrNpi != null ? destAddrNpi.getValue() : NPI.UNKNOWN.getValue());
        } catch (WrongParameterException e) {
            throw new PDUException("destAddrNpi field is invalid", e);
        }
        if (destinationAddr != null && destinationAddr.length() > MAX_ADDRESS_LENGTH) {
            throw new PDUException("destinationAddr field is too long");
        }
        bb.appendCString(destinationAddr);
        return bb.getBuffer();
    }

    /**
     * {@inheritDoc}
     */
    public final CancelSMResp getResponse() {
        CancelSMResp resp = new CancelSMResp();
        resp.setSequenceNumber(getSequenceNumber());
        return resp;
    }

    /**
     * @return SMS Application service indicator
     */
    public final String getServiceType() {
        return serviceType;
    }

    /**
     * @param serviceType SMS Application service indicator
     */
    public final void setServiceType(final String serviceType) {
        this.serviceType = serviceType;
    }

    /**
     * @return message ID of the message to be cancelled
     */
    public final String getMessageId() {
        return messageId;
    }

    /**
     * @param messageId message ID of the message to be cancelled
     */
    public final void setMessageId(final String messageId) {
        this.messageId = messageId;
    }

    /**
     * @return Type of Number of message originator
     */
    public final TON getSourceAddrTon() {
        return sourceAddrTon;
    }

    /**
     * @param sourceAddrTon Type of Number of message originator
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
     * @return source address of message(s) to be cancelled
     */
    public final String getSourceAddr() {
        return sourceAddr;
    }

    /**
     * @param sourceAddr source address of message(s) to be cancelled
     */
    public final void setSourceAddr(final String sourceAddr) {
        this.sourceAddr = sourceAddr;
    }

    /**
     * @return type of number of destination SME address of the message(s) to be
     *         cancelled
     */
    public final TON getDestAddrTon() {
        return destAddrTon;
    }

    /**
     * @param destAddrTon type of number of destination SME address of the message(s) to
     *                    be cancelled
     */
    public final void setDestAddrTon(final TON destAddrTon) {
        this.destAddrTon = destAddrTon;
    }

    /**
     * @return Numbering Plan Indicator of destination SME address of the
     *         message(s)) to be cancelled
     */
    public final NPI getDestAddrNpi() {
        return destAddrNpi;
    }

    /**
     * @param destAddrNpi Numbering Plan Indicator of destination SME address of the
     *                    message(s)) to be cancelled
     */
    public final void setDestAddrNpi(final NPI destAddrNpi) {
        this.destAddrNpi = destAddrNpi;
    }

    /**
     * @return Destination address of message(s) to be cancelled
     */
    public final String getDestinationAddr() {
        return destinationAddr;
    }

    /**
     * @param destinationAddr Destination address of message(s) to be cancelled
     */
    public final void setDestinationAddr(final String destinationAddr) {
        this.destinationAddr = destinationAddr;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String toString() {
        return getClass().getName() + " Object {" + "\nserviceType : "
                + serviceType + "\nmessageId : " + messageId
                + "\nsourceAddrTon : " + sourceAddrTon + "\nsourceAddrNpi : "
                + sourceAddrNpi + "\nsourceAddr : " + sourceAddr
                + "\ndestAddrTon : " + destAddrTon + "\ndestAddrNpi : "
                + destAddrNpi + "\ndestinationAddr : " + destinationAddr
				+ "\n}";
	}

}
