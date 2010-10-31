package org.bulatnig.smpp.domain.tlv;

import junit.framework.JUnit4TestAdapter;
import static org.junit.Assert.assertEquals;

import org.bulatnig.smpp.util.SmppByteBuffer;
import org.junit.Test;
import org.bulatnig.smpp.pdu.tlv.ParameterTag;
import org.bulatnig.smpp.pdu.tlv.SmsSignal;
import org.bulatnig.smpp.pdu.tlv.TLVException;
import org.bulatnig.smpp.pdu.tlv.TLVNotFoundException;
import org.bulatnig.smpp.util.WrongParameterException;

public class SmsSignalTest {
	
	// Used for backward compatibility (IDEs, Ant and JUnit 3 text runner)
	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(SmsSignalTest.class);
	}
	
	@Test
	public void testSSConstructor1() throws TLVException, WrongParameterException {
		SmppByteBuffer bb = new SmppByteBuffer();
		bb.appendShort(0x1203);
		bb.appendShort(0x0002);
		bb.appendShort(0x1111);
		SmsSignal ss = new SmsSignal(bb.getBuffer());
		assertEquals(ParameterTag.SMS_SIGNAL, ss.getTag());
		assertEquals(6, ss.getBytes().length);
		assertEquals(4369, ss.getValue());
		assertEquals("120300021111", new SmppByteBuffer(ss.getBytes()).getHexDump());
	}

    @Test(expected = TLVNotFoundException.class)
	public void testSSConstructor2() throws TLVException, WrongParameterException {
		SmppByteBuffer bb = new SmppByteBuffer();
		bb.appendShort(0x0000);
		bb.appendShort(0x0002);
		bb.appendByte((byte)0x1111);
		new SmsSignal(bb.getBuffer());
	}
	
	@Test(expected= TLVException.class)
	public void testSSConstructor3() throws TLVException, WrongParameterException {
		SmppByteBuffer bb = new SmppByteBuffer();
		bb.appendShort(0x1203);
		bb.appendShort(0x0001);
		bb.appendShort(0x1111);
		new SmsSignal(bb.getBuffer());
	}
	
	@Test
	public void testSSConstructor4() throws TLVException, WrongParameterException {
		SmsSignal ss = new SmsSignal((short)112);
		assertEquals(ParameterTag.SMS_SIGNAL, ss.getTag());
		assertEquals(6, ss.getBytes().length);
		assertEquals(112, ss.getValue());
		assertEquals("120300020070", new SmppByteBuffer(ss.getBytes()).getHexDump());
	}
	
	@Test(expected= TLVException.class)
	public void testSSConstructor5() throws TLVException, WrongParameterException {
		SmppByteBuffer bb = new SmppByteBuffer();
		bb.appendShort(0x1203);
		bb.appendShort(0x0001);
		bb.appendByte((byte)0x11);
		new SmsSignal(bb.getBuffer());
	}
	
	@Test(expected=ClassCastException.class)
	public void testSSConstructor6() throws WrongParameterException, TLVException {
		SmppByteBuffer bb = new SmppByteBuffer();
		bb.appendShort(0x0005);
		bb.appendShort(0x0002);
		bb.appendShort(0x7fff);
		new SmsSignal(bb.getBuffer());
	}

}
