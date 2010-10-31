package org.bulatnig.smpp.domain.pdu;

import junit.framework.JUnit4TestAdapter;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.bulatnig.smpp.pdu.*;
import org.bulatnig.smpp.util.SmppByteBuffer;
import org.bulatnig.smpp.util.WrongParameterException;

/**
 * Comment here.
 * User: Bulat Nigmatullin
 * Date: 26.06.2008
 * Time: 18:13:48
 */
public class SubmitSMTest {

    // Used for backward compatibility (IDEs, Ant and JUnit 3 text runner)

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(SubmitSMTest.class);
    }

    @Test
    public void bytesToObject() throws WrongParameterException, PDUException {
        SmppByteBuffer sbb = new SmppByteBuffer();
        sbb.appendInt(82);
        sbb.appendInt(4);
        sbb.appendInt(21);
        sbb.appendInt(123987465L);
        sbb.appendCString("typo");
        sbb.appendByte((short)2);
        sbb.appendByte((short)11);
        sbb.appendCString("sender");
        sbb.appendByte((short)3);
        sbb.appendByte((short)6);
        sbb.appendCString("receiver with long");
        sbb.appendByte((short)14);
        sbb.appendByte((short)15);
        sbb.appendByte((short)16);
        sbb.appendCString("");
        sbb.appendCString("");
        sbb.appendByte((short)17);
        sbb.appendByte((short)18);
        sbb.appendByte((short)19);
        sbb.appendByte((short)20);
        sbb.appendByte((short)21);
        sbb.appendString("aaaaaaabbbbbbbsssssss");
        SubmitSM submit = new SubmitSM(sbb.getBuffer());
        assertEquals(82L, submit.getCommandLength());
        assertEquals(4L, submit.getCommandId().getValue());
        assertEquals(CommandStatus.ESME_RINVSERTYP, submit.getCommandStatus());
        assertEquals(21L, submit.getCommandStatus().getValue());
        assertEquals(123987465L, submit.getSequenceNumber());
        assertEquals("typo", submit.getServiceType());
        assertEquals(TON.NATIONAL, submit.getSourceAddrTon());
        assertEquals(NPI.RESERVED, submit.getSourceAddrNpi());
        assertEquals("sender", submit.getSourceAddr());
        assertEquals(TON.NETWORK_SPECIFIC, submit.getDestAddrTon());
        assertEquals(NPI.LAND_MOBILE, submit.getDestAddrNpi());
        assertEquals("receiver with long", submit.getDestinationAddr());
        assertEquals(EsmeEsmClass.EsmeMessagingMode.FORWARD, submit.getEsmClass().getMode());
        assertEquals(EsmeEsmClass.EsmeMessageType.DEFAULT, submit.getEsmClass().getType());
        assertEquals(EsmeEsmClass.EsmeGSMFeatures.NO_FEATURES, submit.getEsmClass().getFeatures());
        assertEquals((short)15, submit.getProtocolId());
        assertEquals((short)16, submit.getPriorityFlag());
        assertEquals("", submit.getScheduleDeliveryTime());
        assertEquals("", submit.getValidityPeriod());
        assertEquals((short)17, submit.getRegisteredDelivery());
        assertEquals((short)18, submit.getReplaceIfPresentFlag());
        assertEquals((short)19, submit.getDataCoding());
        assertEquals((short)20, submit.getSmDefaultMsgId());
        assertEquals((short)21, submit.getSmLength());
        assertEquals("aaaaaaabbbbbbbsssssss", submit.getShortMessage());
    }

    @Test
    public void objectToBytes() throws PDUException {
        SubmitSM submit = new SubmitSM();
        assertEquals("000000210000000400000000000000000000000000000000000000000000000000",
                new SmppByteBuffer(submit.getBytes()).getHexDump());
    }
}
