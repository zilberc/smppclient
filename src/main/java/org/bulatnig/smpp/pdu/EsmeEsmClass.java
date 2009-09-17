package org.bulatnig.smpp.pdu;

/**
 * The esm_class parameter is encoded as follows in the submit_sm, submit_multi and data_sm (ESME -> SMSC) PDUs.
 * <p/>
 * User: Bulat Nigmatullin
 * Date: Dec 3, 2008
 * Time: 8:56:43 AM
 */
public class EsmeEsmClass extends EsmClass {

    public EsmeEsmClass() {
        super(EsmeMessagingMode.DEFAULT_SMSC, EsmeMessageType.DEFAULT, EsmeGSMFeatures.NO_FEATURES);
    }

    public EsmeEsmClass(short value) {
        super(value);
    }

    public EsmeEsmClass(EsmeMessagingMode mode, EsmeMessageType type, EsmeGSMFeatures features) {
        super(mode, type, features);
    }

    EsmeMessagingMode parseMessagingMode(byte mode) {
        EsmeMessagingMode result;
        switch (mode) {
            case 0:
                result = EsmeMessagingMode.DEFAULT_SMSC;
                break;
            case 1:
                result = EsmeMessagingMode.DATAGRAM;
                break;
            case 2:
                result = EsmeMessagingMode.FORWARD;
                break;
            case 3:
                result = EsmeMessagingMode.STORE_AND_FORWARD;
                break;
            default:
                result = EsmeMessagingMode.DEFAULT_SMSC;
        }
        return result;
    }

    EsmeMessageType parseMessageType(byte type) {
        EsmeMessageType result;
        switch (type) {
            case 0:
                result = EsmeMessageType.DEFAULT;
                break;
            case 2:
                result = EsmeMessageType.DELIVERY_ACKNOWLEDGEMENT;
                break;
            case 4:
                result = EsmeMessageType.USER_ACKNOWLEDGEMENT;
                break;
            default:
                result = EsmeMessageType.DEFAULT;
                break;
        }
        return result;
    }

    EsmeGSMFeatures parseGSMFeatures(byte features) {
        EsmeGSMFeatures result;
        switch (features) {
            case 0:
                result = EsmeGSMFeatures.NO_FEATURES;
                break;
            case 1:
                result = EsmeGSMFeatures.UDHI_INDICATOR;
                break;
            case 2:
                result = EsmeGSMFeatures.REPLY_PATH;
                break;
            case 3:
                result = EsmeGSMFeatures.UDHI_AND_REPLY_PATH;
                break;
            default:
                result = EsmeGSMFeatures.NO_FEATURES;
                break;
        }
        return result;
    }

    public EsmeMessagingMode getMode() {
        return (EsmeMessagingMode) mode;
    }

    public EsmeMessageType getType() {
        return (EsmeMessageType) type;
    }

    public EsmeGSMFeatures getFeatures() {
        return (EsmeGSMFeatures) features;
    }

    public enum EsmeMessagingMode implements MessagingMode {
        DEFAULT_SMSC((byte) 0),
        DATAGRAM((byte) 1),
        FORWARD((byte) 2),
        STORE_AND_FORWARD((byte) 3);

        private final byte mode;

        private EsmeMessagingMode(byte mode) {
            this.mode = mode;
        }

        public byte getValue() {
            return mode;
        }
    }

    public enum EsmeMessageType implements MessageType {
        DEFAULT((byte) 0),
        DELIVERY_ACKNOWLEDGEMENT((byte) 2),
        USER_ACKNOWLEDGEMENT((byte) 4);

        private final byte type;

        private EsmeMessageType(byte type) {
            this.type = type;
        }

        public byte getValue() {
            return type;
        }

    }

    public enum EsmeGSMFeatures implements GSMFeatures {
        NO_FEATURES((byte) 0),
        UDHI_INDICATOR((byte) 1),
        REPLY_PATH((byte) 2),
        UDHI_AND_REPLY_PATH((byte) 3);

        private final byte features;

        private EsmeGSMFeatures(byte features) {
            this.features = features;
        }

        public byte getValue() {
            return features;
        }
    }

}
