package org.bulatnig.smpp.pdu.udh;

/**
 * Comment here.
 * <p/>
 * User: Bulat Nigmatullin
 * Date: 15.05.2009
 * Time: 19:34:59
 */
public interface UDHHelper {

    public UDHType getUDHType(int udhTypeValue) throws UDHTypeNotFoundException;

}
