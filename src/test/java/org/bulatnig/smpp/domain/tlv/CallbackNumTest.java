package org.bulatnig.smpp.domain.tlv;

import junit.framework.JUnit4TestAdapter;
import static org.junit.Assert.assertEquals;

import org.bulatnig.smpp.util.SmppByteBuffer;
import org.junit.Test;
import org.bulatnig.smpp.pdu.tlv.CallbackNum;
import org.bulatnig.smpp.pdu.tlv.ParameterTag;
import org.bulatnig.smpp.pdu.tlv.TLVException;
import org.bulatnig.smpp.util.WrongParameterException;

public class CallbackNumTest {
	
	// Used for backward compatibility (IDEs, Ant and JUnit 3 text runner)
	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(CallbackNumTest.class);
	}
	
	@Test
	public void testCNConstructor1() throws TLVException, WrongParameterException {
		SmppByteBuffer bb = new SmppByteBuffer();
		bb.appendShort(0x0381);
		bb.appendShort(0x0005);
		bb.appendString("bulat");
		CallbackNum cn = new CallbackNum(bb.array());
		assertEquals(ParameterTag.CALLBACK_NUM, cn.getTag());
		assertEquals(9, cn.getBytes().length);
		assertEquals("bulat", cn.getValue());
		assertEquals("0381000562756c6174", new SmppByteBuffer(cn.getBytes()).getHexDump());
	}
	
	@Test(expected=TLVException.class)
	public void testCNConstructor2() throws WrongParameterException, TLVException {
		SmppByteBuffer bb = new SmppByteBuffer();
		bb.appendShort(0x0000);
		bb.appendShort(0x0001);
		bb.appendByte((byte)0x11);
		new CallbackNum(bb.array());
	}
	
	@Test(expected= TLVException.class)
	public void testCNConstructor3() throws TLVException, WrongParameterException {
		SmppByteBuffer bb = new SmppByteBuffer();
		bb.appendShort(0x0381);
		bb.appendShort(0x0002);
		bb.appendByte((byte)0x11);
		new CallbackNum(bb.array());
	}
	
	@Test
	public void testCNConstructor4() throws TLVException, WrongParameterException {
		CallbackNum cn = new CallbackNum("bulat");
		assertEquals(ParameterTag.CALLBACK_NUM, cn.getTag());
		assertEquals(9, cn.getBytes().length);
		assertEquals("bulat", cn.getValue());
		assertEquals("0381000562756c6174", new SmppByteBuffer(cn.getBytes()).getHexDump());
	}
	
	@Test(expected=ClassCastException.class)
	public void testCNConstructor5() throws TLVException, WrongParameterException {
		SmppByteBuffer bb = new SmppByteBuffer();
		bb.appendShort(0x0005);
		bb.appendShort(0x0001);
		bb.appendByte((byte)0x11);
		new CallbackNum(bb.array());
	}
	
	@Test(expected= TLVException.class)
	public void testCNConstructor6() throws TLVException, WrongParameterException {
		SmppByteBuffer bb = new SmppByteBuffer();
		bb.appendShort(0x0381);
		bb.appendShort(0x0003);
		bb.appendByte((byte)0x110101);
		new CallbackNum(bb.array());
	}

}
