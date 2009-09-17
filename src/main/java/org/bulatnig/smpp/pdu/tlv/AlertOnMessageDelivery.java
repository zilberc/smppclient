package org.bulatnig.smpp.pdu.tlv;

import org.bulatnig.smpp.pdu.EsmClass;

/**
 * The alert_on_message_delivery parameter is set to instruct a MS to alert the
 * user (in a MS implementation specific manner) when the short message arrives
 * at the MS.
 *
 * @author Bulat Nigmatullin
 */
public class AlertOnMessageDelivery extends TLV {
    /**
     * Длина значения параметра.
     */
    private static final int LENGTH = 0;

    /**
     * Constructor.
     */
    public AlertOnMessageDelivery() {
        super(ParameterTag.ALERT_ON_MESSAGE_DELIVERY);
    }

    /**
     * Constructor.
     *
     * @param bytes bytecode of TLV
     * @throws TLVException ошибка разбора TLV
     */
    public AlertOnMessageDelivery(final byte[] bytes) throws TLVException {
        super(bytes);
    }

    @Override
    protected void parseValue(byte[] bytes, final EsmClass esmClass, final short dataCoding) throws TLVException {
        if (getTag() != ParameterTag.ALERT_ON_MESSAGE_DELIVERY) {
            throw new ClassCastException();
        }
        if (bytes.length != LENGTH) {
            throw new TLVException("Wrong value supplied. Expected " + LENGTH + " length.");
        }
    }

    @Override
    protected byte[] getValueBytes(final EsmClass esmClass, final short dataCoding) throws TLVException {
        return new byte[0];
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String toString() {
        return getClass().getName() + " Object {}";
	}

}
