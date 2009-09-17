package org.bulatnig.smpp.pdu.tlv;

/**
 * Значение параметра UssdServiceOp.
 * 
 * @author Bulat Nigmatullin
 * @see UssdServiceOp
 */
public enum ServiceOperation {
	/**
	 * PSSD indication.
	 */
	PSSD_INDICATION((short) 0),
	/**
	 * PSSR indication.
	 */
	PSSR_INDICATION((short) 1),
	/**
	 * USSR request.
	 */
	USSR_REQUEST((short) 2),
	/**
	 * USSN request.
	 */
	USSN_REQUEST((short) 3),
	/**
	 * PSSD response.
	 */
	PSSD_RESPONSE((short) 16),
	/**
	 * PSSR response.
	 */
	PSSR_RESPONSE((short) 17),
	/**
	 * USSR confirm.
	 */
	USSR_CONFIRM((short) 18),
	/**
	 * USSN confirm.
	 */
	USSN_CONFIRM((short) 19),
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
	private ServiceOperation(final short v) {
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
