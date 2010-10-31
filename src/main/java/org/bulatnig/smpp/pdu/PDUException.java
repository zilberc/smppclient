package org.bulatnig.smpp.pdu;

import org.bulatnig.smpp.SmppException;


/**
 * PDU исключение.
 * 
 * @author Bulat Nigmatullin
 *
 */
public class PDUException extends SmppException {

    public PDUException() {
        super();
    }

    public PDUException(String message) {
        super(message);
    }

    public PDUException(String message, Throwable cause) {
        super(message, cause);
    }

    public PDUException(Throwable cause) {
        super(cause);
    }
}
