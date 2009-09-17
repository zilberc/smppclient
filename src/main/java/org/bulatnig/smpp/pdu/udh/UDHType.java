package org.bulatnig.smpp.pdu.udh;

/**
 * Comment here.
 * <p/>
 * User: Bulat Nigmatullin
 * Date: 15.05.2009
 * Time: 19:37:12
 */
public enum UDHType {
    IEI((short) 0x00),
    FORMATTED_TEXT((short) 0x0A),
    PREDEFINED_MELODY((short) 0x0B),
    LOADABLE_MELODY((short) 0x0C),
    ANIMATED_CARTOON_LARGE((short) 0x0E),
    ANIMATED_CARTOON((short) 0x0F),
    LOADABLE_PICTURE((short) 0x11),
    UPI((short) 0x13),
    COLOR_PICTURE((short) 0x14),
    ODI((short) 0x17);

    /**
     * Численное выражение ID команды.
     */
    private short value;

    /**
     * Конструктор.
     *
     * @param shortVal численное выражение ID команды
     */
    private UDHType(final short shortVal) {
        value = shortVal;
    }

    /**
     * Возвращает численное выражение ID команды.
     *
     * @return число
     */
    public final short getValue() {
        return value;
	}


}
