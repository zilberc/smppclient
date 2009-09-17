package org.bulatnig.smpp.pdu;

/**
 * Type of Number (TON).
 * <p/>
 * User: Bulat Nigmatullin
 * Date: Nov 29, 2008
 * Time: 4:44:58 PM
 */
public enum TON {
    /**
     * Unknown.
     */
    UNKNOWN((short) Integer.valueOf("00000000", 2).intValue()),
    /**
     * International.
     */
    INTERNATIONAL((short) Integer.valueOf("00000001", 2).intValue()),
    /**
     * National.
     */
    NATIONAL((short) Integer.valueOf("00000010", 2).intValue()),
    /**
     * Network Specific.
     */
    NETWORK_SPECIFIC((short) Integer.valueOf("00000011", 2).intValue()),
    /**
     * Subscriber number.
     */
    SUBSCRIBER_NUMBER((short) Integer.valueOf("00000100", 2).intValue()),
    /**
     * Alphanumeric.
     */
    ALPHANUMERIC((short) Integer.valueOf("00000101", 2).intValue()),
    /**
     * Abbreviated.
     */
    ABBREVIATED((short) Integer.valueOf("00000110", 2).intValue()),
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
	private TON(final short v) {
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
