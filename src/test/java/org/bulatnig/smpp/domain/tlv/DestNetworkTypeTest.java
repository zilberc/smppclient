package org.bulatnig.smpp.domain.tlv;

import junit.framework.JUnit4TestAdapter;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.bulatnig.smpp.pdu.tlv.*;
import org.bulatnig.smpp.util.SMPPByteBuffer;
import org.bulatnig.smpp.util.WrongParameterException;

public class DestNetworkTypeTest {

	// Used for backward compatibility (IDEs, Ant and JUnit 3 text runner)
	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(DestNetworkTypeTest.class);
	}
	
	@Test
	public void testDNTConstructor1() throws TLVException, WrongParameterException {
		SMPPByteBuffer bb = new SMPPByteBuffer();
		bb.appendShort(0x0006);
		bb.appendShort(0x0001);
		bb.appendByte((byte)0x04);
		DestNetworkType dnt = new DestNetworkType(bb.getBuffer());
		assertEquals(ParameterTag.DEST_NETWORK_TYPE, dnt.getTag());
		assertEquals(5, dnt.getBytes().length);
		assertEquals(NetworkType.PDC, dnt.getValue());
		assertEquals("0006000104", new SMPPByteBuffer(dnt.getBytes()).getHexDump());
	}

    @Test(expected = TLVNotFoundException.class)
	public void testDNTConstructor2() throws TLVException, WrongParameterException {
		SMPPByteBuffer bb = new SMPPByteBuffer();
		bb.appendShort(0x0000);
		bb.appendShort(0x0002);
		bb.appendByte((byte)0x55);
		new DestNetworkType(bb.getBuffer());
	}
	
	@Test(expected= TLVException.class)
	public void testDNTConstructor3() throws TLVException, WrongParameterException {
		SMPPByteBuffer bb = new SMPPByteBuffer();
		bb.appendShort(0x0006);
		bb.appendShort(0x0001);
		bb.appendShort(0x0003);
		new DestNetworkType(bb.getBuffer());
	}
	
	@Test
	public void testDNTConstructor4() throws TLVException, WrongParameterException {
		DestNetworkType dnt = new DestNetworkType(NetworkType.PDC);
		assertEquals(ParameterTag.DEST_NETWORK_TYPE, dnt.getTag());
		assertEquals(5, dnt.getBytes().length);
		assertEquals(NetworkType.PDC, dnt.getValue());
		assertEquals("0006000104", new SMPPByteBuffer(dnt.getBytes()).getHexDump());
	}
	
	@Test(expected= TLVException.class)
	public void testDNTConstructor5() throws WrongParameterException, TLVException {
		SMPPByteBuffer bb = new SMPPByteBuffer();
		bb.appendShort(0x0006);
		bb.appendShort(0x0001);
		bb.appendShort(0x0014);
		new DestNetworkType(bb.getBuffer());
	}
	
	@Test(expected=ClassCastException.class)
	public void testDNTConstructor6() throws TLVException, WrongParameterException {
		SMPPByteBuffer bb = new SMPPByteBuffer();
		bb.appendShort(0x0005);
		bb.appendShort(0x0001);
		bb.appendByte((byte)0x00);
		new DestNetworkType(bb.getBuffer());
	}
	
	@Test
	public void testDNTConstructor7() throws TLVException, WrongParameterException {
		SMPPByteBuffer bb = new SMPPByteBuffer();
		bb.appendShort(0x0006);
		bb.appendShort(0x0001);
		bb.appendByte((byte)0x14);
		DestNetworkType dnt = new DestNetworkType(bb.getBuffer());
		assertEquals(ParameterTag.DEST_NETWORK_TYPE, dnt.getTag());
		assertEquals(5, dnt.getBytes().length);
		assertEquals(NetworkType.RESERVED, dnt.getValue());
		assertEquals(20, dnt.getIntValue());
		assertEquals("0006000114", new SMPPByteBuffer(dnt.getBytes()).getHexDump());
	}
	
}
