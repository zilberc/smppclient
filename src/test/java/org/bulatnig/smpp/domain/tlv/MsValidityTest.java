package org.bulatnig.smpp.domain.tlv;

import junit.framework.JUnit4TestAdapter;
import static org.junit.Assert.assertEquals;

import org.bulatnig.smpp.util.SmppByteBuffer;
import org.junit.Test;
import org.bulatnig.smpp.pdu.tlv.*;
import org.bulatnig.smpp.util.WrongParameterException;

public class MsValidityTest {
	
	// Used for backward compatibility (IDEs, Ant and JUnit 3 text runner)
	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(MsValidityTest.class);
	}
	
	@Test
	public void testMVConstructor1() throws TLVException, WrongParameterException {
		SmppByteBuffer bb = new SmppByteBuffer();
		bb.appendShort(0x1204);
		bb.appendShort(0x0001);
		bb.appendByte((byte)0x02);
		MsValidity mv = new MsValidity(bb.array());
		assertEquals(ParameterTag.MS_VALIDITY, mv.getTag());
		assertEquals(5, mv.getBytes().length);
		assertEquals(Validity.SID_BASED, mv.getValue());
		assertEquals("1204000102", new SmppByteBuffer(mv.getBytes()).getHexDump());
	}

    @Test(expected = TLVNotFoundException.class)
	public void testMVConstructor2() throws TLVException, WrongParameterException {
		SmppByteBuffer bb = new SmppByteBuffer();
		bb.appendShort(0x0000);
		bb.appendShort(0x0002);
		bb.appendByte((byte)0x55);
		new MsValidity(bb.array());
	}
	
	@Test(expected= TLVException.class)
	public void testMVConstructor3() throws WrongParameterException, TLVException {
		SmppByteBuffer bb = new SmppByteBuffer();
		bb.appendShort(0x1204);
		bb.appendShort(0x0001);
		bb.appendShort(0x0003);
		new MsValidity(bb.array());
	}
	
	@Test
	public void testMVConstructor4() throws TLVException, WrongParameterException {
		MsValidity mv = new MsValidity(Validity.SID_BASED);
		assertEquals(ParameterTag.MS_VALIDITY, mv.getTag());
		assertEquals(5, mv.getBytes().length);
		assertEquals(Validity.SID_BASED, mv.getValue());
		assertEquals("1204000102", new SmppByteBuffer(mv.getBytes()).getHexDump());
	}
	
	@Test(expected= TLVException.class)
	public void testSASConstructor5() throws TLVException, WrongParameterException {
		SmppByteBuffer bb = new SmppByteBuffer();
		bb.appendShort(0x1204);
		bb.appendShort(0x0001);
		bb.appendShort(0x0002);
		new MsValidity(bb.array());
	}
	
	@Test(expected=ClassCastException.class)
	public void testMVConstructor6() throws TLVException, WrongParameterException {
		SmppByteBuffer bb = new SmppByteBuffer();
		bb.appendShort(0x0006);
		bb.appendShort(0x0001);
		bb.appendByte((byte)0x00);
		new MsValidity(bb.array());
	}
	
	@Test
	public void testMVConstructor7() throws TLVException, WrongParameterException {
		SmppByteBuffer bb = new SmppByteBuffer();
		bb.appendShort(0x1204);
		bb.appendShort(0x0001);
		bb.appendByte((byte)0x12);
		MsValidity mv = new MsValidity(bb.array());
		assertEquals(ParameterTag.MS_VALIDITY, mv.getTag());
		assertEquals(5, mv.getBytes().length);
		assertEquals(Validity.RESERVED, mv.getValue());
		assertEquals((short) 18, mv.getIntValue());
		assertEquals("1204000112", new SmppByteBuffer(mv.getBytes()).getHexDump());
	}

}
