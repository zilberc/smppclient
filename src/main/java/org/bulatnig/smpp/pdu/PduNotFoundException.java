package org.bulatnig.smpp.pdu;

import org.bulatnig.smpp.util.ByteBuffer;

/**
 * PDU corresponding to read command id not found.
 *
 * @author Bulat Nigmatullin
 */
public class PduNotFoundException extends PduException {

    private final long commandId;
    private final ByteBuffer bytes;

    public PduNotFoundException(long commandId, ByteBuffer bytes) {
        super("PDU corresponding to command id " + commandId + " not found.");
        this.commandId = commandId;
        this.bytes = bytes;
    }

    public long getCommandId() {
        return commandId;
    }

    public ByteBuffer getBytes() {
        return bytes;
    }

}
