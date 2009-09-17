package org.bulatnig.smpp.pdu.tlv;

import org.bulatnig.smpp.util.SMPPByteBuffer;
import org.bulatnig.smpp.util.WrongLengthException;
import org.bulatnig.smpp.util.WrongParameterException;
import org.bulatnig.smpp.pdu.EsmClass;

/**
 * An ESME may use the set_dpf parameter to request the setting of a delivery
 * pending flag (DPF) for certain delivery failure scenarios, such as<br/> - MS
 * is unavailable for message delivery (as indicated by the HLR)<br/>
 * 
 * The SMSC should respond to such a request with an alert_notification PDU when
 * it detects that the destination MS has become available.<br/>
 * 
 * The delivery failure scenarios under which DPF is set is SMSC implementation
 * and network implementation specific. If a delivery pending flag is set by the
 * SMSC or network (e.g. HLR), then the SMSC should indicate this to the ESME in
 * the data_sm_resp message via the dpf_result parameter.
 * 
 * @author Bulat Nigmatullin
 * 
 */
public class SetDpf extends TLV {
	/**
	 * Длина значения параметра.
	 */
	private static final int LENGTH = 1;
	/**
	 * Значение параметра.
	 */
	private short value;

	/**
	 * Constructor.
	 * 
	 * @param dpf
	 *            значение параметра
	 */
	public SetDpf(final short dpf) {
		super(ParameterTag.SET_DPF);
		value = dpf;
	}

	/**
	 * Constructor.
	 * 
	 * @param bytes
	 *            bytecode of TLV
     * @throws TLVException ошибка разбора TLV
	 */
	public SetDpf(final byte[] bytes) throws TLVException {
		super(bytes);
	}

    @Override
    protected void parseValue(byte[] bytes, final EsmClass esmClass, final short dataCoding) throws TLVException {
		if (getTag() != ParameterTag.SET_DPF) {
			throw new ClassCastException();
		}
		if (bytes.length == LENGTH) {
            try {
                value = new SMPPByteBuffer(bytes).removeByte();
            } catch (WrongLengthException e) {
                throw new TLVException("Buffer error during parsing value", e);
            }
		} else {
            throw new TLVException("Value has wrong length: " + bytes.length + " but expected " + LENGTH);
        }
    }

    @Override
    protected byte[] getValueBytes(final EsmClass esmClass, final short dataCoding) throws TLVException {
        SMPPByteBuffer sbb = new SMPPByteBuffer();
        try {
            sbb.appendByte(value);
        } catch (WrongParameterException e) {
            throw new TLVException("Buffer error during parsing value", e);
        }
        return sbb.getBuffer();
    }

    /**
	 * @return значение параметра
	 */
	public final short getValue() {
		return value;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final String toString() {
		return getClass().getName() + " Object {" + "\nvalue : " + value
				+ "\n}";
	}

}
