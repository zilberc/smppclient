package org.bulatnig.smpp.pdu.tlv;

/**
 * Значение параметра MsValidity.
 * 
 * @author Bulat Nigmatullin
 * @see MsValidity
 */
public enum Validity {
	/**
	 * Store Indefinitely (default).
	 */
	STORE_INDEFINITELY((short) 0),
	/**
	 * Power Down.
	 */
	POWER_DOWN((short) 1),
	/**
	 * SID based registration area.
	 */
	SID_BASED((short) 2),
	/**
	 * Display Only.
	 */
	DISPLAY_ONLY((short) 3),
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
	private Validity(final short v) {
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final String toString() {
		return getClass().getName() + " Object {" + "\nvalue : " + value
				+ "\n}";
	}

}
