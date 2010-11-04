package org.bulatnig.smpp.pdu;

/**
 * The esm_class parameter is used to indicate special message attributes associated with the short message.
 * <p/>
 * User: Bulat Nigmatullin
 * Date: Dec 3, 2008
 * Time: 8:50:07 AM
 */
public abstract class EsmClass {

    static final short MODE_MASK = Short.parseShort("00000011", 2);
    static final short TYPE_MASK = Short.parseShort("00111100", 2);
    static final short FEATURES_MASK = Short.parseShort("11000000", 2);

    final MessagingMode mode;
    final MessageType type;
    final GSMFeatures features;

    EsmClass(final int value) {
        mode = parseMessagingMode((byte) (value & MODE_MASK));
        type = parseMessageType((byte) ((value & TYPE_MASK) >> 2));
        features = parseGSMFeatures((byte) ((value & FEATURES_MASK) >> 6));
    }

    EsmClass(MessagingMode mode, MessageType type, GSMFeatures features) {
        this.mode = mode;
        this.type = type;
        this.features = features;
    }

    public short getValue() {
        return (short) (mode.getValue() + (type.getValue() << 2) + (features.getValue() << 6));
    }

    abstract MessagingMode parseMessagingMode(byte mode);

    abstract MessageType parseMessageType(byte type);

    abstract GSMFeatures parseGSMFeatures(byte features);

    public interface MessagingMode {
        byte getValue();
    }

    public interface MessageType {
        byte getValue();
    }

    public interface GSMFeatures {
        byte getValue();
    }

}
