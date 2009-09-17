package org.bulatnig.smpp.pdu;

/**
 * Comment here.
 * <p/>
 * User: Bulat Nigmatullin
 * Date: Nov 16, 2008
 * Time: 6:50:31 PM
 */
public class CommandStatusNotFoundException extends PDUException {

    public CommandStatusNotFoundException() {
        super();
    }

    public CommandStatusNotFoundException(String message) {
        super(message);
    }

    public CommandStatusNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommandStatusNotFoundException(Throwable cause) {
        super(cause);
    }
}
