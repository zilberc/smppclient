package junit4.smpp.domain.tlv;

import junit.framework.JUnit4TestAdapter;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.bulatnig.smpp.pdu.tlv.MoreMessagesToSend;
import org.bulatnig.smpp.pdu.tlv.ParameterTag;
import org.bulatnig.smpp.pdu.tlv.TLVException;
import org.bulatnig.smpp.pdu.tlv.TLVNotFoundException;
import org.bulatnig.smpp.util.SMPPByteBuffer;
import org.bulatnig.smpp.util.WrongParameterException;

public class MoreMessagesToSendTest {
	
	// Used for backward compatibility (IDEs, Ant and JUnit 3 text runner)
	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(MoreMessagesToSendTest.class);
	}
	
	@Test
	public void testMMTSConstructor1() throws TLVException, WrongParameterException {
		SMPPByteBuffer bb = new SMPPByteBuffer();
		bb.appendShort(0x0426);
		bb.appendShort(0x0001);
		bb.appendByte((byte)0x70);
		MoreMessagesToSend mmts = new MoreMessagesToSend(bb.getBuffer());
		assertEquals(ParameterTag.MORE_MESSAGES_TO_SEND, mmts.getTag());
		assertEquals(5, mmts.getBytes().length);
		assertEquals((short)112, mmts.getValue());
		assertEquals("0426000170", new SMPPByteBuffer(mmts.getBytes()).getHexDump());
	}

    @Test(expected = TLVNotFoundException.class)
	public void testMMTSConstructor2() throws WrongParameterException, TLVException {
		SMPPByteBuffer bb = new SMPPByteBuffer();
		bb.appendShort(0x0000);
		bb.appendShort(0x0002);
		bb.appendByte((byte)0x1111);
		new MoreMessagesToSend(bb.getBuffer());
	}
	
	@Test(expected= TLVException.class)
	public void testMMTSConstructor3() throws TLVException, WrongParameterException {
		SMPPByteBuffer bb = new SMPPByteBuffer();
		bb.appendShort(0x0426);
		bb.appendShort(0x0002);
		bb.appendShort(0x1111);
		new MoreMessagesToSend(bb.getBuffer());
	}
	
	@Test
	public void testMMTSConstructor4() throws TLVException, WrongParameterException {
		MoreMessagesToSend mmts = new MoreMessagesToSend((byte)112);
		assertEquals(ParameterTag.MORE_MESSAGES_TO_SEND, mmts.getTag());
		assertEquals(5, mmts.getBytes().length);
		assertEquals((short)112, mmts.getValue());
		assertEquals("0426000170", new SMPPByteBuffer(mmts.getBytes()).getHexDump());
	}
	
	@Test(expected= TLVException.class)
	public void testMMTSConstructor5() throws TLVException, WrongParameterException {
		SMPPByteBuffer bb = new SMPPByteBuffer();
		bb.appendShort(0x0426);
		bb.appendShort(0x0001);
		bb.appendShort(0x0001);
		new MoreMessagesToSend(bb.getBuffer());
	}
	
	@Test(expected=ClassCastException.class)
	public void testMMTSConstructor6() throws TLVException, WrongParameterException {
		SMPPByteBuffer bb = new SMPPByteBuffer();
		bb.appendShort(0x0005);
		bb.appendShort(0x0001);
		bb.appendByte((byte)0x0f);
		new MoreMessagesToSend(bb.getBuffer());
	}

}
