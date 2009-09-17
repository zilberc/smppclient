package org.bulatnig.smpp.client.errors;

import org.bulatnig.smpp.pdu.CommandStatus;

/**
 * Comment here.
 * <p/>
 * User: Bulat Nigmatullin
 * Date: 22.04.2009
 * Time: 20:28:14
 */
public class UnrecoverableStandardPDUError extends StandardPDUError implements UnrecoverablePDUError {

    public UnrecoverableStandardPDUError(CommandStatus errorCode) {
        super(errorCode);
    }

}
