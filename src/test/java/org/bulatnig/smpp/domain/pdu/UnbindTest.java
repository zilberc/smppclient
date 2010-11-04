package org.bulatnig.smpp.domain.pdu;

import junit.framework.JUnit4TestAdapter;
import static org.junit.Assert.assertEquals;

import org.bulatnig.smpp.util.SmppByteBuffer;
import org.junit.Test;
import org.bulatnig.smpp.pdu.CommandId;
import org.bulatnig.smpp.pdu.CommandStatus;
import org.bulatnig.smpp.pdu.PDUException;
import org.bulatnig.smpp.pdu.Unbind;
import org.bulatnig.smpp.util.WrongParameterException;

/**
 * Comment here.
 * User: Bulat Nigmatullin
 * Date: 26.06.2008
 * Time: 7:04:43
 */
public class UnbindTest {

    // Used for backward compatibility (IDEs, Ant and JUnit 3 text runner)

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(UnbindTest.class);
    }

    @Test
    public void bytesToObject() throws WrongParameterException, PDUException {
        SmppByteBuffer sbb = new SmppByteBuffer();
        sbb.appendInt(16L);
        sbb.appendInt(6L);
        sbb.appendInt(88);
        sbb.appendInt(987654321L);
        Unbind u = new Unbind(sbb.array());
        assertEquals(16L, u.getCommandLength());
        assertEquals(CommandId.UNBIND, u.getCommandId());
        assertEquals(6L, u.getCommandId().getValue());
        assertEquals(CommandStatus.ESME_RTHROTTLED, u.getCommandStatus());
        assertEquals(88L, u.getCommandStatus().getValue());
        assertEquals(987654321L, u.getSequenceNumber());
    }

    @Test
    public void objectToBytes() throws PDUException {
        Unbind u = new Unbind();
        u.setCommandStatus(CommandStatus.ESME_RSUBMITFAIL);
        u.setSequenceNumber(0);
        assertEquals("00000010000000060000004500000000", new SmppByteBuffer(u.getBytes()).getHexDump());
    }

}
