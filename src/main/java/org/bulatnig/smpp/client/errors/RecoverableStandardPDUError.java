package org.bulatnig.smpp.client.errors;

import org.bulatnig.smpp.pdu.CommandStatus;

/**
 * Comment here.
 * <p/>
 * User: Bulat Nigmatullin
 * Date: 22.04.2009
 * Time: 20:13:29
 */
public class RecoverableStandardPDUError extends StandardPDUError implements RecoverablePDUError {

    private final int repeatTimeout;

    public RecoverableStandardPDUError(CommandStatus errorCode, int repeatTimeout) {
        super(errorCode);
        this.repeatTimeout = repeatTimeout;
    }

    @Override
    public int getRepeatTimeout() {
        return repeatTimeout;
    }
}
