package org.bulatnig.smpp.pdu.udh;

/**
 * Comment here.
 * <p/>
 * User: Bulat Nigmatullin
 * Date: 15.05.2009
 * Time: 19:50:58
 */
public class UDHTypeNotFoundException extends UDHException {

    public UDHTypeNotFoundException() {
        super();
    }

    public UDHTypeNotFoundException(String message) {
        super(message);
    }

    public UDHTypeNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public UDHTypeNotFoundException(Throwable cause) {
        super(cause);
    }
}
