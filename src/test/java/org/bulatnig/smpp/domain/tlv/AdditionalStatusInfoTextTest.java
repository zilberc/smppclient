package org.bulatnig.smpp.domain.tlv;

import junit.framework.JUnit4TestAdapter;
import static org.junit.Assert.assertEquals;

import org.bulatnig.smpp.util.SmppByteBuffer;
import org.junit.Test;
import org.bulatnig.smpp.pdu.tlv.AdditionalStatusInfoText;
import org.bulatnig.smpp.pdu.tlv.ParameterTag;
import org.bulatnig.smpp.pdu.tlv.TLVException;
import org.bulatnig.smpp.pdu.tlv.TLVNotFoundException;
import org.bulatnig.smpp.util.WrongParameterException;

public class AdditionalStatusInfoTextTest {

    // Used for backward compatibility (IDEs, Ant and JUnit 3 text runner)
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(AdditionalStatusInfoTextTest.class);
    }

    @Test
    public void testASITConstructor1() throws WrongParameterException, TLVException {
        SmppByteBuffer bb = new SmppByteBuffer();
        bb.appendShort(0x001d);
        bb.appendShort(0x0004);
        bb.appendCString("smx");
        AdditionalStatusInfoText asit = new AdditionalStatusInfoText(bb.array());
        assertEquals(ParameterTag.ADDITIONAL_STATUS_INFO_TEXT, asit.getTag());
        assertEquals(8, asit.getBytes().length);
        assertEquals("smx", asit.getValue());
        assertEquals("001d0004736d7800", new SmppByteBuffer(asit.getBytes()).getHexDump());
    }

    @Test(expected = TLVNotFoundException.class)
    public void testASITConstructor2() throws WrongParameterException, TLVException {
        SmppByteBuffer bb = new SmppByteBuffer();
        bb.appendShort(0x0000);
        bb.appendShort(0x0001);
        bb.appendByte((byte) 0x11);
        new AdditionalStatusInfoText(bb.array());
    }

    @Test(expected = TLVException.class)
    public void testASITConstructor3() throws WrongParameterException, TLVException {
        SmppByteBuffer bb = new SmppByteBuffer();
        bb.appendShort(0x001d);
        bb.appendShort(0x0002);
        bb.appendByte((byte) 0x11);
        new AdditionalStatusInfoText(bb.array());
    }

    @Test
    public void testASITConstructor4() throws WrongParameterException, TLVException {
        AdditionalStatusInfoText asit = new AdditionalStatusInfoText("smx");
        assertEquals(ParameterTag.ADDITIONAL_STATUS_INFO_TEXT, asit.getTag());
        assertEquals(8, asit.getBytes().length);
        assertEquals("smx", asit.getValue());
        assertEquals("001d0004736d7800", new SmppByteBuffer(asit.getBytes()).getHexDump());
    }

    @Test(expected = ClassCastException.class)
    public void testASITConstructor5() throws WrongParameterException, TLVException {
        SmppByteBuffer bb = new SmppByteBuffer();
        bb.appendShort(0x0005);
        bb.appendShort(0x0001);
        bb.appendByte((byte) 0x11);
        new AdditionalStatusInfoText(bb.array());
    }

    @Test(expected = TLVException.class)
    public void testASITConstructor6() throws TLVException {
        String s = "";
        for (int i = 0; i < 256; i++) {
            s += Math.round(Math.random() * 9);
        }
        new AdditionalStatusInfoText(s);
    }

}
