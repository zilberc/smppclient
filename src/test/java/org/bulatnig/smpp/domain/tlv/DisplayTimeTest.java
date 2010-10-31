package org.bulatnig.smpp.domain.tlv;

import junit.framework.JUnit4TestAdapter;
import static org.junit.Assert.assertEquals;

import org.bulatnig.smpp.util.SmppByteBuffer;
import org.junit.Test;
import org.bulatnig.smpp.pdu.tlv.DisplayTime;
import org.bulatnig.smpp.pdu.tlv.ParameterTag;
import org.bulatnig.smpp.pdu.tlv.TLVException;
import org.bulatnig.smpp.pdu.tlv.TLVNotFoundException;
import org.bulatnig.smpp.util.WrongParameterException;

public class DisplayTimeTest {
	
	// Used for backward compatibility (IDEs, Ant and JUnit 3 text runner)
	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(DisplayTimeTest.class);
	}
	
	@Test
	public void testDTConstructor1() throws TLVException, WrongParameterException {
		SmppByteBuffer bb = new SmppByteBuffer();
		bb.appendShort(0x1201);
		bb.appendShort(0x0001);
		bb.appendByte((byte)0x70);
		DisplayTime dt = new DisplayTime(bb.getBuffer());
		assertEquals(ParameterTag.DISPLAY_TIME, dt.getTag());
		assertEquals(5, dt.getBytes().length);
		assertEquals((short)112, dt.getValue());
		assertEquals("1201000170", new SmppByteBuffer(dt.getBytes()).getHexDump());
	}

    @Test(expected = TLVNotFoundException.class)
	public void testDTConstructor2() throws TLVException, WrongParameterException {
		SmppByteBuffer bb = new SmppByteBuffer();
		bb.appendShort(0x0000);
		bb.appendShort(0x0002);
		bb.appendByte((byte)0x1111);
		new DisplayTime(bb.getBuffer());
	}
	
	@Test(expected= TLVException.class)
	public void testDTConstructor3() throws TLVException, WrongParameterException {
		SmppByteBuffer bb = new SmppByteBuffer();
		bb.appendShort(0x1201);
		bb.appendShort(0x0002);
		bb.appendShort(0x1111);
		new DisplayTime(bb.getBuffer());
	}
	
	@Test
	public void testDTConstructor4() throws WrongParameterException, TLVException {
		DisplayTime dt = new DisplayTime((byte)112);
		assertEquals(ParameterTag.DISPLAY_TIME, dt.getTag());
		assertEquals(5, dt.getBytes().length);
		assertEquals((short)112, dt.getValue());
		assertEquals("1201000170", new SmppByteBuffer(dt.getBytes()).getHexDump());
	}
	
	@Test(expected= TLVException.class)
	public void testDTConstructor5() throws TLVException, WrongParameterException {
		SmppByteBuffer bb = new SmppByteBuffer();
		bb.appendShort(0x1201);
		bb.appendShort(0x0001);
		bb.appendShort(0x0001);
		new DisplayTime(bb.getBuffer());
	}
	
	@Test(expected=ClassCastException.class)
	public void testDTConstructor6() throws TLVException, WrongParameterException {
		SmppByteBuffer bb = new SmppByteBuffer();
		bb.appendShort(0x0005);
		bb.appendShort(0x0001);
		bb.appendByte((byte)0x0f);
		new DisplayTime(bb.getBuffer());
	}

}
