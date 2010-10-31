package org.bulatnig.smpp.client;

import org.bulatnig.smpp.SmppException;

/**
 * Comment here.
 * <p/>
 * User: Bulat Nigmatullin
 * Date: Nov 6, 2008
 * Time: 11:31:07 AM
 */
public class ProcessingFailedException extends SmppException {

    public ProcessingFailedException() {
        super();
    }

    public ProcessingFailedException(String message) {
        super(message);
    }

    public ProcessingFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProcessingFailedException(Throwable cause) {
        super(cause);
    }
}
