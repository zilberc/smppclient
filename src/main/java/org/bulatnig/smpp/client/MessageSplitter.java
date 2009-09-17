package org.bulatnig.smpp.client;

import org.bulatnig.smpp.pdu.SubmitSM;

import java.util.List;

/**
 * Splits large messages on small chunks.
 * <p/>
 * User: Bulat Nigmatullin
 * Date: Jan 27, 2009
 * Time: 8:21:25 AM
 */
public interface MessageSplitter {

    /**
     * Split large message on small chunks. Sets SubmitSM SourceAddr,
     * DestinationAddr and ShortMessage or MessagePayload values.
     *
     * @param message       send message
     * @param chunkSize     chunk message size
     * @return              message parts in ascending order
     */
    public List<SubmitSM> split(Message message, int chunkSize);

}
