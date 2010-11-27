package org.bulatnig.smpp.pdu;

import org.bulatnig.smpp.util.ByteBuffer;

/**
 * Parses PDU's from bytes.
 *
 * @author Bulat Nigmatullin
 */
public interface PduParser {

    /**
     * Parse PDU.
     *
     * @param bb one pdu bytes
     * @return Pdu
     * @throws PduParsingException        pdu parsing failed
     * @throws PduNotFoundException corresponding PDU not found
     */
    Pdu parse(ByteBuffer bb) throws PduParsingException, PduNotFoundException;

}
