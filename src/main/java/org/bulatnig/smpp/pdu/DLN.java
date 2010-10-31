package org.bulatnig.smpp.pdu;

import org.bulatnig.smpp.util.SmppByteBuffer;
import org.bulatnig.smpp.util.WrongLengthException;

/**
 * Distribution List Name.
 *
 * @author Bulat Nigmatullin
 * @see DestAddress
 */
public class DLN {

    /**
     * Максимальная длина messageId поля.
     */
    private static final int MAX_DLNAME_LENGTH = 20;

    /**
     * Name of Distribution List.
     */
    private String dlName;

    /**
     * Constructor.
     */
    public DLN() {
    }

    /**
     * Constructor.
     *
     * @param bytes байткод PDU
     * @throws PDUException ошибка обработки PDU
     */
    public DLN(final byte[] bytes) throws PDUException {
        SmppByteBuffer bb = new SmppByteBuffer(bytes);
        try {
            dlName = bb.removeCString();
        } catch (WrongLengthException e) {
            throw new PDUException("DLN parsing error", e);
        }
        if (dlName.length() > MAX_DLNAME_LENGTH) {
            throw new PDUException("dlName field is too long");
        }
    }

    /**
     * @return bytes        representing this object
     * @throws PDUException ошибка обработки PDU
     */
    protected final byte[] getBytes() throws PDUException {
        SmppByteBuffer bb = new SmppByteBuffer();
        if (dlName != null && dlName.length() > MAX_DLNAME_LENGTH) {
            throw new PDUException("dlName field is invalid");
        }
        bb.appendCString(dlName);
        return bb.getBuffer();
    }

    /**
     * @return name of Distribution List
     */
    public final String getDlName() {
        return dlName;
    }

    /**
     * @param dlName name of Distribution List
     */
    public final void setDlName(final String dlName) {
        this.dlName = dlName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String toString() {
        return getClass().getName() + " Object {" + "\ndlName : " + dlName
				+ "\n}";
	}

}
