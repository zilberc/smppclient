package org.bulatnig.smpp.pdu.udh;

/**
 * Comment here.
 * <p/>
 * User: Bulat Nigmatullin
 * Date: 15.05.2009
 * Time: 20:37:24
 */
public interface UDHFactory {

    public UDH parseUDH(byte[] bytes) throws UDHException;

}
