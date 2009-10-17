package org.bulatnig.smpp.domain.pdu;

import junit.framework.JUnit4TestAdapter;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.bulatnig.smpp.pdu.CommandId;
import org.bulatnig.smpp.pdu.CommandStatus;
import org.bulatnig.smpp.pdu.GenericNack;
import org.bulatnig.smpp.pdu.PDUException;
import org.bulatnig.smpp.util.SMPPByteBuffer;
import org.bulatnig.smpp.util.WrongParameterException;

/**
 * Comment here.
 * User: Bulat Nigmatullin
 * Date: 26.06.2008
 * Time: 7:16:52
 */
public class GenericNackTest {

    // Used for backward compatibility (IDEs, Ant and JUnit 3 text runner)

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(GenericNackTest.class);
    }

    @Test
    public void bytesToObject() throws WrongParameterException, PDUException {
        SMPPByteBuffer sbb = new SMPPByteBuffer();
        sbb.appendInt(16L);
        sbb.appendInt(2147483648L);
        sbb.appendInt(88);
        sbb.appendInt(987654321L);
        GenericNack gn = new GenericNack(sbb.getBuffer());
        assertEquals(16L, gn.getCommandLength());
        assertEquals(CommandId.GENERIC_NACK, gn.getCommandId());
        assertEquals(2147483648L, gn.getCommandId().getValue());
        assertEquals(CommandStatus.ESME_RTHROTTLED, gn.getCommandStatus());
        assertEquals(88L, gn.getCommandStatus().getValue());
        assertEquals(987654321L, gn.getSequenceNumber());
    }

    @Test
    public void objectToBytes() throws PDUException {
        GenericNack gn = new GenericNack();
        gn.setCommandStatus(CommandStatus.ESME_RSUBMITFAIL);
        gn.setSequenceNumber(0);
        assertEquals("00000010800000000000004500000000", new SMPPByteBuffer(gn.getBytes()).getHexDump());
    }

}
