package junit4.smpp.domain.pdu;

import junit.framework.JUnit4TestAdapter;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.bulatnig.smpp.pdu.BindTransmitterResp;
import org.bulatnig.smpp.pdu.CommandId;
import org.bulatnig.smpp.pdu.CommandStatus;
import org.bulatnig.smpp.pdu.PDUException;
import org.bulatnig.smpp.pdu.tlv.ScInterfaceVersion;
import org.bulatnig.smpp.util.SMPPByteBuffer;
import org.bulatnig.smpp.util.WrongParameterException;

/**
 * Comment here.
 * User: Bulat Nigmatullin
 * Date: 21.06.2008
 * Time: 19:09:54
 */
public class BindTransmitterRespTest {

    // Used for backward compatibility (IDEs, Ant and JUnit 3 text runner)

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(BindTransmitterRespTest.class);
    }

    @Test
    public void bytesToObject() throws WrongParameterException, PDUException {
        SMPPByteBuffer sbb = new SMPPByteBuffer();
        sbb.appendInt(37L);
        sbb.appendInt(2147483650L);
        sbb.appendInt(0);
        sbb.appendInt(2000123456L);
        sbb.appendCString("response super ");
        sbb.appendShort(0x0210);
        sbb.appendShort(1);
        sbb.appendByte((short)51);
        BindTransmitterResp btr = new BindTransmitterResp(sbb.getBuffer());
        assertEquals(37L, btr.getCommandLength());
        assertEquals(CommandId.BIND_TRANSMITTER_RESP, btr.getCommandId());
        assertEquals(2147483650L, btr.getCommandId().getValue());
        assertEquals(CommandStatus.ESME_ROK, btr.getCommandStatus());
        assertEquals(0L, btr.getCommandStatus().getValue());
        assertEquals("response super ", btr.getSystemId());
        assertEquals((short)0x33, btr.getScInterfaceVersion().getValue());
    }

    @Test
    public void objectToBytes() throws PDUException {
        BindTransmitterResp btr = new BindTransmitterResp();
        btr.setCommandStatus(CommandStatus.ESME_ROK);
        btr.setSequenceNumber(42389104L);
        btr.setSystemId("noname id");
        btr.setScInterfaceVersion(new ScInterfaceVersion((short)52));
        assertEquals("0000001f80000002000000000286ce706e6f6e616d65206964000210000134", new SMPPByteBuffer(btr.getBytes()).getHexDump());
    }

    @Test
    public void objectToBytes2() throws PDUException {
        BindTransmitterResp btr = new BindTransmitterResp();
        btr.setCommandStatus(CommandStatus.ESME_RCNTSUBDL);
        btr.setSequenceNumber(42389104L);
        btr.setSystemId("noname id");
        btr.setScInterfaceVersion(new ScInterfaceVersion((short)52));
        assertEquals("0000001080000002000000440286ce70", new SMPPByteBuffer(btr.getBytes()).getHexDump());
    }

}
