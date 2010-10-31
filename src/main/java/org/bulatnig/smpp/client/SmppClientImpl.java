package org.bulatnig.smpp.client;

import org.bulatnig.smpp.SmppException;
import org.bulatnig.smpp.pdu.*;
import org.bulatnig.smpp.session.NoResponseException;
import org.bulatnig.smpp.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

/**
 * Простая реализация SMPP клиента.
 * <p/>
 * User: Bulat Nigmatullin
 * Date: 18.07.2008
 * Time: 15:48:04
 */
public class SmppClientImpl implements SmppClient {

    /**
     * Logger for this class and subclasses
     */
    final static Logger logger = LoggerFactory.getLogger(SmppClientImpl.class);

    private static final MessageCodingHelper codingHelper = MessageCodingHelper.INSTANCE;

    /**
     * Сессия с SMSC.
     */
    private Session session;
    /**
     * Обработчик входящих сообщений.
     */
    private final MessageHandler handler;
    /**
     * Флаг уведомления о доставке сообщения.
     */
    private boolean registeredDelivery = true;

    private MessageSplitter messageSplitter = PayloadMessageSplitter.INSTANCE;

    private MessageCoding defaultDataCoding = MessageCoding.ASCII;

    /**
     * Latin pattern.
     */
    private final static String LATIN_PATTERN = "[\\p{Print}\\p{Space}]*";

    /**
     * Максимальная длина одного UTF сообщения.
     */
    private final static int MAX_UTF_LENGTH = 70;
    /**
     * Максимальная длина одного Latin сообщения.
     */
    private final static int MAX_LATIN_LENGTH = 140;
    /**
     * Максимальное число сегментов в сообщении.
     */
    private final static int MAX_SEGMENTS = 10;

    private final static int PARTS_DELIVERY_TIMEOUT = 60000;

    /**
     * Обработчик ошибок, возвращаемых SMSC.
     */
    private final ErrorHandler errorHandler = ErrorHandlerImpl.INSTANCE;

    /**
     * sourceAddrTon исходящих сообщений.
     */
    private TON sourceAddrTon = TON.UNKNOWN;
    /**
     * sourceAddrNpi исходящих сообщений.
     */
    private NPI sourceAddrNpi = NPI.UNKNOWN;
    /**
     * destAddrTon исходящих сообщений.
     */
    private TON destAddrTon = TON.UNKNOWN;
    /**
     * destAddrNpi исходящих сообщений.
     */
    private NPI destAddrNpi = NPI.UNKNOWN;

    private boolean work = true;

    public SmppClientImpl(Session session, MessageHandler handler, boolean registeredDelivery) throws SmppException {
        this.session = session;
        this.handler = handler;
        sourceAddrTon = session.getAddrTon();
        sourceAddrNpi = session.getAddrNpi();
        destAddrTon = TON.INTERNATIONAL;
        destAddrNpi = session.getAddrNpi();
        this.registeredDelivery = registeredDelivery;
        handler.start();
        session.setPDUHandler(handler);
    }

    /**
     * {@inheritDoc}
     */
    public void stop() {
        work = false;
        session.stop();
        handler.stop();
    }

    @Override
    public String send(Message message) throws ProtocolException, DeliveryFailedException, DeliveryDelayedException,
            NoResponseException {
        MessageCoding coding = codingHelper.getDataCoding(message.getText());
        if (coding.getMaxMessageLength() < message.getText().length()) {
            List<SubmitSM> parts = messageSplitter.split(message, coding.getMaxMessageLength());
            for (SubmitSM submit : parts) {
                submit.setDataCoding(coding.getValue());
                send(submit);
            }
        } else {
            SubmitSM submit = new SubmitSM();
            submit.setSourceAddr(message.getSourceAddr());
            submit.setDestinationAddr(message.getDestAddr());
            if (defaultDataCoding != coding)
                submit.setDataCoding(coding.getValue());
            submit.setShortMessage(message.getText());
            send(submit);
        }
        return null;
    }

