package org.bulatnig.smpp.client;

import org.bulatnig.smpp.pdu.CommandStatus;
import org.bulatnig.smpp.client.PDUError;

/**
 * Перехватчик ошибок, возвращенных SMSC.
 *
 * User: Bulat Nigmatullin
 * Date: 06.07.2008
 * Time: 19:33:01
 */
public interface ErrorHandler {

    PDUError handle(CommandStatus commandStatus) throws UnknownErrorException;

}
