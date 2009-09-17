package junit4.smpp.domain.pdu;

import junit.framework.JUnit4TestAdapter;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.bulatnig.smpp.pdu.CommandId;
import org.bulatnig.smpp.pdu.CommandStatus;
import org.bulatnig.smpp.pdu.PDUException;
import org.bulatnig.smpp.pdu.UnbindResp;
import org.bulatnig.smpp.util.SMPPByteBuffer;
import org.bulatnig.smpp.util.WrongParameterException;

/**
 * Comment here.
 * User: Bulat Nigmatullin
 * Date: 26.06.2008
 * Time: 7:11:58                                     0
 */
public class UnbindRespTest {

    // Used for backward compatibility (IDEs, Ant and JUnit 3 text runner)

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(UnbindRespTest.class);
    }

    @Test
    public void bytesToObject() throws WrongParameterException, PDUException {
        SMPPByteBuffer sbb = new SMPPByteBuffer();
        sbb.appendInt(16L);
        sbb.appendInt(2147483654L);
        sbb.appendInt(0L);
        sbb.appendInt(1111111111L);
        UnbindResp ur = new UnbindResp(sbb.getBuffer());
        assertEquals(16L, ur.getCommandLength());
        assertEquals(CommandId.UNBIND_RESP, ur.getCommandId());
        assertEquals(2147483654L, ur.getCommandId().getValue());
        assertEquals(CommandStatus.ESME_ROK, ur.getCommandStatus());
        assertEquals(0L, ur.getCommandStatus().getValue());
        assertEquals(1111111111L, ur.getSequenceNumber());
    }

    @Test
    public void objectToBytes() throws PDUException {
        UnbindResp ur = new UnbindResp();
        ur.setCommandStatus(CommandStatus.ESME_RINVDLNAME);
        ur.setSequenceNumber(4294967295L);
        assertEquals("000000108000000600000034ffffffff", new SMPPByteBuffer(ur.getBytes()).getHexDump());
    }

}
