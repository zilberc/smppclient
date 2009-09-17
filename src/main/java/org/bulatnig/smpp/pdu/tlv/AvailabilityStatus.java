package org.bulatnig.smpp.pdu.tlv;

/**
 * Значение MsAvailabilityStatus параметра.
 * 
 * @author Bulat Nigmatullin
 * @see MsAvailabilityStatus
 */
public enum AvailabilityStatus {
	/**
	 * Available (Default).
	 */
	AVAILABLE((short) 0),
	/**
	 * Denied (e.g. suspended, no SMS capability, etc.).
	 */
	DENIED((short) 1),
	/**
	 * Unavailable.
	 */
	UNAVAILABLE((short) 2),
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
	private AvailabilityStatus(final short v) {
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
