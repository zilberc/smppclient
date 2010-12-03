package org.bulatnig.smpp.pdu.tlv.impl;

import org.bulatnig.smpp.pdu.tlv.Tlv;
import org.bulatnig.smpp.pdu.tlv.TlvException;
import org.bulatnig.smpp.util.ByteBuffer;

/**
 * General TLV implementation.
 *
 * @author Bulat Nigmatullin
 */
public class TlvImpl implements Tlv {

    public TlvImpl(int tag) {

    }

    @Override
    public ByteBuffer buffer() throws TlvException {
        return null;
    }

    @Override
    public int getTag() {
        return 0;
    }

    @Override
    public int getLength() {
        return 0;
    }

    @Override
    public ByteBuffer getValue() {
        return null;
    }

    @Override
    public void setValue(ByteBuffer valueBytes) {
    }
}
