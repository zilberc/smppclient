package org.bulatnig.smpp.domain.tlv;

import junit.framework.JUnit4TestAdapter;
import static org.junit.Assert.assertEquals;

import org.bulatnig.smpp.util.SmppByteBuffer;
import org.junit.Test;
import org.bulatnig.smpp.pdu.tlv.*;
import org.bulatnig.smpp.util.WrongParameterException;

public class DeliveryFailureReasonTest {
	
	// Used for backward compatibility (IDEs, Ant and JUnit 3 text runner)
	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(DeliveryFailureReasonTest.class);
	}
	
	@Test
	public void testDFRConstructor1() throws TLVException, WrongParameterException {
		SmppByteBuffer bb = new SmppByteBuffer();
		bb.appendShort(0x0425);
		bb.appendShort(0x0001);
		bb.appendByte((byte)0x02);
		DeliveryFailureReason dfr = new DeliveryFailureReason(bb.array());
		assertEquals(ParameterTag.DELIVERY_FAILURE_REASON, dfr.getTag());
		assertEquals(5, dfr.getBytes().length);
		assertEquals(FailureReason.PERMANENT_NETWORK_ERROR, dfr.getValue());
		assertEquals("0425000102", new SmppByteBuffer(dfr.getBytes()).getHexDump());
	}

    @Test(expected = TLVNotFoundException.class)
	public void testDFRConstructor2() throws WrongParameterException, TLVException {
		SmppByteBuffer bb = new SmppByteBuffer();
		bb.appendShort(0x0000);
		bb.appendShort(0x0002);
		bb.appendByte((byte)0x55);
		new DeliveryFailureReason(bb.array());
	}
	
	@Test(expected= TLVException.class)
	public void testDFRConstructor3() throws TLVException, WrongParameterException {
		SmppByteBuffer bb = new SmppByteBuffer();
		bb.appendShort(0x0425);
		bb.appendShort(0x0002);
		bb.appendShort(0x00);
		new DeliveryFailureReason(bb.array());
	}
	
	@Test
	public void testDFRConstructor4() throws TLVException, WrongParameterException {
		DeliveryFailureReason dfr = new DeliveryFailureReason(FailureReason.PERMANENT_NETWORK_ERROR);
		assertEquals(ParameterTag.DELIVERY_FAILURE_REASON, dfr.getTag());
		assertEquals(5, dfr.getBytes().length);
		assertEquals(FailureReason.PERMANENT_NETWORK_ERROR, dfr.getValue());
		assertEquals("0425000102", new SmppByteBuffer(dfr.getBytes()).getHexDump());
	}
	
	@Test(expected= TLVException.class)
	public void testSASConstructor5() throws TLVException, WrongParameterException {
		SmppByteBuffer bb = new SmppByteBuffer();
		bb.appendShort(0x0425);
		bb.appendShort(0x0001);
		bb.appendShort(0x0002);
		new DeliveryFailureReason(bb.array());
	}
	
	@Test(expected=ClassCastException.class)
	public void testDFRConstructor6() throws TLVException, WrongParameterException {
		SmppByteBuffer bb = new SmppByteBuffer();
		bb.appendShort(0x0006);
		bb.appendShort(0x0001);
		bb.appendByte((byte)0x02);
		new DeliveryFailureReason(bb.array());
	}
	
	@Test
	public void testDFRConstructor7() throws TLVException, WrongParameterException {
		SmppByteBuffer bb = new SmppByteBuffer();
		bb.appendShort(0x0425);
		bb.appendShort(0x0001);
		bb.appendByte((byte)0x12);
		DeliveryFailureReason dfr = new DeliveryFailureReason(bb.array());
		assertEquals(ParameterTag.DELIVERY_FAILURE_REASON, dfr.getTag());
		assertEquals(5, dfr.getBytes().length);
		assertEquals(FailureReason.RESERVED, dfr.getValue());
		assertEquals((short) 18, dfr.getIntValue());
		assertEquals("0425000112", new SmppByteBuffer(dfr.getBytes()).getHexDump());
	}

}
