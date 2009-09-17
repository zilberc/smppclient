package org.bulatnig.smpp.session;

import org.bulatnig.smpp.SMPPException;

/**
 * No response on sent message from SMSC received.
 * <p/>
 * User: Bulat Nigmatullin
 * Date: Nov 5, 2008
 * Time: 9:24:09 AM
 */
public class NoResponseException extends SMPPException {

    public NoResponseException() {
        super();
    }

    public NoResponseException(String message) {
        super(message);
    }

    public NoResponseException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoResponseException(Throwable cause) {
        super(cause);
    }
}