    private String send(SubmitSM submit) throws ProtocolException, DeliveryFailedException, DeliveryDelayedException,
            NoResponseException {
        submit.setSourceAddrTon(sourceAddrTon);
        submit.setSourceAddrNpi(sourceAddrNpi);
        submit.setDestAddrTon(destAddrTon);
        submit.setDestAddrNpi(destAddrNpi);
        if (registeredDelivery) {
            submit.setRegisteredDelivery((short) 1);
        }
        try {
            PDU response = session.send(submit);
            if (CommandStatus.ESME_ROK == response.getCommandStatus() &&
                    response instanceof SubmitSMResp) {
                return ((SubmitSMResp) response).getMessageId();
            } else {
                // TODO error handler framework throwing DeliveryFailedException or DeliveryDelayedException
                return null;
            }
        } catch (IOException e) {
            throw new ProtocolException("I/O error", e);
        } catch (PDUException e) {
            throw new ProtocolException("PDU parsing error", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    /*public String sends(Message message) throws NoResponseException, SendFailedException {
        boolean isUTF = !Pattern.matches(LATIN_PATTERN, message.getText());
        String messageId = null;
        if ((isUTF && message.getText().length() > MAX_UTF_LENGTH) ||
                (!isUTF && message.getText().length() > MAX_LATIN_LENGTH)) {

            SubmitSM submit = new SubmitSM();
            submit.setSourceAddrTon(sourceAddrTon);
            submit.setSourceAddrNpi(sourceAddrNpi);
            submit.setSourceAddr(message.getSourceAddr());
            submit.setDestAddrTon(destAddrTon);
            submit.setDestAddrNpi(destAddrNpi);
            submit.setDestinationAddr(message.getDestAddr());
            if (registeredDelivery) {
                submit.setRegisteredDelivery((short) 1);
            }
            if (isUTF) {
                submit.setDataCoding((byte) 8);
            }
//            submit.setMessagePayload(new MessagePayload(message.getText(), isUTF));


            PDU response;
            do {
                response = null;
                try {
                    response = session.send(submit);
                } catch (IOException e) {
                    logger.warn("IOError during sending " + message, e);
                } catch (PDUException e) {
                    throw new SendFailedException("Wrong PDU constructed", e);
                }
                if (response != null) {
                    if (response.getCommandStatus() == CommandStatus.ESME_ROK) {
                        messageId = ((SubmitSMResp) response).getMessageId();
                    } else {
                        try {
                            PDUError error = errorHandler.handle(response.getCommandStatus());
                            if (error.isRepeatPDU()) {
                                if (error.getTimeout() > 0) {
                                    try {
                                        Thread.sleep(error.getTimeout());
                                    } catch (InterruptedException e) {
                                        // omit it
                                    }
                                } else {
                                    throw new NoResponseException("Try send this message later. Message queue is full");
                                }
                            } else {
                                throw new SendFailedException(response.getCommandStatus().getDescription());
                            }
                        } catch (UnknownErrorException e) {
                            logger.warn("Unknown error received " + e.getMessage());
                        }
                        messageId = null;
                    }
                    continue;
                }
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    // omit it
                }
            } while (messageId == null && work);


            int maxMsgLen = isUTF ? MAX_UTF_LENGTH : MAX_LATIN_LENGTH;
            int msgLen = message.getText().length();
            int segments = msgLen / maxMsgLen;
            if (msgLen % maxMsgLen > 0) {
                segments++;
            }
            if (segments > MAX_SEGMENTS) {
                throw new SendFailedException("Message exceed " + MAX_SEGMENTS + " segments number");
            } else {
                Iterator<SubmitSM> partsIt = getMultipartMessage(message, isUTF, maxMsgLen, segments).iterator();
                SubmitSM submit;
                PDU response;
                long started = System.currentTimeMillis();
                while (partsIt.hasNext() && work &&
                        (System.currentTimeMillis() - started) < PARTS_DELIVERY_TIMEOUT) {
                    submit = partsIt.next();
                    do {
                        response = null;
                        try {
                            response = session.send(submit);
                        } catch (IOException e) {
                            logger.warn("IOError during sending " + message, e);
                        } catch (PDUException e) {
                            throw new SendFailedException("Wrong PDU constructed", e);
                        } catch (NoResponseException e) {
                            logger.warn("No response received", e);
                        }
                        if (response != null) {
                            if (response.getCommandStatus() == CommandStatus.ESME_ROK) {
                                messageId = ((SubmitSMResp) response).getMessageId();
                            } else {
                                try {
                                    PDUError error = errorHandler.handle(response.getCommandStatus());
                                    if (error.isRepeatPDU()) {
                                        if (error.getTimeout() > 0) {
                                            try {
                                                Thread.sleep(error.getTimeout());
                                            } catch (InterruptedException e) {
                                                // omit it
                                            }
                                        }
                                    } else {
                                        throw new SendFailedException(error.getDescription());
                                    }
                                } catch (UnknownErrorException e) {
                                    logger.warn("Unknown error received " + e.getMessage());
                                }
                                messageId = null;
                            }
                        } else {
                            try {
                                Thread.sleep(5000);
                            } catch (InterruptedException e) {
                                // omit it
                            }
                        }
                    }
                    while (messageId == null && (System.currentTimeMillis() - started) < PARTS_DELIVERY_TIMEOUT && work);
                }
            }
        } else {
            SubmitSM submit = getSingleMessage(message, isUTF);
            PDU response;
            do {
                response = null;
                try {
                    response = session.send(submit);
                } catch (IOException e) {
                    logger.warn("IOError during sending " + message, e);
                } catch (PDUException e) {
                    throw new SendFailedException("Wrong PDU constructed", e);
                }
                if (response != null) {
                    if (response.getCommandStatus() == CommandStatus.ESME_ROK) {
                        messageId = ((SubmitSMResp) response).getMessageId();
                    } else {
                        try {
                            PDUError error = errorHandler.handle(response.getCommandStatus());
                            if (error.isRepeatPDU()) {
                                if (error.getTimeout() > 0) {
                                    try {
                                        Thread.sleep(error.getTimeout());
                                    } catch (InterruptedException e) {
                                        // omit it
                                    }
                                } else {
                                    throw new NoResponseException("Try send this message later. Message queue is full");
                                }
                            } else {
                                throw new SendFailedException(response.getCommandStatus().getDescription());
                            }
                        } catch (UnknownErrorException e) {
                            logger.warn("Unknown error received " + e.getMessage());
                        }
                        messageId = null;
                    }
                    continue;
                }
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    // omit it
                }
            } while (messageId == null && work);
        }
        return messageId;
    }     */

//    private List<SubmitSM> getMultipartMessage(Message message, boolean isUTF,
//                                               int maxMsgLen, int segments) {
//        List<SubmitSM> messages = new ArrayList<SubmitSM>();
//        SubmitSM submit;
//        int begin;
//        int end;
//        int textLen = message.getText().length();
//        int smrn = nextSarMsgRefNum();
//        for (int i = 1; i <= segments; i++) {
//            begin = (i - 1) * maxMsgLen;
//            end = i * maxMsgLen;
//            if (end > textLen) {
//                end = textLen;
//            }
//            String msg = message.getText().substring(begin, end);
//            submit = getSingleMessage(new Message(message.getSourceAddr(),
//                    message.getDestAddr(), msg), isUTF);
//            submit.setSarMsgRefNum(new SarMsgRefNum(smrn));
//            submit.setSarTotalSegments(new SarTotalSegments((short) segments));
//            submit.setSarSegmentSeqnum(new SarSegmentSeqnum((short) i));
//            messages.add(submit);
//        }
//        return messages;
//    }

//    private SubmitSM getSingleMessage(Message message, boolean isUTF) {
//        SubmitSM submit = new SubmitSM();
//        submit.setSourceAddrTon(sourceAddrTon);
//        submit.setSourceAddrNpi(sourceAddrNpi);
//        submit.setSourceAddr(message.getSourceAddr());
//        submit.setDestAddrTon(destAddrTon);
//        submit.setDestAddrNpi(destAddrNpi);
//        submit.setDestinationAddr(message.getDestAddr());
//        if (registeredDelivery) {
//            submit.setRegisteredDelivery((short) 1);
//        }
//        if (isUTF) {
//            submit.setDataCoding((byte) 8);
//        }
//        submit.setShortMessage(message.getText());
//        return submit;
//    }

    @Override
    public void setMessageSplitStrategy(MessageSplitStrategy mss) {
        switch (mss) {
            case PAYLOAD:
                messageSplitter = PayloadMessageSplitter.INSTANCE;
                break;
            case SAR:
                messageSplitter = SarMessageSplitter.INSTANCE;
                break;
        }
    }

    @Override
    public void setDefaultSMSCDataCoding(MessageCoding cd) {
        // TODO implement
    }

    public TON getSourceAddrTon() {
        return sourceAddrTon;
    }

    public void setSourceAddrTon(TON sourceAddrTon) {
        this.sourceAddrTon = sourceAddrTon;
    }

    public NPI getSourceAddrNpi() {
        return sourceAddrNpi;
    }

    public void setSourceAddrNpi(NPI sourceAddrNpi) {
        this.sourceAddrNpi = sourceAddrNpi;
    }

    public TON getDestAddrTon() {
        return destAddrTon;
    }

    public void setDestAddrTon(TON destAddrTon) {
        this.destAddrTon = destAddrTon;
    }

    public NPI getDestAddrNpi() {
        return destAddrNpi;
    }

    public void setDestAddrNpi(NPI destAddrNpi) {
        this.destAddrNpi = destAddrNpi;
    }

    public boolean isRegisteredDelivery() {
        return registeredDelivery;
    }

}
