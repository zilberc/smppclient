package org.bulatnig.smpp.pdu.udh;

import java.util.HashMap;
import java.util.Map;

/**
 * Comment here.
 * <p/>
 * User: Bulat Nigmatullin
 * Date: 15.05.2009
 * Time: 19:54:10
 */
public enum UDHHelperImpl implements UDHHelper {
    INSTANCE;

    private final Map<Short, UDHType> udhTypes = new HashMap<Short, UDHType>();

    {
        for (UDHType udhType : UDHType.values()) {
            udhTypes.put(udhType.getValue(), udhType);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UDHType getUDHType(short udhTypeValue) throws UDHTypeNotFoundException {
        UDHType udhType = udhTypes.get(udhTypeValue);
        if (udhType != null)
            return udhType;
        else
            throw new UDHTypeNotFoundException("Corresponding udh type not found by value " + udhTypeValue);
    }

}
