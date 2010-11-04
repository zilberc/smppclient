package org.bulatnig.smpp.domain.pdu;

import junit.framework.JUnit4TestAdapter;
import static org.junit.Assert.assertEquals;

import org.bulatnig.smpp.util.SmppByteBuffer;
import org.junit.Test;
import org.bulatnig.smpp.pdu.*;
import org.bulatnig.smpp.util.WrongParameterException;

/**
 * Comment here.
 * User: Bulat Nigmatullin
 * Date: 21.06.2008
 * Time: 21:56:01
 */
public class BindTransceiverTest {

    // Used for backward compatibility (IDEs, Ant and JUnit 3 text runner)

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(BindTransceiverTest.class);
    }

    @Test
    public void bytesToObject() throws WrongParameterException, PDUException {
        SmppByteBuffer sbb = new SmppByteBuffer();
        sbb.appendInt(65L);
        sbb.appendInt(9L);
        sbb.appendInt(194);
        sbb.appendInt(666666L);
        sbb.appendCString("text here");
        sbb.appendCString("pasvordo");
        sbb.appendCString("world");
        sbb.appendByte((short) 100);
        sbb.appendByte((short) 0);
        sbb.appendByte((short) 0);
        sbb.appendCString("adresatos poluchatos");
        BindTransceiver bt = new BindTransceiver(sbb.array());
        assertEquals(65L, bt.getCommandLength());
        assertEquals(CommandId.BIND_TRANSCEIVER, bt.getCommandId());
        assertEquals(9L, bt.getCommandId().getValue());
        assertEquals(CommandStatus.ESME_RINVPARLEN, bt.getCommandStatus());
        assertEquals(194L, bt.getCommandStatus().getValue());
        assertEquals(666666L, bt.getSequenceNumber());
        assertEquals("text here", bt.getSystemId());
        assertEquals("pasvordo", bt.getPassword());
        assertEquals("world", bt.getSystemType());
        assertEquals((short) 100, bt.getInterfaceVersion());
        assertEquals(TON.UNKNOWN, bt.getAddrTon());
        assertEquals(NPI.UNKNOWN, bt.getAddrNpi());
        assertEquals("adresatos poluchatos", bt.getAddressRange());
    }

    @Test
    public void objectToBytes() throws PDUException {
        BindTransceiver bt = new BindTransceiver();
        bt.setSystemId("kirpich");
        bt.setPassword(".$#/`~7");
        bt.setSystemType("-------");
        bt.setInterfaceVersion((short) 115);
        bt.setAddrTon(TON.INTERNATIONAL);
        bt.setAddrNpi(null);
        bt.setAddressRange(")(*&^%$#@!");
        assertEquals("000000360000000900000000000000006b697270696368002e24232f607e37002d2d2d2d2d2d2d0073010029282a265e252423402100", new SmppByteBuffer(bt.getBytes()).getHexDump());
    }

}
