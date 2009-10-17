package org.bulatnig.smpp.domain.tlv;

import junit.framework.JUnit4TestAdapter;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.bulatnig.smpp.pdu.tlv.*;
import org.bulatnig.smpp.util.SMPPByteBuffer;
import org.bulatnig.smpp.util.WrongParameterException;

public class ItsReplyTypeTest {
	
	// Used for backward compatibility (IDEs, Ant and JUnit 3 text runner)
	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(ItsReplyTypeTest.class);
	}
	
	@Test
	public void testIRTConstructor1() throws TLVException, WrongParameterException {
		SMPPByteBuffer bb = new SMPPByteBuffer();
		bb.appendShort(0x1380);
		bb.appendShort(0x0001);
		bb.appendByte((byte)0x07);
		ItsReplyType irt = new ItsReplyType(bb.getBuffer());
		assertEquals(ParameterTag.ITS_REPLY_TYPE, irt.getTag());
		assertEquals(5, irt.getBytes().length);
		assertEquals(ReplyType.TIME, irt.getValue());
		assertEquals("1380000107", new SMPPByteBuffer(irt.getBytes()).getHexDump());
	}

    @Test(expected = TLVNotFoundException.class)
	public void testIRTConstructor2() throws TLVException, WrongParameterException {
		SMPPByteBuffer bb = new SMPPByteBuffer();
		bb.appendShort(0x0000);
		bb.appendShort(0x0002);
		bb.appendByte((byte)0x55);
		new ItsReplyType(bb.getBuffer());
	}
	
	@Test(expected= TLVException.class)
	public void testIRTConstructor3() throws TLVException, WrongParameterException {
		SMPPByteBuffer bb = new SMPPByteBuffer();
		bb.appendShort(0x1380);
		bb.appendShort(0x0001);
		bb.appendShort(0x0003);
		new ItsReplyType(bb.getBuffer());
	}
	
	@Test
	public void testIRTConstructor4() throws TLVException, WrongParameterException {
		ItsReplyType irt = new ItsReplyType(ReplyType.TIME);
		assertEquals(ParameterTag.ITS_REPLY_TYPE, irt.getTag());
		assertEquals(5L, irt.getBytes().length);
		assertEquals(ReplyType.TIME, irt.getValue());
		assertEquals("1380000107", new SMPPByteBuffer(irt.getBytes()).getHexDump());
	}
	
	@Test(expected= TLVException.class)
	public void testSASConstructor5() throws WrongParameterException, TLVException {
		SMPPByteBuffer bb = new SMPPByteBuffer();
		bb.appendShort(0x1380);
		bb.appendShort(0x0001);
		bb.appendShort(0x0007);
		new ItsReplyType(bb.getBuffer());
	}
	
	@Test(expected=ClassCastException.class)
	public void testIRTConstructor6() throws TLVException, WrongParameterException {
		SMPPByteBuffer bb = new SMPPByteBuffer();
		bb.appendShort(0x0006);
		bb.appendShort(0x0001);
		bb.appendByte((byte)0x00);
		new ItsReplyType(bb.getBuffer());
	}
	
	@Test
	public void testIRTConstructor7() throws TLVException, WrongParameterException {
		SMPPByteBuffer bb = new SMPPByteBuffer();
		bb.appendShort(0x1380);
		bb.appendShort(0x0001);
		bb.appendByte((byte)0x12);
		ItsReplyType irt = new ItsReplyType(bb.getBuffer());
		assertEquals(ParameterTag.ITS_REPLY_TYPE, irt.getTag());
		assertEquals(5, irt.getBytes().length);
		assertEquals(ReplyType.RESERVED, irt.getValue());
		assertEquals("1380000112", new SMPPByteBuffer(irt.getBytes()).getHexDump());
	}

}
