package org.bulatnig.smpp.domain.tlv;

import junit.framework.JUnit4TestAdapter;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.bulatnig.smpp.pdu.tlv.ParameterTag;
import org.bulatnig.smpp.pdu.tlv.SourceSubaddress;
import org.bulatnig.smpp.pdu.tlv.TLVException;
import org.bulatnig.smpp.pdu.tlv.TLVNotFoundException;
import org.bulatnig.smpp.util.SMPPByteBuffer;
import org.bulatnig.smpp.util.WrongLengthException;
import org.bulatnig.smpp.util.WrongParameterException;

public class SourceSubaddressTest {
	
	// Used for backward compatibility (IDEs, Ant and JUnit 3 text runner)
	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(SourceSubaddressTest.class);
	}
	
	@Test
	public void testSSConstructor1() throws TLVException, WrongParameterException {
		SMPPByteBuffer bb = new SMPPByteBuffer();
		bb.appendShort(0x0202);
		bb.appendShort(0x0003);
		bb.appendString("smx");
		SourceSubaddress ss = new SourceSubaddress(bb.getBuffer());
		assertEquals(ParameterTag.SOURCE_SUBADDRESS, ss.getTag());
		assertEquals(7, ss.getBytes().length);
		assertEquals("smx", ss.getValue());
		assertEquals("02020003736d78", new SMPPByteBuffer(ss.getBytes()).getHexDump());
	}

    @Test(expected = TLVNotFoundException.class)
	public void testSSConstructor2() throws TLVException, WrongParameterException {
		SMPPByteBuffer bb = new SMPPByteBuffer();
		bb.appendShort(0x0000);
		bb.appendShort(0x0001);
		bb.appendByte((byte)0x11);
		new SourceSubaddress(bb.getBuffer());
	}
	
	@Test(expected= TLVException.class)
	public void testSSConstructor3() throws TLVException, WrongParameterException {
		SMPPByteBuffer bb = new SMPPByteBuffer();
		bb.appendShort(0x0202);
		bb.appendShort(0x0002);
		bb.appendByte((byte)0x11);
		new SourceSubaddress(bb.getBuffer());
	}
	
	@Test
	public void testSSConstructor4() throws TLVException, WrongParameterException {
		SourceSubaddress ss = new SourceSubaddress("smx");
		assertEquals(ParameterTag.SOURCE_SUBADDRESS, ss.getTag());
		assertEquals(7, ss.getBytes().length);
		assertEquals("smx", ss.getValue());
		assertEquals("02020003736d78", new SMPPByteBuffer(ss.getBytes()).getHexDump());
	}
	
	@Test(expected=ClassCastException.class)
	public void testSSConstructor5() throws TLVException, WrongParameterException {
		SMPPByteBuffer bb = new SMPPByteBuffer();
		bb.appendShort(0x0005);
		bb.appendShort(0x0001);
		bb.appendByte((byte)0x11);
		new SourceSubaddress(bb.getBuffer());
	}

	@Test(expected=TLVException.class)
	public void testSSConstructor6() throws TLVException, WrongParameterException {
		SMPPByteBuffer bb = new SMPPByteBuffer();
		bb.appendShort(0x0202);
		bb.appendShort(0x0001);
		bb.appendByte((byte)0x11);
		new SourceSubaddress(bb.getBuffer());
	}

	@Test(expected=TLVException.class)
	public void testSSConstructor7() throws WrongLengthException, TLVException {
		new SourceSubaddress("m");
	}

}
