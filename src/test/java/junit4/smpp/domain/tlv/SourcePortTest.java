package junit4.smpp.domain.tlv;

import junit.framework.JUnit4TestAdapter;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.bulatnig.smpp.pdu.tlv.ParameterTag;
import org.bulatnig.smpp.pdu.tlv.SourcePort;
import org.bulatnig.smpp.pdu.tlv.TLVException;
import org.bulatnig.smpp.pdu.tlv.TLVNotFoundException;
import org.bulatnig.smpp.util.SMPPByteBuffer;
import org.bulatnig.smpp.util.WrongParameterException;

public class SourcePortTest {
	
	// Used for backward compatibility (IDEs, Ant and JUnit 3 text runner)
	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(SourcePortTest.class);
	}
	
	@Test
	public void testSPConstructor1() throws TLVException, WrongParameterException {
		SMPPByteBuffer bb = new SMPPByteBuffer();
		bb.appendShort(0x020a);
		bb.appendShort(0x0002);
		bb.appendShort(0x1111);
		SourcePort sp = new SourcePort(bb.getBuffer());
		assertEquals(ParameterTag.SOURCE_PORT, sp.getTag());
		assertEquals(6, sp.getBytes().length);
		assertEquals(4369, sp.getValue());
		assertEquals("020a00021111", new SMPPByteBuffer(sp.getBytes()).getHexDump());
	}

    @Test(expected = TLVNotFoundException.class)
	public void testSPConstructor2() throws TLVException, WrongParameterException {
		SMPPByteBuffer bb = new SMPPByteBuffer();
		bb.appendShort(0x0000);
		bb.appendShort(0x0002);
		bb.appendByte((byte)0x1111);
		new SourcePort(bb.getBuffer());
	}
	
	@Test(expected= TLVException.class)
	public void testSPConstructor3() throws TLVException, WrongParameterException {
		SMPPByteBuffer bb = new SMPPByteBuffer();
		bb.appendShort(0x020a);
		bb.appendShort(0x0001);
		bb.appendShort(0x1111);
		new SourcePort(bb.getBuffer());
	}
	
	@Test
	public void testSPConstructor4() throws TLVException, WrongParameterException {
		SourcePort sp = new SourcePort((short)112);
		assertEquals(ParameterTag.SOURCE_PORT, sp.getTag());
		assertEquals(6, sp.getBytes().length);
		assertEquals(112, sp.getValue());
		assertEquals("020a00020070", new SMPPByteBuffer(sp.getBytes()).getHexDump());
	}
	
	@Test(expected= TLVException.class)
	public void testSPConstructor5() throws TLVException, WrongParameterException {
		SMPPByteBuffer bb = new SMPPByteBuffer();
		bb.appendShort(0x020a);
		bb.appendShort(0x0001);
		bb.appendByte((byte)0x11);
		new SourcePort(bb.getBuffer());
	}
	
	@Test(expected=ClassCastException.class)
	public void testSPConstructor6() throws WrongParameterException, TLVException {
		SMPPByteBuffer bb = new SMPPByteBuffer();
		bb.appendShort(0x0005);
		bb.appendShort(0x0002);
		bb.appendShort(0x7fff);
		new SourcePort(bb.getBuffer());
	}

}
