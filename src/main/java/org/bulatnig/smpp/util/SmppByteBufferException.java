package org.bulatnig.smpp.util;

import org.bulatnig.smpp.SmppException;

/**
 * Comment here.
 * <p/>
 * User: Bulat Nigmatullin
 * Date: Nov 12, 2008
 * Time: 9:26:06 AM
 */
public class SmppByteBufferException extends SmppException {

    SmppByteBufferException() {
        super();
    }

    SmppByteBufferException(String message) {
        super(message);
    }

    SmppByteBufferException(String message, Throwable cause) {
        super(message, cause);
    }

    SmppByteBufferException(Throwable cause) {
        super(cause);
    }
}
