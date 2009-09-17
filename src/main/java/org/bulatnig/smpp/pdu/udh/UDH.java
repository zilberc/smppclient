package org.bulatnig.smpp.pdu.udh;

import org.bulatnig.smpp.SMPPObject;
import org.bulatnig.smpp.util.SMPPByteBuffer;
import org.bulatnig.smpp.util.WrongLengthException;
import org.bulatnig.smpp.util.WrongParameterException;

/**
 * Comment here.
 * <p/>
 * User: Bulat Nigmatullin
 * Date: 04.05.2009
 * Time: 15:22:23
 */
public abstract class UDH extends SMPPObject {

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
        SMPPByteBuffer bb = new SMPPByteBuffer(bytes);
        try {
            parseHeader(bb.removeBytes(HEADER_LENGTH));
        } catch (WrongParameterException e) {
            throw new UDHException("FATAL ERROR wrong header length supplied", e);
        } catch (WrongLengthException e) {
            throw new UDHException("UDH has not enough length to read header", e);
        }
        if (bytes.length == length) {
            parseBody(bb.getBuffer());
        } else {
            throw new UDHException("UDH has wrong length " + bytes.length + " but should be " + length);
        }
    }

    private void parseHeader(final SMPPByteBuffer bb) throws UDHException {
        try {
            length = (short) (bb.removeByte() + 1);
            type = helper.getUDHType(bb.removeByte());
        } catch (WrongLengthException e) {
            throw new UDHException("UDH has wrong header parameters", e);
        }
    }

    protected abstract void parseBody(final byte[] bytes) throws UDHException;

    public final byte[] getBytes() throws UDHException {
        byte[] body = getBodyBytes();
        if (body.length > 0) {
            length = (short) (HEADER_LENGTH + body.length);
            SMPPByteBuffer bb = getHeader();
            bb.appendBytes(body, body.length);
            return bb.getBuffer();
        } else {
            return getHeader().getBuffer();
        }
    }

    private SMPPByteBuffer getHeader() throws UDHException {
        SMPPByteBuffer bb = new SMPPByteBuffer();
        try {
            bb.appendByte(length);
        } catch (WrongParameterException e) {
            throw new UDHException("length field is invalid", e);
        }
        try {
            bb.appendByte(type.getValue());
        } catch (WrongParameterException e) {
            throw new UDHException("type field is invalid", e);
        }
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
