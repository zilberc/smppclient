package org.bulatnig.smpp.domain.pdu;

import junit.framework.JUnit4TestAdapter;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.bulatnig.smpp.pdu.*;
import org.bulatnig.smpp.util.SMPPByteBuffer;
import org.bulatnig.smpp.util.WrongParameterException;

/**
 * Comment here.
 * User: Bulat Nigmatullin
 * Date: 21.06.2008
 * Time: 19:09:27
 */
public class BindTransmitterTest {

    // Used for backward compatibility (IDEs, Ant and JUnit 3 text runner)

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(BindTransmitterTest.class);
    }

    @Test
    public void bytesToObject() throws WrongParameterException, PDUException {
        SMPPByteBuffer sbb = new SMPPByteBuffer();
        sbb.appendInt(68L);
        sbb.appendInt(2L);
        sbb.appendInt(15);
        sbb.appendInt(2000123456L);
        sbb.appendCString("system id sh");
        sbb.appendCString("passhere");
        sbb.appendCString("system :)");
        sbb.appendByte((short) 10);
        sbb.appendByte((short) 11);
        sbb.appendByte((short) 8);
        sbb.appendCString("range of address");
        BindTransmitter bt = new BindTransmitter(sbb.getBuffer());
        assertEquals(68L, bt.getCommandLength());
        assertEquals(CommandId.BIND_TRANSMITTER, bt.getCommandId());
        assertEquals(2L, bt.getCommandId().getValue());
        assertEquals(CommandStatus.ESME_RINVSYSID, bt.getCommandStatus());
        assertEquals(15L, bt.getCommandStatus().getValue());
        assertEquals(2000123456L, bt.getSequenceNumber());
        assertEquals("system id sh", bt.getSystemId());
        assertEquals("passhere", bt.getPassword());
        assertEquals("system :)", bt.getSystemType());
        assertEquals((short) 10, bt.getInterfaceVersion());
        assertEquals(TON.RESERVED, bt.getAddrTon());
        assertEquals(NPI.NATIONAL, bt.getAddrNpi());
        assertEquals("range of address", bt.getAddressRange());
    }

    @Test
    public void objectToBytes() throws PDUException {
        BindTransmitter bt = new BindTransmitter();
        bt.setSystemId("my id");
        bt.setPassword("Roman");
        bt.setSystemType("Albert");
        bt.setInterfaceVersion((short) 115);
        bt.setAddrTon(TON.ABBREVIATED);
        bt.setAddrNpi(NPI.TELEX);
        bt.setAddressRange("Artur");
        assertEquals("0000002c0000000200000000000000006d7920696400526f6d616e00416c6265727400730604417274757200", new SMPPByteBuffer(bt.getBytes()).getHexDump());
    }
}
