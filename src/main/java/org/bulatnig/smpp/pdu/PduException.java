package org.bulatnig.smpp.pdu;

import org.bulatnig.smpp.SmppException;

/**
 * PDU parsing exception.
 *
 * @author Bulat Nigmatullin
 */
public class PduException extends SmppException {

    public PduException(String message) {
        super(message);
    }

    public PduException(String message, Throwable cause) {
        super(message, cause);
    }
}
