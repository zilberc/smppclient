package org.bulatnig.smpp.domain.pdu;

import junit.framework.JUnit4TestAdapter;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.bulatnig.smpp.pdu.BindTransceiverResp;
import org.bulatnig.smpp.pdu.CommandId;
import org.bulatnig.smpp.pdu.CommandStatus;
import org.bulatnig.smpp.pdu.PDUException;
import org.bulatnig.smpp.pdu.tlv.ScInterfaceVersion;
import org.bulatnig.smpp.util.SmppByteBuffer;
import org.bulatnig.smpp.util.WrongParameterException;

/**
 * Comment here.
 * User: Bulat Nigmatullin
 * Date: 26.06.2008
 * Time: 6:37:47
 */
public class BindTransceiverRespTest {

    // Used for backward compatibility (IDEs, Ant and JUnit 3 text runner)

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(BindTransceiverRespTest.class);
    }

    @Test
    public void bytesToObject() throws WrongParameterException, PDUException {
        SmppByteBuffer sbb = new SmppByteBuffer();
        sbb.appendInt(37L);
        sbb.appendInt(2147483657L);
        sbb.appendInt(0);
        sbb.appendInt(2000123456L);
        sbb.appendCString("Rome is the cap");
        sbb.appendShort(0x0210);
        sbb.appendShort(1);
        sbb.appendByte((short)0);
        BindTransceiverResp btr = new BindTransceiverResp(sbb.array());
        assertEquals(37L, btr.getCommandLength());
        assertEquals(CommandId.BIND_TRANSCEIVER_RESP, btr.getCommandId());
        assertEquals(2147483657L, btr.getCommandId().getValue());
        assertEquals(CommandStatus.ESME_ROK, btr.getCommandStatus());
        assertEquals(0L, btr.getCommandStatus().getValue());
        assertEquals(2000123456L, btr.getSequenceNumber());
        assertEquals("Rome is the cap", btr.getSystemId());
        assertEquals((short)0, btr.getScInterfaceVersion().getValue());
    }

    @Test
    public void objectToBytes() throws PDUException {
        BindTransceiverResp btr = new BindTransceiverResp();
        btr.setCommandStatus(CommandStatus.ESME_ROK);
        btr.setSequenceNumber(123456789L);
        btr.setSystemId("COMMANDOS!");
        btr.setScInterfaceVersion(new ScInterfaceVersion((short)100));
        assertEquals("000000208000000900000000075bcd15434f4d4d414e444f5321000210000164", new SmppByteBuffer(btr.getBytes()).getHexDump());
    }

    @Test
    public void objectToBytes2() throws PDUException {
        BindTransceiverResp btr = new BindTransceiverResp();
        btr.setCommandStatus(CommandStatus.ESME_RUNKNOWNERR);
        btr.setSequenceNumber(123456789L);
        btr.setSystemId("COMMANDOS!");
        btr.setScInterfaceVersion(new ScInterfaceVersion((short)100));
        assertEquals("0000001080000009000000ff075bcd15", new SmppByteBuffer(btr.getBytes()).getHexDump());
    }
    
}
