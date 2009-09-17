package junit4.smpp.domain.tlv;

import junit.framework.JUnit4TestAdapter;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.bulatnig.smpp.pdu.tlv.AlertOnMessageDelivery;
import org.bulatnig.smpp.pdu.tlv.ParameterTag;
import org.bulatnig.smpp.pdu.tlv.TLVException;
import org.bulatnig.smpp.pdu.tlv.TLVNotFoundException;
import org.bulatnig.smpp.util.SMPPByteBuffer;
import org.bulatnig.smpp.util.WrongParameterException;

public class AlertOnMessageDeliveryTest {

    // Used for backward compatibility (IDEs, Ant and JUnit 3 text runner)
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(AlertOnMessageDeliveryTest.class);
    }

    @Test
    public void testAOMDConstructor1() throws WrongParameterException, TLVException {
        SMPPByteBuffer bb = new SMPPByteBuffer();
        bb.appendShort(0x130c);
        bb.appendShort(0x0000);
        new AlertOnMessageDelivery(bb.getBuffer());
        AlertOnMessageDelivery aomd = new AlertOnMessageDelivery(bb.getBuffer());
        assertEquals(ParameterTag.ALERT_ON_MESSAGE_DELIVERY, aomd.getTag());
        assertEquals(4, aomd.getBytes().length);
        assertEquals("130c0000", new SMPPByteBuffer(aomd.getBytes()).getHexDump());
    }

    @Test(expected = TLVNotFoundException.class)
    public void testAOMDConstructor2() throws WrongParameterException, TLVException {
        SMPPByteBuffer bb = new SMPPByteBuffer();
        bb.appendShort(0x0000);
        bb.appendShort(0x0000);
        new AlertOnMessageDelivery(bb.getBuffer());
    }

    @Test(expected = TLVException.class)
    public void testAOMDConstructor3() throws WrongParameterException, TLVException {
        SMPPByteBuffer bb = new SMPPByteBuffer();
        bb.appendShort(0x130c);
        bb.appendShort(0x0001);
        bb.appendShort(0x1111);
        new AlertOnMessageDelivery(bb.getBuffer());
    }

    @Test
    public void testAOMDConstructor4() throws WrongParameterException, TLVException {
        AlertOnMessageDelivery aomd = new AlertOnMessageDelivery();
        assertEquals(ParameterTag.ALERT_ON_MESSAGE_DELIVERY, aomd.getTag());
        assertEquals(4, aomd.getBytes().length);
        assertEquals("130c0000", new SMPPByteBuffer(aomd.getBytes()).getHexDump());
    }

    @Test(expected = TLVException.class)
    public void testAOMDConstructor5() throws WrongParameterException, TLVException {
        SMPPByteBuffer bb = new SMPPByteBuffer();
        bb.appendShort(0x130c);
        bb.appendShort(0x0001);
        bb.appendByte((byte) 0x11);
        new AlertOnMessageDelivery(bb.getBuffer());
    }

    @Test(expected = ClassCastException.class)
    public void testAOMDConstructor6() throws WrongParameterException, TLVException {
        SMPPByteBuffer bb = new SMPPByteBuffer();
        bb.appendShort(0x0005);
        bb.appendShort(0x0000);
        new AlertOnMessageDelivery(bb.getBuffer());
    }

}
