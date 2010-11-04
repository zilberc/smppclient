package org.bulatnig.smpp.domain.tlv;

import junit.framework.JUnit4TestAdapter;
import static org.junit.Assert.assertEquals;

import org.bulatnig.smpp.util.SmppByteBuffer;
import org.junit.Test;
import org.bulatnig.smpp.pdu.tlv.DestTelematicsId;
import org.bulatnig.smpp.pdu.tlv.ParameterTag;
import org.bulatnig.smpp.pdu.tlv.TLVException;
import org.bulatnig.smpp.pdu.tlv.TLVNotFoundException;
import org.bulatnig.smpp.util.WrongParameterException;

public class DestTelematicsIdTest {

    // Used for backward compatibility (IDEs, Ant and JUnit 3 text runner)
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(DestTelematicsIdTest.class);
    }

    @Test
    public void testDTIConstructor1() throws WrongParameterException, TLVException {
        SmppByteBuffer bb = new SmppByteBuffer();
        bb.appendShort(0x0008);
        bb.appendShort(0x0002);
        bb.appendShort(0x1111);
        DestTelematicsId dti = new DestTelematicsId(bb.array());
        assertEquals(ParameterTag.DEST_TELEMATICS_ID, dti.getTag());
        assertEquals(6, dti.getBytes().length);
        assertEquals(4369, dti.getValue());
        assertEquals("000800021111", new SmppByteBuffer(dti.getBytes()).getHexDump());
    }

    @Test(expected = TLVNotFoundException.class)
    public void testDTIConstructor2() throws WrongParameterException, TLVException {
        SmppByteBuffer bb = new SmppByteBuffer();
        bb.appendShort(0x0000);
        bb.appendShort(0x0002);
        bb.appendByte((byte) 0x1111);
        new DestTelematicsId(bb.array());
    }

    @Test(expected = TLVException.class)
    public void testDTIConstructor3() throws WrongParameterException, TLVException {
        SmppByteBuffer bb = new SmppByteBuffer();
        bb.appendShort(0x0008);
        bb.appendShort(0x0001);
        bb.appendShort(0x1111);
        new DestTelematicsId(bb.array());
    }

    @Test
    public void testDTIConstructor4() throws WrongParameterException, TLVException {
        DestTelematicsId dti = new DestTelematicsId((short) 112);
        assertEquals(ParameterTag.DEST_TELEMATICS_ID, dti.getTag());
        assertEquals(6, dti.getBytes().length);
        assertEquals(112, dti.getValue());
        assertEquals("000800020070", new SmppByteBuffer(dti.getBytes()).getHexDump());
    }

    @Test(expected = TLVException.class)
    public void testDTIConstructor5() throws WrongParameterException, TLVException {
        SmppByteBuffer bb = new SmppByteBuffer();
        bb.appendShort(0x0008);
        bb.appendShort(0x0001);
        bb.appendByte((byte) 0x11);
        new DestTelematicsId(bb.array());
    }

    @Test(expected = ClassCastException.class)
    public void testDTIConstructor6() throws WrongParameterException, TLVException {
        SmppByteBuffer bb = new SmppByteBuffer();
        bb.appendShort(0x0005);
        bb.appendShort(0x0002);
        bb.appendShort(0x7fff);
        new DestTelematicsId(bb.array());
    }

    @Test(expected = TLVException.class)
    public void testDTIConstructor7() throws TLVException {
        new DestTelematicsId(1000000000).getBytes();
    }

}
