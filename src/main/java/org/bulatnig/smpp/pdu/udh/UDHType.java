package org.bulatnig.smpp.pdu.udh;

/**
 * Comment here.
 * <p/>
 * User: Bulat Nigmatullin
 * Date: 15.05.2009
 * Time: 19:37:12
 */
public enum UDHType {
    IEI(0x00),
    FORMATTED_TEXT(0x0A),
    PREDEFINED_MELODY(0x0B),
    LOADABLE_MELODY(0x0C),
    ANIMATED_CARTOON_LARGE(0x0E),
    ANIMATED_CARTOON(0x0F),
    LOADABLE_PICTURE(0x11),
    UPI(0x13),
    COLOR_PICTURE(0x14),
    ODI(0x17);

    /**
     * Численное выражение ID команды.
     */
    private int value;

    /**
     * Конструктор.
     *
     * @param shortVal численное выражение ID команды
     */
    private UDHType(final int shortVal) {
        value = shortVal;
    }

    /**
     * Возвращает численное выражение ID команды.
     *
     * @return число
     */
    public final int getValue() {
        return value;
	}


}
