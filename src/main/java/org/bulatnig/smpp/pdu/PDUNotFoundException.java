package org.bulatnig.smpp.pdu;

/**
 * Comment here.
 * <p/>
 * User: Bulat Nigmatullin
 * Date: Nov 16, 2008
 * Time: 9:00:21 PM
 */
public class PDUNotFoundException extends PDUException {

    public PDUNotFoundException() {
        super();
    }

    public PDUNotFoundException(String message) {
        super(message);
    }

    public PDUNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public PDUNotFoundException(Throwable cause) {
        super(cause);
    }
}
