package junit4.smpp.domain.tlv;

import junit.framework.JUnit4TestAdapter;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.bulatnig.smpp.pdu.tlv.LanguageIndicator;
import org.bulatnig.smpp.pdu.tlv.ParameterTag;
import org.bulatnig.smpp.pdu.tlv.TLVException;
import org.bulatnig.smpp.pdu.tlv.TLVNotFoundException;
import org.bulatnig.smpp.util.SMPPByteBuffer;
import org.bulatnig.smpp.util.WrongParameterException;

public class LanguageIndicatorTest {
	
	// Used for backward compatibility (IDEs, Ant and JUnit 3 text runner)
	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(LanguageIndicatorTest.class);
	}
	
	@Test
	public void testLIConstructor1() throws TLVException, WrongParameterException {
		SMPPByteBuffer bb = new SMPPByteBuffer();
		bb.appendShort(0x020d);
		bb.appendShort(0x0001);
		bb.appendByte((byte)0x70);
		LanguageIndicator li = new LanguageIndicator(bb.getBuffer());
		assertEquals(ParameterTag.LANGUAGE_INDICATOR, li.getTag());
		assertEquals(5, li.getBytes().length);
		assertEquals((short)112, li.getValue());
		assertEquals("020d000170", new SMPPByteBuffer(li.getBytes()).getHexDump());
	}

    @Test(expected = TLVNotFoundException.class)
	public void testLIConstructor2() throws TLVException, WrongParameterException {
		SMPPByteBuffer bb = new SMPPByteBuffer();
		bb.appendShort(0x0000);
		bb.appendShort(0x0002);
		bb.appendByte((byte)0x1111);
		new LanguageIndicator(bb.getBuffer());
	}
	
	@Test(expected= TLVException.class)
	public void testLIConstructor3() throws TLVException, WrongParameterException {
		SMPPByteBuffer bb = new SMPPByteBuffer();
		bb.appendShort(0x020d);
		bb.appendShort(0x0002);
		bb.appendShort(0x1111);
		new LanguageIndicator(bb.getBuffer());
	}
	
	@Test
	public void testLIConstructor4() throws TLVException, WrongParameterException {
		LanguageIndicator li = new LanguageIndicator((byte)112);
		assertEquals(ParameterTag.LANGUAGE_INDICATOR, li.getTag());
		assertEquals(5, li.getBytes().length);
		assertEquals((short)112, li.getValue());
		assertEquals("020d000170", new SMPPByteBuffer(li.getBytes()).getHexDump());
	}
	
	@Test(expected= TLVException.class)
	public void testLIConstructor5() throws TLVException, WrongParameterException {
		SMPPByteBuffer bb = new SMPPByteBuffer();
		bb.appendShort(0x020d);
		bb.appendShort(0x0001);
		bb.appendShort(0x0001);
		new LanguageIndicator(bb.getBuffer());
	}
	
	@Test(expected=ClassCastException.class)
	public void testLIConstructor6() throws TLVException, WrongParameterException {
		SMPPByteBuffer bb = new SMPPByteBuffer();
		bb.appendShort(0x0005);
		bb.appendShort(0x0001);
		bb.appendByte((byte)0x0f);
		new LanguageIndicator(bb.getBuffer());
	}

}
