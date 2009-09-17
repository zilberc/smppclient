package org.bulatnig.smpp.pdu;

import org.bulatnig.smpp.SMPPObject;
import org.bulatnig.smpp.util.SMPPByteBuffer;
import org.bulatnig.smpp.util.WrongLengthException;
import org.bulatnig.smpp.util.WrongParameterException;

/**
 * Unsuccessful delivery.
 *
 * @author Bulat Nigmatullin
 * @see SubmitMulti
 */
public class UnsuccessSme extends SMPPObject {

    /**
     * Максимальная длина messageId поля.
     */
    private static final int MAX_DESTADDR_LENGTH = 16;

    /**
     * Type of number for destination SME.
     */
    private TON destAddrTon;
    /**
     * Numbering Plan Indicator for destination SME.
     */
    private NPI destAddrNpi;
    /**
     * Destination Address of destination SME.
     */
    private String destinationAddr;
    /**
     * Indicates the success or failure of the submit_multi request to this SME
     * address.
     */
    private long errorStatusCode;

    /**
     * Constructor.
     */
    public UnsuccessSme() {
    }

    /**
     * Constructor.
     *
     * @param bytes байткод PDU
     * @throws PDUException ошибка обработки PDU
     */
    public UnsuccessSme(final byte[] bytes) throws PDUException {
        SMPPByteBuffer bb = new SMPPByteBuffer(bytes);
        try {
            short b = bb.removeByte();
            for (TON ton : TON.values()) {
                if (ton.getValue() == b) {
                    destAddrTon = ton;
                }
            }
            if (destAddrTon == null) {
                destAddrTon = TON.RESERVED;
            }
            b = bb.removeByte();
            for (NPI npi : NPI.values()) {
                if (npi.getValue() == b) {
                    destAddrNpi = npi;
                }
            }
            if (destAddrNpi == null) {
                destAddrNpi = NPI.RESERVED;
            }
            destinationAddr = bb.removeCString();
            if (destinationAddr.length() > MAX_DESTADDR_LENGTH) {
                throw new PDUException("destinationAddr field is too long");
            }
            errorStatusCode = bb.removeInt();
        } catch (WrongLengthException e) {
            throw new PDUException("PDU parsing error", e);
        }
    }

    /**
     * @return bytes representing this object
     * @throws PDUException ошибка обработки PDU
     */
    protected final byte[] getBytes() throws PDUException {
        SMPPByteBuffer bb = new SMPPByteBuffer();
        try {
            bb.appendByte(destAddrTon != null ? destAddrTon.getValue() : TON.UNKNOWN.getValue());
        } catch (WrongParameterException e) {
            throw new PDUException("destAddrTon field is invalid", e);
        }
        try {
            bb.appendByte(destAddrNpi != null ? destAddrNpi.getValue() : NPI.UNKNOWN.getValue());
        } catch (WrongParameterException e) {
            throw new PDUException("destAddrNpi field is invalid", e);
        }
        if (destinationAddr != null && destinationAddr.length() > MAX_DESTADDR_LENGTH) {
            throw new PDUException("destinationAddr field is invalid");
        }
        bb.appendCString(destinationAddr);
        try {
            bb.appendInt(errorStatusCode);
        } catch (
                WrongParameterException e) {
            throw new PDUException("errorStatusCode field is invalid", e);
        }
        return bb.getBuffer();
    }

    /**
     * @return type of number for destination SME
     */
    public final TON getDestAddrTon() {
        return destAddrTon;
    }

    /**
     * @param destAddrTon type of number for destination SME
     */
    public final void setDestAddrTon(final TON destAddrTon) {
        this.destAddrTon = destAddrTon;
    }

    /**
     * @return Numbering Plan Indicator for destination SME
     */
    public final NPI getDestAddrNpi() {
        return destAddrNpi;
    }

    /**
     * @param destAddrNpi Numbering Plan Indicator for destination SME
     */
    public final void setDestAddrNpi(final NPI destAddrNpi) {
        this.destAddrNpi = destAddrNpi;
    }

    /**
     * @return Destination Address of destination SME
     */
    public final String getDestinationAddr() {
        return destinationAddr;
    }

    /**
     * @param destinationAddr Destination Address of destination SME
     */
    public final void setDestinationAddr(final String destinationAddr) {
        this.destinationAddr = destinationAddr;
    }

    /**
     * @return the success or failure of the submit_multi request indicator
     */
    public final long getErrorStatusCode() {
        return errorStatusCode;
    }

    /**
     * @param errorStatusCode the success or failure of the submit_multi request indicator
     */
    public final void setErrorStatusCode(final long errorStatusCode) {
        this.errorStatusCode = errorStatusCode;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String toString() {
        return getClass().getName() + " Object {" + "\ndestAddrTon : "
                + destAddrTon + "\ndestAddrNpi : " + destAddrNpi
                + "\ndestinationAddr : " + destinationAddr
                + "\nerrorStatusCode : " + errorStatusCode + "\n}";
    }

}
