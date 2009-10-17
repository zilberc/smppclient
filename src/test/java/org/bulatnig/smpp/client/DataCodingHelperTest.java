package org.bulatnig.smpp.client;

import static org.junit.Assert.assertEquals;
import junit.framework.JUnit4TestAdapter;
import org.junit.Test;
import org.bulatnig.smpp.client.MessageCoding;
import org.bulatnig.smpp.client.MessageCodingHelper;

/**
 * Comment here.
 * <p/>
 * User: Bulat Nigmatullin
 * Date: 27.04.2009
 * Time: 20:04:38
 */
public class DataCodingHelperTest {

// Used for backward compatibility (IDEs, Ant and JUnit 3 text runner)

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(DataCodingHelperTest.class);
    }

    @Test
    public void testASCII() {
        assertEquals(MessageCoding.ASCII,
                MessageCodingHelper.INSTANCE.getDataCoding(" !\"#$%&'()*+,-./0123456789:;<=>?@" +
                        "ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~"));
        assertEquals(MessageCoding.ASCII, MessageCodingHelper.INSTANCE.getDataCoding(""));
    }

    @Test
    public void testLatin1() {
        assertEquals(MessageCoding.LATIN1,
                MessageCodingHelper.INSTANCE.getDataCoding(" !\"#$%&'()*+,-./0123456789:;<=>?@" +
                        "ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~" +
                        "¡¢£¤¥¦§¨©ª«¬ýþÿ"));
    }

    @Test
    public void testUCS2() {
        assertEquals(MessageCoding.UCS2,
                MessageCodingHelper.INSTANCE.getDataCoding(" !\"#$%&'()*+,-./0123456789:;<=>?@" +
                        "ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~" +
                        "¡¢£¤¥¦§¨©ª«¬ýþÿ" +
                        "Привет"));
    }

}
