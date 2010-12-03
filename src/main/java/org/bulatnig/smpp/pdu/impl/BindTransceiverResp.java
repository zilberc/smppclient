package org.bulatnig.smpp.pdu.impl;

import org.bulatnig.smpp.pdu.CommandId;
import org.bulatnig.smpp.pdu.PduParsingException;
import org.bulatnig.smpp.util.ByteBuffer;
import org.bulatnig.smpp.util.TerminatingNullNotFoundException;

/**
 * BindTransceiver Response PDU.
 *
 * @author Bulat Nigmatullin
 */
public class BindTransceiverResp extends AbstractPdu {

    /**
     * SMSC identifier. Identifies the SMSC to the ESME.
     */
    private String systemId;
    /**
     * SMPP version supported by SMSC.
     */
//    private ScInterfaceVersion scInterfaceVersion;

    public BindTransceiverResp() {
        super(CommandId.BIND_TRANSCEIVER_RESP);
    }

    public BindTransceiverResp(ByteBuffer bb) throws PduParsingException {
        super(bb);
        try {
            systemId = bb.removeCString();
        } catch (TerminatingNullNotFoundException e) {
            throw new PduParsingException("System id parsing failed.", e);
        }
    }

    @Override
    protected ByteBuffer body() throws PduParsingException {
        ByteBuffer bb = new ByteBuffer();
        bb.appendCString(systemId);
        return bb;
    }

    public String getSystemId() {
        return systemId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }
}
