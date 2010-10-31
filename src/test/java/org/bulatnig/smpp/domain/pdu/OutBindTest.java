package org.bulatnig.smpp.domain.pdu;

import junit.framework.JUnit4TestAdapter;
import static org.junit.Assert.assertEquals;

import org.bulatnig.smpp.util.SmppByteBuffer;
import org.junit.Test;
import org.bulatnig.smpp.pdu.CommandId;
import org.bulatnig.smpp.pdu.CommandStatus;
import org.bulatnig.smpp.pdu.OutBind;
import org.bulatnig.smpp.pdu.PDUException;
import org.bulatnig.smpp.util.WrongParameterException;

/**
 * Comment here.
 * User: Bulat Nigmatullin
 * Date: 26.06.2008
 * Time: 6:52:14
 */
public class OutBindTest {

    // Used for backward compatibility (IDEs, Ant and JUnit 3 text runner)

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(OutBindTest.class);
    }

    @Test
    public void bytesToObject() throws WrongParameterException, PDUException {
        SmppByteBuffer sbb = new SmppByteBuffer();
        sbb.appendInt(32L);
        sbb.appendInt(11L);
        sbb.appendInt(0);
        sbb.appendInt(44444444L);
        sbb.appendCString("Lomotech");
        sbb.appendCString("secreT");
        OutBind ob = new OutBind(sbb.getBuffer());
        assertEquals(32L, ob.getCommandLength());
        assertEquals(CommandId.OUTBIND, ob.getCommandId());
        assertEquals(11L, ob.getCommandId().getValue());
        assertEquals(CommandStatus.ESME_ROK, ob.getCommandStatus());
        assertEquals(0L, ob.getCommandStatus().getValue());
        assertEquals(44444444L, ob.getSequenceNumber());
        assertEquals("Lomotech", ob.getSystemId());
        assertEquals("secreT", ob.getPassword());
    }

    @Test
    public void objectToBytes() throws PDUException {
        OutBind ob = new OutBind();
        ob.setCommandStatus(CommandStatus.ESME_RDELIVERYFAILURE);
        ob.setSequenceNumber(14);
        ob.setSystemId("");
        ob.setPassword(null);
        assertEquals("000000120000000b000000fe0000000e0000", new SmppByteBuffer(ob.getBytes()).getHexDump());
    }

}
