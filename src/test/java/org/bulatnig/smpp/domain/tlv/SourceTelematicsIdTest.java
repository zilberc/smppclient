package org.bulatnig.smpp.domain.tlv;

import junit.framework.JUnit4TestAdapter;
import static org.junit.Assert.assertEquals;

import org.bulatnig.smpp.util.SmppByteBuffer;
import org.junit.Test;
import org.bulatnig.smpp.pdu.tlv.ParameterTag;
import org.bulatnig.smpp.pdu.tlv.SourceTelematicsId;
import org.bulatnig.smpp.pdu.tlv.TLVException;
import org.bulatnig.smpp.pdu.tlv.TLVNotFoundException;
import org.bulatnig.smpp.util.WrongParameterException;

public class SourceTelematicsIdTest {

	// Used for backward compatibility (IDEs, Ant and JUnit 3 text runner)
	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(SourceTelematicsIdTest.class);
	}
	
	@Test
	public void testSTIConstructor1() throws TLVException, WrongParameterException {
		SmppByteBuffer bb = new SmppByteBuffer();
		bb.appendShort(0x0010);
		bb.appendShort(0x0001);
		bb.appendByte((byte)0x70);
		SourceTelematicsId sti = new SourceTelematicsId(bb.array());
		assertEquals(ParameterTag.SOURCE_TELEMATICS_ID, sti.getTag());
		assertEquals(5, sti.getBytes().length);
		assertEquals((short)112, sti.getValue());
		assertEquals("0010000170", new SmppByteBuffer(sti.getBytes()).getHexDump());
	}

    @Test(expected = TLVNotFoundException.class)
	public void testSTIConstructor2() throws WrongParameterException, TLVException {
		SmppByteBuffer bb = new SmppByteBuffer();
		bb.appendShort(0x0000);
		bb.appendShort(0x0002);
		bb.appendByte((byte)0x1111);
		new SourceTelematicsId(bb.array());
	}
	
	@Test(expected= TLVException.class)
	public void testSTIConstructor3() throws TLVException, WrongParameterException {
		SmppByteBuffer bb = new SmppByteBuffer();
		bb.appendShort(0x0010);
		bb.appendShort(0x0002);
		bb.appendShort(0x1111);
		new SourceTelematicsId(bb.array());
	}
	
	@Test
	public void testSTIConstructor4() throws TLVException, WrongParameterException {
		SourceTelematicsId sti = new SourceTelematicsId((byte)112);
		assertEquals(ParameterTag.SOURCE_TELEMATICS_ID, sti.getTag());
		assertEquals(5, sti.getBytes().length);
		assertEquals((short)112, sti.getValue());
		assertEquals("0010000170", new SmppByteBuffer(sti.getBytes()).getHexDump());
	}
	
	@Test(expected= TLVException.class)
	public void testSTIConstructor5() throws TLVException, WrongParameterException {
		SmppByteBuffer bb = new SmppByteBuffer();
		bb.appendShort(0x0010);
		bb.appendShort(0x0001);
		bb.appendShort(0x0001);
		new SourceTelematicsId(bb.array());
	}
	
	@Test(expected=ClassCastException.class)
	public void testSTIConstructor6() throws TLVException, WrongParameterException {
		SmppByteBuffer bb = new SmppByteBuffer();
		bb.appendShort(0x0005);
		bb.appendShort(0x0001);
		bb.appendByte((byte)0x0f);
		new SourceTelematicsId(bb.array());
	}

    @Test(expected = IllegalArgumentException.class)
    public void testSTIConstructor7() throws TLVException {
        new SourceTelematicsId((short)32000).getBytes();
    }
	
}
