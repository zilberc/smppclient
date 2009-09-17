package org.bulatnig.smpp.pdu.tlv;

/**
 * Значение параметра ItsReplyType.
 * 
 * @author Bulat Nigmatullin
 * @see ItsReplyType
 */
public enum ReplyType {
	/**
	 * Digit.
	 */
	DIGIT((short) 0),
	/**
	 * Number.
	 */
	NUMBER((short) 1),
	/**
	 * Telephone No.
	 */
	TELEPHONE((short) 2),
	/**
	 * Password.
	 */
	PASSWORD((short) 3),
	/**
	 * Character Line.
	 */
	CHARACTER_LINE((short) 4),
	/**
	 * Menu.
	 */
	MENU((short) 5),
	/**
	 * Date.
	 */
	DATE((short) 6),
	/**
	 * Time.
	 */
	TIME((short) 7),
	/**
	 * Continue.
	 */
	CONTINUE((short) 8),
    /**
     * Reserved value. This value sets when unknown parameter value was read.
     */
    RESERVED((short) -1);
	
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
	private ReplyType(final short v) {
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
