package org.bulatnig.smpp.client.errors;

import org.bulatnig.smpp.pdu.CommandStatus;

/**
 * SMPP defined PDU error status.
 * <p/>
 * User: Bulat Nigmatullin
 * Date: 22.04.2009
 * Time: 20:23:21
 */
class StandardPDUError implements PDUError {

    private final CommandStatus errorCode;

    StandardPDUError(CommandStatus errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public CommandStatus getErrorCode() {
        return errorCode;
    }

    @Override
    public long getErrorCodeValue() {
        return errorCode.getValue();
    }

    @Override
    public String getDescription() {
        return errorCode.getDescription();
    }

}
