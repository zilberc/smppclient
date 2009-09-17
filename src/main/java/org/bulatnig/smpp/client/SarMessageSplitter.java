package org.bulatnig.smpp.client;

import org.bulatnig.smpp.pdu.SubmitSM;
import org.bulatnig.smpp.pdu.tlv.SarMsgRefNum;
import org.bulatnig.smpp.pdu.tlv.SarSegmentSeqnum;
import org.bulatnig.smpp.pdu.tlv.SarTotalSegments;

import java.util.ArrayList;
import java.util.List;

/**
 * Comment here.
 * <p/>
 * User: Bulat Nigmatullin
 * Date: 18.05.2009
 * Time: 18:51:02
 */
public enum SarMessageSplitter implements MessageSplitter {
    INSTANCE;

    /**
     * Уникальный в рамках сессии идентификатор большого сообщения.
     */
    private int multipartMsgRefNum = 1;

    @Override
    public List<SubmitSM> split(Message message, int chunkSize) {
        List<SubmitSM> result = new ArrayList<SubmitSM>();
        int parts = message.getText().length() / chunkSize + 1;
        SubmitSM submit;
        int end;
        for (int i = 0; i < parts; i++) {
            submit = new SubmitSM();
            submit.setSourceAddr(message.getSourceAddr());
            submit.setDestinationAddr(message.getDestAddr());
            end = (i + 1) * chunkSize;
            if (end > message.getText().length()) {
                end = message.getText().length();
            }
            submit.setShortMessage(message.getText().substring(i, end));
            submit.setSarMsgRefNum(new SarMsgRefNum(nextSarMsgRefNum()));
            submit.setSarSegmentSeqnum(new SarSegmentSeqnum((short) (i + 1)));
            submit.setSarTotalSegments(new SarTotalSegments((short) parts));
        }
        return result;
    }

    private int nextSarMsgRefNum() {
        if (multipartMsgRefNum == 65535) {
            multipartMsgRefNum = 0;
        }
        return ++multipartMsgRefNum;
    }

}
