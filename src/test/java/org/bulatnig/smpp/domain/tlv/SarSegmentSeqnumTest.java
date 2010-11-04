package org.bulatnig.smpp.domain.tlv;

import junit.framework.JUnit4TestAdapter;
import static org.junit.Assert.assertEquals;

import org.bulatnig.smpp.util.SmppByteBuffer;
import org.junit.Test;
import org.bulatnig.smpp.pdu.tlv.ParameterTag;
import org.bulatnig.smpp.pdu.tlv.SarSegmentSeqnum;
import org.bulatnig.smpp.pdu.tlv.TLVException;
import org.bulatnig.smpp.pdu.tlv.TLVNotFoundException;
import org.bulatnig.smpp.util.WrongParameterException;

public class SarSegmentSeqnumTest {
	
	// Used for backward compatibility (IDEs, Ant and JUnit 3 text runner)
	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(SarSegmentSeqnumTest.class);
	}
	
	@Test
	public void testSSSConstructor1() throws TLVException, WrongParameterException {
		SmppByteBuffer bb = new SmppByteBuffer();
		bb.appendShort(0x020f);
		bb.appendShort(0x0001);
		bb.appendByte((byte)0x70);
		SarSegmentSeqnum sss = new SarSegmentSeqnum(bb.array());
		assertEquals(ParameterTag.SAR_SEGMENT_SEQNUM, sss.getTag());
		assertEquals(5, sss.getBytes().length);
		assertEquals((short)112, sss.getValue());
		assertEquals("020f000170", new SmppByteBuffer(sss.getBytes()).getHexDump());
	}

    @Test(expected = TLVNotFoundException.class)
	public void testSSSConstructor2() throws TLVException, WrongParameterException {
		SmppByteBuffer bb = new SmppByteBuffer();
		bb.appendShort(0x0000);
		bb.appendShort(0x0002);
		bb.appendByte((byte)0x1111);
		new SarSegmentSeqnum(bb.array());
	}
	
	@Test(expected= TLVException.class)
	public void testSSSConstructor3() throws TLVException, WrongParameterException {
		SmppByteBuffer bb = new SmppByteBuffer();
		bb.appendShort(0x020f);
		bb.appendShort(0x0002);
		bb.appendShort(0x1111);
		new SarSegmentSeqnum(bb.array());
	}
	
	@Test
	public void testSSSConstructor4() throws TLVException, WrongParameterException {
		SarSegmentSeqnum sss = new SarSegmentSeqnum((byte)112);
		assertEquals(ParameterTag.SAR_SEGMENT_SEQNUM, sss.getTag());
		assertEquals(5, sss.getBytes().length);
		assertEquals((short)112, sss.getValue());
		assertEquals("020f000170", new SmppByteBuffer(sss.getBytes()).getHexDump());
	}
	
	@Test(expected= TLVException.class)
	public void testSSSConstructor5() throws TLVException, WrongParameterException {
		SmppByteBuffer bb = new SmppByteBuffer();
		bb.appendShort(0x020f);
		bb.appendShort(0x0001);
		bb.appendShort(0x0001);
		new SarSegmentSeqnum(bb.array());
	}
	
	@Test(expected=ClassCastException.class)
	public void testSSSConstructor6() throws TLVException, WrongParameterException {
		SmppByteBuffer bb = new SmppByteBuffer();
		bb.appendShort(0x0005);
		bb.appendShort(0x0001);
		bb.appendByte((byte)0x0f);
		new SarSegmentSeqnum(bb.array());
	}

}
