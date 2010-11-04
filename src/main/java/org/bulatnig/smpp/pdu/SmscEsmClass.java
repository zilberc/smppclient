package org.bulatnig.smpp.pdu;

/**
 * esm_class received from SMSC.
 * <p/>
 * User: Bulat Nigmatullin
 * Date: Dec 3, 2008
 * Time: 10:49:53 AM
 */
public class SmscEsmClass extends EsmClass {

    public SmscEsmClass() {
        super(SmscMessagingMode.DEFAULT, SmscMessageType.DEFAULT, SmscGSMFeatures.NO_FEATURES);
    }

    public SmscEsmClass(int value) {
        super(value);
    }

    public SmscEsmClass(SmscMessageType type, SmscGSMFeatures features) {
        super(SmscMessagingMode.DEFAULT, type, features);
    }

    SmscMessagingMode parseMessagingMode(byte mode) {
        return SmscMessagingMode.DEFAULT;
    }

    SmscMessageType parseMessageType(byte type) {
        SmscMessageType result;
        switch (type) {
            case 0:
                result = SmscMessageType.DEFAULT;
                break;
            case 1:
                result = SmscMessageType.SMSC_DELIVERY_RECEIPT;
                break;
            case 2:
                result = SmscMessageType.SME_DELIVERY_ACKNOWLEDGEMENT;
                break;
            case 4:
                result = SmscMessageType.SME_USER_ACKNOWLEDGEMENT;
                break;
            case 6:
                result = SmscMessageType.CONVERSATION_ABORT;
                break;
            case 8:
                result = SmscMessageType.INTERMEDIATE_DELIVERY_NOTIFICATION;
                break;
            default:
                result = SmscMessageType.RESERVER;
                break;
        }
        return result;
    }

    SmscGSMFeatures parseGSMFeatures(byte features) {
        SmscGSMFeatures result;
        switch(features) {
            case 0:
                result = SmscGSMFeatures.NO_FEATURES;
                break;
            case 1:
                result = SmscGSMFeatures.UDHI_INDICATOR;
                break;
            case 2:
                result = SmscGSMFeatures.REPLY_PATH;
                break;
            case 3:
                result = SmscGSMFeatures.UDHI_AND_REPLY_PATH;
                break;
            default:
                result = SmscGSMFeatures.NO_FEATURES;
                break;
        }
        return result;
    }

    public enum SmscMessagingMode implements MessagingMode {
        DEFAULT((byte) 0);

        private final byte mode;

        private SmscMessagingMode(byte mode) {
            this.mode = mode;
        }

        public byte getValue() {
            return mode;
        }
    }

    public SmscMessagingMode getMode() {
        return (SmscMessagingMode) mode;
    }

    public SmscMessageType getType() {
        return (SmscMessageType) type;
    }

    public SmscGSMFeatures getFeatures() {
        return (SmscGSMFeatures) features;
    }

    public enum SmscMessageType implements MessageType {
        DEFAULT((byte) 0),
        SMSC_DELIVERY_RECEIPT((byte) 1),
        SME_DELIVERY_ACKNOWLEDGEMENT((byte) 2),
        SME_USER_ACKNOWLEDGEMENT((byte) 4),
        CONVERSATION_ABORT((byte) 6),
        INTERMEDIATE_DELIVERY_NOTIFICATION((byte) 8),
        RESERVER((byte) -1);

        private final byte type;

        private SmscMessageType(byte type) {
            this.type = type;
        }

        public byte getValue() {
            return type;
        }

    }

    public enum SmscGSMFeatures implements GSMFeatures {
        NO_FEATURES((byte) 0),
        UDHI_INDICATOR((byte) 1),
        REPLY_PATH((byte) 2),
        UDHI_AND_REPLY_PATH((byte) 3);

        private final byte type;

        private SmscGSMFeatures(byte type) {
            this.type = type;
        }

        public byte getValue() {
            return type;
        }

    }

}
