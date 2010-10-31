package org.bulatnig.smpp.pdu.udh;

import org.bulatnig.smpp.util.SmppByteBuffer;
import org.bulatnig.smpp.util.WrongLengthException;

/**
 * Comment here.
 * <p/>
 * User: Bulat Nigmatullin
 * Date: 16.05.2009
 * Time: 10:28:01
 */
public enum UDHFactoryImpl implements UDHFactory {
    INSTANCE;

    private final UDHHelper helper = UDHHelperImpl.INSTANCE;

    @Override
    public UDH parseUDH(byte[] bytes) throws UDHException {
        UDH udh;
        if (bytes.length >= UDH.HEADER_LENGTH) {
            SmppByteBuffer byteBuffer = new SmppByteBuffer(bytes);
            short length;
            try {
                length = (short) (byteBuffer.removeByte() + 1);
            } catch (WrongLengthException e) {
                throw new UDHException("FATAL ERROR while reading UDH length field", e);
            }
            if (length == bytes.length) {
                short udhTypeId;
                try {
                    udhTypeId = byteBuffer.removeByte();
                } catch (WrongLengthException e) {
                    throw new UDHException("FATAL ERROR while reading UDH type field", e);
                }
                UDHType udhType = helper.getUDHType(udhTypeId);
                switch (udhType) {
                    case IEI:
                        udh = new IEI(bytes);
                        break;
                    default:
                        udh = new StubUDH(bytes);
                }
            } else {
                throw new UDHException("UDH has wrong length. Expected " + length + " but has " + bytes.length);
            }
        } else {
            throw new UDHException("UDH has not enough length to read header. Expected " + UDH.HEADER_LENGTH + " but has " + bytes.length);
        }
        return udh;
    }

}
