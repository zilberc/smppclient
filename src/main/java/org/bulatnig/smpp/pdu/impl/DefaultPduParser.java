package org.bulatnig.smpp.pdu.impl;

import org.bulatnig.smpp.pdu.*;
import org.bulatnig.smpp.pdu.tlv.DefaultTlvParser;
import org.bulatnig.smpp.pdu.tlv.TlvException;
import org.bulatnig.smpp.pdu.tlv.TlvParser;
import org.bulatnig.smpp.util.ByteBuffer;

/**
 * Default PDU parser implementation.
 *
 * @author Bulat Nigmatullin
 */
public class DefaultPduParser implements PduParser {

    public TlvParser tlvParser = new DefaultTlvParser();

    @Override
    public Pdu parse(ByteBuffer bb) throws PduException {
        long commandId = bb.readInt(4);
        AbstractPdu result;
        if (CommandId.BIND_TRANSCEIVER_RESP == commandId) {
            result = new BindTransceiverResp(bb);
        } else if (CommandId.GENERIC_NACK == commandId) {
            result = new GenericNack(bb);
        } else if (CommandId.ALERT_NOTIFICATION == commandId) {
            result = new AlertNotification(bb);
        } else {
            throw new PduException("Corresponding PDU not found: " + commandId + ".");
        }
        if (bb.length() > 0) {
            try {
                result.tlvs = tlvParser.parse(bb);
            } catch (TlvException e) {
                throw new PduException("TLV parsing failed.", e);
            }
        }
        return result;
    }
}
