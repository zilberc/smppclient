package org.bulatnig.smpp.pdu;

import org.bulatnig.smpp.util.ByteBuffer;
import org.bulatnig.smpp.util.TerminatingNullNotFoundException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * AlertNotification test.
 *
 * @author Bulat Nigmatullin
 */
public class AlertNotificationTest {

    @Test
    public void bytesToObject() throws TerminatingNullNotFoundException, PduException {
        ByteBuffer sbb = new ByteBuffer();
//        sbb.appendInt(49L);
        sbb.appendInt(44L);
        sbb.appendInt(CommandId.ALERT_NOTIFICATION);
        sbb.appendInt(0);
        sbb.appendInt(1000123456L);
        sbb.appendByte((short) 0);
        sbb.appendByte((short) 0);
        sbb.appendCString("99501363400");
        sbb.appendByte((short) 2);
        sbb.appendByte((short) 1);
        sbb.appendCString("destination");
//        sbb.appendShort(0x0422);
//        sbb.appendShort(1);
//        sbb.appendByte((short) 2);
        AlertNotification an = new AlertNotification(sbb);
        assertEquals(CommandId.ALERT_NOTIFICATION, an.getCommandId());
        assertEquals(0, an.getCommandStatus());
        assertEquals(1000123456L, an.getSequenceNumber());
        assertEquals(0, an.getSourceAddrTon());
        assertEquals(0, an.getSourceAddrNpi());
        assertEquals("99501363400", an.getSourceAddr());
        assertEquals(2, an.getEsmeAddrTon());
        assertEquals(1, an.getEsmeAddrNpi());
        assertEquals("destination", an.getEsmeAddr());
//        assertEquals(AvailabilityStatus.UNAVAILABLE, an.getMsAvailabilityStatus().getValue());
    }

    @Test
    public void objectToBytes() throws PduException {
        AlertNotification an = new AlertNotification();
        an.setCommandStatus(0);
        an.setSequenceNumber(115);
        an.setSourceAddrTon(1);
        an.setSourceAddrNpi(6);
        an.setSourceAddr("remarema");
        an.setEsmeAddrTon(0);
        an.setEsmeAddrNpi(18);
        an.setEsmeAddr("destmy");
//        an.setMsAvailabilityStatus(new MsAvailabilityStatus(AvailabilityStatus.AVAILABLE));
//        assertEquals("00000029000001020000000000000073010672656d6172656d61000012646573746d79000422000100", an.buffer().hexDump());
        assertEquals("00000024000001020000000000000073010672656d6172656d61000012646573746d7900", an.buffer().hexDump());
    }
}
