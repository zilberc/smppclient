package junit4.smpp.domain.tlv;

import junit.framework.JUnit4TestAdapter;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.bulatnig.smpp.pdu.tlv.ParameterTag;
import org.bulatnig.smpp.pdu.tlv.SarTotalSegments;
import org.bulatnig.smpp.pdu.tlv.TLVException;
import org.bulatnig.smpp.pdu.tlv.TLVNotFoundException;
import org.bulatnig.smpp.util.SMPPByteBuffer;
import org.bulatnig.smpp.util.WrongParameterException;

public class SarTotalSegmentsTest {
	
	// Used for backward compatibility (IDEs, Ant and JUnit 3 text runner)
	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(SarTotalSegmentsTest.class);
	}
	
	@Test
	public void testSTSConstructor1() throws TLVException, WrongParameterException {
		SMPPByteBuffer bb = new SMPPByteBuffer();
		bb.appendShort(0x020e);
		bb.appendShort(0x0001);
		bb.appendByte((byte)0x70);
		SarTotalSegments sts = new SarTotalSegments(bb.getBuffer());
		assertEquals(ParameterTag.SAR_TOTAL_SEGMENTS, sts.getTag());
		assertEquals(5, sts.getBytes().length);
		assertEquals((short)112, sts.getValue());
		assertEquals("020e000170", new SMPPByteBuffer(sts.getBytes()).getHexDump());
	}

    @Test(expected = TLVNotFoundException.class)
	public void testSTSConstructor2() throws TLVException, WrongParameterException {
		SMPPByteBuffer bb = new SMPPByteBuffer();
		bb.appendShort(0x0000);
		bb.appendShort(0x0002);
		bb.appendByte((byte)0x1111);
		new SarTotalSegments(bb.getBuffer());
	}
	
	@Test(expected= TLVException.class)
	public void testSTSConstructor3() throws TLVException, WrongParameterException {
		SMPPByteBuffer bb = new SMPPByteBuffer();
		bb.appendShort(0x020e);
		bb.appendShort(0x0002);
		bb.appendShort(0x1111);
		new SarTotalSegments(bb.getBuffer());
	}
	
	@Test
	public void testSTSConstructor4() throws TLVException, WrongParameterException {
		SarTotalSegments sts = new SarTotalSegments((byte)112);
		assertEquals(ParameterTag.SAR_TOTAL_SEGMENTS, sts.getTag());
		assertEquals(5, sts.getBytes().length);
		assertEquals((short)112, sts.getValue());
		assertEquals("020e000170", new SMPPByteBuffer(sts.getBytes()).getHexDump());
	}
	
	@Test(expected= TLVException.class)
	public void testSTSConstructor5() throws TLVException, WrongParameterException {
		SMPPByteBuffer bb = new SMPPByteBuffer();
		bb.appendShort(0x020e);
		bb.appendShort(0x0001);
		bb.appendShort(0x0001);
		new SarTotalSegments(bb.getBuffer());
	}
	
	@Test(expected=ClassCastException.class)
	public void testSTSConstructor6() throws TLVException, WrongParameterException {
		SMPPByteBuffer bb = new SMPPByteBuffer();
		bb.appendShort(0x0005);
		bb.appendShort(0x0001);
		bb.appendByte((byte)0x0f);
		new SarTotalSegments(bb.getBuffer());
	}

}
