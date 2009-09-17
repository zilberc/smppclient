package org.bulatnig.smpp.pdu.tlv;

import org.bulatnig.smpp.SMPPException;

/**
 * Comment here.
 * <p/>
 * User: Bulat Nigmatullin
 * Date: Nov 12, 2008
 * Time: 9:39:18 AM
 */
public class TLVException extends SMPPException {

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
