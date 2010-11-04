package org.bulatnig.smpp.domain.tlv;

import junit.framework.JUnit4TestAdapter;
import static org.junit.Assert.assertEquals;

import org.bulatnig.smpp.util.SmppByteBuffer;
import org.junit.Test;
import org.bulatnig.smpp.pdu.tlv.NumberOfMessages;
import org.bulatnig.smpp.pdu.tlv.ParameterTag;
import org.bulatnig.smpp.pdu.tlv.TLVException;
import org.bulatnig.smpp.pdu.tlv.TLVNotFoundException;
import org.bulatnig.smpp.util.WrongParameterException;

public class NumberOfMessagesTest {
	
	// Used for backward compatibility (IDEs, Ant and JUnit 3 text runner)
	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(NumberOfMessagesTest.class);
	}
	
	@Test
	public void testNOMConstructor1() throws TLVException, WrongParameterException {
		SmppByteBuffer bb = new SmppByteBuffer();
		bb.appendShort(0x0304);
		bb.appendShort(0x0001);
		bb.appendByte((byte)0x70);
		NumberOfMessages nom = new NumberOfMessages(bb.array());
		assertEquals(ParameterTag.NUMBER_OF_MESSAGES, nom.getTag());
		assertEquals(5, nom.getBytes().length);
		assertEquals((short)112, nom.getValue());
		assertEquals("0304000170", new SmppByteBuffer(nom.getBytes()).getHexDump());
	}

    @Test(expected = TLVNotFoundException.class)
	public void testNOMConstructor2() throws TLVException, WrongParameterException {
		SmppByteBuffer bb = new SmppByteBuffer();
		bb.appendShort(0x0000);
		bb.appendShort(0x0002);
		bb.appendByte((byte)0x1111);
		new NumberOfMessages(bb.array());
	}
	
	@Test(expected= TLVException.class)
	public void testNOMConstructor3() throws TLVException, WrongParameterException {
		SmppByteBuffer bb = new SmppByteBuffer();
		bb.appendShort(0x0304);
		bb.appendShort(0x0002);
		bb.appendShort(0x1111);
		new NumberOfMessages(bb.array());
	}
	
	@Test
	public void testNOMConstructor4() throws TLVException, WrongParameterException {
		NumberOfMessages nom = new NumberOfMessages((byte)112);
		assertEquals(ParameterTag.NUMBER_OF_MESSAGES, nom.getTag());
		assertEquals(5L, nom.getBytes().length);
		assertEquals((short)112, nom.getValue());
		assertEquals("0304000170", new SmppByteBuffer(nom.getBytes()).getHexDump());
	}
	
	@Test(expected= TLVException.class)
	public void testNOMConstructor5() throws WrongParameterException, TLVException {
		SmppByteBuffer bb = new SmppByteBuffer();
		bb.appendShort(0x0304);
		bb.appendShort(0x0001);
		bb.appendShort(0x0001);
		new NumberOfMessages(bb.array());
	}
	
	@Test(expected=ClassCastException.class)
	public void testNOMConstructor6() throws TLVException, WrongParameterException {
		SmppByteBuffer bb = new SmppByteBuffer();
		bb.appendShort(0x0005);
		bb.appendShort(0x0001);
		bb.appendByte((byte)0x0f);
		new NumberOfMessages(bb.array());
	}

}
