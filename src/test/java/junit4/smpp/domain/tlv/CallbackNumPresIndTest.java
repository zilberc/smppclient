package junit4.smpp.domain.tlv;

import junit.framework.JUnit4TestAdapter;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.bulatnig.smpp.pdu.tlv.CallbackNumPresInd;
import org.bulatnig.smpp.pdu.tlv.ParameterTag;
import org.bulatnig.smpp.pdu.tlv.TLVException;
import org.bulatnig.smpp.pdu.tlv.TLVNotFoundException;
import org.bulatnig.smpp.util.SMPPByteBuffer;
import org.bulatnig.smpp.util.WrongParameterException;

public class CallbackNumPresIndTest {
	
	// Used for backward compatibility (IDEs, Ant and JUnit 3 text runner)
	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(CallbackNumPresIndTest.class);
	}
	
	@Test
	public void testCNPIConstructor1() throws WrongParameterException, TLVException {
		SMPPByteBuffer bb = new SMPPByteBuffer();
		bb.appendShort(0x0302);
		bb.appendShort(0x0001);
		bb.appendByte((byte)0x10);
		CallbackNumPresInd cnpi = new CallbackNumPresInd(bb.getBuffer());
		assertEquals(ParameterTag.CALLBACK_NUM_PRES_IND, cnpi.getTag());
		assertEquals(5, cnpi.getBytes().length);
		assertEquals((short)0x10, cnpi.getValue());
		assertEquals("0302000110", new SMPPByteBuffer(cnpi.getBytes()).getHexDump());
	}

    @Test(expected = TLVNotFoundException.class)
	public void testCNPIConstructor2() throws TLVException, WrongParameterException {
		SMPPByteBuffer bb = new SMPPByteBuffer();
		bb.appendShort(0x0000);
		bb.appendShort(0x0002);
		bb.appendByte((byte)0x1111);
		new CallbackNumPresInd(bb.getBuffer());
	}
	
	@Test(expected= TLVException.class)
	public void testCNPIConstructor3() throws TLVException, WrongParameterException {
		SMPPByteBuffer bb = new SMPPByteBuffer();
		bb.appendShort(0x0302);
		bb.appendShort(0x0002);
		bb.appendShort(0x1111);
		new CallbackNumPresInd(bb.getBuffer());
	}
	
	@Test
	public void testCNPIConstructor4() throws TLVException, WrongParameterException {
		CallbackNumPresInd cnpi = new CallbackNumPresInd((byte)0x10);
		assertEquals(ParameterTag.CALLBACK_NUM_PRES_IND, cnpi.getTag());
		assertEquals(5, cnpi.getBytes().length);
		assertEquals((short)0x10, cnpi.getValue());
		assertEquals("0302000110", new SMPPByteBuffer(cnpi.getBytes()).getHexDump());
	}
	
	@Test(expected= TLVException.class)
	public void testCNPIConstructor5() throws TLVException, WrongParameterException {
		SMPPByteBuffer bb = new SMPPByteBuffer();
		bb.appendShort(0x0302);
		bb.appendShort(0x0001);
		bb.appendShort(0x0001);
		new CallbackNumPresInd(bb.getBuffer());
	}
	
	@Test(expected=ClassCastException.class)
	public void testCNPIConstructor6() throws TLVException, WrongParameterException {
		SMPPByteBuffer bb = new SMPPByteBuffer();
		bb.appendShort(0x0005);
		bb.appendShort(0x0001);
		bb.appendByte((byte)0x0f);
		new CallbackNumPresInd(bb.getBuffer());
	}

}
