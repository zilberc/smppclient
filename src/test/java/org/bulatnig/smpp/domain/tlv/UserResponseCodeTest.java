package org.bulatnig.smpp.domain.tlv;

import junit.framework.JUnit4TestAdapter;
import static org.junit.Assert.assertEquals;

import org.bulatnig.smpp.util.SmppByteBuffer;
import org.junit.Test;
import org.bulatnig.smpp.pdu.tlv.ParameterTag;
import org.bulatnig.smpp.pdu.tlv.TLVException;
import org.bulatnig.smpp.pdu.tlv.TLVNotFoundException;
import org.bulatnig.smpp.pdu.tlv.UserResponseCode;
import org.bulatnig.smpp.util.WrongParameterException;

public class UserResponseCodeTest {
	
	// Used for backward compatibility (IDEs, Ant and JUnit 3 text runner)
	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(UserResponseCodeTest.class);
	}
	
	@Test
	public void testURCConstructor1() throws TLVException, WrongParameterException {
		SmppByteBuffer bb = new SmppByteBuffer();
		bb.appendShort(0x0205);
		bb.appendShort(0x0001);
		bb.appendByte((byte)0x70);
		UserResponseCode urc = new UserResponseCode(bb.array());
		assertEquals(ParameterTag.USER_RESPONSE_CODE, urc.getTag());
		assertEquals(5, urc.getBytes().length);
		assertEquals((short)112, urc.getValue());
		assertEquals("0205000170", new SmppByteBuffer(urc.getBytes()).getHexDump());
	}

    @Test(expected = TLVNotFoundException.class)
	public void testURCConstructor2() throws TLVException, WrongParameterException {
		SmppByteBuffer bb = new SmppByteBuffer();
		bb.appendShort(0x0000);
		bb.appendShort(0x0002);
		bb.appendByte((byte)0x1111);
		new UserResponseCode(bb.array());
	}
	
	@Test(expected= TLVException.class)
	public void testURCConstructor3() throws TLVException, WrongParameterException {
		SmppByteBuffer bb = new SmppByteBuffer();
		bb.appendShort(0x0205);
		bb.appendShort(0x0002);
		bb.appendShort(0x1111);
		new UserResponseCode(bb.array());
	}
	
	@Test
	public void testURCConstructor4() throws TLVException, WrongParameterException {
		UserResponseCode urc = new UserResponseCode((byte)112);
		assertEquals(ParameterTag.USER_RESPONSE_CODE, urc.getTag());
		assertEquals(5, urc.getBytes().length);
		assertEquals((short)112, urc.getValue());
		assertEquals("0205000170", new SmppByteBuffer(urc.getBytes()).getHexDump());
	}
	
	@Test(expected= TLVException.class)
	public void testURCConstructor5() throws WrongParameterException, TLVException {
		SmppByteBuffer bb = new SmppByteBuffer();
		bb.appendShort(0x0205);
		bb.appendShort(0x0001);
		bb.appendShort(0x0001);
		new UserResponseCode(bb.array());
	}
	
	@Test(expected=ClassCastException.class)
	public void testURCConstructor6() throws TLVException, WrongParameterException {
		SmppByteBuffer bb = new SmppByteBuffer();
		bb.appendShort(0x0005);
		bb.appendShort(0x0001);
		bb.appendByte((byte)0x0f);
		new UserResponseCode(bb.array());
	}

}
