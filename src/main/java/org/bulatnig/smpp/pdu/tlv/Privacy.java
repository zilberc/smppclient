package org.bulatnig.smpp.pdu.tlv;

/**
 * Значение PrivacyIndicator параметра.
 * 
 * @author Bulat Nigmatullin
 * @see PrivacyIndicator
 */
public enum Privacy {
	/**
	 * Privacy Level 0 (Not Restricted) (default).
	 */
	NOT_RESTRICTED((short) 0),
	/**
	 * Privacy Level 1 (Restricted).
	 */
	RESTRICTED((short) 1),
	/**
	 * Privacy Level 2 (Confidential).
	 */
	CONFIDENTIAL((short) 2),
	/**
	 * Privacy Level 3 (Secret).
	 */
	SECRET((short) 3),
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
	private Privacy(final short v) {
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
