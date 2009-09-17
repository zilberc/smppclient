package org.bulatnig.smpp.session;

/**
 * SMSC connection type.
 * 
 * @author Bulat Nigmatullin
 *
 */
public enum ConnectionType {
	/**
	 * Только отправка смс.
	 */
	TRANSMITTER(1),
	/**
	 * Только прием смс.
	 */
	RECEIVER(2),
	/**
	 * Отправка и прием смс.
	 */
	TRANSCEIVER(3);
	
	/**
	 * Код типа соединения.
	 */
	private int value;
	
	/**
	 * Конструктор.
	 * 
	 * @param val	код типа соединения
	 */
	private ConnectionType(final int val) {
		value = val;
	}
	
	/**
	 * Возвращает код типа соединения.
	 * 
	 * @return		код типа соединения
	 */
	public final int getValue() {
		return value;
	}

}
