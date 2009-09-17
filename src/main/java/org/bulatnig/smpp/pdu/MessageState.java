package org.bulatnig.smpp.pdu;

/**
 * States for a short message.
 * The message_state value is returned by the SMSC to the ESME as part of the query_sm_resp PDU.
 * <p/>
 * User: Bulat Nigmatullin
 * Date: Nov 27, 2008
 * Time: 11:20:24 PM
 */
public enum MessageState {
    /**
     * The message is in enroute state.
     */
    ENROUTE((short) 1),
    /**
     * Message is delivered to destination.
     */
    DELIVERED((short) 2),
    /**
     * Message validity period has expired.
     */
    EXPIRED((short) 3),
    /**
     * Message has been deleted.
     */
    DELETED((short) 4),
    /**
     * Message is undeliverable.
     */
    UNDELIVERABLE((short) 5),
    /**
     * Message is in accepted state (i.e. has been manually read on behalf of the subscriber by customer service).
     */
    ACCEPTED((short) 6),
    /**
     * Message is in invalid state.
     */
    UNKNOWN((short) 7),
    /**
     * Message is in a rejected state.
     */
    REJECTED((short) 8);

	/**
	 * Код параметра.
	 */
	private short value;

	/**
	 * Constructor.
	 *
	 * @param v
	 *            код параметра
	 */
	private MessageState(final short v) {
		value = v;
	}

	/**
	 * Возвращает код параметра.
	 *
	 * @return код параметра
	 */
	public final short getValue() {
		return value;
	}

}
