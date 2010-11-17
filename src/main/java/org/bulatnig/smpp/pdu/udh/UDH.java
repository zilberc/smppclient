package org.bulatnig.smpp.pdu.udh;

import org.bulatnig.smpp.util.SmppByteBuffer;

/**
 * Comment here.
 * <p/>
 * User: Bulat Nigmatullin
 * Date: 04.05.2009
 * Time: 15:22:23
 */
public abstract class UDH {

    /**
     * udhLength field length + udhType field length = 2 octets
     */
    public static final int HEADER_LENGTH = 2;

    /**
     * Helper.
     */
    private static final UDHHelper helper = UDHHelperImpl.INSTANCE;

    /**
     * udhLength = dataLength + HEADER_LENGTH
     */
    private short length;

    private UDHType type;

    protected UDH(UDHType type) {
        this.type = type;
    }

    protected UDH(byte[] bytes) throws UDHException {
        SmppByteBuffer bb = new SmppByteBuffer(bytes);
        parseHeader(bb.removeBytes(HEADER_LENGTH));
        if (bytes.length == length) {
            parseBody(bb.array());
        } else {
            throw new UDHException("UDH has wrong length " + bytes.length + " but should be " + length);
        }
    }

    private void parseHeader(final SmppByteBuffer bb) throws UDHException {
        length = (short) (bb.removeByte() + 1);
        type = helper.getUDHType(bb.removeByte());
    }

    protected abstract void parseBody(final byte[] bytes) throws UDHException;

    public final byte[] getBytes() throws UDHException {
        byte[] body = getBodyBytes();
        if (body.length > 0) {
            length = (short) (HEADER_LENGTH + body.length);
            SmppByteBuffer bb = getHeader();
            bb.appendBytes(body);
            return bb.array();
        } else {
            return getHeader().array();
        }
    }

    private SmppByteBuffer getHeader() throws UDHException {
        SmppByteBuffer bb = new SmppByteBuffer();
        bb.appendByte(length);
        bb.appendByte(type.getValue());
        return bb;
    }

    protected abstract byte[] getBodyBytes() throws UDHException;

    public final short length() {
        return length;
    }

    public final UDHType type() {
        return type;
    }

    @Override
    public String toString() {
        return "UDH{" +
                "length=" + length +
                ", type=" + type +
                '}';
    }

}
