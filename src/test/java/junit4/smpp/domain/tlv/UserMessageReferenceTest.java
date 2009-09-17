package junit4.smpp.domain.tlv;

import junit.framework.JUnit4TestAdapter;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.bulatnig.smpp.pdu.tlv.ParameterTag;
import org.bulatnig.smpp.pdu.tlv.TLVException;
import org.bulatnig.smpp.pdu.tlv.TLVNotFoundException;
import org.bulatnig.smpp.pdu.tlv.UserMessageReference;
import org.bulatnig.smpp.util.SMPPByteBuffer;
import org.bulatnig.smpp.util.WrongParameterException;

public class UserMessageReferenceTest {
	
	// Used for backward compatibility (IDEs, Ant and JUnit 3 text runner)
	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(UserMessageReferenceTest.class);
	}
	
	@Test
	public void testUMRConstructor1() throws TLVException, WrongParameterException {
		SMPPByteBuffer bb = new SMPPByteBuffer();
		bb.appendShort(0x0204);
		bb.appendShort(0x0002);
		bb.appendShort(0x1111);
		UserMessageReference umr = new UserMessageReference(bb.getBuffer());
		assertEquals(ParameterTag.USER_MESSAGE_REFERENCE, umr.getTag());
		assertEquals(6, umr.getBytes().length);
		assertEquals(4369, umr.getValue());
		assertEquals("020400021111", new SMPPByteBuffer(umr.getBytes()).getHexDump());
	}

    @Test(expected = TLVNotFoundException.class)
	public void testUMRConstructor2() throws TLVException, WrongParameterException {
		SMPPByteBuffer bb = new SMPPByteBuffer();
		bb.appendShort(0x0000);
		bb.appendShort(0x0002);
		bb.appendByte((byte)0x1111);
		new UserMessageReference(bb.getBuffer());
	}
	
	@Test(expected= TLVException.class)
	public void testUMRConstructor3() throws WrongParameterException, TLVException {
		SMPPByteBuffer bb = new SMPPByteBuffer();
		bb.appendShort(0x0204);
		bb.appendShort(0x0001);
		bb.appendShort(0x1111);
		new UserMessageReference(bb.getBuffer());
	}
	
	@Test
	public void testUMRConstructor4() throws TLVException, WrongParameterException {
		UserMessageReference umr = new UserMessageReference((short)112);
		assertEquals(ParameterTag.USER_MESSAGE_REFERENCE, umr.getTag());
		assertEquals(6, umr.getBytes().length);
		assertEquals(112, umr.getValue());
		assertEquals("020400020070", new SMPPByteBuffer(umr.getBytes()).getHexDump());
	}
	
	@Test(expected= TLVException.class)
	public void testUMRConstructor5() throws TLVException, WrongParameterException {
		SMPPByteBuffer bb = new SMPPByteBuffer();
		bb.appendShort(0x0204);
		bb.appendShort(0x0001);
		bb.appendByte((byte)0x11);
		new UserMessageReference(bb.getBuffer());
	}
	
	@Test(expected=ClassCastException.class)
	public void testUMRConstructor6() throws TLVException, WrongParameterException {
		SMPPByteBuffer bb = new SMPPByteBuffer();
		bb.appendShort(0x0005);
		bb.appendShort(0x0002);
		bb.appendShort(0x7fff);
		new UserMessageReference(bb.getBuffer());
	}

}
