package org.bulatnig.smpp.client;

import junit.framework.JUnit4TestAdapter;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.bulatnig.smpp.util.SmppByteBuffer;
import org.junit.Test;
import org.bulatnig.smpp.pdu.DeliverSM;

/**
 * Comment here.
 * <p/>
 * User: Bulat Nigmatullin
 * Date: Dec 25, 2008
 * Time: 9:47:47 AM
 */
public class MessageHandlerTest {

// Used for backward compatibility (IDEs, Ant and JUnit 3 text runner)

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(MessageHandlerTest.class);
    }

    private static Message message;

    /**
     * A little wrong UDH test.
     *
     * @throws Exception
     */
    @Test
    public void multipartMessageTest() throws Exception {
        // 000000da000000050000000004b7d12a00010137393039393938353737370003013139353023323039323930313200400000000000000000a1050003efbfbd020159612c706f2070726176646520676f766f7279612c6e652064796d6179752c34746f206e61646f20707279616d2069736b617427214d6f6a65742065746f20626f6e616c276e6f2c6e6f20796120646f2073697820706f722076657279752c34746f2034797673747661206b616b20736e6567206e6120676f6c6f767920646f6c6a6e69207376616c6974277379612145746f2074616b2072
        // 000000da 00000005 00000000 04b7d12a 00 01 01 373930393939383537373700 03 01 3139353023323039323930313200 40 00 00 00 00 00 00 00 00 a1
        // 050003ef bfbd0201 59612c70 6f207072 61766465 20676f76 6f727961 2c6e6520 64796d61 79752c34 746f206e 61646f20 70727961 6d206973 6b617427
        // 214d6f6a 65742065 746f2062 6f6e616c 276e6f2c 6e6f2079 6120646f 20736978 20706f72 20766572 79752c34 746f2034 79767374 7661206b 616b2073
        // 6e656720 6e612067 6f6c6f76 7920646f 6c6a6e69 20737661 6c697427 73796121 45746f20 74616b20 72
        SmppByteBuffer sbb1 = new SmppByteBuffer();
        sbb1.appendInt(0x000000daL);
        sbb1.appendInt(0x00000005L);
        sbb1.appendInt(0x00000000L);
        sbb1.appendInt(0x04b7d12aL);
        sbb1.appendByte((short) 0x00);
        sbb1.appendByte((short) 0x01);
        sbb1.appendByte((short) 0x01);
        sbb1.appendCString("79099985777");
        sbb1.appendByte((short) 0x03);
        sbb1.appendByte((short) 0x01);
        sbb1.appendCString("1950#20929012");
        sbb1.appendByte((short) 0x40);
        sbb1.appendByte((short) 0x00);
        sbb1.appendByte((short) 0x00);
        sbb1.appendByte((short) 0x00);
        sbb1.appendByte((short) 0x00);
        sbb1.appendByte((short) 0x00);
        sbb1.appendByte((short) 0x00);
        sbb1.appendByte((short) 0x00);
        sbb1.appendByte((short) 0x00);
        sbb1.appendByte((short) 0xa1);
        sbb1.appendInt(0x050003efL);
        sbb1.appendInt(0xbfbd0201L);
        sbb1.appendInt(0x59612c70L);
        sbb1.appendInt(0x6f207072L);
        sbb1.appendInt(0x61766465L);
        sbb1.appendInt(0x20676f76L);
        sbb1.appendInt(0x6f727961L);
        sbb1.appendInt(0x2c6e6520L);
        sbb1.appendInt(0x64796d61L);
        sbb1.appendInt(0x79752c34L);
        sbb1.appendInt(0x746f206eL);
        sbb1.appendInt(0x61646f20L);
        sbb1.appendInt(0x70727961L);
        sbb1.appendInt(0x6d206973L);
        sbb1.appendInt(0x6b617427L);
        sbb1.appendInt(0x214d6f6aL);
        sbb1.appendInt(0x65742065L);
        sbb1.appendInt(0x746f2062L);
        sbb1.appendInt(0x6f6e616cL);
        sbb1.appendInt(0x276e6f2cL);
        sbb1.appendInt(0x6e6f2079L);
        sbb1.appendInt(0x6120646fL);
        sbb1.appendInt(0x20736978L);
        sbb1.appendInt(0x20706f72L);
        sbb1.appendInt(0x20766572L);
        sbb1.appendInt(0x79752c34L);
        sbb1.appendInt(0x746f2034L);
        sbb1.appendInt(0x79767374L);
        sbb1.appendInt(0x7661206bL);
        sbb1.appendInt(0x616b2073L);
        sbb1.appendInt(0x6e656720L);
        sbb1.appendInt(0x6e612067L);
        sbb1.appendInt(0x6f6c6f76L);
        sbb1.appendInt(0x7920646fL);
        sbb1.appendInt(0x6c6a6e69L);
        sbb1.appendInt(0x20737661L);
        sbb1.appendInt(0x6c697427L);
        sbb1.appendInt(0x73796121L);
        sbb1.appendInt(0x45746f20L);
        sbb1.appendInt(0x74616b20L);
        sbb1.appendByte((short) 0x72);
        assertEquals("000000da000000050000000004b7d12a00010137393039393938353737370003013139353023323039323930313200400000000000000000a1050003efbfbd020159612c706f2070726176646520676f766f7279612c6e652064796d6179752c34746f206e61646f20707279616d2069736b617427214d6f6a65742065746f20626f6e616c276e6f2c6e6f20796120646f2073697820706f722076657279752c34746f2034797673747661206b616b20736e6567206e6120676f6c6f767920646f6c6a6e69207376616c6974277379612145746f2074616b2072", sbb1.getHexDump());

        // 00000095000000050000000004b7d191000101373930393939383537373700030131393530233230393239303338004000000000000000005c050003efbfbd02026f6d616e7469346e6f213a2d294120706f6b61206e61646f207072696e696d61742720767365206b616b20657374272145746f207379642762612154616b2034746f2067796c7961656d213a2d503b2d293a2d2a
        // 00000095 00000005 00000000 04b7d191 00 01 01 373930393939383537373700 03 01 3139353023323039323930333800 40 00 00 00 00 00 00 00 00 5c
        // 050003ef bfbd0202 6f6d616e 7469346e 6f213a2d 29412070 6f6b6120 6e61646f 20707269 6e696d61 74272076 7365206b 616b2065
        // 73742721 45746f20 73796427 62612154 616b2034 746f2067 796c7961 656d213a 2d503b2d 293a2d2a
        SmppByteBuffer sbb2 = new SmppByteBuffer();
        sbb2.appendInt(0x00000095L);
        sbb2.appendInt(0x00000005L);
        sbb2.appendInt(0x00000000L);
        sbb2.appendInt(0x04b7d191L);
        sbb2.appendByte((short) 0x00);
        sbb2.appendByte((short) 0x01);
        sbb2.appendByte((short) 0x01);
        sbb2.appendCString("79099985777");
        sbb2.appendByte((short) 0x03);
        sbb2.appendByte((short) 0x01);
        sbb2.appendCString("1950#20929012");
        sbb2.appendByte((short) 0x40);
        sbb2.appendByte((short) 0x00);
        sbb2.appendByte((short) 0x00);
        sbb2.appendByte((short) 0x00);
        sbb2.appendByte((short) 0x00);
        sbb2.appendByte((short) 0x00);
        sbb2.appendByte((short) 0x00);
        sbb2.appendByte((short) 0x00);
        sbb2.appendByte((short) 0x00);
        sbb2.appendByte((short) 0x5c);
        sbb2.appendInt(0x050003efL);
        sbb2.appendInt(0xbfbd0202L);
        sbb2.appendInt(0x6f6d616eL);
        sbb2.appendInt(0x7469346eL);
        sbb2.appendInt(0x6f213a2dL);
        sbb2.appendInt(0x29412070L);
        sbb2.appendInt(0x6f6b6120L);
        sbb2.appendInt(0x6e61646fL);
        sbb2.appendInt(0x20707269L);
        sbb2.appendInt(0x6e696d61L);
        sbb2.appendInt(0x74272076L);
        sbb2.appendInt(0x7365206bL);
        sbb2.appendInt(0x616b2065L);
        sbb2.appendInt(0x73742721L);
        sbb2.appendInt(0x45746f20L);
        sbb2.appendInt(0x73796427L);
        sbb2.appendInt(0x62612154L);
        sbb2.appendInt(0x616b2034L);
        sbb2.appendInt(0x746f2067L);
        sbb2.appendInt(0x796c7961L);
        sbb2.appendInt(0x656d213aL);
        sbb2.appendInt(0x2d503b2dL);
        sbb2.appendInt(0x293a2d2aL);
        assertEquals("00000095000000050000000004b7d191000101373930393939383537373700030131393530233230393239303132004000000000000000005c050003efbfbd02026f6d616e7469346e6f213a2d294120706f6b61206e61646f207072696e696d61742720767365206b616b20657374272145746f207379642762612154616b2034746f2067796c7961656d213a2d503b2d293a2d2a", sbb2.getHexDump());

        DeliverSM deliver1 = new DeliverSM(sbb1.getBuffer());
        DeliverSM deliver2 = new DeliverSM(sbb2.getBuffer());

        MessageHandler handler = new MessageHandlerTestImpl();
        handler.start();
        handler.received(deliver1);
        handler.received(deliver2);
        assertNotNull(message);
        assertEquals("79099985777", message.getSourceAddr());
        assertEquals("1950#20929012", message.getDestAddr());
        handler.stop();
    }

    private final class MessageHandlerTestImpl extends MessageHandler {

        public void handle(Message message) throws ProcessingFailedException {
            MessageHandlerTest.message = message;
        }

        public void deliveredToDest(String messageId) throws ProcessingFailedException {
        }
    }

}
