package org.bulatnig.smpp.pdu.udh;

import org.bulatnig.smpp.pdu.PDUException;

/**
 * Comment here.
 * <p/>
 * User: Bulat Nigmatullin
 * Date: 15.05.2009
 * Time: 19:35:33
 */
public class UDHException extends PDUException {

    public UDHException() {
        super();
    }

    public UDHException(String message) {
        super(message);
    }

    public UDHException(String message, Throwable cause) {
        super(message, cause);
    }

    public UDHException(Throwable cause) {
        super(cause);
    }
}
