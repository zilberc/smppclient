package org.bulatnig.smpp.domain.tlv;

import junit.framework.JUnit4TestAdapter;
import static org.junit.Assert.assertEquals;

import org.bulatnig.smpp.util.SmppByteBuffer;
import org.junit.Test;
import org.bulatnig.smpp.pdu.tlv.*;
import org.bulatnig.smpp.util.WrongParameterException;

public class UssdServiceOpTest {
	
	// Used for backward compatibility (IDEs, Ant and JUnit 3 text runner)
	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(UssdServiceOpTest.class);
	}
	
	@Test
	public void testUSOConstructor1() throws TLVException, WrongParameterException {
		SmppByteBuffer bb = new SmppByteBuffer();
		bb.appendShort(0x0501);
		bb.appendShort(0x0001);
		bb.appendByte((byte)0x12);
		UssdServiceOp uso = new UssdServiceOp(bb.getBuffer());
		assertEquals(ParameterTag.USSD_SERVICE_OP, uso.getTag());
		assertEquals(5, uso.getBytes().length);
		assertEquals(ServiceOperation.USSR_CONFIRM, uso.getValue());
		assertEquals(18, uso.getIntValue());
		assertEquals("0501000112", new SmppByteBuffer(uso.getBytes()).getHexDump());
	}

    @Test(expected = TLVNotFoundException.class)
	public void testUSOConstructor2() throws TLVException, WrongParameterException {
		SmppByteBuffer bb = new SmppByteBuffer();
		bb.appendShort(0x0000);
		bb.appendShort(0x0002);
		bb.appendByte((byte)0x55);
		new UssdServiceOp(bb.getBuffer());
	}
	
	@Test(expected= TLVException.class)
	public void testUSOConstructor3() throws TLVException, WrongParameterException {
		SmppByteBuffer bb = new SmppByteBuffer();
		bb.appendShort(0x0501);
		bb.appendShort(0x0001);
		bb.appendShort(0x0003);
		new UssdServiceOp(bb.getBuffer());
	}
	
	@Test
	public void testUSOConstructor4() throws TLVException, WrongParameterException {
		UssdServiceOp uso = new UssdServiceOp(ServiceOperation.USSR_CONFIRM);
		assertEquals(ParameterTag.USSD_SERVICE_OP, uso.getTag());
		assertEquals(5, uso.getBytes().length);
		assertEquals(ServiceOperation.USSR_CONFIRM, uso.getValue());
		assertEquals(18, uso.getIntValue());
		assertEquals("0501000112", new SmppByteBuffer(uso.getBytes()).getHexDump());
	}
	
	@Test(expected= TLVException.class)
	public void testSASConstructor5() throws TLVException, WrongParameterException {
		SmppByteBuffer bb = new SmppByteBuffer();
		bb.appendShort(0x0501);
		bb.appendShort(0x0001);
		bb.appendShort(0x0002);
		new UssdServiceOp(bb.getBuffer());
	}
	
	@Test(expected=ClassCastException.class)
	public void testUSOConstructor6() throws WrongParameterException, TLVException {
		SmppByteBuffer bb = new SmppByteBuffer();
		bb.appendShort(0x0006);
		bb.appendShort(0x0001);
		bb.appendByte((byte)0x00);
		new UssdServiceOp(bb.getBuffer());
	}
	
	public void testUSOConstructor7() throws TLVException, WrongParameterException {
		SmppByteBuffer bb = new SmppByteBuffer();
		bb.appendShort(0x0501);
		bb.appendShort(0x0001);
		bb.appendByte((byte)0x08);
		UssdServiceOp uso = new UssdServiceOp(bb.getBuffer());
		assertEquals(ParameterTag.USSD_SERVICE_OP, uso.getTag());
		assertEquals(5, uso.getBytes().length);
		assertEquals(ServiceOperation.RESERVED, uso.getValue());
		assertEquals(8, uso.getIntValue());
		assertEquals("0501000108", new SmppByteBuffer(uso.getBytes()).getHexDump());
	}

}
