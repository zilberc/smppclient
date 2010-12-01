package org.bulatnig.smpp.pdu.impl;

import org.bulatnig.smpp.pdu.CommandId;
import org.bulatnig.smpp.pdu.CommandStatus;
import org.bulatnig.smpp.pdu.PduParsingException;
import org.bulatnig.smpp.pdu.impl.GenericNack;
import org.bulatnig.smpp.util.ByteBuffer;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * GenericNack test.
 *
 * @author Bulat Nigmatullin
 */
public class GenericNackTest {


    @Test
    public void bytesToObject() throws PduParsingException {
        ByteBuffer sbb = new ByteBuffer();
        sbb.appendInt(16L);
        sbb.appendInt(2147483648L);
        sbb.appendInt(88);
        sbb.appendInt(987654321L);
        GenericNack gn = new GenericNack(sbb);
        Assert.assertEquals(CommandId.GENERIC_NACK, gn.getCommandId());
        Assert.assertEquals(CommandStatus.ESME_RTHROTTLED, gn.getCommandStatus());
        assertEquals(987654321L, gn.getSequenceNumber());
    }

    @Test
    public void objectToBytes() throws PduParsingException {
        GenericNack gn = new GenericNack();
        gn.setCommandStatus(CommandStatus.ESME_RSUBMITFAIL);
        gn.setSequenceNumber(0);
        assertEquals("00000010800000000000004500000000", gn.buffer().hexDump());
    }

}
