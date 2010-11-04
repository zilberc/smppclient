package org.bulatnig.smpp.domain.tlv;

import junit.framework.JUnit4TestAdapter;
import static org.junit.Assert.assertEquals;

import org.bulatnig.smpp.util.SmppByteBuffer;
import org.junit.Test;
import org.bulatnig.smpp.pdu.tlv.DestSubaddress;
import org.bulatnig.smpp.pdu.tlv.ParameterTag;
import org.bulatnig.smpp.pdu.tlv.TLVException;
import org.bulatnig.smpp.pdu.tlv.TLVNotFoundException;
import org.bulatnig.smpp.util.WrongParameterException;

public class DestSubaddressTest {
	
	// Used for backward compatibility (IDEs, Ant and JUnit 3 text runner)
	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(DestSubaddressTest.class);
	}
	
	@Test
	public void testDSConstructor1() throws TLVException, WrongParameterException {
		SmppByteBuffer bb = new SmppByteBuffer();
		bb.appendShort(0x0203);
		bb.appendShort(0x0003);
		bb.appendString("smx");
		DestSubaddress ss = new DestSubaddress(bb.array());
		assertEquals(ParameterTag.DEST_SUBADDRESS, ss.getTag());
		assertEquals(7, ss.getBytes().length);
		assertEquals("smx", ss.getValue());
		assertEquals("02030003736d78", new SmppByteBuffer(ss.getBytes()).getHexDump());
	}

    @Test(expected = TLVNotFoundException.class)
	public void testDSConstructor2() throws TLVException, WrongParameterException {
		SmppByteBuffer bb = new SmppByteBuffer();
		bb.appendShort(0x0000);
		bb.appendShort(0x0001);
		bb.appendByte((byte)0x11);
		new DestSubaddress(bb.array());
	}
	
	@Test(expected= TLVException.class)
	public void testDSConstructor3() throws TLVException, WrongParameterException {
		SmppByteBuffer bb = new SmppByteBuffer();
		bb.appendShort(0x0203);
		bb.appendShort(0x0002);
		bb.appendByte((byte)0x11);
		new DestSubaddress(bb.array());
	}
	
	@Test
	public void testDSConstructor4() throws TLVException, WrongParameterException {
		DestSubaddress ss = new DestSubaddress("smx");
		assertEquals(ParameterTag.DEST_SUBADDRESS, ss.getTag());
		assertEquals(7, ss.getBytes().length);
		assertEquals("smx", ss.getValue());
		assertEquals("02030003736d78", new SmppByteBuffer(ss.getBytes()).getHexDump());
	}
	
	@Test(expected=ClassCastException.class)
	public void testDSConstructor5() throws TLVException, WrongParameterException {
		SmppByteBuffer bb = new SmppByteBuffer();
		bb.appendShort(0x0005);
		bb.appendShort(0x0001);
		bb.appendByte((byte)0x11);
		new DestSubaddress(bb.array());
	}

	@Test(expected=TLVException.class)
	public void testSSConstructor6() throws TLVException, WrongParameterException {
		SmppByteBuffer bb = new SmppByteBuffer();
		bb.appendShort(0x0203);
		bb.appendShort(0x0001);
		bb.appendByte((byte)0x11);
		new DestSubaddress(bb.array());
	}

	@Test(expected=TLVException.class)
	public void testSSConstructor7() throws TLVException {
		new DestSubaddress("m");
	}

}
