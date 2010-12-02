package org.bulatnig.smpp.pdu.impl;

import org.bulatnig.smpp.pdu.Pdu;
import org.bulatnig.smpp.pdu.PduParsingException;
import org.bulatnig.smpp.pdu.tlv.Tlv;
import org.bulatnig.smpp.pdu.tlv.TlvParsingException;
import org.bulatnig.smpp.util.ByteBuffer;

import java.util.Map;

/**
 * PDU header.
 *
 * @author Bulat Nigmatullin
 */
public abstract class AbstractPdu implements Pdu {

    // lazy init tlv map
    public Map<Integer, Tlv> tlvs;

    private long commandId;
    private long commandStatus;
    private long sequenceNumber;

    /**
     * Construct new PDU.
     *
     * @param commandId PDU command identificator
     */
    protected AbstractPdu(long commandId) {
        this.commandId = commandId;
        this.commandStatus = 0;
        this.sequenceNumber = 0;
    }

    /**
     * Parse PDU from bytes.
     *
     * @param bb pdu bytes
     * @throws org.bulatnig.smpp.pdu.PduParsingException
     *          parsing failed.
     */
    protected AbstractPdu(ByteBuffer bb) throws PduParsingException {
        long length = bb.readInt();
        if (length != bb.length())
            throw new PduParsingException("PDU length expected " + length + " but has " + bb.length() + ".");
        bb.removeInt();
        commandId = bb.removeInt();
        commandStatus = bb.removeInt();
        sequenceNumber = bb.removeInt();
    }

    /**
     * Calculate and return PDU body bytes.
     *
     * @return body bytes, can be null
     * @throws PduParsingException wrong pdu body
     */
    protected abstract ByteBuffer body() throws PduParsingException;

    /**
     * Calculate and return PDU bytes.
     *
     * @return pdu bytes
     * @throws PduParsingException pdu contains wrong values
     */
    @Override
    public ByteBuffer buffer() throws PduParsingException {
        long length = HEADER_LENGTH;
        ByteBuffer body = body();
        if (body != null)
            length += body.length();
        ByteBuffer tlvBb = tlv();
        if (tlvBb != null)
            length += tlvBb.length();
        ByteBuffer bb = new ByteBuffer();
        bb.appendInt(length);
        bb.appendInt(commandId);
        bb.appendInt(commandStatus);
        bb.appendInt(sequenceNumber);
        if (body != null)
            bb.appendBytes(body.array());
        if (tlvBb != null)
            bb.appendBytes(tlvBb.array());
        return bb;
    }

    private ByteBuffer tlv() throws PduParsingException {
        if (tlvs != null) {
            ByteBuffer result = new ByteBuffer();
            for (Tlv tlv : tlvs.values()) {
                try {
                    result.appendBytes(tlv.buffer().array());
                } catch (TlvParsingException e) {
                    throw new PduParsingException("Tlv to bytes parsing error.", e);
                }
            }
            return result;
        } else
            return null;
    }

    @Override
    public long getCommandId() {
        return commandId;
    }

    @Override
    public long getCommandStatus() {
        return commandStatus;
    }

    @Override
    public long getSequenceNumber() {
        return sequenceNumber;
    }

    @Override
    public void setCommandStatus(long commandStatus) {
        this.commandStatus = commandStatus;
    }

    @Override
    public void setSequenceNumber(long sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }
}
