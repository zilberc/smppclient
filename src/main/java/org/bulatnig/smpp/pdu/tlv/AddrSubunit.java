package org.bulatnig.smpp.pdu.tlv;

/**
 * Значение AddrSubunit параметра.
 * 
 * @author Bulat Nigmatullin
 * @see DestAddrSubunit
 * @see SourceAddrSubunit
 */
public enum AddrSubunit {
	/**
	 * Unknown (default).
	 */
	UNKNOWN((short) 0x00),
	/**
	 * MS Display.
	 */
	MS_DISPLAY((short) 0x01),
	/**
	 * Mobile Equipment.
	 */
	MOBILE_EQUIPMENT((short) 0x02),
	/**
	 * Smart Card 1 (expected to be SIM if a SIM exists in the MS).
	 */
	SMART_CARD_1((short) 0x03),
	/**
	 * External Unit 1.
	 */
	EXTERNAL_UNIT_1((short) 0x04),
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
	private AddrSubunit(final short v) {
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
