package org.bulatnig.smpp.client;

import org.bulatnig.smpp.pdu.SubmitSM;
import org.bulatnig.smpp.pdu.tlv.MessagePayload;

import java.util.ArrayList;
import java.util.List;

/**
 * Comment here.
 * <p/>
 * User: Bulat Nigmatullin
 * Date: Jan 27, 2009
 * Time: 10:44:07 AM
 */
public enum PayloadMessageSplitter implements MessageSplitter {
    INSTANCE;

    @Override
    public List<SubmitSM> split(Message message, int chunkSize) {
        List<SubmitSM> result = new ArrayList<SubmitSM>();
        int parts = message.getText().length() / chunkSize + 1;
        for (int i = 0; i < parts; i++) {
            SubmitSM submit = new SubmitSM();
            submit.setSourceAddr(message.getSourceAddr());
            submit.setDestinationAddr(message.getDestAddr());
            submit.setMessagePayload(new MessagePayload(message.getText()));
        }
        return result;
    }
    
}
