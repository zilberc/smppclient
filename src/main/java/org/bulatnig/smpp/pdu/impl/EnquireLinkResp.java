package org.bulatnig.smpp.pdu.impl;

import org.bulatnig.smpp.pdu.PduException;
import org.bulatnig.smpp.util.ByteBuffer;

/**
 * EnquireLink response PDU.
 *
 * @author Bulat Nigmatullin
 */
public class EnquireLinkResp extends AbstractPdu {

    public EnquireLinkResp(long commandId) {
        super(commandId);
    }

    protected EnquireLinkResp(ByteBuffer bb) throws PduException {
        super(bb);
    }

    @Override
    protected ByteBuffer body() throws PduException {
        return null;
    }

}
