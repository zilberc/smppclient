package org.bulatnig.smpp.domain.pdu;

import junit.framework.JUnit4TestAdapter;
import static org.junit.Assert.assertEquals;

import org.bulatnig.smpp.util.SmppByteBuffer;
import org.junit.Test;
import org.bulatnig.smpp.pdu.*;
import org.bulatnig.smpp.pdu.udh.IEI;
import org.bulatnig.smpp.util.WrongParameterException;

/**
 * Comment here.
 * User: Bulat Nigmatullin
 * Date: 26.06.2008
 * Time: 20:11:19
 */
public class DeliverSMTest {

    // Used for backward compatibility (IDEs, Ant and JUnit 3 text runner)

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(DeliverSMTest.class);
    }

    @Test
    public void bytesToObject() throws WrongParameterException, PDUException {
        SmppByteBuffer sbb = new SmppByteBuffer();
        sbb.appendInt(82);
        sbb.appendInt(5);
        sbb.appendInt(21);
        sbb.appendInt(123987465L);
        sbb.appendCString("typo");
        sbb.appendByte((short) 1);
        sbb.appendByte((short) 3);
        sbb.appendCString("sender");
        sbb.appendByte((short) 2);
        sbb.appendByte((short) 4);
        sbb.appendCString("receiver with long");
        sbb.appendByte((short) 14);
        sbb.appendByte((short) 15);
        sbb.appendByte((short) 16);
        sbb.appendCString("");
        sbb.appendCString("");
        sbb.appendByte((short) 17);
        sbb.appendByte((short) 18);
        sbb.appendByte((short) 19);
        sbb.appendByte((short) 20);
        sbb.appendByte((short) 21);
        sbb.appendString("aaaaaaabbbbbbbsssssss");
        DeliverSM deliver = new DeliverSM(sbb.getBuffer());
        assertEquals(82L, deliver.getCommandLength());
        assertEquals(5L, deliver.getCommandId().getValue());
        assertEquals(CommandStatus.ESME_RINVSERTYP, deliver.getCommandStatus());
        assertEquals(21L, deliver.getCommandStatus().getValue());
        assertEquals(123987465L, deliver.getSequenceNumber());
        assertEquals("typo", deliver.getServiceType());
        assertEquals(TON.INTERNATIONAL, deliver.getSourceAddrTon());
        assertEquals(NPI.DATA, deliver.getSourceAddrNpi());
        assertEquals("sender", deliver.getSourceAddr());
        assertEquals(TON.NATIONAL, deliver.getDestAddrTon());
        assertEquals(NPI.TELEX, deliver.getDestAddrNpi());
        assertEquals("receiver with long", deliver.getDestinationAddr());
        assertEquals(SmscEsmClass.SmscMessagingMode.DEFAULT, deliver.getEsmClass().getMode());
        assertEquals(SmscEsmClass.SmscMessageType.RESERVER, deliver.getEsmClass().getType());
        assertEquals(SmscEsmClass.SmscGSMFeatures.NO_FEATURES, deliver.getEsmClass().getFeatures());
        assertEquals((short) 15, deliver.getProtocolId());
        assertEquals((short) 16, deliver.getPriorityFlag());
        assertEquals("", deliver.getScheduleDeliveryTime());
        assertEquals("", deliver.getValidityPeriod());
        assertEquals((short) 17, deliver.getRegisteredDelivery());
        assertEquals((short) 18, deliver.getReplaceIfPresentFlag());
        assertEquals((short) 19, deliver.getDataCoding());
        assertEquals((short) 20, deliver.getSmDefaultMsgId());
        assertEquals((short) 21, deliver.getSmLength());
        assertEquals("aaaaaaabbbbbbbsssssss", deliver.getShortMessage());
    }

    @Test
    public void objectToBytes() throws PDUException {
        DeliverSM deliver = new DeliverSM();
        assertEquals("000000210000000500000000000000000000000000000000000000000000000000",
                new SmppByteBuffer(deliver.getBytes()).getHexDump());
    }

    @Test
    public void udhiIgnoreTest() throws Exception {
        // 000000480000000500000000047f3a78000101373930353738393639383000030135313531233139393430393439004000000000000000000f050a03000904372b37373730323437
        // 00000048 00000005 00000000 047f3a78 00 01 01 373930353738393639383000 03 01 3531353123313939343039343900 40 00 00 00 00 00 00 00 00 0f 050a03000904 372b37373730323437
        SmppByteBuffer sbb = new SmppByteBuffer();
        sbb.appendInt(72);
        sbb.appendInt(5);
        sbb.appendInt(0);
        sbb.appendInt(75446904L);
        sbb.appendCString("");
        sbb.appendByte((short) 1);
        sbb.appendByte((short) 1);
        sbb.appendCString("79057896980");
        sbb.appendByte((short) 3);
        sbb.appendByte((short) 1);
        sbb.appendCString("5151#19940949");
        sbb.appendByte((short) 64);
        sbb.appendByte((short) 0);
        sbb.appendByte((short) 0);
        sbb.appendCString("");
        sbb.appendCString("");
        sbb.appendByte((short) 0);
        sbb.appendByte((short) 0);
        sbb.appendByte((short) 0);
        sbb.appendByte((short) 0);
        sbb.appendByte((short) 15);
        sbb.appendByte((short) 05);
        sbb.appendByte((short) 10);
        sbb.appendInt(50333956L);
        sbb.appendString("7+7770247");
        assertEquals("000000480000000500000000047f3a78000101373930353738393639383000030135313531233139393430393439004000000000000000000f050a03000904372b37373730323437", sbb.getHexDump());
        DeliverSM deliver = new DeliverSM(sbb.getBuffer());
        assertEquals("7+7770247", deliver.getShortMessage());
    }

    @Test
    public void udhiToSarTest() throws Exception {
        // 000000a300000005000000000001361000010137393236313733323030350000013139353000400000000000000000730500030b0202616b2076656465772c207469207365626961206e6520766964657720692064756d6165772034746f20767365206e6f726d616c6e6f2c20706f64756d61692034746f2074692064656c6165772c2074766f6920616c6b6f676f6c207673652074656265206973706f727469742e
        // 000000a3 00000005 00000000 00013610 00 01 01 373932363137333230303500 00 01 3139353000 40 00 00 00 00 00 00 00 00 73
        // 0500030b 0202616b 20766564 65772c20 74692073 65626961 206e6520 76696465 77206920 64756d61 65772034 746f2076
        // 7365206e 6f726d61 6c6e6f2c 20706f64 756d6169 2034746f 20746920 64656c61 65772c20 74766f69 20616c6b 6f676f6c
        // 20767365 20746562 65206973 706f7274 69742e
        SmppByteBuffer sbb = new SmppByteBuffer();
        sbb.appendInt(0x000000a3L);
        sbb.appendInt(0x00000005L);
        sbb.appendInt(0x00000000L);
        sbb.appendInt(0x00013610L);
        sbb.appendByte((short) 0x00);
        sbb.appendByte((short) 0x01);
        sbb.appendByte((short) 0x01);
        sbb.appendCString("79261732005");
        sbb.appendByte((short) 0x00);
        sbb.appendByte((short) 0x01);
        sbb.appendCString("1950");
        sbb.appendByte((short) 0x40);
        sbb.appendByte((short) 0x00);
        sbb.appendByte((short) 0x00);
        sbb.appendByte((short) 0x00);
        sbb.appendByte((short) 0x00);
        sbb.appendByte((short) 0x00);
        sbb.appendByte((short) 0x00);
        sbb.appendByte((short) 0x00);
        sbb.appendByte((short) 0x00);
        sbb.appendByte((short) 0x73);
        sbb.appendInt(0x0500030bL);
        sbb.appendInt(0x0202616bL);
        sbb.appendInt(0x20766564L);
        sbb.appendInt(0x65772c20L);
        sbb.appendInt(0x74692073L);
        sbb.appendInt(0x65626961L);
        sbb.appendInt(0x206e6520L);
        sbb.appendInt(0x76696465L);
        sbb.appendInt(0x77206920L);
        sbb.appendInt(0x64756d61L);
        sbb.appendInt(0x65772034L);
        sbb.appendInt(0x746f2076L);
        sbb.appendInt(0x7365206eL);
        sbb.appendInt(0x6f726d61L);
        sbb.appendInt(0x6c6e6f2cL);
        sbb.appendInt(0x20706f64L);
        sbb.appendInt(0x756d6169L);
        sbb.appendInt(0x2034746fL);
        sbb.appendInt(0x20746920L);
        sbb.appendInt(0x64656c61L);
        sbb.appendInt(0x65772c20L);
        sbb.appendInt(0x74766f69L);
        sbb.appendInt(0x20616c6bL);
        sbb.appendInt(0x6f676f6cL);
        sbb.appendInt(0x20767365L);
        sbb.appendInt(0x20746562L);
        sbb.appendInt(0x65206973L);
        sbb.appendInt(0x706f7274L);
        sbb.appendByte((short) 0x69);
        sbb.appendByte((short) 0x74);
        sbb.appendByte((short) 0x2e);
        assertEquals("000000a300000005000000000001361000010137393236313733323030350000013139353000400000000000000000730500030b0202616b2076656465772c207469207365626961206e6520766964657720692064756d6165772034746f20767365206e6f726d616c6e6f2c20706f64756d61692034746f2074692064656c6165772c2074766f6920616c6b6f676f6c207673652074656265206973706f727469742e", sbb.getHexDump());
        DeliverSM deliver = new DeliverSM(sbb.getBuffer());
        assertEquals("ak vedew, ti sebia ne videw i dumaew 4to vse normalno, podumai 4to ti delaew, tvoi alkogol vse tebe isportit.", deliver.getShortMessage());
        IEI iei = (IEI) deliver.getUdh();
        assertEquals(11, iei.getMsgRefNum());
        assertEquals(2, iei.getTotalSegments());
        assertEquals(2, iei.getSegmentSeqnum());
    }

    @Test
    public void messagePayload() throws Exception {
        // 00000094000000050000000000000ece000101373931383535303031323700000131393531000000000000000008000004240060041a043e0442043e0440044b04390020044104350433043e0434043d044f00200434043e043b04360435043d00200431044b0442044c00200438043b04380020043a043e0442043e0440044b0439002004320447043504400430003a002d0029
        // 00000094 00000005 00000000 00000ece 00 01 01 373931383535303031323700 00 01 3139353100 00 00 00 00 00 00 00 08 00 00
        // 04240060 041a043e 0442043e 0440044b 04390020 04410435 0433043e 0434043d 044f0020 0434043e 043b0436 0435043d
        // 00200431 044b0442 044c0020 0438043b 04380020 043a043e 0442043e 0440044b 04390020 04320447 04350440 0430003a
        // 002d0029
        SmppByteBuffer sbb = new SmppByteBuffer();
        sbb.appendInt(0x00000094L);
        sbb.appendInt(0x00000005L);
        sbb.appendInt(0x00000000L);
        sbb.appendInt(0x00000eceL);
        sbb.appendByte((short) 0x00);
        sbb.appendByte((short) 0x01);
        sbb.appendByte((short) 0x01);
        sbb.appendCString("79185500127");
        sbb.appendByte((short) 0x00);
        sbb.appendByte((short) 0x01);
        sbb.appendCString("1951");
        sbb.appendByte((short) 0x00);
        sbb.appendByte((short) 0x00);
        sbb.appendByte((short) 0x00);
        sbb.appendByte((short) 0x00);
        sbb.appendByte((short) 0x00);
        sbb.appendByte((short) 0x00);
        sbb.appendByte((short) 0x00);
        sbb.appendByte((short) 0x08);
        sbb.appendByte((short) 0x00);
        sbb.appendByte((short) 0x00);
        sbb.appendInt(0x04240060L);
        sbb.appendInt(0x041a043eL);
        sbb.appendInt(0x0442043eL);
        sbb.appendInt(0x0440044bL);
        sbb.appendInt(0x04390020L);
        sbb.appendInt(0x04410435L);
        sbb.appendInt(0x0433043eL);
        sbb.appendInt(0x0434043dL);
        sbb.appendInt(0x044f0020L);
        sbb.appendInt(0x0434043eL);
        sbb.appendInt(0x043b0436L);
        sbb.appendInt(0x0435043dL);
        sbb.appendInt(0x00200431L);
        sbb.appendInt(0x044b0442L);
        sbb.appendInt(0x044c0020L);
        sbb.appendInt(0x0438043bL);
        sbb.appendInt(0x04380020L);
        sbb.appendInt(0x043a043eL);
        sbb.appendInt(0x0442043eL);
        sbb.appendInt(0x0440044bL);
        sbb.appendInt(0x04390020L);
        sbb.appendInt(0x04320447L);
        sbb.appendInt(0x04350440L);
        sbb.appendInt(0x0430003aL);
        sbb.appendInt(0x002d0029L);
        assertEquals("00000094000000050000000000000ece000101373931383535303031323700000131393531000000000000000008000004240060041a043e0442043e0440044b04390020044104350433043e0434043d044f00200434043e043b04360435043d00200431044b0442044c00200438043b04380020043a043e0442043e0440044b0439002004320447043504400430003a002d0029", sbb.getHexDump());
        DeliverSM deliver = new DeliverSM(sbb.getBuffer());
    }

    @Test
    public void checkNullShortMessage() throws Exception {
        // 00000046000000050000000000000ecd000101373931373435343530383000040938313831004000000000000000000004240012050a03000c0839332b323032373431303337
        // 00000046 00000005 00000000 00000ecd 00 01 01 373931373435343530383000 04 09 3831383100 40 00 00 00 00 00 00 00 00 00
        // 04240012 050a0300 0c083933 2b323032 37343130 33 37
        SmppByteBuffer sbb = new SmppByteBuffer();
        sbb.appendInt(0x00000046L);
        sbb.appendInt(0x00000005L);
        sbb.appendInt(0x00000000L);
        sbb.appendInt(0x00000ecdL);
        sbb.appendByte((short) 0x00);
        sbb.appendByte((short) 0x01);
        sbb.appendByte((short) 0x01);
        sbb.appendCString("79174545080");
        sbb.appendByte((short) 0x04);
        sbb.appendByte((short) 0x09);
        sbb.appendCString("8181");
        sbb.appendByte((short) 0x40);
        sbb.appendByte((short) 0x00);
        sbb.appendByte((short) 0x00);
        sbb.appendByte((short) 0x00);
        sbb.appendByte((short) 0x00);
        sbb.appendByte((short) 0x00);
        sbb.appendByte((short) 0x00);
        sbb.appendByte((short) 0x00);
        sbb.appendByte((short) 0x00);
        sbb.appendByte((short) 0x00);
        sbb.appendInt(0x04240012L);
        sbb.appendInt(0x050a0300L);
        sbb.appendInt(0x0c083933L);
        sbb.appendInt(0x2b323032L);
        sbb.appendInt(0x37343130L);
        sbb.appendByte((short) 0x33);
        sbb.appendByte((short) 0x37);
        assertEquals("00000046000000050000000000000ecd000101373931373435343530383000040938313831004000000000000000000004240012050a03000c0839332b323032373431303337", sbb.getHexDump());
        DeliverSM deliver = new DeliverSM(sbb.getBuffer());
        assertEquals("93+202741037", deliver.getMessagePayload().getValue());
        assertEquals("00000040000000050000000000000ecd00010137393137343534353038300004093831383100400000000000000000000424000c39332b323032373431303337", new SmppByteBuffer(deliver.getBytes()).getHexDump());
    }

}
