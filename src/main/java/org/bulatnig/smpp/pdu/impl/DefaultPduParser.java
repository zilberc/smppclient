package org.bulatnig.smpp.pdu.impl;

import org.bulatnig.smpp.pdu.*;
import org.bulatnig.smpp.util.ByteBuffer;

/**
 * Default PDU parser implementation.
 *
 * @author Bulat Nigmatullin
 */
public class DefaultPduParser implements PduParser {

    @Override
    public Pdu parse(ByteBuffer bb) throws PduParsingException, PduNotFoundException {
        long commandId = bb.readInt(4);
        if (CommandId.BIND_TRANSCEIVER_RESP == commandId) {
            return new BindTransceiverResp(bb);
        } if (CommandId.GENERIC_NACK == commandId) {
            return new GenericNack(bb);
        } else {
            throw new PduNotFoundException(commandId, bb);
        }
    }
}
