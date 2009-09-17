package org.bulatnig.smpp.pdu;

/**
 * The command_status field indicates the success or failure of an SMPP request.
 * It is relevant only in the SMPP response PDU and it must contain a NULL value
 * in an SMPP request PDU.
 * 
 * @author Bulat Nigmatullin
 * 
 */
public enum CommandStatus {
	/**
	 * No Error.
	 */
	ESME_ROK(0x00000000L, "No Error"),
	/**
	 * Message Length is invalid.
	 */
	ESME_RINVMSGLEN(0x00000001L, "Message Length is invalid"),
	/**
	 * Command Length is invalid.
	 */
	ESME_RINVCMDLEN(0x00000002L, "Command Length is invalid"),
	/**
	 * Invalid Command ID.
	 */
	ESME_RINVCMDID(0x00000003L, "Invalid Command ID"),
	/**
	 * Incorrect BIND Status for given command.
	 */
	ESME_RINVBNDSTS(0x00000004L, "Incorrect BIND Status for given command"),
	/**
	 * ESME ALready in Bound State.
	 */
	ESME_RALYBND(0x00000005L, "ESME ALready in Bound State"),
	/**
	 * Invalid Priority Flag.
	 */
	ESME_RINVPRTFLG(0x00000006L, "Invalid Priority Flag"),
	/**
	 * Invalid Registered Delivery Flag.
	 */
	ESME_RINVREGDLVFLG(0x00000007L, "Invalid Registered Delivery Flag"),
	/**
	 * System Error.
	 */
	ESME_RSYSERR(0x00000008L, "System Error"),
	/**
	 * Invalid Source Address.
	 */
	ESME_RINVSRCADR(0x0000000AL, "Invalid Source Address"),
	/**
	 * Invalid Dest Addr.
	 */
	ESME_RINVDSTADR(0x0000000BL, "Invalid Dest Addr"),
	/**
	 * Message ID is invalid.
	 */
	ESME_RINVMSGID(0x0000000CL, "Message ID is invalid"),
	/**
	 * Bind Failed.
	 */
	ESME_RBINDFAIL(0x0000000DL, "Bind Failed"),
	/**
	 * Invalid Password.
	 */
	ESME_RINVPASWD(0x0000000EL, "Invalid Password"),
	/**
	 * Invalid System ID.
	 */
	ESME_RINVSYSID(0x0000000FL, "Invalid System ID"),
	/**
	 * Cancel SM Failed.
	 */
	ESME_RCANCELFAIL(0x00000011L, "Cancel SM Failed"),
	/**
	 * Replace SM Failed.
	 */
	ESME_RREPLACEFAIL(0x00000013L, "Replace SM Failed"),
	/**
	 * Message Queue Full.
	 */
	ESME_RMSGQFUL(0x00000014L, "Message Queue Full"),
	/**
	 * Invalid Service Type.
	 */
	ESME_RINVSERTYP(0x00000015L, "Invalid Service Type"),
	/**
	 * Invalid number of destinations.
	 */
	ESME_RINVNUMDESTS(0x00000033L, "Invalid number of destinations"),
	/**
	 * Invalid Distribution List name.
	 */
	ESME_RINVDLNAME(0x00000034L, "Invalid Distribution List name"),
	/**
	 * Destination flag is invalid (submit_multi).
	 */
	ESME_RINVDESTFLAG(0x00000040L, "Destination flag is invalid (submit_multi)"),
	/**
	 * Invalid ‘submit with replace’ request (i.e. submit_sm with
	 * replace_if_present_flag set).
	 */
	ESME_RINVSUBREP(
			0x00000042L,
			"Invalid ‘submit with replace’ request (i.e. submit_sm with replace_if_present_flag set)"),
	/**
	 * Invalid esm_class field data.
	 */
	ESME_RINVESMCLASS(0x00000043L, "Invalid esm_class field data"),
	/**
	 * Cannot Submit to Distribution List.
	 */
	ESME_RCNTSUBDL(0x00000044L, "Cannot Submit to Distribution List"),
	/**
	 * submit_sm or submit_multi failed.
	 */
	ESME_RSUBMITFAIL(0x00000045L, "submit_sm or submit_multi failed"),
	/**
	 * Invalid Source address TON.
	 */
	ESME_RINVSRCTON(0x00000048L, "Invalid Source address TON"),
	/**
	 * Invalid Source address NPI.
	 */
	ESME_RINVSRCNPI(0x00000049L, "Invalid Source address NPI"),
	/**
	 * Invalid Destination address TON.
	 */
	ESME_RINVDSTTON(0x00000050L, "Invalid Destination address TON"),
	/**
	 * Invalid Destination address NPI.
	 */
	ESME_RINVDSTNPI(0x00000051L, "Invalid Destination address NPI"),
	/**
	 * Invalid system_type field.
	 */
	ESME_RINVSYSTYP(0x00000053L, "Invalid system_type field"),
	/**
	 * Invalid replace_if_present flag.
	 */
	ESME_RINVREPFLAG(0x00000054L, "Invalid replace_if_present flag"),
	/**
	 * Invalid number of messages.
	 */
	ESME_RINVNUMMSGS(0x00000055L, "Invalid number of messages"),
	/**
	 * Throttling error (ESME has exceeded allowed message limits).
	 */
	ESME_RTHROTTLED(0x00000058L,
			"Throttling error (ESME has exceeded allowed message limits)"),
	/**
	 * Invalid Scheduled Delivery Time.
	 */
	ESME_RINVSCHED(0x00000061L, "Invalid Scheduled Delivery Time"),
	/**
	 * Invalid message validity period (Expiry time).
	 */
	ESME_RINVEXPIRY(0x00000062L,
			"Invalid message validity period (Expiry time)"),
	/**
	 * Predefined Message Invalid or Not Found.
	 */
	ESME_RINVDFTMSGID(0x00000063L, "Predefined Message Invalid or Not Found"),
	/**
	 * ESME Receiver Temporary App Error Code.
	 */
	ESME_RX_T_APPN(0x00000064L, "ESME Receiver Temporary App Error Code"),
	/**
	 * ESME Receiver Permanent App Error Code.
	 */
	ESME_RX_P_APPN(0x00000065L, "ESME Receiver Permanent App Error Code"),
	/**
	 * ESME Receiver Reject Message Error Code.
	 */
	ESME_RX_R_APPN(0x00000066L, "ESME Receiver Reject Message Error Code"),
	/**
	 * query_sm request failed.
	 */
	ESME_RQUERYFAIL(0x00000067L, "query_sm request failed"),
	/**
	 * Error in the optional part of the PDU Body.
	 */
	ESME_RINVOPTPARSTREAM(0x000000C0L,
			"Error in the optional part of the PDU Body"),
	/**
	 * Optional Parameter not allowed.
	 */
	ESME_ROPTPARNOTALLWD(0x000000C1L, "Optional Parameter not allowed"),
	/**
	 * Invalid Parameter Length.
	 */
	ESME_RINVPARLEN(0x000000C2L, "Invalid Parameter Length"),
	/**
	 * Expected Optional Parameter missing.
	 */
	ESME_RMISSINGOPTPARAM(0x000000C3L, "Expected Optional Parameter missing"),
	/**
	 * Invalid Optional Parameter Value.
	 */
	ESME_RINVOPTPARAMVAL(0x000000C4L, "Invalid Optional Parameter Value"),
	/**
	 * Delivery Failure (used for data_sm_resp).
	 */
	ESME_RDELIVERYFAILURE(0x000000FEL,
			"Delivery Failure (used for data_sm_resp)"),
	/**
	 * Unknown Error.
	 */
	ESME_RUNKNOWNERR(0x000000FFL, "Unknown Error"),

    /**
     * Reserved value. This value sets when unknown parameter value was read.
     */
    RESERVED(-1, "Reserved");

	/**
	 * Численное выражение статуса команды.
	 */
	private long value;

	/**
	 * Описание статуса.
	 */
	private String description;

	/**
	 * Конструктор.
	 * 
	 * @param longVal
	 *            численное выражение статуса команды
	 * @param descVal
	 *            описание статуса команды
	 */
	private CommandStatus(final long longVal, final String descVal) {
		value = longVal;
		description = descVal;
	}

	/**
	 * Возвращает численное выражение статуса команды.
	 * 
	 * @return число
	 */
	public final long getValue() {
		return value;
	}

	/**
	 * Возвращает описание статуса команды.
	 * 
	 * @return описание
	 */
	public final String getDescription() {
		return description;
	}

}
