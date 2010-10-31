package org.bulatnig.smpp.pdu.tlv;

import org.bulatnig.smpp.SmppException;

/**
 * Comment here.
 * <p/>
 * User: Bulat Nigmatullin
 * Date: Nov 12, 2008
 * Time: 9:39:18 AM
 */
public class TLVException extends SmppException {

    TLVException() {
        super();
    }

    TLVException(String message) {
        super(message);
    }

    TLVException(String message, Throwable cause) {
        super(message, cause);
    }

    TLVException(Throwable cause) {
        super(cause);
    }
}
