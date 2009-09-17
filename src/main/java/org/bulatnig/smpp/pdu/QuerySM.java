package org.bulatnig.smpp.pdu;

import org.bulatnig.smpp.util.SMPPByteBuffer;
import org.bulatnig.smpp.util.WrongLengthException;
import org.bulatnig.smpp.util.WrongParameterException;

/**
 * This command is issued by the ESME to query the status of a previously
 * submitted short message.<br/>
 * <p/>
 * The matching mechanism is based on the SMSC assigned message_id and source
 * address. Where the original submit_sm, data_sm or submit_multi ‘source
 * address’ was defaulted to NULL, then the source address in the query_sm
 * command should also be set to NULL.
 *
 * @author Bulat Nigmatullin
 */
public class QuerySM extends PDU implements Responsable {
    /**
     * Максимальная длина messageId поля.
     */
    private static final int MAX_MESSAGEID_LENGTH = 5;
    /**
     * Максимальная длина sourceAddr поля.
     */
    private static final int MAX_ADDRESS_LENGTH = 20;

    /**
     * Message ID of the message whose state is to be queried. This must be the
     * SMSC assigned Message ID allocated to the original short message when
     * submitted to the SMSC by the submit_sm, data_sm or submit_multi command,
     * and returned in the response PDU by the SMSC.
     */
    private String messageId;
    /**
     * Type of Number of message originator. This is used for verification
     * purposes, and must match that supplied in the original request PDU (e.g.
     * submit_sm). If not known, set to NULL.
     */
    private TON sourceAddrTon;
    /**
     * Numbering Plan Identity of message originator. This is used for
     * verification purposes, and must match that supplied in the original
     * request PDU (e.g. submit_sm). If not known, set to NULL.
     */
    private NPI sourceAddrNpi;
    /**
     * Address of message originator. This is used for verification purposes,
     * and must match that supplied in the original request PDU (e.g.
     * submit_sm). If not known, set to NULL.
     */
    private String sourceAddr;

    /**
     * Constructor.
     */
    public QuerySM() {
        super(CommandId.QUERY_SM);
    }

    /**
     * Constructor.
     *
     * @param bytes байткод PDU
     * @throws PDUException ошибка обработки PDU
     */
    public QuerySM(final byte[] bytes) throws PDUException {
        super(bytes);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected final void parseBody(final byte[] bytes)
            throws PDUException {
        if (getCommandId() != CommandId.QUERY_SM) {
            throw new ClassCastException();
        }
        SMPPByteBuffer bb = new SMPPByteBuffer(bytes);
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
        return bb.getBuffer();
    }

    /**
     * {@inheritDoc}
     */
    public final QuerySMResp getResponse() {
        QuerySMResp resp = new QuerySMResp();
        resp.setSequenceNumber(getSequenceNumber());
        return resp;
    }

    /**
     * @return message ID of the message whose state is to be queried
     */
    public final String getMessageId() {
        return messageId;
    }

    /**
     * @param messageId message ID of the message whose state is to be queried
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
     * @return address of message originator
     */
    public final String getSourceAddr() {
        return sourceAddr;
    }

    /**
     * @param sourceAddr address of message originator
     */
    public final void setSourceAddr(final String sourceAddr) {
        this.sourceAddr = sourceAddr;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String toString() {
        return getClass().getName() + " Object {" + "\nmessageId : "
                + messageId + "\nsourceAddrTon : " + sourceAddrTon
                + "\nsourceAddrNpi : " + sourceAddrNpi + "\nsourceAddr : "
                + sourceAddr + "\n}";
    }

}
