package org.bulatnig.smpp.client;

import org.bulatnig.smpp.SMPPException;

/**
 * Comment here.
 * <p/>
 * User: Bulat Nigmatullin
 * Date: 22.04.2009
 * Time: 18:57:09
 */
public class MessageDeliveryException extends SMPPException {

    private final PDUError pduError;

    public MessageDeliveryException(PDUError pduError) {
        this.pduError = pduError;
    }

    public PDUError getPduError() {
        return pduError;
    }
}