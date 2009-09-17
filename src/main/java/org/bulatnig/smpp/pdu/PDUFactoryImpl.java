package org.bulatnig.smpp.pdu;

import org.bulatnig.smpp.util.SMPPByteBuffer;
import org.bulatnig.smpp.util.WrongLengthException;

/**
 * Простая реализация PDUFactory интерфейса.
 * <p/>
 * User: Bulat Nigmatullin
 * Date: Nov 1, 2008
 * Time: 10:09:21 AM
 */
public enum PDUFactoryImpl implements PDUFactory {
    INSTANCE;

    private final PDUHelper helper = PDUHelperImpl.INSTANCE;

    /**
     * {@inheritDoc}
     */
    public PDU parsePDU(byte[] bytes) throws PDUException {
        PDU pdu;
        if (bytes.length >= PDU.HEADER_LENGTH) {
            byte[] header = new byte[PDU.HEADER_LENGTH];
            System.arraycopy(bytes, 0, header, 0, header.length);
            SMPPByteBuffer headerSbb = new SMPPByteBuffer(header);
            long length;
            try {
                length = headerSbb.removeInt();
            } catch (WrongLengthException e) {
                throw new PDUException("FATAL ERROR while reading PDU length field", e);
            }
            if (length == bytes.length) {
                long cmdId;
                try {
                    cmdId = headerSbb.removeInt();
                } catch (WrongLengthException e) {
                    throw new PDUException("FATAL ERROR while reading PDU command id", e);
                }
                CommandId commandId = helper.getCommandId(cmdId);
                switch (commandId) {
                    case ALERT_NOTIFICATION:
                        pdu = new AlertNotification(bytes);
                        break;
                    case BIND_RECEIVER:
                        pdu = new BindReceiver(bytes);
                        break;
                    case BIND_RECEIVER_RESP:
                        pdu = new BindReceiverResp(bytes);
                        break;
                    case BIND_TRANSCEIVER:
                        pdu = new BindTransceiver(bytes);
                        break;
                    case BIND_TRANSCEIVER_RESP:
                        pdu = new BindTransceiverResp(bytes);
                        break;
                    case BIND_TRANSMITTER:
                        pdu = new BindTransmitter(bytes);
                        break;
                    case BIND_TRANSMITTER_RESP:
                        pdu = new BindTransmitterResp(bytes);
                        break;
                    case CANCEL_SM:
                        pdu = new CancelSM(bytes);
                        break;
                    case CANCEL_SM_RESP:
                        pdu = new CancelSMResp(bytes);
                        break;
                    case DATA_SM:
                        pdu = new DataSM(bytes);
                        break;
                    case DATA_SM_RESP:
                        pdu = new DataSMResp(bytes);
                        break;
                    case DELIVER_SM:
                        pdu = new DeliverSM(bytes);
                        break;
                    case DELIVER_SM_RESP:
                        pdu = new DeliverSMResp(bytes);
                        break;
                    case ENQUIRE_LINK:
                        pdu = new EnquireLink(bytes);
                        break;
                    case ENQUIRE_LINK_RESP:
                        pdu = new EnquireLinkResp(bytes);
                        break;
                    case GENERIC_NACK:
                        pdu = new GenericNack(bytes);
                        break;
                    case OUTBIND:
                        pdu = new OutBind(bytes);
                        break;
                    case QUERY_SM:
                        pdu = new QuerySM(bytes);
                        break;
                    case QUERY_SM_RESP:
                        pdu = new QuerySMResp(bytes);
                        break;
                    case REPLACE_SM:
                        pdu = new ReplaceSM(bytes);
                        break;
                    case REPLACE_SM_RESP:
                        pdu = new ReplaceSMResp(bytes);
                        break;
                    case SUBMIT_MULTI:
                        pdu = new SubmitMulti(bytes);
                        break;
                    case SUBMIT_MULTI_RESP:
                        pdu = new SubmitMultiResp(bytes);
                        break;
                    case SUBMIT_SM:
                        pdu = new SubmitSM(bytes);
                        break;
                    case SUBMIT_SM_RESP:
                        pdu = new SubmitSMResp(bytes);
                        break;
                    case UNBIND:
                        pdu = new Unbind(bytes);
                        break;
                    case UNBIND_RESP:
                        pdu = new UnbindResp(bytes);
                        break;
                    default:
                        throw new PDUNotFoundException("Corresponding PDU not found by command id: " + commandId);
                }
            } else {
                throw new PDUException("PDU has wrong length. Expected " + length + " but has " + bytes.length);
            }
        } else {
            throw new PDUException("PDU has not enough length to read header. Expected " + PDU.HEADER_LENGTH + " but has " + bytes.length);
        }
        return pdu;
    }
}
