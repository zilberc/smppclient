package org.bulatnig.smpp.pdu.impl;

import org.bulatnig.smpp.pdu.CommandId;
import org.bulatnig.smpp.pdu.CommandStatus;
import org.bulatnig.smpp.pdu.PduException;
import org.bulatnig.smpp.util.ByteBuffer;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * BindTransceiverResp test.
 *
 * @author Bulat Nigmatullin
 */
public class BindTransceiverRespTest {

    @Test
    public void bytesToObject() throws PduException {
        ByteBuffer sbb = new ByteBuffer();
        sbb.appendInt(37L);
        sbb.appendInt(2147483657L);
        sbb.appendInt(0);
        sbb.appendInt(2000123456L);
        sbb.appendCString("Rome is the cap");
        sbb.appendShort(0x0210);
        sbb.appendShort(1);
        sbb.appendByte((short)0);
        BindTransceiverResp btr = new BindTransceiverResp(sbb);
        assertEquals(CommandId.BIND_TRANSCEIVER_RESP, btr.getCommandId());
        assertEquals(CommandStatus.ESME_ROK, btr.getCommandStatus());
        assertEquals(2000123456L, btr.getSequenceNumber());
        assertEquals("Rome is the cap", btr.getSystemId());
//        assertEquals((short)0, btr.getScInterfaceVersion().getValue());
    }

    @Test
    public void objectToBytes() throws PduException {
        BindTransceiverResp btr = new BindTransceiverResp();
        btr.setCommandStatus(CommandStatus.ESME_ROK);
        btr.setSequenceNumber(123456789L);
        btr.setSystemId("COMMANDOS!");
//        btr.setScInterfaceVersion(new ScInterfaceVersion((short)100));
//        assertEquals("000000208000000900000000075bcd15434f4d4d414e444f5321000210000164", btr.buffer().hexDump());
        assertEquals("0000001b8000000900000000075bcd15434f4d4d414e444f532100", btr.buffer().hexDump());
    }
}
