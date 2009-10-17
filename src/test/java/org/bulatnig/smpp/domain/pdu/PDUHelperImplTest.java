package org.bulatnig.smpp.domain.pdu;

import junit.framework.JUnit4TestAdapter;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.bulatnig.smpp.pdu.CommandId;
import org.bulatnig.smpp.pdu.CommandIdNotFoundException;
import org.bulatnig.smpp.pdu.PDUHelper;
import org.bulatnig.smpp.pdu.PDUHelperImpl;

/**
 * Comment here.
 * <p/>
 * User: Bulat Nigmatullin
 * Date: Nov 1, 2008
 * Time: 11:44:37 AM
 */
public class PDUHelperImplTest {

    // Used for backward compatibility (IDEs, Ant and JUnit 3 text runner)
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(PDUHelperImplTest.class);
    }

    @Test
    public void testPDUHelper1() throws CommandIdNotFoundException {
        PDUHelper helper = PDUHelperImpl.INSTANCE;
        assertEquals(CommandId.BIND_TRANSCEIVER, helper.getCommandId(9));
    }

    @Test(expected = CommandIdNotFoundException.class)
    public void testPDUHelper2() throws CommandIdNotFoundException {
        PDUHelper helper = PDUHelperImpl.INSTANCE;
        helper.getCommandId(10);
    }

    @Test(expected = CommandIdNotFoundException.class)
    public void testPDUHelper3() throws CommandIdNotFoundException {
        PDUHelper helper = PDUHelperImpl.INSTANCE;
        helper.getCommandId(-10);
    }

    @Test(expected = CommandIdNotFoundException.class)
    public void testPDUHelper4() throws CommandIdNotFoundException {
        PDUHelper helper = PDUHelperImpl.INSTANCE;
        helper.getCommandId(0);
    }

}
