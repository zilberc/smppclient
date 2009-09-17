package org.bulatnig.smpp.pdu.tlv;

/**
 * Comment here.
 * <p/>
 * User: Bulat Nigmatullin
 * Date: Nov 12, 2008
 * Time: 9:40:35 AM
 */
public class TLVNotFoundException extends TLVException {

    TLVNotFoundException() {
        super();
    }

    TLVNotFoundException(String message) {
        super(message);
    }

    TLVNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    TLVNotFoundException(Throwable cause) {
        super(cause);
    }
}
