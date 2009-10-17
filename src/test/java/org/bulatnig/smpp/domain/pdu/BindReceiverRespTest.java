package org.bulatnig.smpp.domain.pdu;

import junit.framework.JUnit4TestAdapter;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.bulatnig.smpp.pdu.BindReceiverResp;
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
 * Time: 21:35:11
 */
public class BindReceiverRespTest {

    // Used for backward compatibility (IDEs, Ant and JUnit 3 text runner)

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(BindReceiverRespTest.class);
    }

    @Test
    public void bytesToObject() throws WrongParameterException, PDUException {
        SMPPByteBuffer sbb = new SMPPByteBuffer();
        sbb.appendInt(33L);
        sbb.appendInt(2147483649L);
        sbb.appendInt(0);
        sbb.appendInt(70L);
        sbb.appendCString("simple resp");
        sbb.appendShort(0x0210);
        sbb.appendShort(1);
        sbb.appendByte((short)50);
        BindReceiverResp brr = new BindReceiverResp(sbb.getBuffer());
        assertEquals(33L, brr.getCommandLength());
        assertEquals(CommandId.BIND_RECEIVER_RESP, brr.getCommandId());
        assertEquals(0x80000001L, brr.getCommandId().getValue());
        assertEquals(CommandStatus.ESME_ROK, brr.getCommandStatus());
        assertEquals(0L, brr.getCommandStatus().getValue());
        assertEquals("simple resp", brr.getSystemId());
        assertEquals((short)0x32, brr.getScInterfaceVersion().getValue());
    }

    @Test
    public void objectToBytes() throws PDUException {
        BindReceiverResp brr = new BindReceiverResp();
        brr.setCommandStatus(CommandStatus.ESME_ROK);
        brr.setSequenceNumber(100000000L);
        brr.setSystemId("systemaida");
        brr.setScInterfaceVersion(new ScInterfaceVersion((short)0x40));
        assertEquals("00000020800000010000000005f5e10073797374656d61696461000210000140", new SMPPByteBuffer(brr.getBytes()).getHexDump());
    }

    @Test
    public void objectToBytes2() throws PDUException {
        BindReceiverResp brr = new BindReceiverResp();
        brr.setCommandStatus(CommandStatus.ESME_RCNTSUBDL);
        brr.setSequenceNumber(100000000L);
        brr.setSystemId("systemaida");
        brr.setScInterfaceVersion(new ScInterfaceVersion((short)0x40));
        assertEquals("00000010800000010000004405f5e100", new SMPPByteBuffer(brr.getBytes()).getHexDump());
    }

}
