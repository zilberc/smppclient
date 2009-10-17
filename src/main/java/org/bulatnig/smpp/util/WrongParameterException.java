package org.bulatnig.smpp.util;

/**
 * Wrong value set or parameter passed to method.
 *
 * User: Bulat Nigmatullin
 * Date: 08.06.2008
 * Time: 18:44:03
 */
public final class WrongParameterException extends SMPPByteBufferException {

    WrongParameterException() {
        super();
    }

    WrongParameterException(String message) {
        super(message);
    }

    WrongParameterException(String message, Throwable cause) {
        super(message, cause);
    }

    WrongParameterException(Throwable cause) {
        super(cause);
    }
}
