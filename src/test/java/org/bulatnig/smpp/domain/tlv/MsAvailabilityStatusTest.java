package org.bulatnig.smpp.domain.tlv;

import junit.framework.JUnit4TestAdapter;
import static org.junit.Assert.assertEquals;

import org.bulatnig.smpp.util.SmppByteBuffer;
import org.junit.Test;
import org.bulatnig.smpp.pdu.tlv.*;
import org.bulatnig.smpp.util.WrongParameterException;

public class MsAvailabilityStatusTest {
	
	// Used for backward compatibility (IDEs, Ant and JUnit 3 text runner)
	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(MsAvailabilityStatusTest.class);
	}
	
	@Test
	public void testMASConstructor1() throws TLVException, WrongParameterException {
		SmppByteBuffer bb = new SmppByteBuffer();
		bb.appendShort(0x0422);
		bb.appendShort(0x0001);
		bb.appendByte((byte)0x01);
		MsAvailabilityStatus mas = new MsAvailabilityStatus(bb.getBuffer());
		assertEquals(ParameterTag.MS_AVAILABILITY_STATUS, mas.getTag());
		assertEquals(5, mas.getBytes().length);
		assertEquals(AvailabilityStatus.DENIED, mas.getValue());
		assertEquals("0422000101", new SmppByteBuffer(mas.getBytes()).getHexDump());
	}

    @Test(expected = TLVNotFoundException.class)
	public void testMASConstructor2() throws TLVException, WrongParameterException {
		SmppByteBuffer bb = new SmppByteBuffer();
		bb.appendShort(0x0000);
		bb.appendShort(0x0002);
		bb.appendByte((byte)0x55);
		new MsAvailabilityStatus(bb.getBuffer());
	}
	
	@Test(expected= TLVException.class)
	public void testMASConstructor3() throws TLVException, WrongParameterException {
		SmppByteBuffer bb = new SmppByteBuffer();
		bb.appendShort(0x0422);
		bb.appendShort(0x0001);
		bb.appendShort(0x0001);
		new MsAvailabilityStatus(bb.getBuffer());
	}
	
	@Test
	public void testMASConstructor4() throws WrongParameterException, TLVException {
		MsAvailabilityStatus mas = new MsAvailabilityStatus(AvailabilityStatus.DENIED);
		assertEquals(ParameterTag.MS_AVAILABILITY_STATUS, mas.getTag());
		assertEquals(5, mas.getBytes().length);
		assertEquals(AvailabilityStatus.DENIED, mas.getValue());
		assertEquals("0422000101", new SmppByteBuffer(mas.getBytes()).getHexDump());
	}
	
	@Test(expected= TLVException.class)
	public void testSASConstructor5() throws TLVException, WrongParameterException {
		SmppByteBuffer bb = new SmppByteBuffer();
		bb.appendShort(0x0422);
		bb.appendShort(0x0001);
		bb.appendShort(0x0002);
		new MsAvailabilityStatus(bb.getBuffer());
	}
	
	@Test(expected=ClassCastException.class)
	public void testMASConstructor6() throws TLVException, WrongParameterException {
		SmppByteBuffer bb = new SmppByteBuffer();
		bb.appendShort(0x0006);
		bb.appendShort(0x0001);
		bb.appendByte((byte)0x00);
		new MsAvailabilityStatus(bb.getBuffer());
	}
	
	@Test
	public void testMASConstructor7() throws TLVException, WrongParameterException {
		SmppByteBuffer bb = new SmppByteBuffer();
		bb.appendShort(0x0422);
		bb.appendShort(0x0001);
		bb.appendByte((byte)0x12);
		MsAvailabilityStatus mas = new MsAvailabilityStatus(bb.getBuffer());
		assertEquals(ParameterTag.MS_AVAILABILITY_STATUS, mas.getTag());
		assertEquals(5, mas.getBytes().length);
		assertEquals(AvailabilityStatus.RESERVED, mas.getValue());
		assertEquals((short) 18, mas.getIntValue());
		assertEquals("0422000112", new SmppByteBuffer(mas.getBytes()).getHexDump());
	}

}
