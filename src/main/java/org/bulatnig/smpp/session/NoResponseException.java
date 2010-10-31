package org.bulatnig.smpp.session;

import org.bulatnig.smpp.SmppException;

/**
 * No response on sent message from SMSC received.
 *
 * @author Bulat nigmatullin
 */
public final class NoResponseException extends SmppException {

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
