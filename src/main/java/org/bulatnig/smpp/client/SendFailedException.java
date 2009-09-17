package org.bulatnig.smpp.client;

import org.bulatnig.smpp.SMPPException;

/**
 * Comment here.
 * <p/>
 * User: Bulat Nigmatullin
 * Date: Nov 19, 2008
 * Time: 3:02:31 PM
 */
public class SendFailedException extends SMPPException {

    public SendFailedException() {
        super();
    }

    public SendFailedException(String message) {
        super(message);
    }

    public SendFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public SendFailedException(Throwable cause) {
        super(cause);
    }
}
