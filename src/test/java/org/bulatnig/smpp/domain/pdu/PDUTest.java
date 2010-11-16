package org.bulatnig.smpp.domain.pdu;

import junit.framework.JUnit4TestAdapter;
import static org.junit.Assert.assertEquals;

import org.bulatnig.smpp.util.SmppByteBuffer;
import org.junit.Test;
import org.bulatnig.smpp.pdu.*;
import org.bulatnig.smpp.util.WrongParameterException;

/**
 * Put file description here.
 * User: Bulat Nigmatullin
 * Date: 11.06.2008
 * Time: 6:04:17
 */
public class PDUTest {

    // Used for backward compatibility (IDEs, Ant and JUnit 3 text runner)

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(PDUTest.class);
    }

    @Test
    public void constructor() throws WrongParameterException, PDUException {
        SmppByteBuffer bb = new SmppByteBuffer();
		bb.appendInt(41);
		bb.appendInt(9);
		bb.appendInt(0);
		bb.appendInt(1);
		bb.appendCString("bulat");
		bb.appendCString("java");
		bb.appendCString("Logica");
		bb.appendByte((byte) 0x34);
		bb.appendByte((byte) 0x01);
		bb.appendByte((byte) 0x01);
		bb.appendCString("11*");
        PDU pdu = new BindTransceiver(bb.array());
        assertEquals(41,pdu.getCommandLength());
        assertEquals("0000002900000009000000000000000162756c6174006a617661004c6f676963610034010131312a00", new SmppByteBuffer(pdu.getBytes()).getHexDump());
        assertEquals(CommandId.BIND_TRANSCEIVER,pdu.getCommandId());
        assertEquals(9,pdu.getCommandId().getValue());
        assertEquals(CommandStatus.ESME_ROK,pdu.getCommandStatus());
        assertEquals(0,pdu.getCommandStatus().getValue());
        assertEquals(1,pdu.getSequenceNumber());
    }

    @Test(expected = PDUException.class)
    public void constructor2() throws WrongParameterException, PDUException {
        SmppByteBuffer bb = new SmppByteBuffer();
		bb.appendInt(41);
		bb.appendInt(9);
		bb.appendInt(0);
		bb.appendInt(1);
        new BindTransceiver(bb.array());
    }
}
