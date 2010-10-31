package org.bulatnig.smpp.pdu;

import org.bulatnig.smpp.util.SmppByteBuffer;
import org.bulatnig.smpp.util.WrongLengthException;
import org.bulatnig.smpp.util.WrongParameterException;

/**
 * Contains one or more (number_of_dests) SME addresses or/and Distribution List
 * names.
 *
 * @author Bulat Nigmatullin
 * @see SubmitMulti
 */
public class DestAddress {
    /**
     * Flag which will identify whether destination address is a Distribution
     * List name or SME address.
     */
    private short destFlag;
    /**
     * Depending on dest_flag this could be an SME Address or a Distribution
     * List Name.
     */
    private SMEAddress smeAddress;
    /**
     * Depending on dest_flag this could be an SME Address or a Distribution
     * List Name.
     */
    private DLN dlnAddress;

    /**
     * Constructor.
     */
    public DestAddress() {
    }

    /**
     * Constructor.
     *
     * @param bytes байткод PDU
     * @throws PDUException ошибка обработки PDU
     */
    public DestAddress(final byte[] bytes) throws PDUException {
        SmppByteBuffer bb = new SmppByteBuffer(bytes);
        try {
            destFlag = bb.removeByte();
            if (destFlag == 1) {
                smeAddress = new SMEAddress(bb.removeBytes(bb.length()).getBuffer());
            } else {
                dlnAddress = new DLN(bb.removeBytes(bb.length()).getBuffer());
            }
        } catch (WrongLengthException e) {
            throw new PDUException("Wrong parameters supplied", e);
        } catch (WrongParameterException e) {
            throw new PDUException("FATAL ERROR wrong SmppByteBuffer work", e);
        }
    }

    /**
     * @return bytes representing this parameter
     * @throws PDUException ошибка обработки PDU
     */
    protected final byte[] getBytes() throws PDUException {
        SmppByteBuffer bb = new SmppByteBuffer();
        try {
            bb.appendByte(destFlag);
        } catch (WrongParameterException e) {
            throw new PDUException("destFlag field is invalid", e);
        }
        if (destFlag == 1) {
            bb.appendBytes(smeAddress.getBytes(), smeAddress.getBytes().length);
        } else {
            bb.appendBytes(dlnAddress.getBytes(), dlnAddress.getBytes().length);
        }
        return bb.getBuffer();
    }

    /**
     * @return identifier whether destination address is a Distribution List
     *         name or SME address
     */
    public final short getDestFlag() {
        return destFlag;
    }

    /**
     * @param destFlag identifier whether destination address is a Distribution List
     *                 name or SME address
     */
    public final void setDestFlag(final short destFlag) {
        this.destFlag = destFlag;
    }

    /**
     * @return SME Address
     */
    public final SMEAddress getSmeAddress() {
        return smeAddress;
    }

    /**
     * @param smeAddress SME Address
     */
    public final void setSmeAddress(final SMEAddress smeAddress) {
        this.smeAddress = smeAddress;
    }

    /**
     * @return Distribution List Name
     */
    public final DLN getDlnAddress() {
        return dlnAddress;
    }

    /**
     * @param dlnAddress Distribution List Name
     */
    public final void setDlnAddress(final DLN dlnAddress) {
        this.dlnAddress = dlnAddress;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String toString() {
        return getClass().getName() + " Object {" + "\ndestFlag : " + destFlag
                + "\nsmeAddress : " + smeAddress + "\ndlnAddress : "
                + dlnAddress + "\n}";
    }

}
