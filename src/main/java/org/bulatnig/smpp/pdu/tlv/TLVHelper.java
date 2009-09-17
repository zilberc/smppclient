package org.bulatnig.smpp.pdu.tlv;

/**
 * Возвращает ParameterTag, соответсвующий его числовому выражению.
 * Для избежания итераций по возможным значениям используется Map.
 * <p/>
 * User: Bulat Nigmatullin
 * Date: 28.10.2008
 * Time: 16:00:52
 */
public interface TLVHelper {

    /**
     * Возвращает ParameterTag, соответсвующий его числовому выражению.
     *
     * @param parameterTagValue числовое выражение ParameterTag
     * @return ParamTag
     * @throws ParameterTagNotFoundException соответствующий ParameterTag не найден
     */
    public ParameterTag getParameterTag(int parameterTagValue) throws ParameterTagNotFoundException;
}
