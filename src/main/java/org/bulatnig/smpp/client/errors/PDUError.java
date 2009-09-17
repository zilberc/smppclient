package org.bulatnig.smpp.client.errors;

import org.bulatnig.smpp.pdu.CommandStatus;

/**
 * Error status returned by SMSC.
 * <p/>
 * User: Bulat Nigmatullin
 * Date: 22.04.2009
 * Time: 19:28:27
 */
public interface PDUError {

    public CommandStatus getErrorCode();

    public long getErrorCodeValue();

    public String getDescription();

}
