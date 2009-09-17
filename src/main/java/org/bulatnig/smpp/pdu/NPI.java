package org.bulatnig.smpp.pdu;

/**
 * Numbric Plan Indicator (NPI).
 * <p/>
 * User: Bulat Nigmatullin
 * Date: Dec 1, 2008
 * Time: 7:51:16 AM
 */
public enum NPI {
    /**
     * Unknown.
     */
    UNKNOWN((short) Integer.valueOf("00000000", 2).intValue()),
    /**
     * ISDN (E163/E164).
     */
    ISDN((short) Integer.valueOf("00000001", 2).intValue()),
    /**
     * Data (X.121).
     */
    DATA((short) Integer.valueOf("00000011", 2).intValue()),
    /**
     * Telex (F.69).
     */
    TELEX((short) Integer.valueOf("00000100", 2).intValue()),
    /**
     * Land Mobile (E.212).
     */
    LAND_MOBILE((short) Integer.valueOf("00000110", 2).intValue()),
    /**
     * National.
     */
    NATIONAL((short) Integer.valueOf("00001000", 2).intValue()),
    /**
     * Private.
     */
    PRIVATE((short) Integer.valueOf("00001001", 2).intValue()),
    /**
     * ERMES
     */
    ERMES((short) Integer.valueOf("00001010", 2).intValue()),
    /**
     * Internet (IP).
     */
    INTERNET((short) Integer.valueOf("00001110", 2).intValue()),
    /**
     * WAP Client Id (to be defined by WAP Forum). 
     */
    WAP_CLIENT_ID((short) Integer.valueOf("00010010", 2).intValue()),
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
	private NPI(final short v) {
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
