package org.bulatnig.smpp.client;

/**
 * Часть большого сообщения.
 * <p/>
 * User: Bulat Nigmatullin
 * Date: 25.07.2008
 * Time: 20:41:29
 */
public class PartialMessage extends Message implements Comparable {

    /**
     * Номер части сообщения.
     */
    private int seqnum;

    /**
     * Constructor.
     *
     * @param sourceAddr отправитель
     * @param destAddr   адресат
     * @param text       текст
     * @param seqnum     номер части сообщения
     */
    public PartialMessage(String sourceAddr, String destAddr, String text, int seqnum) {
        super(sourceAddr, destAddr, text);
        this.seqnum = seqnum;
    }

    /**
     * Последовательный номер части сообщения в большом сообщении.
     *
     * @return номер части сообщения
     */
    public int getSeqnum() {
        return seqnum;
    }

    /**
     * {@inheritDoc}
     */
    public int compareTo(Object o) {
        PartialMessage pmsg = (PartialMessage) o;
        return new Integer(seqnum).compareTo(pmsg.getSeqnum());
    }

}
