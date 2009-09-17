package org.bulatnig.smpp.client;

/**
 * Comment here.
 * <p/>
 * User: Bulat Nigmatullin
 * Date: 27.04.2009
 * Time: 19:45:04
 */
public enum MessageCoding {
    ASCII((short) 1, 160),
    LATIN1((short) 3, 140),
    UCS2((short) 8, 70);

    /**
     * Код типа соединения.
     */
    private final short value;

    private final int maxMessageLength;

    
    private MessageCoding(final short val, final int maxMessageLength) {
        value = val;
        this.maxMessageLength = maxMessageLength;
    }

    /**
     * Возвращает код типа соединения.
     *
     * @return код типа соединения
     */
    public final short getValue() {
        return value;
    }

    public int getMaxMessageLength() {
        return maxMessageLength;
    }
}
