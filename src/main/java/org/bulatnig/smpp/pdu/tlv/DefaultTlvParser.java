package org.bulatnig.smpp.pdu.tlv;

import org.bulatnig.smpp.pdu.tlv.impl.TlvImpl;
import org.bulatnig.smpp.util.ByteBuffer;

import java.util.HashMap;
import java.util.Map;

/**
 * Default TLV parser implementation.
 *
 * @author Bulat Nigmatullin
 */
public class DefaultTlvParser implements TlvParser {

    @Override
    public Map<Integer, Tlv> parse(ByteBuffer bb) throws TlvException {
        Map<Integer, Tlv> tlvs = null;
        if (bb.length() >= 4)
            tlvs = new HashMap<Integer, Tlv>();
        while (bb.length() > 0) {
            int tag = bb.removeShort();
            int length = bb.removeShort();
            byte[] value = bb.removeBytes(length);
            Tlv tlv = new TlvImpl(tag);
            tlv.setValue(value);
            tlvs.put(tag, tlv);
        }
        return tlvs;
    }

}
