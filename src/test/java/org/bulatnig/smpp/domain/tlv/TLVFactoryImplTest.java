package org.bulatnig.smpp.domain.tlv;

import junit.framework.JUnit4TestAdapter;
import static org.junit.Assert.assertEquals;

import org.bulatnig.smpp.SmppException;
import org.bulatnig.smpp.util.SmppByteBuffer;
import org.junit.Test;
import org.bulatnig.smpp.pdu.PDUException;
import org.bulatnig.smpp.pdu.SmscEsmClass;
import org.bulatnig.smpp.pdu.EsmeEsmClass;
import org.bulatnig.smpp.pdu.tlv.*;
import org.bulatnig.smpp.util.WrongParameterException;

import java.util.List;

/**
 * Comments here.
 * User: Bulat Nigmatullin
 * Date: 30.10.2008
 * Time: 15:46:02
 */
public class TLVFactoryImplTest {

    // Used for backward compatibility (IDEs, Ant and JUnit 3 text runner)
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(TLVFactoryImplTest.class);
    }

    @Test
    public void testTLVFactory1() throws TLVException {
        TLVFactory factory = TLVFactoryImpl.INSTANCE;
        assertEquals(10000000L, ((QosTimeToLive) factory.parseTLV(new QosTimeToLive(10000000L).getBytes(), new SmscEsmClass(), (short) 0)).getValue());
    }

    @Test(expected = TLVException.class)
    public void testTLVFactory2() throws TLVException, PDUException, WrongParameterException {
        TLVFactory factory = TLVFactoryImpl.INSTANCE;
        SmppByteBuffer bb = new SmppByteBuffer();
        bb.appendShort(0x0017);
        bb.appendShort(0x0004);
        bb.appendInt(999999999);
        bb.appendByte((byte) 99);
        factory.parseTLV(bb.getBuffer(), new SmscEsmClass(), (short) 0);
    }

    @Test(expected = TLVException.class)
    public void testTLVFactory3() throws TLVException {
        TLVFactory factory = TLVFactoryImpl.INSTANCE;
        factory.parseTLV(new byte[0], new SmscEsmClass(), (short) 0);
    }

    @Test
    public void testTLVFactory4() throws SmppException {
        TLVFactory factory = TLVFactoryImpl.INSTANCE;
        SarMsgRefNum smrn = new SarMsgRefNum(10);
        PayloadType pt = new PayloadType((short) 150);
        MessagePayload mp = new MessagePayload("СерПантиН");
        SmppByteBuffer sbb = new SmppByteBuffer();
        sbb.appendBytes(smrn.getBytes(), smrn.getBytes().length);
        sbb.appendBytes(pt.getBytes(), pt.getBytes().length);
        sbb.appendBytes(mp.getBytes(new EsmeEsmClass(), (short) 8), mp.getBytes(new EsmeEsmClass(), (short) 8).length);
        List<TLV> tlvs = factory.parseTLVs(sbb.getBuffer(), new SmscEsmClass(), (short) 8);
        for (TLV tlv : tlvs) {
            switch (tlv.getTag()) {
                case SAR_MSG_REF_NUM:
                    smrn = (SarMsgRefNum) tlv;
                    assertEquals(10, smrn.getValue());
                    break;
                case PAYLOAD_TYPE:
                    pt = (PayloadType) tlv;
                    assertEquals((short) 150, pt.getValue());
                    break;
                case MESSAGE_PAYLOAD:
                    mp = (MessagePayload) tlv;
                    assertEquals("СерПантиН", mp.getValue());
                    break;
                default:
                    throw new SmppException("We should not be here");
            }
        }
    }

    @Test(expected = TLVException.class)
    public void testTLVFactory5() throws SmppException {
        TLVFactory factory = TLVFactoryImpl.INSTANCE;
        SarMsgRefNum smrn = new SarMsgRefNum(10);
        PayloadType pt = new PayloadType((short) 150);
        MessagePayload mp = new MessagePayload("СерПантиН");
        SmppByteBuffer sbb = new SmppByteBuffer();
        sbb.appendBytes(smrn.getBytes(), smrn.getBytes().length);
        sbb.appendBytes(pt.getBytes(), pt.getBytes().length);
        sbb.appendBytes(mp.getBytes(new EsmeEsmClass(), (short) 8), mp.getBytes(new EsmeEsmClass(), (short) 8).length);
        sbb.appendShort(192);
        List<TLV> tlvs = factory.parseTLVs(sbb.getBuffer(), new EsmeEsmClass(), (short) 8);
        for (TLV tlv : tlvs) {
            switch (tlv.getTag()) {
                case SAR_MSG_REF_NUM:
                    smrn = (SarMsgRefNum) tlv;
                    assertEquals(10, smrn.getValue());
                    break;
                case PAYLOAD_TYPE:
                    pt = (PayloadType) tlv;
                    assertEquals((short) 150, pt.getValue());
                    break;
                case MESSAGE_PAYLOAD:
                    mp = (MessagePayload) tlv;
                    assertEquals("СерПантиН", mp.getValue());
                    break;
                default:
                    throw new SmppException("We should not be here");
            }
        }
    }

    @Test
    public void testTLVFactory6() throws TLVException {
        TLVFactory factory = TLVFactoryImpl.INSTANCE;
        assertEquals(0, factory.parseTLVs(new byte[0], new EsmeEsmClass(), (short) 0).size());
    }

}
