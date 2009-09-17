package org.bulatnig.smpp.pdu.udh;

/**
 * Comment here.
 * <p/>
 * User: Bulat Nigmatullin
 * Date: 16.05.2009
 * Time: 11:21:04
 */
public class StubUDH extends UDH {

    private byte[] bytes;

    public StubUDH(byte[] bytes) throws UDHException {
        super(bytes);
    }

    @Override
    protected void parseBody(byte[] bytes) throws UDHException {
        this.bytes = bytes;
    }

    @Override
    protected byte[] getBodyBytes() throws UDHException {
        return bytes;
    }
}
