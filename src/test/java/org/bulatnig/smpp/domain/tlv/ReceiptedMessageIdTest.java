package org.bulatnig.smpp.domain.tlv;

import junit.framework.JUnit4TestAdapter;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.bulatnig.smpp.pdu.tlv.ParameterTag;
import org.bulatnig.smpp.pdu.tlv.ReceiptedMessageId;
import org.bulatnig.smpp.pdu.tlv.TLVException;
import org.bulatnig.smpp.pdu.tlv.TLVNotFoundException;
import org.bulatnig.smpp.util.SMPPByteBuffer;
import org.bulatnig.smpp.util.WrongLengthException;
import org.bulatnig.smpp.util.WrongParameterException;

public class ReceiptedMessageIdTest {
	
	// Used for backward compatibility (IDEs, Ant and JUnit 3 text runner)
	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(ReceiptedMessageIdTest.class);
	}
	
	@Test
	public void testRMIConstructor1() throws TLVException, WrongParameterException {
		SMPPByteBuffer bb = new SMPPByteBuffer();
		bb.appendShort(0x001e);
		bb.appendShort(0x0004);
		bb.appendCString("smx");
		ReceiptedMessageId rmi = new ReceiptedMessageId(bb.getBuffer());
		assertEquals(ParameterTag.RECEIPTED_MESSAGE_ID, rmi.getTag());
		assertEquals(8, rmi.getBytes().length);
		assertEquals("smx", rmi.getValue());
		assertEquals("001e0004736d7800", new SMPPByteBuffer(rmi.getBytes()).getHexDump());
	}

    @Test(expected = TLVNotFoundException.class)
	public void testRMIConstructor2() throws WrongParameterException, TLVException {
		SMPPByteBuffer bb = new SMPPByteBuffer();
		bb.appendShort(0x0000);
		bb.appendShort(0x0001);
		bb.appendByte((byte)0x11);
		new ReceiptedMessageId(bb.getBuffer());
	}
	
	@Test(expected= TLVException.class)
	public void testRMIConstructor3() throws TLVException, WrongParameterException {
		SMPPByteBuffer bb = new SMPPByteBuffer();
		bb.appendShort(0x001e);
		bb.appendShort(0x0002);
		bb.appendByte((byte)0x11);
		new ReceiptedMessageId(bb.getBuffer());
	}
	
	@Test
	public void testRMIConstructor4() throws TLVException, WrongParameterException {
		ReceiptedMessageId rmi = new ReceiptedMessageId("smx");
		assertEquals(ParameterTag.RECEIPTED_MESSAGE_ID, rmi.getTag());
		assertEquals(8, rmi.getBytes().length);
		assertEquals("smx", rmi.getValue());
		assertEquals("001e0004736d7800", new SMPPByteBuffer(rmi.getBytes()).getHexDump());
	}
	
	@Test(expected=ClassCastException.class)
	public void testRMIConstructor5() throws TLVException, WrongParameterException {
		SMPPByteBuffer bb = new SMPPByteBuffer();
		bb.appendShort(0x0005);
		bb.appendShort(0x0001);
		bb.appendByte((byte)0x11);
		new ReceiptedMessageId(bb.getBuffer());
	}

    @Test(expected = TLVException.class)
    public void testRMIConstructor6() throws WrongLengthException, TLVException {
        String s = "";
        for (int i = 0; i < 65; i++) {
            s += Math.round(Math.random()*9);
        }
        new ReceiptedMessageId(s);
    }

}
