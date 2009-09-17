package junit4.smpp.domain.tlv;

import junit.framework.JUnit4TestAdapter;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.bulatnig.smpp.pdu.tlv.ParameterTag;
import org.bulatnig.smpp.pdu.tlv.PayloadType;
import org.bulatnig.smpp.pdu.tlv.TLVException;
import org.bulatnig.smpp.pdu.tlv.TLVNotFoundException;
import org.bulatnig.smpp.util.SMPPByteBuffer;
import org.bulatnig.smpp.util.WrongParameterException;

public class PayloadTypeTest {

	// Used for backward compatibility (IDEs, Ant and JUnit 3 text runner)
	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(PayloadTypeTest.class);
	}
	
	@Test
	public void testPTTConstructor1() throws TLVException, WrongParameterException {
		SMPPByteBuffer bb = new SMPPByteBuffer();
		bb.appendShort(0x0019);
		bb.appendShort(0x0001);
		bb.appendByte((byte)0x11);
		PayloadType pt = new PayloadType(bb.getBuffer());
		assertEquals(ParameterTag.PAYLOAD_TYPE, pt.getTag());
		assertEquals(5, pt.getBytes().length);
		assertEquals((short)17, pt.getValue());
		assertEquals("0019000111", new SMPPByteBuffer(pt.getBytes()).getHexDump());
	}

    @Test(expected = TLVNotFoundException.class)
	public void testPTTConstructor2() throws TLVException, WrongParameterException {
		SMPPByteBuffer bb = new SMPPByteBuffer();
		bb.appendShort(0x0000);
		bb.appendShort(0x0001);
		bb.appendByte((byte)0x11);
		new PayloadType(bb.getBuffer());
	}
	
	@Test(expected= TLVException.class)
	public void testPTTConstructor3() throws TLVException, WrongParameterException {
		SMPPByteBuffer bb = new SMPPByteBuffer();
		bb.appendShort(0x0019);
		bb.appendShort(0x0002);
		bb.appendByte((byte)0x11);
		new PayloadType(bb.getBuffer());
	}
	
	@Test
	public void testPTTConstructor4() throws TLVException, WrongParameterException {
		PayloadType pt = new PayloadType((byte)112);
		assertEquals(ParameterTag.PAYLOAD_TYPE, pt.getTag());
		assertEquals(5, pt.getBytes().length);
		assertEquals((short)112, pt.getValue());
		assertEquals("0019000170", new SMPPByteBuffer(pt.getBytes()).getHexDump());
	}
	
	@Test(expected= TLVException.class)
	public void testPTTConstructor5() throws TLVException, WrongParameterException {
		SMPPByteBuffer bb = new SMPPByteBuffer();
		bb.appendShort(0x0019);
		bb.appendShort(0x0001);
		bb.appendShort(0x0011);
		new PayloadType(bb.getBuffer());
	}
	
	@Test(expected=ClassCastException.class)
	public void testPTTConstructor6() throws TLVException, WrongParameterException {
		SMPPByteBuffer bb = new SMPPByteBuffer();
		bb.appendShort(0x0005);
		bb.appendShort(0x0001);
		bb.appendByte((byte)0x11);
		new PayloadType(bb.getBuffer());
	}

	@Test(expected=TLVException.class)
	public void testPTTConstructor7() throws TLVException {
		new PayloadType((short)32000).getBytes();
	}
	
}
