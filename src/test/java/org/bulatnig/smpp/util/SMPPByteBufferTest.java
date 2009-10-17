package org.bulatnig.smpp.util;

import junit.framework.JUnit4TestAdapter;
import org.bulatnig.smpp.util.SMPPByteBuffer;
import org.bulatnig.smpp.util.WrongLengthException;
import org.bulatnig.smpp.util.WrongParameterException;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Put file description here.
 * <p/>
 * User: Bulat Nigmatullin
 * Date: 04.06.2008
 * Time: 7:35:23
 */
public class SMPPByteBufferTest {

// Used for backward compatibility (IDEs, Ant and JUnit 3 text runner)

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(SMPPByteBufferTest.class);
    }

    @Test
    public void emptyConstructor() throws WrongLengthException {
        SMPPByteBuffer sbb = new SMPPByteBuffer();
        assertEquals(0, sbb.length());
        assertEquals("", sbb.getHexDump());
    }

    @Test
    public void nonEmptyConstructor() throws WrongLengthException {
        byte[] bb = new byte[4];
        for (int i = 0; i < bb.length; i++) {
            bb[i] = (byte) i;
        }
        SMPPByteBuffer sbb = new SMPPByteBuffer(bb);
        assertEquals(4, sbb.length());
        assertEquals("00010203", sbb.getHexDump());
        assertEquals(4, sbb.length());
        assertEquals(0, sbb.removeByte());
        assertEquals("010203", sbb.getHexDump());
        assertEquals(3, sbb.length());
        assertEquals(1, sbb.removeByte());
        assertEquals("0203", sbb.getHexDump());
        assertEquals(2, sbb.length());
        assertEquals(2, sbb.removeByte());
        assertEquals("03", sbb.getHexDump());
        assertEquals(1, sbb.length());
        assertEquals(3, sbb.removeByte());
        assertEquals("", sbb.getHexDump());
        assertEquals(0, sbb.length());
    }

    @Test
    public void appenders() throws WrongParameterException {
        SMPPByteBuffer sbb = new SMPPByteBuffer();
        sbb.appendByte((short) 255);
        sbb.appendShort(65535);
        sbb.appendInt(4294967295L);
        assertEquals("ffffffffffffff", sbb.getHexDump());
        assertEquals(7, sbb.length());

        sbb = new SMPPByteBuffer();
        sbb.appendByte((short) 0);
        sbb.appendShort(0);
        sbb.appendInt(0);
        assertEquals("00000000000000", sbb.getHexDump());
        assertEquals(7, sbb.length());

        sbb = new SMPPByteBuffer();
        sbb.appendByte((short) 127);
        sbb.appendShort(32767);
        sbb.appendInt(2147483647);
        assertEquals("7f7fff7fffffff", sbb.getHexDump());
        assertEquals(7, sbb.length());

        sbb = new SMPPByteBuffer();
        sbb.appendByte((short) 128);
        sbb.appendShort(32768);
        sbb.appendInt(2147483648L);
        assertEquals("80800080000000", sbb.getHexDump());
        assertEquals(7, sbb.length());

        sbb = new SMPPByteBuffer();
        sbb.appendByte((short) 129);
        sbb.appendShort(32769);
        sbb.appendInt(2147483649L);
        assertEquals("81800180000001", sbb.getHexDump());
        assertEquals(7, sbb.length());
    }

    @Test(expected = WrongParameterException.class)
    public void appendByteWrongParameterException1() throws WrongParameterException {
        SMPPByteBuffer sbb = new SMPPByteBuffer();
        sbb.appendByte((short) 256);
    }

    @Test(expected = WrongParameterException.class)
    public void appendByteWrongParameterException2() throws WrongParameterException {
        SMPPByteBuffer sbb = new SMPPByteBuffer();
        sbb.appendByte((short) -1);
    }

    @Test(expected = WrongParameterException.class)
    public void appendShortWrongParameterException1() throws WrongParameterException {
        SMPPByteBuffer sbb = new SMPPByteBuffer();
        sbb.appendShort(66666);
    }

    @Test(expected = WrongParameterException.class)
    public void appendShortWrongParameterException2() throws WrongParameterException {
        SMPPByteBuffer sbb = new SMPPByteBuffer();
        sbb.appendShort(-3000);
    }

    @Test(expected = WrongParameterException.class)
    public void appendIntWrongParameterException1() throws WrongParameterException {
        SMPPByteBuffer sbb = new SMPPByteBuffer();
        sbb.appendInt(4294967296L);
    }

    @Test(expected = WrongParameterException.class)
    public void appendIntWrongParameterException2() throws WrongParameterException {
        SMPPByteBuffer sbb = new SMPPByteBuffer();
        sbb.appendInt(-429496729600L);
    }

    @Test
    public void removers() throws WrongParameterException, WrongLengthException {
        SMPPByteBuffer sbb = new SMPPByteBuffer();
        assertEquals(0, sbb.length());
        sbb.appendByte((short) 150);
        sbb.appendByte((short) 70);
        assertEquals("9646", sbb.getHexDump());
        assertEquals(2, sbb.length());
        assertEquals(150, sbb.removeByte());
        assertEquals(70, sbb.removeByte());
        assertEquals(0, sbb.length());
        sbb.appendByte((short) 150);
        sbb.appendInt(4000000000L);
        sbb.appendShort(5000);
        assertEquals("96ee6b28001388", sbb.getHexDump());
        assertEquals(7, sbb.length());
        assertEquals(150, sbb.removeByte());
        assertEquals(4000000000L, sbb.removeInt());
        assertEquals(5000, sbb.removeShort());
        sbb.appendInt(3000000000L);
        assertEquals("b2d05e00", sbb.getHexDump());
        assertEquals(178, sbb.removeByte());
        assertEquals(208, sbb.removeByte());
        assertEquals(94, sbb.removeByte());
        assertEquals(0, sbb.removeByte());
    }

    @Test
    public void strings() throws WrongLengthException {
        SMPPByteBuffer sbb = new SMPPByteBuffer();
        String s = "good evening";
        sbb.appendCString(s);
        assertEquals("good",sbb.removeString(4));
        assertEquals(" evening",sbb.removeCString());
    }

    @Test
    public void nullStrings() throws WrongLengthException {
        SMPPByteBuffer sbb = new SMPPByteBuffer();
        String s = null;
        sbb.appendString(s);
        assertEquals("", sbb.removeString(0));
        sbb.appendCString(s);
        assertEquals("", sbb.removeCString());
        s = "";
        sbb.appendString(s);
        assertEquals("", sbb.removeString(0));
        sbb.appendCString(s);
        assertEquals("", sbb.removeString(0));
        assertEquals(1, sbb.length());
        assertEquals("", sbb.removeCString());
    }

    @Test
    public void asciiString() throws WrongLengthException {
        SMPPByteBuffer sbb = new SMPPByteBuffer();
        sbb.appendString("Hello");
        assertEquals(5, sbb.length());
        assertEquals("48656c6c6f", sbb.getHexDump());
        assertEquals("Hello", sbb.removeString(5));
    }

    @Test
    public void ucsString() throws WrongLengthException {
        SMPPByteBuffer sbb = new SMPPByteBuffer();
        sbb.appendString("Привет", "UTF-16BE");
        assertEquals(12, sbb.length());
        assertEquals("041f04400438043204350442", sbb.getHexDump());
        assertEquals("Привет", sbb.removeString(12, "UTF-16BE"));
    }

}

