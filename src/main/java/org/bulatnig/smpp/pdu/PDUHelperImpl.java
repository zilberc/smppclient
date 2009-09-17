package org.bulatnig.smpp.pdu;

import java.util.HashMap;
import java.util.Map;

/**
 * HashMap реализация PDUHelper'a.
 * <p/>
 * User: Bulat Nigmatullin
 * Date: Nov 1, 2008
 * Time: 9:58:18 AM
 */
public enum PDUHelperImpl implements PDUHelper {
    INSTANCE;

    private final Map<Long, CommandId> commandIds= new HashMap<Long, CommandId>();
    private final Map<Long, CommandStatus> commandStatuses= new HashMap<Long, CommandStatus>();

    {
        for (CommandId commandId : CommandId.values()) {
            commandIds.put(commandId.getValue(), commandId);
        }
        for (CommandStatus commandStatus : CommandStatus.values()) {
            commandStatuses.put(commandStatus.getValue(), commandStatus);
        }
    }

    /**
     * {@inheritDoc}
     */
    public CommandId getCommandId(long commandIdValue) throws CommandIdNotFoundException {
        CommandId commandId = commandIds.get(commandIdValue);
        if (commandId != null)
            return commandId;
        else
            throw new CommandIdNotFoundException("Corresponding command id not found by value " + commandIdValue);
    }

    /**
     * {@inheritDoc}
     */
    public CommandStatus getCommandStatus(long commandStatusValue) throws CommandStatusNotFoundException {
        CommandStatus commandStatus = commandStatuses.get(commandStatusValue);
        if (commandStatus != null)
            return commandStatus;
        else
            throw new CommandStatusNotFoundException("Corresponding command status not found by value " + commandStatusValue);
    }
    
}
