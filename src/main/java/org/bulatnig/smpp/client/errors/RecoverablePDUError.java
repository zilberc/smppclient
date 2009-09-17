package org.bulatnig.smpp.client.errors;

/**
 * Recoverable PDU error.
 * <p/>
 * User: Bulat Nigmatullin
 * Date: 22.04.2009
 * Time: 19:28:48
 */
public interface RecoverablePDUError extends PDUError {

    /**
     * @return required timeout before message resend
     */
    public int getRepeatTimeout();

}
