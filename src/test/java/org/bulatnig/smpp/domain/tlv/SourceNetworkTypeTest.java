package org.bulatnig.smpp.domain.tlv;

import junit.framework.JUnit4TestAdapter;
import static org.junit.Assert.assertEquals;

import org.bulatnig.smpp.util.SmppByteBuffer;
import org.junit.Test;
import org.bulatnig.smpp.pdu.tlv.*;
import org.bulatnig.smpp.util.WrongParameterException;

public class SourceNetworkTypeTest {
	
	// Used for backward compatibility (IDEs, Ant and JUnit 3 text runner)
	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(SourceNetworkTypeTest.class);
	}
	
	@Test
	public void testSNTConstructor1() throws WrongParameterException, TLVException {
		SmppByteBuffer bb = new SmppByteBuffer();
		bb.appendShort(0x000E);
		bb.appendShort(0x0001);
		bb.appendByte((byte)0x04);
		SourceNetworkType snt = new SourceNetworkType(bb.getBuffer());
		assertEquals(ParameterTag.SOURCE_NETWORK_TYPE, snt.getTag());
		assertEquals(5, snt.getBytes().length);
		assertEquals(NetworkType.PDC, snt.getValue());
		assertEquals("000e000104", new SmppByteBuffer(snt.getBytes()).getHexDump());
	}

    @Test(expected = TLVNotFoundException.class)
	public void testSNTConstructor2() throws TLVException, WrongParameterException {
		SmppByteBuffer bb = new SmppByteBuffer();
		bb.appendShort(0x0000);
		bb.appendShort(0x0002);
		bb.appendByte((byte)0x55);
		new SourceNetworkType(bb.getBuffer());
	}
	
	@Test(expected= TLVException.class)
	public void testSNTConstructor3() throws TLVException, WrongParameterException {
		SmppByteBuffer bb = new SmppByteBuffer();
		bb.appendShort(0x000E);
		bb.appendShort(0x0001);
		bb.appendShort(0x0003);
		new SourceNetworkType(bb.getBuffer());
	}
	
	@Test
	public void testSNTConstructor4() throws TLVException, WrongParameterException {
		SourceNetworkType snt = new SourceNetworkType(NetworkType.PDC);
		assertEquals(ParameterTag.SOURCE_NETWORK_TYPE, snt.getTag());
		assertEquals(5, snt.getBytes().length);
		assertEquals(NetworkType.PDC, snt.getValue());
		assertEquals("000e000104", new SmppByteBuffer(snt.getBytes()).getHexDump());
	}
	
	@Test(expected= TLVException.class)
	public void testSNTConstructor5() throws TLVException, WrongParameterException {
		SmppByteBuffer bb = new SmppByteBuffer();
		bb.appendShort(0x000E);
		bb.appendShort(0x0001);
		bb.appendShort(0x0014);
		new SourceNetworkType(bb.getBuffer());
	}
	
	@Test(expected=ClassCastException.class)
	public void testSNTConstructor6() throws TLVException, WrongParameterException {
		SmppByteBuffer bb = new SmppByteBuffer();
		bb.appendShort(0x0005);
		bb.appendShort(0x0001);
		bb.appendByte((byte)0x00);
		new SourceNetworkType(bb.getBuffer());
	}
	
	@Test
	public void testSNTConstructor7() throws TLVException, WrongParameterException {
		SmppByteBuffer bb = new SmppByteBuffer();
		bb.appendShort(0x000E);
		bb.appendShort(0x0001);
		bb.appendByte((byte)0x14);
		SourceNetworkType snt = new SourceNetworkType(bb.getBuffer());
		assertEquals(ParameterTag.SOURCE_NETWORK_TYPE, snt.getTag());
		assertEquals(5, snt.getBytes().length);
		assertEquals(NetworkType.RESERVED, snt.getValue());
		assertEquals(20, snt.getIntValue());
		assertEquals("000e000114", new SmppByteBuffer(snt.getBytes()).getHexDump());
	}

}
