package org.bulatnig.smpp.pdu;

/**
 * PDU parsing exception.
 *
 * @author Bulat Nigmatullin
 */
public class PduParsingException extends PduException {

    public PduParsingException(String message) {
        super(message);
    }

    public PduParsingException(String message, Throwable cause) {
        super(message, cause);
    }
}
