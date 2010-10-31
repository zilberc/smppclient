package org.bulatnig.smpp.client;

/**
 * SMS сообщениe.
 *
 * User: Bulat Nigmatullin
 * Date: 18.07.2008
 * Time: 15:49:34
 */
public class Message {

    /**
     * Отправитель.
     */
    private String sourceAddr;
    /**
     * Адресат.
     */
    private String destAddr;
    /**
     * Текст сообщения.
     */
    private String text;

    /**
     * Конструктор.
     *
     * @param sourceAddr    отправитель
     * @param destAddr      адресат
     * @param text          текст сообщения
     */
    public Message(String sourceAddr, String destAddr, String text) {
        this.sourceAddr = sourceAddr;
        this.destAddr = destAddr;
        this.text = text;
    }

    public String getSourceAddr() {
        return sourceAddr;
    }

    public String getDestAddr() {
        return destAddr;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return "Message{" +
                "sourceAddr='" + sourceAddr + '\'' +
                ", destAddr='" + destAddr + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
