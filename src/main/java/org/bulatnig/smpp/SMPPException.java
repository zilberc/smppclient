package org.bulatnig.smpp;

/**
 * General SMPP exception.
 * 
 * @author Bulat Nigmatullin
 *
 */
public class SMPPException extends Exception {

    public SMPPException() {
        super();
    }

    public SMPPException(String message) {
        super(message);
    }

    public SMPPException(String message, Throwable cause) {
        super(message, cause);
    }

    public SMPPException(Throwable cause) {
        super(cause);
    }
}
