package org.bulatnig.smpp.pdu;

import org.bulatnig.smpp.pdu.tlv.MsAvailabilityStatus;
import org.bulatnig.smpp.pdu.tlv.ParameterTag;
import org.bulatnig.smpp.pdu.tlv.TLV;
import org.bulatnig.smpp.pdu.tlv.TLVException;
import org.bulatnig.smpp.util.SmppByteBuffer;
import org.bulatnig.smpp.util.WrongLengthException;
import org.bulatnig.smpp.util.WrongParameterException;

import java.util.List;

/**
 * This message is sent by the SMSC to the ESME, when the SMSC has detected that
 * a particular mobile subscriber has become available and a delivery pending
 * flag had been set for that subscriber from a previous data_sm operation.
 *
 * @author Bulat Nigmatullin
 */
public class AlertNotification extends PDU {

    /**
     * Максимальная длина sourceAddr и esmeAddr полей.
     */
    private static final int MAX_ADDRESS_LENGTH = 64;

    /**
     * Type of number for the MS which has become available. If not known, set
     * to NULL.
     */
    private TON sourceAddrTon;
    /**
     * Numbering Plan Indicator for the MS which has become available. If not
     * known, set to NULL.
     */
    private NPI sourceAddrNpi;
    /**
     * Address of MS which has become available.
     */
    private String sourceAddr;
    /**
     * Type of number for destination address which requested an alert on a
     * particular MS becoming available. If not known, set to NULL.
     */
    private TON esmeAddrTon;
    /**
     * Numbering Plan Indicator for ESME which requested an alert on a
     * particular MS becoming available. If not known, set to NULL.
     */
    private NPI esmeAddrNpi;
    /**
     * Address of ESME which requested an alert on a particular MS becoming
     * available.
     */
    private String esmeAddr;
    /**
     * The status of the mobile station.
     */
    private MsAvailabilityStatus msAvailabilityStatus;

    /**
     * Конструктор.
     */
    public AlertNotification() {
        super(CommandId.ALERT_NOTIFICATION);
    }

