package org.bulatnig.smpp.domain.pdu;

import junit.framework.JUnit4TestAdapter;
import static org.junit.Assert.assertEquals;

import org.bulatnig.smpp.util.SmppByteBuffer;
import org.junit.Test;
import org.bulatnig.smpp.pdu.*;
import org.bulatnig.smpp.pdu.tlv.MessagePayload;
import org.bulatnig.smpp.pdu.tlv.AlertOnMessageDelivery;
import org.bulatnig.smpp.util.WrongParameterException;

/**
 * Comment here.
 * <p/>
 * User: Bulat Nigmatullin
 * Date: Nov 1, 2008
 * Time: 11:51:46 AM
 */
public class PDUFactoryImplTest {

    // Used for backward compatibility (IDEs, Ant and JUnit 3 text runner)
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(PDUFactoryImplTest.class);
    }

    @Test
    public void testPDUFactory1() throws PDUException {
        PDUFactory factory = PDUFactoryImpl.INSTANCE;
        DeliverSM deliver = new DeliverSM();
        deliver.setMessagePayload(new MessagePayload("hello"));
        assertEquals("hello", ((DeliverSM) factory.parsePDU(deliver.getBytes())).getMessagePayload().getValue());
    }

    @Test(expected = PDUException.class)
    public void testPDUFactory2() throws PDUException, WrongParameterException {
        PDUFactory factory = PDUFactoryImpl.INSTANCE;
        SubmitSM submit = new SubmitSM();
        submit.setSequenceNumber(5);
        SmppByteBuffer bb = new SmppByteBuffer(submit.getBytes());
        bb.appendShort(0x0004);
        factory.parsePDU(bb.array());
    }

    @Test(expected = PDUException.class)
    public void testPDUFactory3() throws PDUException, WrongParameterException {
        PDUFactory factory = PDUFactoryImpl.INSTANCE;
        factory.parsePDU(new byte[0]);
    }

    @Test
    public void testPDUFactory4() throws WrongParameterException, PDUException {
        //000000248000000400000000000000023532363537393837393534343536363430343700
        SmppByteBuffer sbb = new SmppByteBuffer();
        sbb.appendInt(0x00000024L);
        sbb.appendInt(0x80000004L);
        sbb.appendInt(0x00000000L);
        sbb.appendInt(0x00000002L);
        sbb.appendCString("5265798795445664047");
        assertEquals("000000248000000400000000000000023532363537393837393534343536363430343700", sbb.getHexDump());
        PDUFactory factory = PDUFactoryImpl.INSTANCE;
        factory.parsePDU(sbb.array());
    }

    @Test
    public void testPDUFactory5() throws WrongParameterException, PDUException {
        //000000550000000500000000000009b300020138393031353732393535310002013731373100000000000001000000000204000201a91401000d303831313037313535333338000424000a38352b33323735383736
        //000000550000000500000000000009b3 00 02 01 383930313537323935353100 02 01 3731373100 00 00 00 00 00 01 00 00 00 00 0204 0002 01a9 1401 000d 30383131303731353533333800 0424 000a 38352b33323735383736
        SmppByteBuffer sbb = new SmppByteBuffer();
        sbb.appendInt(0x00000055L);
        sbb.appendInt(0x00000005L);
        sbb.appendInt(0x00000000L);
        sbb.appendInt(0x000009b3L);
        sbb.appendByte((short) 0x00);
        sbb.appendByte((short) 0x02);
        sbb.appendByte((short) 0x01);
        sbb.appendCString("89015729551");
        sbb.appendByte((short) 0x02);
        sbb.appendByte((short) 0x01);
        sbb.appendCString("7171");
        sbb.appendByte((short) 0x00);
        sbb.appendByte((short) 0x00);
        sbb.appendByte((short) 0x00);
        sbb.appendByte((short) 0x00);
        sbb.appendByte((short) 0x00);
        sbb.appendByte((short) 0x01);
        sbb.appendByte((short) 0x00);
        sbb.appendByte((short) 0x00);
        sbb.appendByte((short) 0x00);
        sbb.appendByte((short) 0x00);
        sbb.appendShort(0x0204);
        sbb.appendShort(0x0002);
        sbb.appendShort(0x01a9);
        sbb.appendShort(0x1401);
        sbb.appendShort(0x000d);
        sbb.appendCString("081107155338");
        sbb.appendShort(0x0424);
        sbb.appendShort(0x000a);
        sbb.appendString("85+3275876");
        assertEquals("000000550000000500000000000009b300020138393031353732393535310002013731373100000000000001000000000204000201a91401000d303831313037313535333338000424000a38352b33323735383736", sbb.getHexDump());
        PDUFactory factory = PDUFactoryImpl.INSTANCE;
        factory.parsePDU(sbb.array());
    }

    @Test
    public void testPDUFactory6() throws WrongParameterException, PDUException {
        //0000007e00000004000000000000000e000001313935300001013739323631373537323633000000000000000008004e00280421043e043d044f002c00200049004300510029000a043f044004380432000a0020041e044204320435044204380442044c0020043200200049004300510020044104350439044704300441
        // 0000007e 00000004 00000000 0000000e 00 00 01 3139353000 01 01 373932363137353732363300 00 00 00 00 00 00 00 08 00 4e
        // 00280421 043e043d 044f002c 00200049 00430051 0029000a 043f0440 04380432 000a0020 041e0442 04320435 04420438 0442044c 00200432 00200049 00430051 00200441 04350439 04470430 0441
        SmppByteBuffer sbb = new SmppByteBuffer();
        sbb.appendInt(0x0000007eL);
        sbb.appendInt(0x00000004L);
        sbb.appendInt(0x00000000L);
        sbb.appendInt(0x0000000eL);
        sbb.appendByte((short) 0x00);
        sbb.appendByte((short) 0x00);
        sbb.appendByte((short) 0x01);
        sbb.appendCString("1950");
        sbb.appendByte((short) 0x01);
        sbb.appendByte((short) 0x01);
        sbb.appendCString("79261757263");
        sbb.appendByte((short) 0x00);
        sbb.appendByte((short) 0x00);
        sbb.appendByte((short) 0x00);
        sbb.appendByte((short) 0x00);
        sbb.appendByte((short) 0x00);
        sbb.appendByte((short) 0x00);
        sbb.appendByte((short) 0x00);
        sbb.appendByte((short) 0x08);
        sbb.appendByte((short) 0x00);
        sbb.appendByte((short) 0x4e);
        sbb.appendInt(0x00280421L);
        sbb.appendInt(0x043e043dL);
        sbb.appendInt(0x044f002cL);
        sbb.appendInt(0x00200049L);
        sbb.appendInt(0x00430051L);
        sbb.appendInt(0x0029000aL);
        sbb.appendInt(0x043f0440L);
        sbb.appendInt(0x04380432L);
        sbb.appendInt(0x000a0020L);
        sbb.appendInt(0x041e0442L);
        sbb.appendInt(0x04320435L);
        sbb.appendInt(0x04420438L);
        sbb.appendInt(0x0442044cL);
        sbb.appendInt(0x00200432L);
        sbb.appendInt(0x00200049L);
        sbb.appendInt(0x00430051L);
        sbb.appendInt(0x00200441L);
        sbb.appendInt(0x04350439L);
        sbb.appendInt(0x04470430L);
        sbb.appendShort(0x0441);
        assertEquals("0000007e00000004000000000000000e000001313935300001013739323631373537323633000000000000000008004e00280421043e043d044f002c00200049004300510029000a043f044004380432000a0020041e044204320435044204380442044c0020043200200049004300510020044104350439044704300441", sbb.getHexDump());
        PDUFactory factory = PDUFactoryImpl.INSTANCE;
        factory.parsePDU(sbb.array());
    }

    @Test
    public void testPDUFactory7() throws WrongParameterException, PDUException {
        //000000bc0000000400000000000001f0000001313935330001013737303131353734383430000000000000000008008c00270061007300640061007300640027002000200020002000200020002000200020002000200020002000200020002000200020002000200020002000200020002000200020002000200020002000200020002000200020002000200020002000200020002000200020002000200020002000200020002000200020002000200020002000200020002000b7
        //000000bc 00000004 00000000 000001f0 00 00 01 3139353300 01 01 373730313135373438343000 00 00 00 00 00 00 00 08 00 8c
        // 00270061 00730064 00610073 00640027 00200020 00200020 00200020 00200020 00200020 00200020 00200020 00200020 00200020 00200020 00200020 00200020 00200020 00200020 00200020 00200020
        // 00200020 00200020 00200020 00200020 00200020 00200020 00200020 00200020 00200020 00200020 00200020 00200020 00200020 00200020 002000b7
        SmppByteBuffer sbb = new SmppByteBuffer();
        sbb.appendInt(0x000000bcL);
        sbb.appendInt(0x00000004L);
        sbb.appendInt(0x00000000L);
        sbb.appendInt(0x000001f0L);
        sbb.appendByte((short) 0x00);
        sbb.appendByte((short) 0x00);
        sbb.appendByte((short) 0x01);
        sbb.appendCString("1953");
        sbb.appendByte((short) 0x01);
        sbb.appendByte((short) 0x01);
        sbb.appendCString("77011574840");
        sbb.appendByte((short) 0x00);
        sbb.appendByte((short) 0x00);
        sbb.appendByte((short) 0x00);
        sbb.appendByte((short) 0x00);
        sbb.appendByte((short) 0x00);
        sbb.appendByte((short) 0x00);
        sbb.appendByte((short) 0x00);
        sbb.appendByte((short) 0x08);
        sbb.appendByte((short) 0x00);
        sbb.appendByte((short) 0x8c);
        sbb.appendInt(0x00270061L);
        sbb.appendInt(0x00730064L);
        sbb.appendInt(0x00610073L);
        sbb.appendInt(0x00640027L);
        sbb.appendInt(0x00200020L);
        sbb.appendInt(0x00200020L);
        sbb.appendInt(0x00200020L);
        sbb.appendInt(0x00200020L);
        sbb.appendInt(0x00200020L);
        sbb.appendInt(0x00200020L);
        sbb.appendInt(0x00200020L);
        sbb.appendInt(0x00200020L);
        sbb.appendInt(0x00200020L);
        sbb.appendInt(0x00200020L);
        sbb.appendInt(0x00200020L);
        sbb.appendInt(0x00200020L);
        sbb.appendInt(0x00200020L);
        sbb.appendInt(0x00200020L);
        sbb.appendInt(0x00200020L);
        sbb.appendInt(0x00200020L);
        sbb.appendInt(0x00200020L);
        sbb.appendInt(0x00200020L);
        sbb.appendInt(0x00200020L);
        sbb.appendInt(0x00200020L);
        sbb.appendInt(0x00200020L);
        sbb.appendInt(0x00200020L);
        sbb.appendInt(0x00200020L);
        sbb.appendInt(0x00200020L);
        sbb.appendInt(0x00200020L);
        sbb.appendInt(0x00200020L);
        sbb.appendInt(0x00200020L);
        sbb.appendInt(0x00200020L);
        sbb.appendInt(0x00200020L);
        sbb.appendInt(0x00200020L);
        sbb.appendInt(0x002000b7L);
        assertEquals("000000bc0000000400000000000001f0000001313935330001013737303131353734383430000000000000000008008c00270061007300640061007300640027002000200020002000200020002000200020002000200020002000200020002000200020002000200020002000200020002000200020002000200020002000200020002000200020002000200020002000200020002000200020002000200020002000200020002000200020002000200020002000200020002000b7", sbb.getHexDump());
        PDUFactory factory = PDUFactoryImpl.INSTANCE;
        SubmitSM submit = (SubmitSM)factory.parsePDU(sbb.array());
        assertEquals("'asdasd'                                                             ·", submit.getShortMessage());
    }

    @Test
    public void testPDUFactory8() throws WrongParameterException, PDUException {
        // 0000004f 00000005 00000000 076af6d4 00 01 01 373930323831343732323300 01 01
        // 3831383123323339333939323700 83 00 00 00 00 00 00 00 00 05 592b313034
        // 1383 0002 0000 0204 0002 005f 0501 0001 23
        SmppByteBuffer sbb = new SmppByteBuffer();
        sbb.appendInt(0x0000004fL);
        sbb.appendInt(0x00000005L);
        sbb.appendInt(0x00000000L);
        sbb.appendInt(0x076af6d4L);
        sbb.appendByte((short) 0x00);
        sbb.appendByte((short) 0x01);
        sbb.appendByte((short) 0x01);
        sbb.appendCString("79028147223");
        sbb.appendByte((short) 0x01);
        sbb.appendByte((short) 0x01);
        sbb.appendCString("8181#23939927");
        sbb.appendByte((short) 0x83);
        sbb.appendByte((short) 0x00);
        sbb.appendByte((short) 0x00);
        sbb.appendByte((short) 0x00);
        sbb.appendByte((short) 0x00);
        sbb.appendByte((short) 0x00);
        sbb.appendByte((short) 0x00);
        sbb.appendByte((short) 0x00);
        sbb.appendByte((short) 0x00);
        sbb.appendByte((short) 0x05);
        sbb.appendInt(0x592b3130L);
        sbb.appendByte((short) 0x34);
        sbb.appendShort(0x1383);
        sbb.appendShort(0x0002);
        sbb.appendShort(0x0000);
        sbb.appendShort(0x0204);
        sbb.appendShort(0x0002);
        sbb.appendShort(0x005f);
        sbb.appendShort(0x0501);
        sbb.appendShort(0x0001);
        sbb.appendByte((short) 0x23);
        assertEquals("0000004f0000000500000000076af6d40001013739303238313437323233000101383138312332333933393932370083000000000000000005592b31303413830002000002040002005f0501000123", sbb.getHexDump());
        PDUFactory factory = PDUFactoryImpl.INSTANCE;
        factory.parsePDU(sbb.array());
    }

    @Test
    public void testPDUFactory9() throws WrongParameterException, PDUException {
        SmppByteBuffer sbb = new SmppByteBuffer();
        sbb.appendInt(0x00000010L);
        sbb.appendInt(0x80000004L);
        sbb.appendInt(0x00000443L);
        sbb.appendInt(0x000dff13L);
        assertEquals("000000108000000400000443000dff13", sbb.getHexDump());
        PDUFactory factory = PDUFactoryImpl.INSTANCE;
        PDU pdu = factory.parsePDU(sbb.array());
        assertEquals(16, pdu.getCommandLength());
        assertEquals(CommandId.SUBMIT_SM_RESP, pdu.getCommandId());
        assertEquals(CommandStatus.RESERVED, pdu.getCommandStatus());
        assertEquals(1091, pdu.getCommandStatusValue());
        assertEquals(917267, pdu.getSequenceNumber());
    }

    @Test
    public void testPDUFactory10() throws WrongParameterException, PDUException {
        SubmitSM submit = new SubmitSM();
        submit.setAlertOnMessageDelivery(new AlertOnMessageDelivery());
        PDUFactory factory = PDUFactoryImpl.INSTANCE;
        factory.parsePDU(submit.getBytes());
    }

}
