package org.bulatnig.smpp.domain.tlv;

import junit.framework.JUnit4TestAdapter;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.bulatnig.smpp.pdu.tlv.ParameterTag;
import org.bulatnig.smpp.pdu.tlv.ScInterfaceVersion;
import org.bulatnig.smpp.pdu.tlv.TLVException;
import org.bulatnig.smpp.pdu.tlv.TLVNotFoundException;
import org.bulatnig.smpp.util.SMPPByteBuffer;
import org.bulatnig.smpp.util.WrongParameterException;

public class ScInterfaceVersionTest {
	
	// Used for backward compatibility (IDEs, Ant and JUnit 3 text runner)
	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(ScInterfaceVersionTest.class);
	}
	
	@Test
	public void testSIVConstructor1() throws TLVException, WrongParameterException {
		SMPPByteBuffer bb = new SMPPByteBuffer();
		bb.appendShort(0x0210);
		bb.appendShort(0x0001);
		bb.appendByte((byte)0x70);
		ScInterfaceVersion siv = new ScInterfaceVersion(bb.getBuffer());
		assertEquals(ParameterTag.SC_INTERFACE_VERSION, siv.getTag());
		assertEquals(5, siv.getBytes().length);
		assertEquals((short)112, siv.getValue());
		assertEquals("0210000170", new SMPPByteBuffer(siv.getBytes()).getHexDump());
	}

    @Test(expected = TLVNotFoundException.class)
	public void testSIVConstructor2() throws TLVException, WrongParameterException {
		SMPPByteBuffer bb = new SMPPByteBuffer();
		bb.appendShort(0x0000);
		bb.appendShort(0x0002);
		bb.appendByte((byte)0x1111);
		new ScInterfaceVersion(bb.getBuffer());
	}
	
	@Test(expected= TLVException.class)
	public void testSIVConstructor3() throws WrongParameterException, TLVException {
		SMPPByteBuffer bb = new SMPPByteBuffer();
		bb.appendShort(0x0210);
		bb.appendShort(0x0002);
		bb.appendShort(0x1111);
		new ScInterfaceVersion(bb.getBuffer());
	}
	
	@Test
	public void testSIVConstructor4() throws TLVException, WrongParameterException {
		ScInterfaceVersion siv = new ScInterfaceVersion((byte)112);
		assertEquals(ParameterTag.SC_INTERFACE_VERSION, siv.getTag());
		assertEquals(5, siv.getBytes().length);
		assertEquals((short)112, siv.getValue());
		assertEquals("0210000170", new SMPPByteBuffer(siv.getBytes()).getHexDump());
	}
	
	@Test(expected= TLVException.class)
	public void testSIVConstructor5() throws TLVException, WrongParameterException {
		SMPPByteBuffer bb = new SMPPByteBuffer();
		bb.appendShort(0x0210);
		bb.appendShort(0x0001);
		bb.appendShort(0x0001);
		new ScInterfaceVersion(bb.getBuffer());
	}
	
	@Test(expected=ClassCastException.class)
	public void testSIVConstructor6() throws TLVException, WrongParameterException {
		SMPPByteBuffer bb = new SMPPByteBuffer();
		bb.appendShort(0x0005);
		bb.appendShort(0x0001);
		bb.appendByte((byte)0x0f);
		new ScInterfaceVersion(bb.getBuffer());
	}

}
