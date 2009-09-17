package org.bulatnig.smpp.pdu.tlv;

import java.util.HashMap;
import java.util.Map;

/**
 * HashMap реализация хелпера.
 *
 * User: Bulat Nigmatullin
 * Date: Oct 30, 2008
 * Time: 7:19:40 AM
 */
public enum TLVHelperImpl implements TLVHelper{
    INSTANCE;

    private final Map<Integer, ParameterTag> parameterTags= new HashMap<Integer, ParameterTag>();

    {
        for (ParameterTag parameterTag : ParameterTag.values()) {
            parameterTags.put(parameterTag.getValue(), parameterTag);
        }
    }

    /**
     * {@inheritDoc}
     */
    public ParameterTag getParameterTag(int parameterTagValue) throws ParameterTagNotFoundException {
        ParameterTag parameterTag = parameterTags.get(parameterTagValue);
        if (parameterTag != null)
            return parameterTag;
        else
            throw new ParameterTagNotFoundException("Corresponding parameter tag not found by value " + parameterTagValue);
    }
}
