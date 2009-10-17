package org.bulatnig.smpp.domain.pdu;

import junit.framework.JUnit4TestAdapter;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.bulatnig.smpp.pdu.*;
import org.bulatnig.smpp.pdu.tlv.AvailabilityStatus;
import org.bulatnig.smpp.pdu.tlv.MsAvailabilityStatus;
import org.bulatnig.smpp.util.SMPPByteBuffer;
import org.bulatnig.smpp.util.WrongParameterException;

/**
 * AlertNotification test class.
 * <p/>
 * User: Bulat Nigmatullin
 * Date: 04.06.2008
 * Time: 7:37:26
 */
public class AlertNotificationTest {

    // Used for backward compatibility (IDEs, Ant and JUnit 3 text runner)

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(AlertNotificationTest.class);
    }

    @Test
    public void bytesToObject() throws WrongParameterException, PDUException {
        SMPPByteBuffer sbb = new SMPPByteBuffer();
        sbb.appendInt(49L);
        sbb.appendInt(258L);
        sbb.appendInt(0);
        sbb.appendInt(1000123456L);
        sbb.appendByte((short) 0);
        sbb.appendByte((short) 0);
        sbb.appendCString("99501363400");
        sbb.appendByte((short) 2);
        sbb.appendByte((short) 1);
        sbb.appendCString("destination");
        sbb.appendShort(0x0422);
        sbb.appendShort(1);
        sbb.appendByte((short) 2);
        AlertNotification an = new AlertNotification(sbb.getBuffer());
        assertEquals(49L, an.getCommandLength());
        assertEquals(CommandId.ALERT_NOTIFICATION, an.getCommandId());
        assertEquals(258L, an.getCommandId().getValue());
        assertEquals(CommandStatus.ESME_ROK, an.getCommandStatus());
        assertEquals(0, an.getCommandStatus().getValue());
        assertEquals(1000123456L, an.getSequenceNumber());
        assertEquals(TON.UNKNOWN, an.getSourceAddrTon());
        assertEquals(NPI.UNKNOWN, an.getSourceAddrNpi());
        assertEquals("99501363400", an.getSourceAddr());
        assertEquals(TON.NATIONAL, an.getEsmeAddrTon());
        assertEquals(NPI.ISDN, an.getEsmeAddrNpi());
        assertEquals("destination", an.getEsmeAddr());
        assertEquals(AvailabilityStatus.UNAVAILABLE, an.getMsAvailabilityStatus().getValue());
    }

    @Test
    public void objectToBytes() throws PDUException {
        AlertNotification an = new AlertNotification();
        an.setCommandStatus(CommandStatus.ESME_ROK);
        an.setSequenceNumber(115L);
        an.setSourceAddrTon(TON.INTERNATIONAL);
        an.setSourceAddrNpi(NPI.LAND_MOBILE);
        an.setSourceAddr("remarema");
        an.setEsmeAddrTon(TON.UNKNOWN);
        an.setEsmeAddrNpi(NPI.WAP_CLIENT_ID);
        an.setEsmeAddr("destmy");
        an.setMsAvailabilityStatus(new MsAvailabilityStatus(AvailabilityStatus.AVAILABLE));
        SMPPByteBuffer sbb = new SMPPByteBuffer(an.getBytes());
        assertEquals("00000029000001020000000000000073010672656d6172656d61000012646573746d79000422000100", sbb.getHexDump());
    }

}
