package org.bulatnig.smpp.pdu;

/**
 * Возвращает CommandId или CommandStatus соответсвующий их числовому представлени.
 * Для избежания итераций по возможным значениям используется Map.
 *
 * User: Bulat Nigmatullin
 * Date: 28.10.2008
 * Time: 14:41:56
 */
public interface PDUHelper {

    /**
     * Возвращает идентификатор команды соответствующий его числовому выражению.
     *
     * @param commandIdValue            числовое выражение идентификатор команды
     * @return                          идентификатор команды
     * @throws CommandIdNotFoundException   соответствующий идентификатор команды не найден
     */
    public CommandId getCommandId(long commandIdValue) throws CommandIdNotFoundException;

    /**
     * Возвращает статус команды соответствующий его числовому выражению.
     *
     * @param commandStatusValue        числовое выражение статуса команды
     * @return                          статус команды
     * @throws CommandStatusNotFoundException  соответствующий статус команды не найден
     */
    public CommandStatus getCommandStatus(long commandStatusValue) throws CommandStatusNotFoundException;

}
