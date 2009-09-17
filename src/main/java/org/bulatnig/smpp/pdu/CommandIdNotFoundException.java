package org.bulatnig.smpp.pdu;

/**
 * Comment here.
 * <p/>
 * User: Bulat Nigmatullin
 * Date: Nov 16, 2008
 * Time: 6:48:59 PM
 */
public class CommandIdNotFoundException extends PDUException {

    public CommandIdNotFoundException() {
        super();
    }

    public CommandIdNotFoundException(String message) {
        super(message);
    }

    public CommandIdNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommandIdNotFoundException(Throwable cause) {
        super(cause);
    }
}
