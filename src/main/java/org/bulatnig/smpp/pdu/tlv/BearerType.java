package org.bulatnig.smpp.pdu.tlv;

/**
 * Значение BearerType параметра.
 * 
 * @author Bulat Nigmatullin
 * @see DestBearerType
 * @see SourceBearerType
 */
public enum BearerType {
	/**
	 * Unknown.
	 */
	UNKNOWN((short) 0x00),
	/**
	 * SMS.
	 */
	SMS((short) 0x01),
	/**
	 * Circuit Switched Data (CSD).
	 */
	CSD((short) 0x02),
	/**
	 * Packet Data.
	 */
	PACKET_DATA((short) 0x03),
	/**
	 * USSD.
	 */
	USSD((short) 0x04),
	/**
	 * CDPD.
	 */
	CDPD((short) 0x05),
	/**
	 * DataTAC.
	 */
	DATATAC((short) 0x06),
	/**
	 * FLEX/ReFLEX.
	 */
	FLEX((short) 0x07),
	/**
	 * Cell Broadcast (cellcast).
	 */
	CELL_BROADCAST((short) 0x08),
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
	private BearerType(final short v) {
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
