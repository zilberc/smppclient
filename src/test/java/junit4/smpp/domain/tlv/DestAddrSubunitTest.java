package junit4.smpp.domain.tlv;

import junit.framework.JUnit4TestAdapter;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.bulatnig.smpp.pdu.tlv.*;
import org.bulatnig.smpp.util.SMPPByteBuffer;
import org.bulatnig.smpp.util.WrongParameterException;

public class DestAddrSubunitTest {
	
	// Used for backward compatibility (IDEs, Ant and JUnit 3 text runner)
	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(DestAddrSubunitTest.class);
	}
	
	@Test
	public void testDASConstructor1() throws WrongParameterException, TLVException {
		SMPPByteBuffer bb = new SMPPByteBuffer();
		bb.appendShort(0x0005);
		bb.appendShort(0x0001);
		bb.appendByte((byte)0x04);
		DestAddrSubunit das = new DestAddrSubunit(bb.getBuffer());
		assertEquals(ParameterTag.DEST_ADDR_SUBUNIT, das.getTag());
		assertEquals(5, das.getBytes().length);
		assertEquals(AddrSubunit.EXTERNAL_UNIT_1, das.getValue());
		assertEquals("0005000104", new SMPPByteBuffer(das.getBytes()).getHexDump());
	}

    @Test(expected = TLVNotFoundException.class)
	public void testDASConstructor2() throws TLVException, WrongParameterException {
		SMPPByteBuffer bb = new SMPPByteBuffer();
		bb.appendShort(0x0000);
		bb.appendShort(0x0002);
		bb.appendByte((byte)0x55);
		new DestAddrSubunit(bb.getBuffer());
	}
	
	@Test(expected= TLVException.class)
	public void testDASConstructor3() throws TLVException, WrongParameterException {
		SMPPByteBuffer bb = new SMPPByteBuffer();
		bb.appendShort(0x0005);
		bb.appendShort(0x0001);
		bb.appendShort(0x0003);
		new DestAddrSubunit(bb.getBuffer());
	}
	
	@Test
	public void testDASConstructor4() throws TLVException, WrongParameterException {
		DestAddrSubunit das = new DestAddrSubunit(AddrSubunit.MOBILE_EQUIPMENT);
		assertEquals(ParameterTag.DEST_ADDR_SUBUNIT, das.getTag());
		assertEquals(5, das.getBytes().length);
		assertEquals(AddrSubunit.MOBILE_EQUIPMENT, das.getValue());
		assertEquals("0005000102", new SMPPByteBuffer(das.getBytes()).getHexDump());
	}
	
	@Test(expected= TLVException.class)
	public void testSASConstructor5() throws TLVException, WrongParameterException {
		SMPPByteBuffer bb = new SMPPByteBuffer();
		bb.appendShort(0x0006);
		bb.appendShort(0x0001);
		bb.appendShort(0x0002);
		new DestAddrSubunit(bb.getBuffer());
	}
	
	@Test(expected=ClassCastException.class)
	public void testDASConstructor6() throws TLVException, WrongParameterException {
		SMPPByteBuffer bb = new SMPPByteBuffer();
		bb.appendShort(0x0006);
		bb.appendShort(0x0001);
		bb.appendByte((byte)0x00);
		new DestAddrSubunit(bb.getBuffer());
	}
	
	@Test
	public void testDASConstructor7() throws TLVException, WrongParameterException {
		SMPPByteBuffer bb = new SMPPByteBuffer();
		bb.appendShort(0x0005);
		bb.appendShort(0x0001);
		bb.appendByte((byte)0x12);
		DestAddrSubunit das = new DestAddrSubunit(bb.getBuffer());
		assertEquals(ParameterTag.DEST_ADDR_SUBUNIT, das.getTag());
		assertEquals(5, das.getBytes().length);
		assertEquals(AddrSubunit.RESERVED, das.getValue());
		assertEquals((short) 18, das.getIntValue());
		assertEquals("0005000112", new SMPPByteBuffer(das.getBytes()).getHexDump());
	}

}
