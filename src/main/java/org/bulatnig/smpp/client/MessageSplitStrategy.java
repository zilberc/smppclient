package org.bulatnig.smpp.client;

/**
 * Comment here.
 * <p/>
 * User: Bulat Nigmatullin
 * Date: 16.05.2009
 * Time: 12:20:58
 */
public enum MessageSplitStrategy {
    PAYLOAD((byte) 1),
    SAR((byte) 2);

    private final byte value;

    private MessageSplitStrategy(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }
}
