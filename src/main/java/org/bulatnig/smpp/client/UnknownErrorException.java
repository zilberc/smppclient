package org.bulatnig.smpp.client;

import org.bulatnig.smpp.SMPPException;

/**
 * Comment here.
 * <p/>
 * User: Bulat Nigmatullin
 * Date: Nov 19, 2008
 * Time: 3:34:11 PM
 */
public class UnknownErrorException extends SMPPException {

    public UnknownErrorException() {
        super();
    }

    public UnknownErrorException(String message) {
        super(message);
    }

    public UnknownErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnknownErrorException(Throwable cause) {
        super(cause);
    }
}
