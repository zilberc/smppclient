package org.bulatnig.smpp.client.errors;

/**
 * Comment here.
 * <p/>
 * User: Bulat Nigmatullin
 * Date: 22.04.2009
 * Time: 20:36:04
 */
public class RecoverableReservedPDUError extends ReservedPDUError implements RecoverablePDUError {

    private final int repeatTimeout;

    public RecoverableReservedPDUError(long errorCode, String description, int repeatTimeout) {
        super(errorCode, description);
        this.repeatTimeout = repeatTimeout;
    }

    @Override
    public int getRepeatTimeout() {
        return repeatTimeout;
    }

}