    /**
     * Конструктор.
     *
     * @param bytes байткод PDU
     * @throws PDUException ошибка обработки PDU
     */
    public AlertNotification(final byte[] bytes) throws PDUException {
        super(bytes);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected final void parseBody(final byte[] bytes)
            throws PDUException {
        if (getCommandId() != CommandId.ALERT_NOTIFICATION) {
            throw new ClassCastException();
        }
        SmppByteBuffer bb = new SmppByteBuffer(bytes);
        try {
            short b = bb.removeByte();
            for (TON ton : TON.values()) {
                if (ton.getValue() == b) {
                    sourceAddrTon = ton;
                }
            }
            if (sourceAddrTon == null) {
                sourceAddrTon = TON.RESERVED;
            }
            b = bb.removeByte();
            for (NPI npi : NPI.values()) {
                if (npi.getValue() == b) {
                    sourceAddrNpi = npi;
                }
            }
            if (sourceAddrNpi == null) {
                sourceAddrNpi = NPI.RESERVED;
            }
            sourceAddr = bb.removeCString();
            if (sourceAddr.length() > MAX_ADDRESS_LENGTH) {
                throw new PDUException("sourceAddr field is too long");
            }
            b = bb.removeByte();
            for (TON ton : TON.values()) {
                if (ton.getValue() == b) {
                    esmeAddrTon = ton;
                }
            }
            if (esmeAddrTon == null) {
                esmeAddrTon = TON.RESERVED;
            }
            b = bb.removeByte();
            for (NPI npi : NPI.values()) {
                if (npi.getValue() == b) {
                    esmeAddrNpi = npi;
                }
            }
            if (esmeAddrNpi == null) {
                esmeAddrNpi = NPI.RESERVED;
            }
            esmeAddr = bb.removeCString();
            if (esmeAddr.length() > MAX_ADDRESS_LENGTH) {
                throw new PDUException("esmeAddr field is too long");
            }
        } catch (WrongLengthException e) {
            throw new PDUException("PDU parsing error", e);
        }
        if (bb.length() > 0) {
            List<TLV> list = getOptionalParams(bb.getBuffer());
            for (TLV tlv : list) {
                if (tlv.getTag() == ParameterTag.MS_AVAILABILITY_STATUS) {
                    msAvailabilityStatus = (MsAvailabilityStatus) tlv;
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected final byte[] getBodyBytes() throws PDUException {
        SmppByteBuffer bb = new SmppByteBuffer();
        try {
            bb.appendByte(sourceAddrTon != null ? sourceAddrTon.getValue() : TON.UNKNOWN.getValue());
        } catch (WrongParameterException e) {
            throw new PDUException("sourceAddrTon field is invalid", e);
        }
        try {
            bb.appendByte(sourceAddrNpi != null ? sourceAddrNpi.getValue() : NPI.UNKNOWN.getValue());
        } catch (WrongParameterException e) {
            throw new PDUException("sourceAddrNpi field is invalid", e);
        }
        if (sourceAddr != null && sourceAddr.length() > MAX_ADDRESS_LENGTH) {
            throw new PDUException("sourceAddr field is too long");
        }
        bb.appendCString(sourceAddr);
        try {
            bb.appendByte(esmeAddrTon != null ? esmeAddrTon.getValue() : TON.UNKNOWN.getValue());
        } catch (WrongParameterException e) {
            throw new PDUException("esmeAddrTon field is invalid", e);
        }
        try {
            bb.appendByte(esmeAddrNpi != null ? esmeAddrNpi.getValue() : NPI.UNKNOWN.getValue());
        } catch (WrongParameterException e) {
            throw new PDUException("esmeAddrNpi field is invalid", e);
        }
        if (esmeAddr != null && esmeAddr.length() > MAX_ADDRESS_LENGTH) {
            throw new PDUException("esmeAddr field is too long");
        }
        bb.appendCString(esmeAddr);
        if (msAvailabilityStatus != null) {
            try {
                bb.appendBytes(msAvailabilityStatus.getBytes(),
                        msAvailabilityStatus.getBytes().length);
            } catch (TLVException e) {
                throw new PDUException("TLVs parsing failed", e);
            }
        }
        return bb.getBuffer();
    }

    /**
     * @return the type of number for the MS
     */
    public final TON getSourceAddrTon() {
        return sourceAddrTon;
    }

    /**
     * @param sourceAddrTon type of number for the MS
     */
    public final void setSourceAddrTon(final TON sourceAddrTon) {
        this.sourceAddrTon = sourceAddrTon;
    }

    /**
     * @return the numbering Plan Indicator for the MS
     */
    public final NPI getSourceAddrNpi() {
        return sourceAddrNpi;
    }

    /**
     * @param sourceAddrNpi numbering Plan Indicator for the MS
     */
    public final void setSourceAddrNpi(final NPI sourceAddrNpi) {
        this.sourceAddrNpi = sourceAddrNpi;
    }

    /**
     * @return address of MS
     */
    public final String getSourceAddr() {
        return sourceAddr;
    }

    /**
     * @param sourceAddr address of MS
     */
    public final void setSourceAddr(final String sourceAddr) {
        this.sourceAddr = sourceAddr;
    }

    /**
     * @return type of number for destination address
     */
    public final TON getEsmeAddrTon() {
        return esmeAddrTon;
    }

    /**
     * @param esmeAddrTon type of number for destination address
     */
    public final void setEsmeAddrTon(final TON esmeAddrTon) {
        this.esmeAddrTon = esmeAddrTon;
    }

    /**
     * @return numbering Plan Indicator for ESME
     */
    public final NPI getEsmeAddrNpi() {
        return esmeAddrNpi;
    }

    /**
     * @param esmeAddrNpi numbering Plan Indicator for ESME
     */
    public final void setEsmeAddrNpi(final NPI esmeAddrNpi) {
        this.esmeAddrNpi = esmeAddrNpi;
    }

    /**
     * @return address of ESME
     */
    public final String getEsmeAddr() {
        return esmeAddr;
    }

    /**
     * @param esmeAddr address of ESME
     */
    public final void setEsmeAddr(final String esmeAddr) {
        this.esmeAddr = esmeAddr;
    }

    /**
     * @return the status of the mobile station
     */
    public final MsAvailabilityStatus getMsAvailabilityStatus() {
        return msAvailabilityStatus;
    }

    /**
     * @param msAvailabilityStatus the status of the mobile station
     */
    public final void setMsAvailabilityStatus(
            final MsAvailabilityStatus msAvailabilityStatus) {
        this.msAvailabilityStatus = msAvailabilityStatus;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String toString() {
        return getClass().getName() + " Object {" + "\nsourceAddTon : "
                + sourceAddrTon + "\nsourceAddrNpi : " + sourceAddrNpi
                + "\nsourceAddr : " + sourceAddr + "\nesmeAddrTon : "
                + esmeAddrTon + "\nesmeAddrNpi : " + esmeAddrNpi
                + "\nesmeAddr : " + esmeAddr + "\nmsAvailabilityStatus : "
                + msAvailabilityStatus + "\n}";
    }

}
