package org.bulatnig.smpp.domain.tlv;

import junit.framework.JUnit4TestAdapter;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.bulatnig.smpp.pdu.tlv.MessagePayload;
import org.bulatnig.smpp.pdu.tlv.ParameterTag;
import org.bulatnig.smpp.pdu.tlv.TLVException;
import org.bulatnig.smpp.pdu.tlv.TLVNotFoundException;
import org.bulatnig.smpp.pdu.SmscEsmClass;
import org.bulatnig.smpp.pdu.DataCodingHelper;
import org.bulatnig.smpp.util.SMPPByteBuffer;
import org.bulatnig.smpp.util.WrongParameterException;

public class MessagePayloadTest {
	
	// Used for backward compatibility (IDEs, Ant and JUnit 3 text runner)
	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(MessagePayloadTest.class);
	}
	
	@Test
	public void testMSConstructor1() throws TLVException, WrongParameterException {
		SMPPByteBuffer bb = new SMPPByteBuffer();
		bb.appendShort(0x0424);
		bb.appendShort(0x000A);
		bb.appendString("Булат", DataCodingHelper.INSTANCE.getCharsetName((short) 8));
		MessagePayload mp = new MessagePayload(bb.getBuffer(), new SmscEsmClass(), (short) 8);
		assertEquals(ParameterTag.MESSAGE_PAYLOAD, mp.getTag());
		assertEquals(14, mp.getBytes(new SmscEsmClass(), (short) 8).length);
		assertEquals("Булат", mp.getValue());
		assertEquals("0424000a04110443043b04300442", new SMPPByteBuffer(mp.getBytes(new SmscEsmClass(), (short) 8)).getHexDump());
	}

    @Test(expected = TLVNotFoundException.class)
	public void testMSConstructor2() throws TLVException, WrongParameterException {
		SMPPByteBuffer bb = new SMPPByteBuffer();
		bb.appendShort(0x0000);
		bb.appendShort(0x0001);
		bb.appendByte((byte)0x11);
		new MessagePayload(bb.getBuffer(), new SmscEsmClass(), (short) 0);
	}
	
	@Test(expected= TLVException.class)
	public void testMSConstructor3() throws WrongParameterException, TLVException {
		SMPPByteBuffer bb = new SMPPByteBuffer();
		bb.appendShort(0x0424);
		bb.appendShort(0x0002);
		bb.appendByte((byte)0x11);
		new MessagePayload(bb.getBuffer(), new SmscEsmClass(), (short) 0);
	}
	
	@Test
	public void testMSConstructor4() throws TLVException, WrongParameterException {
		MessagePayload mp = new MessagePayload("smx");
		assertEquals(ParameterTag.MESSAGE_PAYLOAD, mp.getTag());
		assertEquals(7, mp.getBytes().length);
		assertEquals("smx", mp.getValue());
		assertEquals("04240003736d78", new SMPPByteBuffer(mp.getBytes()).getHexDump());
	}
	
	@Test(expected=ClassCastException.class)
	public void testMSConstructor5() throws TLVException, WrongParameterException {
		SMPPByteBuffer bb = new SMPPByteBuffer();
		bb.appendShort(0x0005);
		bb.appendShort(0x0001);
		bb.appendByte((byte)0x11);
		new MessagePayload(bb.getBuffer(), new SmscEsmClass(), (short) 0);
	}

}
