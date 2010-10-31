package org.bulatnig.smpp.domain.tlv;

import junit.framework.JUnit4TestAdapter;
import org.bulatnig.smpp.pdu.MessageState;
import org.bulatnig.smpp.pdu.tlv.MessageStateTlv;
import org.bulatnig.smpp.pdu.tlv.ParameterTag;
import org.bulatnig.smpp.pdu.tlv.TLVException;
import org.bulatnig.smpp.pdu.tlv.TLVNotFoundException;
import org.bulatnig.smpp.util.SmppByteBuffer;
import org.bulatnig.smpp.util.WrongParameterException;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class MessageStateTLVTest {
	
	// Used for backward compatibility (IDEs, Ant and JUnit 3 text runner)
	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(MessageStateTLVTest.class);
	}
	
	@Test
	public void testMSConstructor1() throws TLVException, WrongParameterException {
		SmppByteBuffer bb = new SmppByteBuffer();
		bb.appendShort(0x0427);
		bb.appendShort(0x0001);
		bb.appendByte((byte)0x08);
		MessageStateTlv das = new MessageStateTlv(bb.getBuffer());
		assertEquals(ParameterTag.MESSAGE_STATE, das.getTag());
		assertEquals(5, das.getBytes().length);
		assertEquals(MessageState.REJECTED, das.getValue());
		assertEquals("0427000108", new SmppByteBuffer(das.getBytes()).getHexDump());
	}

    @Test(expected = TLVNotFoundException.class)
	public void testMSConstructor2() throws TLVException, WrongParameterException {
		SmppByteBuffer bb = new SmppByteBuffer();
		bb.appendShort(0x0000);
		bb.appendShort(0x0002);
		bb.appendByte((byte)0x55);
		new MessageStateTlv(bb.getBuffer());
	}
	
	@Test(expected= TLVException.class)
	public void testMSConstructor3() throws TLVException, WrongParameterException {
		SmppByteBuffer bb = new SmppByteBuffer();
		bb.appendShort(0x0427);
		bb.appendShort(0x0001);
		bb.appendShort(0x0003);
		new MessageStateTlv(bb.getBuffer());
	}
	
	@Test
	public void testMSConstructor4() throws TLVException, WrongParameterException {
		MessageStateTlv das = new MessageStateTlv(MessageState.REJECTED);
		assertEquals(ParameterTag.MESSAGE_STATE, das.getTag());
		assertEquals(5, das.getBytes().length);
		assertEquals(MessageState.REJECTED, das.getValue());
		assertEquals("0427000108", new SmppByteBuffer(das.getBytes()).getHexDump());
	}
	
	@Test(expected= TLVException.class)
	public void testSASConstructor5() throws TLVException, WrongParameterException {
		SmppByteBuffer bb = new SmppByteBuffer();
		bb.appendShort(0x0427);
		bb.appendShort(0x0002);
		bb.appendShort(0x0002);
		new MessageStateTlv(bb.getBuffer());
	}
	
	@Test(expected=ClassCastException.class)
	public void testMSConstructor6() throws TLVException, WrongParameterException {
		SmppByteBuffer bb = new SmppByteBuffer();
		bb.appendShort(0x0006);
		bb.appendShort(0x0001);
		bb.appendByte((byte)0x00);
		new MessageStateTlv(bb.getBuffer());
	}
	
	@Test(expected=TLVException.class)
	public void testMSConstructor7() throws WrongParameterException, TLVException {
		SmppByteBuffer bb = new SmppByteBuffer();
		bb.appendShort(0x0427);
		bb.appendShort(0x0001);
		bb.appendByte((byte)0x12);
		new MessageStateTlv(bb.getBuffer());
	}

}
