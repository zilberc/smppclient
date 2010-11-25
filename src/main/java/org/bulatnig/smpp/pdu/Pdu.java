package org.bulatnig.smpp.pdu;

import org.bulatnig.smpp.util.ByteBuffer;

/**
 * PDU header.
 *
 * @author Bulat Nigmatullin
 */
public abstract class Pdu {

    /**
     * Header length.
     */
    public static final int HEADER_LENGTH = 16;

    private long commandId;
    private long commandStatus;
    private long sequenceNumber;

    protected Pdu(long commandId) {
        this.commandId = commandId;
        this.commandStatus = 0;
        this.sequenceNumber = 0;
    }

    protected Pdu(ByteBuffer bb) throws PduException {
        long length = bb.readInt();
        if (length != bb.length())
            throw new PduException("PDU length expected " + length + " but has " + bb.length() + ".");
        bb.removeInt();
        commandId = bb.removeInt();
        commandStatus = bb.removeInt();
        sequenceNumber = bb.removeInt();
    }

    protected abstract ByteBuffer body() throws PduException;

    public ByteBuffer buffer() throws PduException {
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

    public long getCommandId() {
        return commandId;
    }

    public long getCommandStatus() {
        return commandStatus;
    }

    public long getSequenceNumber() {
        return sequenceNumber;
    }

    public void setCommandStatus(long commandStatus) {
        this.commandStatus = commandStatus;
    }

    public void setSequenceNumber(long sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }
}
