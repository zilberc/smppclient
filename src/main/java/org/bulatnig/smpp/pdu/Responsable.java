package org.bulatnig.smpp.pdu;

/**
 * Следует отправить ответное PDU.
 * 
 * @author Bulat Nigmatullin
 * 
 */
public interface Responsable {

	/**
	 * Создает ответное PDU.
	 * 
	 * @return ответное PDU
	 */
	PDU getResponse();

}
