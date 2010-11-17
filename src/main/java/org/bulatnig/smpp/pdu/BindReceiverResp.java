package org.bulatnig.smpp.pdu;

import org.bulatnig.smpp.pdu.tlv.ParameterTag;
import org.bulatnig.smpp.pdu.tlv.ScInterfaceVersion;
import org.bulatnig.smpp.pdu.tlv.TLV;
import org.bulatnig.smpp.pdu.tlv.TLVException;
import org.bulatnig.smpp.util.SmppByteBuffer;

import java.util.List;

/**
 * BindReceiver Response PDU.
 *
 * @author Bulat Nigmatullin
 * @see BindReceiver
 */
public class BindReceiverResp extends PDU {

    /**
     * Максимальная длина systemId поля.
     */
    private static final int MAX_SYSTEMID_LENGTH = 15;

    /**
     * SMSC identifier. Identifies the SMSC to the ESME.
     */
    private String systemId;
    /**
     * SMPP version supported by SMSC.
     */
    private ScInterfaceVersion scInterfaceVersion;

    /**
     * Constructor.
     */
    public BindReceiverResp() {
        super(CommandId.BIND_RECEIVER_RESP);
    }

    /**
     * Constructor.
     *
     * @param bytes байткод PDU
     * @throws PDUException ошибка обработки PDU
     */
    public BindReceiverResp(final byte[] bytes) throws PDUException {
        super(bytes);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected final byte[] getBodyBytes() throws PDUException {
        if (getCommandStatus() == CommandStatus.ESME_ROK) {
            SmppByteBuffer bb = new SmppByteBuffer();
            if (systemId != null && systemId.length() > MAX_SYSTEMID_LENGTH) {
                throw new PDUException("systemId field is too long");
            }
            bb.appendCString(systemId);
            if (scInterfaceVersion != null) {
                try {
                    bb.appendBytes(scInterfaceVersion.getBytes());
                } catch (TLVException e) {
                    throw new PDUException("TLVs parsing failed", e);
                }
            }
            return bb.array();
        } else {
            return new byte[0];
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected final void parseBody(final byte[] bytes)
            throws PDUException {
        if (getCommandId() != CommandId.BIND_RECEIVER_RESP) {
            throw new ClassCastException();
        }
        if (getCommandStatus() == CommandStatus.ESME_ROK) {
            SmppByteBuffer bb = new SmppByteBuffer(bytes);
            systemId = bb.removeCString();
            if (systemId.length() > MAX_SYSTEMID_LENGTH) {
                throw new PDUException("systemId field is too long");
            }
            if (bb.length() > 0) {
                List<TLV> list = getOptionalParams(bb.array());
                for (TLV tlv : list) {
                    if (tlv.getTag() == ParameterTag.SC_INTERFACE_VERSION) {
                        scInterfaceVersion = (ScInterfaceVersion) tlv;
                    }
                }
            }
        }
    }

    /**
     * @return SMSC identifier
     */
    public final String getSystemId() {
        return systemId;
    }

    /**
     * @param systemId SMSC identifier
     */
    public final void setSystemId(final String systemId) {
        this.systemId = systemId;
    }

    /**
     * @return SMPP version supported by SMSC
     */
    public final ScInterfaceVersion getScInterfaceVersion() {
        return scInterfaceVersion;
    }

    /**
     * @param scInterfaceVersionVal SMPP version supported by SMSC
     */
    public final void setScInterfaceVersion(
            final ScInterfaceVersion scInterfaceVersionVal) {
        scInterfaceVersion = scInterfaceVersionVal;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String toString() {
        return getClass().getName() + " Object {" + "\nsystemId : " + systemId
                + "\nscInterfaceVersion : " + scInterfaceVersion + "\n}";
    }

}
