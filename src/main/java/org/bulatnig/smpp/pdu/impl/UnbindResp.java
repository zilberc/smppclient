package org.bulatnig.smpp.pdu.impl;

import org.bulatnig.smpp.pdu.CommandId;
import org.bulatnig.smpp.pdu.PduParsingException;
import org.bulatnig.smpp.util.ByteBuffer;

/**
 * Unbind Response PDU.
 *
 * @author Bulat Nigmatullin
 */
public class UnbindResp extends AbstractPdu {

    protected UnbindResp() {
        super(CommandId.UNBIND_RESP);
    }

    protected UnbindResp(ByteBuffer bb) throws PduParsingException {
        super(bb);
    }

    @Override
    protected ByteBuffer body() throws PduParsingException {
        return null;
    }

}
