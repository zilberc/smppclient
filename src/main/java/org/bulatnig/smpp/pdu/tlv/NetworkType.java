package org.bulatnig.smpp.pdu.tlv;

/**
 * Значение параметра NetworkType.
 * 
 * @author Bulat Nigmatullin
 * @see DestNetworkType
 * @see SourceNetworkType
 */
public enum NetworkType {
	/**
	 * Unknown.
	 */
	UNKNOWN((short) 0x00),
	/**
	 * GSM.
	 */
	GSM((short) 0x01),
	/**
	 * ANSI-136/TDMA.
	 */
	TDMA((short) 0x02),
	/**
	 * IS-95/CDMA.
	 */
	CDMA((short) 0x03),
	/**
	 * PDC.
	 */
	PDC((short) 0x04),
	/**
	 * PHS.
	 */
	PHS((short) 0x05),
	/**
	 * iDEN.
	 */
	IDEN((short) 0x06),
	/**
	 * AMPS.
	 */
	AMPS((short) 0x07),
	/**
	 * Paging Network.
	 */
	PAGING_NETWORK((short) 0x08),
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
	private NetworkType(final short v) {
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
