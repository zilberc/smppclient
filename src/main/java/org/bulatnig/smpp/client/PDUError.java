package org.bulatnig.smpp.client;

import org.bulatnig.smpp.SMPPObject;
import org.bulatnig.smpp.pdu.CommandStatus;

/**
 * Ошибка отправки PDU.
 * 
 * @author Bulat Nigmatullin
 * 
 */
public class PDUError extends SMPPObject {

	/**
	 * Код ошибки.
	 */
	private CommandStatus commandStatus;
	/**
	 * Повторная отправка PDU (true- требуется отправить PDU еще раз, иначе
	 * false).
	 */
	private boolean repeat;
	/**
	 * Таймаут (положительное число - таймаут в миллисекундах, иначе - таймаута
	 * нет). На время указанное в timeout прекращается отправка сообщений:
     * сообщения только принимаются.
	 */
	private int timeout;

	/**
	 * Constructor.
	 *
	 * @param commandStatus			код ошибки
	 * @param repeatPDU		        требуется повторная отправка PDU
	 * @param timeout		        таймаут отправки смс
	 */
	public PDUError(final CommandStatus commandStatus, final boolean repeatPDU,
                    final int timeout) {
		this.commandStatus = commandStatus;
		this.repeat = repeatPDU;
		this.timeout = timeout;
	}

    public CommandStatus getCommandStatus() {
        return commandStatus;
    }

    public boolean isRepeatPDU() {
        return repeat;
    }

    public int getTimeout() {
        return timeout;
    }

}
