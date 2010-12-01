package org.bulatnig.smpp.pdu.impl;

import org.bulatnig.smpp.pdu.Pdu;
import org.bulatnig.smpp.pdu.PduParsingException;
import org.bulatnig.smpp.util.ByteBuffer;

/**
 * PDU header.
 *
 * @author Bulat Nigmatullin
 */
public abstract class AbstractPdu implements Pdu {

    private long commandId;
    private long commandStatus;
    private long sequenceNumber;

    /**
     * Construct new PDU.
     *
     * @param commandId    PDU command identificator
     */
    protected AbstractPdu(long commandId) {
        this.commandId = commandId;
        this.commandStatus = 0;
        this.sequenceNumber = 0;
    }

    /**
     * Parse PDU from bytes.
     *
     * @param bb    pdu bytes
     * @throws org.bulatnig.smpp.pdu.PduParsingException parsing failed.
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
     * @return  body bytes
     * @throws PduParsingException wrong pdu body
     */
    protected abstract ByteBuffer body() throws PduParsingException;

    /**
     * Calculate and return PDU bytes.
     *
     * @return  pdu bytes
     * @throws PduParsingException pdu contains wrong values
     */
    @Override
    public ByteBuffer buffer() throws PduParsingException {
        ByteBuffer body = body();
        long length = HEADER_LENGTH + body.length();
        ByteBuffer bb = new ByteBuffer();
        bb.appendInt(length);
        bb.appendInt(commandId);
        bb.appendInt(commandStatus);
        bb.appendInt(sequenceNumber);
        bb.appendBytes(body.array());
        return bb;
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
