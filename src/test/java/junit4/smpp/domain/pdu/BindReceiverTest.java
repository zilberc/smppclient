package junit4.smpp.domain.pdu;

import junit.framework.JUnit4TestAdapter;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.bulatnig.smpp.pdu.*;
import org.bulatnig.smpp.util.SMPPByteBuffer;
import org.bulatnig.smpp.util.WrongParameterException;

/**
 * Put file description here.
 * <p/>
 * User: Bulat Nigmatullin
 * Date: 04.06.2008
 * Time: 7:39:43
 */
public class BindReceiverTest {

    // Used for backward compatibility (IDEs, Ant and JUnit 3 text runner)

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(BindReceiverTest.class);
    }

    @Test
    public void bytesToObject() throws WrongParameterException, PDUException {
        SMPPByteBuffer sbb = new SMPPByteBuffer();
        sbb.appendInt(58L);
        sbb.appendInt(1L);
        sbb.appendInt(13L);
        sbb.appendInt(2000123456L);
        sbb.appendCString("miluoki");
        sbb.appendCString("asim");
        sbb.appendCString("yakamoto");
        sbb.appendByte((short) 10);
        sbb.appendByte((short) 5);
        sbb.appendByte((short) 14);
        sbb.appendCString("romaromaromaroma");
        BindReceiver br = new BindReceiver(sbb.getBuffer());
        assertEquals(58L, br.getCommandLength());
        assertEquals(CommandId.BIND_RECEIVER, br.getCommandId());
        assertEquals(1L, br.getCommandId().getValue());
        assertEquals(CommandStatus.ESME_RBINDFAIL, br.getCommandStatus());
        assertEquals(13L, br.getCommandStatus().getValue());
        assertEquals("miluoki", br.getSystemId());
        assertEquals("asim", br.getPassword());
        assertEquals("yakamoto", br.getSystemType());
        assertEquals((short) 10, br.getInterfaceVersion());
        assertEquals(TON.ALPHANUMERIC, br.getAddrTon());
        assertEquals(NPI.INTERNET, br.getAddrNpi());
        assertEquals("romaromaromaroma", br.getAddressRange());
    }

    @Test
    public void objectToBytes() throws PDUException {
        BindReceiver br = new BindReceiver();
        br.setCommandStatus(CommandStatus.ESME_RINVSRCADR);
        br.setSequenceNumber(1921041);
        br.setSystemId("abakan");
        br.setPassword("chertog");
        br.setSystemType("closure");
        br.setInterfaceVersion((short)168);
        br.setAddrTon(TON.NETWORK_SPECIFIC);
        br.setAddrNpi(NPI.DATA);
        br.setAddressRange("all can");
        assertEquals("00000032000000010000000a001d50116162616b616e0063686572746f6700636c6f7375726500a80303616c6c2063616e00", new SMPPByteBuffer(br.getBytes()).getHexDump());
    }

}
