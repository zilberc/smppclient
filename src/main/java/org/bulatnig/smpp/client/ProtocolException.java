package org.bulatnig.smpp.client;

import org.bulatnig.smpp.SMPPException;

/**
 * Interaction with SMSC is broken. This is critical error.
 * Throwed by Client on Session IOException or PDUException.
 *
 * User: Bulat Nigmatullin
 * Date: 22.04.2009
 * Time: 18:42:48
 */
public class ProtocolException extends SMPPException {

    public ProtocolException() {
        super();
    }

    public ProtocolException(String s) {
        super(s);
    }

    public ProtocolException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public ProtocolException(Throwable throwable) {
        super(throwable);
    }
}
