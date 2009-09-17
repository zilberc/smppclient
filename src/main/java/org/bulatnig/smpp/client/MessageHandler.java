package org.bulatnig.smpp.client;

import org.bulatnig.smpp.pdu.*;
import org.bulatnig.smpp.session.PDUHandler;

import java.util.*;

/**
 * TODO refactoring and bug fix
 *
Exception in thread "Timer-10" java.util.ConcurrentModificationException
        at java.util.HashMap$HashIterator.nextEntry(HashMap.java:793)
        at java.util.HashMap$KeyIterator.next(HashMap.java:828)
        at ru.rambler.mobile.smpp.client.MessageHandler$SegmentsMap.run(MessageHandler.java:222)
        at java.util.TimerThread.mainLoop(Timer.java:512)
        at java.util.TimerThread.run(Timer.java:462)

 * Упрощенные методы по приему сообщений.
 * <p/>
 * User: Bulat Nigmatullin
 * Date: 14.07.2008
 * Time: 15:47:05
 */
public abstract class MessageHandler implements PDUHandler {

    /**
     * Содержит части большого PDU для сборки.
     */
    private SegmentsMap segmentsMap = new SegmentsMap();

    private Timer timer;

    public void start() {
        timer = new Timer();
        timer.schedule(segmentsMap, 0, 1000);
    }

    public void stop() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public final PDU received(PDU pdu) {
        PDU response;
        switch (pdu.getCommandId()) {
            case DATA_SM:
                DataSM data = (DataSM) pdu;
                response = data.getResponse();
                if (data.getSourceAddr().length() == 0) {
                    response.setCommandStatus(CommandStatus.ESME_RINVSRCADR);
                    break;
                }
                if (data.getDestinationAddr().length() == 0) {
                    response.setCommandStatus(CommandStatus.ESME_RINVDSTADR);
                    break;
                }
                try {
                    SmscEsmClass esmClass = (SmscEsmClass) data.getEsmClass();
                    switch (esmClass.getType()) {
                        case DEFAULT:
                            String text = data.getMessagePayload() != null ? data.getMessagePayload().getValue() : "";
                            if (data.getSarMsgRefNum() != null &&
                                    data.getSarSegmentSeqnum() != null &&
                                    data.getSarTotalSegments() != null &&
                                    data.getSarTotalSegments().getValue() > 1) {
                                process(data.getSourceAddr(), data.getDestinationAddr(), text,
                                        data.getSarMsgRefNum().getValue(),
                                        data.getSarSegmentSeqnum().getValue(),
                                        data.getSarTotalSegments().getValue());
                            } else {
                                handle(new Message(data.getSourceAddr(), data.getDestinationAddr(), text));
                            }
                            break;
                        case SMSC_DELIVERY_RECEIPT:
                            if (data.getReceiptedMessageId() != null) {
                                deliveredToDest(data.getReceiptedMessageId().getValue());
                            }
                    }
                } catch (ProcessingFailedException e) {
                    response.setCommandStatus(CommandStatus.ESME_RSYSERR);
                }
                break;
            case DELIVER_SM:
                DeliverSM deliver = (DeliverSM) pdu;
                response = deliver.getResponse();
                if (deliver.getSourceAddr().length() == 0) {
                    response.setCommandStatus(CommandStatus.ESME_RINVSRCADR);
                    break;
                }
                if (deliver.getDestinationAddr().length() == 0) {
                    response.setCommandStatus(CommandStatus.ESME_RINVDSTADR);
                    break;
                }
                try {
                    SmscEsmClass esm = deliver.getEsmClass();
                    switch (esm.getType()) {
                        case DEFAULT:
                            String text;
                            if (deliver.getSmLength() > 0) {
                                text = deliver.getShortMessage();
                            } else if (deliver.getMessagePayload() != null) {
                                text = deliver.getMessagePayload().getValue();
                            } else {
                                text = "";
                            }
                            if (deliver.getSarMsgRefNum() != null &&
                                    deliver.getSarSegmentSeqnum() != null &&
                                    deliver.getSarTotalSegments() != null &&
                                    deliver.getSarTotalSegments().getValue() > 1) {
                                process(deliver.getSourceAddr(), deliver.getDestinationAddr(), text,
                                        deliver.getSarMsgRefNum().getValue(),
                                        deliver.getSarSegmentSeqnum().getValue(),
                                        deliver.getSarTotalSegments().getValue());
                            } else {
                                handle(new Message(deliver.getSourceAddr(), deliver.getDestinationAddr(), text));
                            }
                            break;
                        case SMSC_DELIVERY_RECEIPT:
                            if (deliver.getReceiptedMessageId() != null) {
                                deliveredToDest(deliver.getReceiptedMessageId().getValue());
                            }
                    }
                } catch (ProcessingFailedException e) {
                    response.setCommandStatus(CommandStatus.ESME_RSYSERR);
                }
                break;
            default:
                response = null;
        }
        return response;
    }

