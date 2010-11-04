package org.bulatnig.smpp.domain.tlv;

import junit.framework.JUnit4TestAdapter;
import static org.junit.Assert.assertEquals;

import org.bulatnig.smpp.util.SmppByteBuffer;
import org.junit.Test;
import org.bulatnig.smpp.pdu.tlv.ItsSessionInfo;
import org.bulatnig.smpp.pdu.tlv.ParameterTag;
import org.bulatnig.smpp.pdu.tlv.TLVException;
import org.bulatnig.smpp.pdu.tlv.TLVNotFoundException;
import org.bulatnig.smpp.util.WrongParameterException;

public class ItsSessionInfoTest {
	
	// Used for backward compatibility (IDEs, Ant and JUnit 3 text runner)
	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(ItsSessionInfoTest.class);
	}
	
	@Test
	public void testISIConstructor1() throws TLVException, WrongParameterException {
		SmppByteBuffer bb = new SmppByteBuffer();
		bb.appendShort(0x1383);
		bb.appendShort(0x0002);
		bb.appendShort(0x1111);
		ItsSessionInfo isi = new ItsSessionInfo(bb.array());
		assertEquals(ParameterTag.ITS_SESSION_INFO, isi.getTag());
		assertEquals(6, isi.getBytes().length);
		assertEquals(4369, isi.getValue());
		assertEquals("138300021111", new SmppByteBuffer(isi.getBytes()).getHexDump());
	}

    @Test(expected = TLVNotFoundException.class)
	public void testISIConstructor2() throws WrongParameterException, TLVException {
		SmppByteBuffer bb = new SmppByteBuffer();
		bb.appendShort(0x0000);
		bb.appendShort(0x0002);
		bb.appendByte((byte)0x1111);
		new ItsSessionInfo(bb.array());
	}
	
	@Test(expected= TLVException.class)
	public void testISIConstructor3() throws TLVException, WrongParameterException {
		SmppByteBuffer bb = new SmppByteBuffer();
		bb.appendShort(0x1383);
		bb.appendShort(0x0001);
		bb.appendShort(0x1111);
		new ItsSessionInfo(bb.array());
	}
	
	@Test
	public void testISIConstructor4() throws TLVException, WrongParameterException {
		ItsSessionInfo isi = new ItsSessionInfo((short)112);
		assertEquals(ParameterTag.ITS_SESSION_INFO, isi.getTag());
		assertEquals(6, isi.getBytes().length);
		assertEquals(112, isi.getValue());
		assertEquals("138300020070", new SmppByteBuffer(isi.getBytes()).getHexDump());
	}
	
	@Test(expected= TLVException.class)
	public void testISIConstructor5() throws TLVException, WrongParameterException {
		SmppByteBuffer bb = new SmppByteBuffer();
		bb.appendShort(0x1383);
		bb.appendShort(0x0001);
		bb.appendByte((byte)0x11);
		new ItsSessionInfo(bb.array());
	}
	
	@Test(expected=ClassCastException.class)
	public void testISIConstructor6() throws TLVException, WrongParameterException {
		SmppByteBuffer bb = new SmppByteBuffer();
		bb.appendShort(0x0005);
		bb.appendShort(0x0002);
		bb.appendShort(0x7fff);
		new ItsSessionInfo(bb.array());
	}

}
