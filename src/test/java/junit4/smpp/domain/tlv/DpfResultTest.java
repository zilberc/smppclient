package junit4.smpp.domain.tlv;

import junit.framework.JUnit4TestAdapter;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.bulatnig.smpp.pdu.tlv.DpfResult;
import org.bulatnig.smpp.pdu.tlv.ParameterTag;
import org.bulatnig.smpp.pdu.tlv.TLVException;
import org.bulatnig.smpp.pdu.tlv.TLVNotFoundException;
import org.bulatnig.smpp.util.SMPPByteBuffer;
import org.bulatnig.smpp.util.WrongParameterException;

public class DpfResultTest {
	
	// Used for backward compatibility (IDEs, Ant and JUnit 3 text runner)
	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(DpfResultTest.class);
	}
	
	@Test
	public void testDRConstructor1() throws TLVException, WrongParameterException {
		SMPPByteBuffer bb = new SMPPByteBuffer();
		bb.appendShort(0x0420);
		bb.appendShort(0x0001);
		bb.appendByte((byte)0x70);
		DpfResult dr = new DpfResult(bb.getBuffer());
		assertEquals(ParameterTag.DPF_RESULT, dr.getTag());
		assertEquals(5, dr.getBytes().length);
		assertEquals((short)112, dr.getValue());
		assertEquals("0420000170", new SMPPByteBuffer(dr.getBytes()).getHexDump());
	}

    @Test(expected = TLVNotFoundException.class)
	public void testDRConstructor2() throws TLVException, WrongParameterException {
		SMPPByteBuffer bb = new SMPPByteBuffer();
		bb.appendShort(0x0000);
		bb.appendShort(0x0002);
		bb.appendByte((byte)0x1111);
		new DpfResult(bb.getBuffer());
	}
	
	@Test(expected= TLVException.class)
	public void testDRConstructor3() throws TLVException, WrongParameterException {
		SMPPByteBuffer bb = new SMPPByteBuffer();
		bb.appendShort(0x0420);
		bb.appendShort(0x0002);
		bb.appendShort(0x1111);
		new DpfResult(bb.getBuffer());
	}
	
	@Test
	public void testDRConstructor4() throws TLVException, WrongParameterException {
		DpfResult dr = new DpfResult((byte)112);
		assertEquals(ParameterTag.DPF_RESULT, dr.getTag());
		assertEquals(5, dr.getBytes().length);
		assertEquals((short)112, dr.getValue());
		assertEquals("0420000170", new SMPPByteBuffer(dr.getBytes()).getHexDump());
	}
	
	@Test(expected= TLVException.class)
	public void testDRConstructor5() throws TLVException, WrongParameterException {
		SMPPByteBuffer bb = new SMPPByteBuffer();
		bb.appendShort(0x0420);
		bb.appendShort(0x0001);
		bb.appendShort(0x0001);
		new DpfResult(bb.getBuffer());
	}
	
	@Test(expected=ClassCastException.class)
	public void testDRConstructor6() throws TLVException, WrongParameterException {
		SMPPByteBuffer bb = new SMPPByteBuffer();
		bb.appendShort(0x0005);
		bb.appendShort(0x0001);
		bb.appendByte((byte)0x0f);
		new DpfResult(bb.getBuffer());
	}

}
