package org.bulatnig.smpp.domain.tlv;

import junit.framework.JUnit4TestAdapter;
import static org.junit.Assert.assertEquals;

import org.bulatnig.smpp.util.SmppByteBuffer;
import org.junit.Test;
import org.bulatnig.smpp.pdu.tlv.CallbackNumAtag;
import org.bulatnig.smpp.pdu.tlv.ParameterTag;
import org.bulatnig.smpp.pdu.tlv.TLVException;
import org.bulatnig.smpp.pdu.tlv.TLVNotFoundException;
import org.bulatnig.smpp.util.WrongParameterException;

public class CallbackNumAtagTest {

    // Used for backward compatibility (IDEs, Ant and JUnit 3 text runner)
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(CallbackNumAtagTest.class);
    }

    @Test
    public void testCNAConstructor1() throws WrongParameterException, TLVException {
        SmppByteBuffer bb = new SmppByteBuffer();
        bb.appendShort(0x0303);
        bb.appendShort(0x0003);
        bb.appendString("smx");
        CallbackNumAtag cna = new CallbackNumAtag(bb.getBuffer());
        assertEquals(ParameterTag.CALLBACK_NUM_ATAG, cna.getTag());
        assertEquals(7, cna.getBytes().length);
        assertEquals("smx", cna.getValue());
        assertEquals("03030003736d78", new SmppByteBuffer(cna.getBytes()).getHexDump());
    }

    @Test(expected = TLVNotFoundException.class)
    public void testCNAConstructor2() throws WrongParameterException, TLVException {
        SmppByteBuffer bb = new SmppByteBuffer();
        bb.appendShort(0x0000);
        bb.appendShort(0x0001);
        bb.appendByte((byte) 0x11);
        new CallbackNumAtag(bb.getBuffer());
    }

    @Test(expected = TLVException.class)
    public void testCNAConstructor3() throws WrongParameterException, TLVException {
        SmppByteBuffer bb = new SmppByteBuffer();
        bb.appendShort(0x0303);
        bb.appendShort(0x0002);
        bb.appendByte((byte) 0x11);
        new CallbackNumAtag(bb.getBuffer());
    }

    @Test
    public void testCNAConstructor4() throws WrongParameterException, TLVException {
        CallbackNumAtag cna = new CallbackNumAtag("smx");
        assertEquals(ParameterTag.CALLBACK_NUM_ATAG, cna.getTag());
        assertEquals(7, cna.getBytes().length);
        assertEquals("smx", cna.getValue());
        assertEquals("03030003736d78", new SmppByteBuffer(cna.getBytes()).getHexDump());
    }

    @Test(expected = ClassCastException.class)
    public void testCNAConstructor5() throws WrongParameterException, TLVException {
        SmppByteBuffer bb = new SmppByteBuffer();
        bb.appendShort(0x0005);
        bb.appendShort(0x0001);
        bb.appendByte((byte) 0x11);
        new CallbackNumAtag(bb.getBuffer());
    }

    @Test(expected = TLVException.class)
    public void testCNAConstructor6() throws TLVException {
        String s = "";
        for (int i = 0; i < 66; i++) {
            s += Math.round(Math.random() * 9);
        }
        new CallbackNumAtag(s);
    }

}
