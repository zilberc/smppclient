package org.bulatnig.smpp.pdu;

import org.bulatnig.smpp.util.SmppByteBuffer;

import java.util.List;

/**
 * SubmitMulti Response PDU.
 *
 * @author Bulat Nigmatullin
 */
public class SubmitMultiResp extends PDU {

    /**
     * Максимальная длина messageId поля.
     */
    private static final int MAX_MESSAGEID_LENGTH = 16;
    /**
     * Максимальное число адресов.
     */
    private static final int UNCUCCESS_SMES_MAX = 256;

    /**
     * The SMSC message ID of the submitted message.
     */
    private String messageId;
    /**
     * The number of messages to destination SME addresses that were
     * unsuccessfully submitted to the SMSC.
     */
    private int noUnsuccess;
    /**
     * Contains one or more (no_unsuccess) SME address(es) or/and Distribution
     * List names to which submission was unsuccessful.
     */
    private List<UnsuccessSme> unsuccessSmes;

    /**
     * Constructor.
     */
    public SubmitMultiResp() {
        super(CommandId.SUBMIT_MULTI_RESP);
    }

    /**
     * Constructor.
     *
     * @param bytes байткод PDU
     * @throws PDUException ошибка обработки PDU
     */
    public SubmitMultiResp(final byte[] bytes) throws PDUException {
        super(bytes);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected final byte[] getBodyBytes() throws PDUException {
        SmppByteBuffer bb = new SmppByteBuffer();
        if (messageId != null && messageId.length() > MAX_MESSAGEID_LENGTH) {
            throw new PDUException("messageId field is invalid");
        }
        bb.appendCString(messageId);
        bb.appendByte(noUnsuccess);
        if (unsuccessSmes != null) {
            for (UnsuccessSme us : unsuccessSmes) {
                bb.appendBytes(us.getBytes());
            }
        }
        return bb.array();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected final void parseBody(final byte[] bytes)
            throws PDUException {
        if (getCommandId() != CommandId.SUBMIT_MULTI_RESP) {
            throw new ClassCastException();
        }
        SmppByteBuffer bb = new SmppByteBuffer(bytes);
        messageId = bb.removeCString();
        if (messageId.length() > MAX_MESSAGEID_LENGTH) {
            throw new PDUException("messageId field is too long");
        }
        noUnsuccess = bb.removeByte();
        for (int i = 0; i < noUnsuccess; i++) {
            UnsuccessSme sme = new UnsuccessSme(bb.array());
            bb.removeBytes(sme.getBytes().length);
            unsuccessSmes.add(sme);
        }
    }

    /**
     * @return the SMSC message ID of the submitted message
     */
    public final String getMessageId() {
        return messageId;
    }

    /**
     * @param messageId the SMSC message ID of the submitted message
     */
    public final void setMessageId(final String messageId) {
        this.messageId = messageId;
    }

    /**
     * @return the number of messages to destination SME addresses that were
     *         unsuccessfully submitted to the SMSC
     */
    public final int getNoUnsuccess() {
        return noUnsuccess;
    }

    /**
     * @param noUnsuccess the number of messages to destination SME addresses that were
     *                    unsuccessfully submitted to the SMSC
     */
    public final void setNoUnsuccess(final short noUnsuccess) {
        this.noUnsuccess = noUnsuccess;
    }

    /**
     * @return SME address(es) or/and Distribution List names to which
     *         submission was unsuccessful
     */
    public final List<UnsuccessSme> getUnsuccessSmes() {
        return unsuccessSmes;
    }

    /**
     * @param unsuccessSmes SME address(es) or/and Distribution List names to which
     *                      submission was unsuccessful
     * @throws PDUException ошибка обработки PDU
     */
    public final void setUnsuccessSmes(final List<UnsuccessSme> unsuccessSmes)
            throws PDUException {
        if (unsuccessSmes.size() < UNCUCCESS_SMES_MAX) {
            noUnsuccess = (short) unsuccessSmes.size();
            this.unsuccessSmes = unsuccessSmes;
        } else {
            throw new PDUException("Unsuccess SMEs count should be less than " + UNCUCCESS_SMES_MAX);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String toString() {
        return getClass().getName() + " Object {" + "\nmessageId : "
                + messageId + "\nnoUnsuccess : " + noUnsuccess
                + "\nunsuccessSmes : " + unsuccessSmes + "\n}";
    }

}
