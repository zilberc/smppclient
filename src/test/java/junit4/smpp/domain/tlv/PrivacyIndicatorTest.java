package junit4.smpp.domain.tlv;

import junit.framework.JUnit4TestAdapter;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.bulatnig.smpp.pdu.tlv.*;
import org.bulatnig.smpp.util.SMPPByteBuffer;
import org.bulatnig.smpp.util.WrongParameterException;

public class PrivacyIndicatorTest {
	
	// Used for backward compatibility (IDEs, Ant and JUnit 3 text runner)
	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(PrivacyIndicatorTest.class);
	}
	
	@Test
	public void testPIConstructor1() throws WrongParameterException, TLVException {
		SMPPByteBuffer bb = new SMPPByteBuffer();
		bb.appendShort(0x0201);
		bb.appendShort(0x0001);
		bb.appendByte((byte)0x02);
		PrivacyIndicator pi = new PrivacyIndicator(bb.getBuffer());
		assertEquals(ParameterTag.PRIVACY_INDICATOR, pi.getTag());
		assertEquals(5, pi.getBytes().length);
		assertEquals(Privacy.CONFIDENTIAL, pi.getValue());
		assertEquals("0201000102", new SMPPByteBuffer(pi.getBytes()).getHexDump());
	}

    @Test(expected = TLVNotFoundException.class)
	public void testPIConstructor2() throws TLVException, WrongParameterException {
		SMPPByteBuffer bb = new SMPPByteBuffer();
		bb.appendShort(0x0000);
		bb.appendShort(0x0001);
		bb.appendByte((byte)0x02);
		new PrivacyIndicator(bb.getBuffer());
	}
	
	@Test(expected= TLVException.class)
	public void testPIConstructor3() throws TLVException, WrongParameterException {
		SMPPByteBuffer bb = new SMPPByteBuffer();
		bb.appendShort(0x0201);
		bb.appendShort(0x0001);
		bb.appendShort(0x0003);
		new PrivacyIndicator(bb.getBuffer());
	}
	
	@Test
	public void testPIConstructor4() throws TLVException, WrongParameterException {
		PrivacyIndicator pi = new PrivacyIndicator(Privacy.CONFIDENTIAL);
		assertEquals(ParameterTag.PRIVACY_INDICATOR, pi.getTag());
		assertEquals(5, pi.getBytes().length);
		assertEquals(Privacy.CONFIDENTIAL, pi.getValue());
		assertEquals("0201000102", new SMPPByteBuffer(pi.getBytes()).getHexDump());
	}
	
	@Test(expected= TLVException.class)
	public void testSASConstructor5() throws TLVException, WrongParameterException {
		SMPPByteBuffer bb = new SMPPByteBuffer();
		bb.appendShort(0x0201);
		bb.appendShort(0x0002);
		bb.appendShort(0x0012);
		new PrivacyIndicator(bb.getBuffer());
	}
	
	@Test(expected=ClassCastException.class)
	public void testPIConstructor6() throws TLVException, WrongParameterException {
		SMPPByteBuffer bb = new SMPPByteBuffer();
		bb.appendShort(0x0006);
		bb.appendShort(0x0001);
		bb.appendByte((byte)0x00);
		new PrivacyIndicator(bb.getBuffer());
	}
	
	@Test
	public void testPIConstructor7() throws TLVException, WrongParameterException {
		SMPPByteBuffer bb = new SMPPByteBuffer();
		bb.appendShort(0x0201);
		bb.appendShort(0x0001);
		bb.appendByte((byte)0x12);
		PrivacyIndicator pi = new PrivacyIndicator(bb.getBuffer());
		assertEquals(ParameterTag.PRIVACY_INDICATOR, pi.getTag());
		assertEquals(5, pi.getBytes().length);
		assertEquals(Privacy.RESERVED, pi.getValue());
		assertEquals((short) 18, pi.getIntValue());
		assertEquals("0201000112", new SMPPByteBuffer(pi.getBytes()).getHexDump());
	}

}
