package org.bulatnig.smpp.pdu.tlv;

/**
 * Значение DeliveryFailureReason параметра.
 * 
 * @author Bulat Nigmatullin
 * @see DeliveryFailureReason
 */
public enum FailureReason {
	/**
	 * Destination unavailable.
	 */
	DESTINATION_UNAVAILABLE((short) 0),
	/**
	 * Destination Address Invalid (e.g. suspended, no SMS capability, etc.).
	 */
	DESTINATION_ADDRESS_INVALID((short) 1),
	/**
	 * Permanent network error.
	 */
	PERMANENT_NETWORK_ERROR((short) 2),
	/**
	 * Temporary network error.
	 */
	TEMPORARY_NETWORK_ERROR((short) 3),
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
	private FailureReason(final short v) {
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
