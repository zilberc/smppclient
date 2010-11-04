package org.bulatnig.smpp.pdu;

import org.bulatnig.smpp.util.SmppByteBuffer;
import org.bulatnig.smpp.util.WrongLengthException;
import org.bulatnig.smpp.util.WrongParameterException;

/**
 * SME Address.
 *
 * @author Bulat Nigmatullin
 * @see DestAddress
 */
public class SMEAddress {

    /**
     * Максимальная длина messageId поля.
     */
    private static final int MAX_DESTADDR_LENGTH = 20;

    /**
     * Type of Number for destination SME.
     */
    private TON destAddrTon;
    /**
     * Numbering Plan Indicator for destination SME.
     */
    private NPI destAddrNpi;
    /**
     * Destination Address for this short message.
     */
    private String destinationAddr;

    /**
     * Constructor.
     */
    public SMEAddress() {
    }

    /**
     * Constructor.
     *
     * @param bytes байткод PDU
     * @throws PDUException ошибка обработки PDU
     */
    public SMEAddress(final byte[] bytes) throws PDUException {
        SmppByteBuffer bb = new SmppByteBuffer(bytes);
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
        } catch (WrongLengthException e) {
            throw new PDUException("PDU parsing error", e);
        }
    }

    /**
     * @return bytes representing this object
     * @throws PDUException ошибка обработки PDU
     */
    protected final byte[] getBytes() throws PDUException {
        SmppByteBuffer bb = new SmppByteBuffer();
        try {
            bb.appendByte(destAddrTon != null ? destAddrTon.getValue() : TON.UNKNOWN.getValue());
        } catch (
                WrongParameterException e) {
            throw new PDUException("sourceAddrTon field is invalid", e);
        }
        try {
            bb.appendByte(destAddrNpi != null ? destAddrNpi.getValue() : NPI.UNKNOWN.getValue());
        } catch (WrongParameterException e) {
            throw new PDUException("sourceAddrTon field is invalid", e);
        }
        if (destinationAddr != null && destinationAddr.length() > MAX_DESTADDR_LENGTH) {
            throw new PDUException("destinationAddr field is invalid");
        }
        bb.appendCString(destinationAddr);
        return bb.array();
    }

    /**
     * @return Type of Number for destination SME
     */
    public final TON getDestAddrTon() {
        return destAddrTon;
    }

    /**
     * @param destAddrTon Type of Number for destination SME
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
     * @return Destination Address
     */
    public final String getDestinationAddr() {
        return destinationAddr;
    }

    /**
     * @param destinationAddr Destination Address
     */
    public final void setDestinationAddr(final String destinationAddr) {
        this.destinationAddr = destinationAddr;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String toString() {
        return getClass().getName() + " Object {" + "\ndestAddrTon : "
                + destAddrTon + "\ndestAddrNpi : " + destAddrNpi
                + "\ndestinationAddr : " + destinationAddr + "\n}";
    }

}
