package org.bulatnig.smpp.domain.tlv;

import junit.framework.JUnit4TestAdapter;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.bulatnig.smpp.pdu.tlv.ParameterTag;
import org.bulatnig.smpp.pdu.tlv.ParameterTagNotFoundException;
import org.bulatnig.smpp.pdu.tlv.TLVHelper;
import org.bulatnig.smpp.pdu.tlv.TLVHelperImpl;

/**
 * Comments here.
 * User: Bulat Nigmatullin
 * Date: 30.10.2008
 * Time: 15:37:40
 */
public class TLVHelperImplTest {

    // Used for backward compatibility (IDEs, Ant and JUnit 3 text runner)
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(TLVHelperImplTest.class);
    }

    @Test
    public void testTLVHelper1() throws ParameterTagNotFoundException {
        TLVHelper helper = TLVHelperImpl.INSTANCE;
        assertEquals(ParameterTag.DPF_RESULT, helper.getParameterTag(1056));
    }

    @Test(expected = ParameterTagNotFoundException.class)
    public void testTLVHelper2() throws ParameterTagNotFoundException {
        TLVHelper helper = TLVHelperImpl.INSTANCE;
        helper.getParameterTag(100000000);
    }

    @Test(expected = ParameterTagNotFoundException.class)
    public void testTLVHelper3() throws ParameterTagNotFoundException {
        TLVHelper helper = TLVHelperImpl.INSTANCE;
        helper.getParameterTag(-100000000);
    }

    @Test(expected = ParameterTagNotFoundException.class)
    public void testTLVHelper4() throws ParameterTagNotFoundException {
        TLVHelper helper = TLVHelperImpl.INSTANCE;
        helper.getParameterTag(0);
    }
}
