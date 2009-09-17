package org.bulatnig.smpp.client.errors;

/**
 * Comment here.
 * <p/>
 * User: Bulat Nigmatullin
 * Date: 22.04.2009
 * Time: 20:39:29
 */
public class UnrecoverableReservedPDUError extends ReservedPDUError implements UnrecoverablePDUError {

    public UnrecoverableReservedPDUError(long errorCode, String description) {
        super(errorCode, description);
    }

}
