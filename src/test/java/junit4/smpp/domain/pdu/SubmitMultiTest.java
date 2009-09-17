package junit4.smpp.domain.pdu;

import junit.framework.JUnit4TestAdapter;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.bulatnig.smpp.pdu.*;
import org.bulatnig.smpp.util.SMPPByteBuffer;

import java.util.ArrayList;
import java.util.List;

/**
 * Comment here.
 * User: Bulat Nigmatullin
 * Date: 26.07.2008
 * Time: 21:56:53
 */
public class SubmitMultiTest {

    // Used for backward compatibility (IDEs, Ant and JUnit 3 text runner)

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(SubmitMultiTest.class);
    }

    @Test
    public void objectToBytes() throws PDUException {
        SubmitMulti sm = new SubmitMulti();
        sm.setSourceAddrNpi(NPI.ISDN);
        sm.setSourceAddr("751");
        List<DestAddress> dests = new ArrayList<DestAddress>();
        DestAddress da = new DestAddress();
        da.setDestFlag((short) 1);
        SMEAddress smea = new SMEAddress();
        smea.setDestAddrTon(TON.INTERNATIONAL);
        smea.setDestAddrNpi(NPI.ISDN);
        smea.setDestinationAddr("79163979712");
        da.setSmeAddress(smea);
        dests.add(da);
        sm.setDestAddresses(dests);
        sm.setNumberOfDests((short) dests.size());
        sm.setShortMessage("bulat");
        assertEquals("0000003600000021000000000000000000000137353100010101013739313633393739373132000000000000000000000562756c6174",
                new SMPPByteBuffer(sm.getBytes()).getHexDump());
    }

}