    private void process(String source, String dest, String text, int refNum, short segNum, short totalSegments) throws ProcessingFailedException {
        SortedSet<PartialMessage> pmsgs = segmentsMap.get(refNum);
        if (pmsgs == null) {
            if (segNum == 1) {
                pmsgs = Collections.synchronizedSortedSet(new TreeSet<PartialMessage>());
                pmsgs.add(new PartialMessage(source, dest, text, segNum));
                segmentsMap.put(refNum, pmsgs);
            } else {
                // 1-я часть сообщения не была получена, значит сообщение не удастся собрать!
                throw new ProcessingFailedException();
            }
        } else {
            PartialMessage pmsg = new PartialMessage(source, dest, text, segNum);
            pmsgs.add(pmsg);
            if (segNum == totalSegments) {
                Message message = assembleMessage(pmsgs);
                segmentsMap.remove(refNum);
                if (message != null) {
                    handle(message);
                } else {
                    throw new ProcessingFailedException();
                }
            }
        }
    }

    private Message assembleMessage(Collection<PartialMessage> pmsgs) {
        Message result = null;
        String sourceAddr = null;
        String destAddr = null;
        String text = null;
        Iterator<PartialMessage> it = pmsgs.iterator();
        boolean firstIteration = true;
        while (it.hasNext()) {
            PartialMessage pm = it.next();
            if (firstIteration) {
                sourceAddr = pm.getSourceAddr();
                destAddr = pm.getDestAddr();
                text = pm.getText();
                firstIteration = false;
            } else {
                text += pm.getText();
            }
        }
        if (sourceAddr != null && destAddr != null && text != null) {
            result = new Message(sourceAddr, destAddr, text);
        }
        return result;
    }

    /**
     * Входящее сообщение.
     *
     * @param message сообщение
     * @throws ProcessingFailedException ошибка обработки
     */
    public abstract void handle(Message message) throws ProcessingFailedException;

    /**
     * Сообщение доставлено абоненту.
     *
     * @param messageId SMSC ID сообщения
     * @throws ProcessingFailedException ошибка обработки
     */
    public abstract void deliveredToDest(String messageId) throws ProcessingFailedException;

    /**
     * Управляет сборкоя большого сообщения доставляемого частями.
     */
    private class SegmentsMap extends TimerTask {

        /**
         * Время в течении которого должны придти все части большого сообщения.
         */
        private final static int COLLECT_TIME = 60000;

        /**
         * Соответствие Sar_msg_ref_num коллекции частей сообщения.
         */
        private Map<Integer, SortedSet<PartialMessage>> messages =
                new HashMap<Integer, SortedSet<PartialMessage>>();

        /**
         * Время когда была доставлена первая часть сообщения.
         */
        private Map<Integer, Long> times = new HashMap<Integer, Long>();

        /**
         * {@inheritDoc}
         */
        @Override
        public void run() {
            if (times.size() > 0) {
                long currentTime = System.currentTimeMillis();
                for (Integer key : times.keySet()) {
                    if (currentTime - times.get(key) > COLLECT_TIME) {
                        messages.remove(key);
                        times.remove(key);
                    }
                }
            }
        }

        /**
         * Вставить часть сообщения.
         *
         * @param refNum sar_msg_ref_num сообщения
         * @param parts  часть сообщения
         */
        public void put(Integer refNum, SortedSet<PartialMessage> parts) {
            messages.put(refNum, parts);
            times.put(refNum, System.currentTimeMillis());
        }

        public SortedSet<PartialMessage> get(Integer refNum) {
            return messages.get(refNum);
        }

        /**
         * Удалить части сообщения.
         *
         * @param refNum sar_msg_ref_num сообщений
         */
        public void remove(Integer refNum) {
            messages.remove(refNum);
            times.remove(refNum);
        }

    }


}
