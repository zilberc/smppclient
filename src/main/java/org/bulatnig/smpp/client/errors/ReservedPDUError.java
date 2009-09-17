package org.bulatnig.smpp.client.errors;

import org.bulatnig.smpp.pdu.CommandStatus;

/**
 * SMPP not defined (resrved) PDU error status.
 * <p/>
 * User: Bulat Nigmatullin
 * Date: 22.04.2009
 * Time: 20:30:11
 */
class ReservedPDUError implements PDUError {

    private final long errorCode;
    private final String description;

    ReservedPDUError(long errorCode, String description) {
        this.errorCode = errorCode;
        this.description = description;
    }

    @Override
    public CommandStatus getErrorCode() {
        return CommandStatus.RESERVED;
    }

    @Override
    public long getErrorCodeValue() {
        return errorCode;
    }

    @Override
    public String getDescription() {
        return description;
    }
}
