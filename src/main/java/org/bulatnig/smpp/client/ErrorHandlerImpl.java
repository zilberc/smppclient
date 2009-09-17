package org.bulatnig.smpp.client;

import org.bulatnig.smpp.client.PDUError;
import org.bulatnig.smpp.pdu.CommandStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * Обработчик ошибок возвращаемых SMSC.
 *
 * User: Bulat Nigmatullin
 * Date: 06.07.2008
 * Time: 19:39:36
 */
public enum ErrorHandlerImpl implements ErrorHandler {
    INSTANCE;

    private Map<CommandStatus, PDUError> errors =
            new HashMap<CommandStatus, PDUError>();

    private ErrorHandlerImpl() {
        errors.put(CommandStatus.ESME_RINVMSGLEN, new PDUError(
                CommandStatus.ESME_RINVMSGLEN, false, 0));
        errors.put(CommandStatus.ESME_RINVCMDLEN, new PDUError(
                CommandStatus.ESME_RINVCMDLEN, false, 0));
        errors.put(CommandStatus.ESME_RINVCMDID, new PDUError(
                CommandStatus.ESME_RINVCMDID, false, 0));
        errors.put(CommandStatus.ESME_RINVBNDSTS, new PDUError(
                CommandStatus.ESME_RINVBNDSTS, false, 0));
        errors.put(CommandStatus.ESME_RALYBND, new PDUError(
                CommandStatus.ESME_RALYBND, false, 0));
        errors.put(CommandStatus.ESME_RINVPRTFLG, new PDUError(
                CommandStatus.ESME_RINVPRTFLG, false, 0));
        errors.put(CommandStatus.ESME_RINVREGDLVFLG, new PDUError(
                CommandStatus.ESME_RINVREGDLVFLG, false, 0));
        errors.put(CommandStatus.ESME_RSYSERR, new PDUError(
                CommandStatus.ESME_RSYSERR, true, 5000));
        errors.put(CommandStatus.ESME_RINVSRCADR, new PDUError(
                CommandStatus.ESME_RINVSRCADR, false, 0));
        errors.put(CommandStatus.ESME_RINVDSTADR, new PDUError(
                CommandStatus.ESME_RINVDSTADR, false, 0));
        errors.put(CommandStatus.ESME_RINVMSGID, new PDUError(
                CommandStatus.ESME_RINVMSGID, false, 0));
        errors.put(CommandStatus.ESME_RBINDFAIL, new PDUError(
                CommandStatus.ESME_RBINDFAIL, false, 0));
        errors.put(CommandStatus.ESME_RINVPASWD, new PDUError(
                CommandStatus.ESME_RINVPASWD, false, 0));
        errors.put(CommandStatus.ESME_RINVSYSID, new PDUError(
                CommandStatus.ESME_RINVSYSID, false, 0));
        errors.put(CommandStatus.ESME_RCANCELFAIL, new PDUError(
                CommandStatus.ESME_RCANCELFAIL, false, 0));
        errors.put(CommandStatus.ESME_RREPLACEFAIL, new PDUError(
                CommandStatus.ESME_RREPLACEFAIL, false, 0));
        errors.put(CommandStatus.ESME_RMSGQFUL, new PDUError(
                CommandStatus.ESME_RMSGQFUL, true, 0));
        errors.put(CommandStatus.ESME_RINVSERTYP, new PDUError(
                CommandStatus.ESME_RINVSERTYP, false, 0));
        errors.put(CommandStatus.ESME_RINVNUMDESTS, new PDUError(
                CommandStatus.ESME_RINVNUMDESTS, false, 0));
        errors.put(CommandStatus.ESME_RINVDLNAME, new PDUError(
                CommandStatus.ESME_RINVDLNAME, false, 0));
        errors.put(CommandStatus.ESME_RINVDESTFLAG, new PDUError(
                CommandStatus.ESME_RINVDESTFLAG, false, 0));
        errors.put(CommandStatus.ESME_RINVSUBREP, new PDUError(
                CommandStatus.ESME_RINVSUBREP, false, 0));
        errors.put(CommandStatus.ESME_RINVESMCLASS, new PDUError(
                CommandStatus.ESME_RINVESMCLASS, false, 0));
        errors.put(CommandStatus.ESME_RCNTSUBDL, new PDUError(
                CommandStatus.ESME_RCNTSUBDL, false, 0));
        errors.put(CommandStatus.ESME_RSUBMITFAIL, new PDUError(
                CommandStatus.ESME_RSUBMITFAIL, false, 0));
        errors.put(CommandStatus.ESME_RINVSRCTON, new PDUError(
                CommandStatus.ESME_RINVSRCTON, false, 0));
        errors.put(CommandStatus.ESME_RINVSRCNPI, new PDUError(
                CommandStatus.ESME_RINVSRCNPI, false, 0));
        errors.put(CommandStatus.ESME_RINVDSTTON, new PDUError(
                CommandStatus.ESME_RINVDSTTON, false, 0));
        errors.put(CommandStatus.ESME_RINVDSTNPI, new PDUError(
                CommandStatus.ESME_RINVDSTNPI, false, 0));
        errors.put(CommandStatus.ESME_RINVSYSTYP, new PDUError(
                CommandStatus.ESME_RINVSYSTYP, false, 0));
        errors.put(CommandStatus.ESME_RINVREPFLAG, new PDUError(
                CommandStatus.ESME_RINVREPFLAG, false, 0));
        errors.put(CommandStatus.ESME_RINVNUMMSGS, new PDUError(
                CommandStatus.ESME_RINVNUMMSGS, true, 0));
        errors.put(CommandStatus.ESME_RTHROTTLED, new PDUError(
                CommandStatus.ESME_RTHROTTLED, true, 5000));
    }

    public PDUError handle(CommandStatus commandStatus) throws UnknownErrorException {
        PDUError error = errors.get(commandStatus);
        if (error != null) {
            return error;
        } else {
            throw new UnknownErrorException("" + commandStatus);
        }
    }
}
