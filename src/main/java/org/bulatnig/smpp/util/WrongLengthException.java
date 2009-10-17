package org.bulatnig.smpp.util;

/**
 * Indicates the wrong length of value.
 * 
 * @author Bulat Nigmatullin
 *
 */
public final class WrongLengthException extends SMPPByteBufferException {

    WrongLengthException() {
        super();
    }

    WrongLengthException(String message) {
        super(message);
    }

    WrongLengthException(String message, Throwable cause) {
        super(message, cause);
    }

    WrongLengthException(Throwable cause) {
        super(cause);
    }
}
