package org.bulatnig.smpp.domain.tlv;

import junit.framework.JUnit4TestAdapter;
import static org.junit.Assert.assertEquals;

import org.bulatnig.smpp.util.SmppByteBuffer;
import org.junit.Test;
import org.bulatnig.smpp.pdu.tlv.ParameterTag;
import org.bulatnig.smpp.pdu.tlv.SetDpf;
import org.bulatnig.smpp.pdu.tlv.TLVException;
import org.bulatnig.smpp.pdu.tlv.TLVNotFoundException;
import org.bulatnig.smpp.util.WrongParameterException;

public class SetDpfTest {
	
	// Used for backward compatibility (IDEs, Ant and JUnit 3 text runner)
	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(SetDpfTest.class);
	}
	
	@Test
	public void testSDConstructor1() throws TLVException, WrongParameterException {
		SmppByteBuffer bb = new SmppByteBuffer();
		bb.appendShort(0x0421);
		bb.appendShort(0x0001);
		bb.appendByte((byte)0x70);
		SetDpf sd = new SetDpf(bb.array());
		assertEquals(ParameterTag.SET_DPF, sd.getTag());
		assertEquals(5, sd.getBytes().length);
		assertEquals((short)112, sd.getValue());
		assertEquals("0421000170", new SmppByteBuffer(sd.getBytes()).getHexDump());
	}

    @Test(expected = TLVNotFoundException.class)
	public void testSDConstructor2() throws TLVException, WrongParameterException {
		SmppByteBuffer bb = new SmppByteBuffer();
		bb.appendShort(0x0000);
		bb.appendShort(0x0002);
		bb.appendByte((byte)0x1111);
		new SetDpf(bb.array());
	}
	
	@Test(expected= TLVException.class)
	public void testSDConstructor3() throws TLVException, WrongParameterException {
		SmppByteBuffer bb = new SmppByteBuffer();
		bb.appendShort(0x0421);
		bb.appendShort(0x0002);
		bb.appendShort(0x1111);
		new SetDpf(bb.array());
	}
	
	@Test
	public void testSDConstructor4() throws TLVException, WrongParameterException {
		SetDpf sd = new SetDpf((byte)112);
		assertEquals(ParameterTag.SET_DPF, sd.getTag());
		assertEquals(5, sd.getBytes().length);
		assertEquals((short)112, sd.getValue());
		assertEquals("0421000170", new SmppByteBuffer(sd.getBytes()).getHexDump());
	}
	
	@Test(expected= TLVException.class)
	public void testSDConstructor5() throws TLVException, WrongParameterException {
		SmppByteBuffer bb = new SmppByteBuffer();
		bb.appendShort(0x0421);
		bb.appendShort(0x0001);
		bb.appendShort(0x0001);
		new SetDpf(bb.array());
	}
	
	@Test(expected=ClassCastException.class)
	public void testSDConstructor6() throws WrongParameterException, TLVException {
		SmppByteBuffer bb = new SmppByteBuffer();
		bb.appendShort(0x0005);
		bb.appendShort(0x0001);
		bb.appendByte((byte)0x0f);
		new SetDpf(bb.array());
	}

}
