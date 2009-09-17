package junit4.smpp.domain.tlv;

import junit.framework.JUnit4TestAdapter;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.bulatnig.smpp.pdu.tlv.DestinationPort;
import org.bulatnig.smpp.pdu.tlv.ParameterTag;
import org.bulatnig.smpp.pdu.tlv.TLVException;
import org.bulatnig.smpp.pdu.tlv.TLVNotFoundException;
import org.bulatnig.smpp.util.SMPPByteBuffer;
import org.bulatnig.smpp.util.WrongParameterException;

public class DestinationPortTest {
	
	// Used for backward compatibility (IDEs, Ant and JUnit 3 text runner)
	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(DestinationPortTest.class);
	}
	
	@Test
	public void testDPConstructor1() throws TLVException, WrongParameterException {
		SMPPByteBuffer bb = new SMPPByteBuffer();
		bb.appendShort(0x020b);
		bb.appendShort(0x0002);
		bb.appendShort(0x1111);
		DestinationPort dp = new DestinationPort(bb.getBuffer());
		assertEquals(ParameterTag.DESTINATION_PORT, dp.getTag());
		assertEquals(6, dp.getBytes().length);
		assertEquals(4369, dp.getValue());
		assertEquals("020b00021111", new SMPPByteBuffer(dp.getBytes()).getHexDump());
	}

    @Test(expected = TLVNotFoundException.class)
	public void testDPConstructor2() throws TLVException, WrongParameterException {
		SMPPByteBuffer bb = new SMPPByteBuffer();
		bb.appendShort(0x0000);
		bb.appendShort(0x0002);
		bb.appendByte((byte)0x1111);
		new DestinationPort(bb.getBuffer());
	}
	
	@Test(expected= TLVException.class)
	public void testDPConstructor3() throws TLVException, WrongParameterException {
		SMPPByteBuffer bb = new SMPPByteBuffer();
		bb.appendShort(0x020b);
		bb.appendShort(0x0001);
		bb.appendShort(0x1111);
		new DestinationPort(bb.getBuffer());
	}
	
	@Test
	public void testDPConstructor4() throws TLVException, WrongParameterException {
		DestinationPort dp = new DestinationPort((short)112);
		assertEquals(ParameterTag.DESTINATION_PORT, dp.getTag());
		assertEquals(6, dp.getBytes().length);
		assertEquals(112, dp.getValue());
		assertEquals("020b00020070", new SMPPByteBuffer(dp.getBytes()).getHexDump());
	}
	
	@Test(expected= TLVException.class)
	public void testDPConstructor5() throws TLVException, WrongParameterException {
		SMPPByteBuffer bb = new SMPPByteBuffer();
		bb.appendShort(0x020b);
		bb.appendShort(0x0001);
		bb.appendByte((byte)0x11);
		new DestinationPort(bb.getBuffer());
	}
	
	@Test(expected=ClassCastException.class)
	public void testDPConstructor6() throws TLVException, WrongParameterException {
		SMPPByteBuffer bb = new SMPPByteBuffer();
		bb.appendShort(0x0005);
		bb.appendShort(0x0002);
		bb.appendShort(0x7fff);
		new DestinationPort(bb.getBuffer());
	}

}
