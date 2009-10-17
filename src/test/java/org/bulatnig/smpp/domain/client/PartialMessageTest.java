package org.bulatnig.smpp.domain.client;

import static org.junit.Assert.*;
import junit.framework.JUnit4TestAdapter;
import org.junit.Test;

import java.util.SortedSet;
import java.util.TreeSet;

import org.bulatnig.smpp.client.PartialMessage;

/**
 * Проверка сортировки PartialMessage.
 *
 * User: Bulat Nigmatullin
 * Date: 20.07.2008
 * Time: 12:08:25
 */
public class PartialMessageTest {

// Used for backward compatibility (IDEs, Ant and JUnit 3 text runner)

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(PartialMessageTest.class);
    }

    @Test
    public void testComaparable() {
        SortedSet<PartialMessage> set = new TreeSet<PartialMessage>();
        set.add(new PartialMessage("source", "dest", "text", 1));
        set.add(new PartialMessage("source", "dest", "text", 5));
        set.add(new PartialMessage("source", "dest", "text", 3));
        set.add(new PartialMessage("source", "dest", "text", 2));
        set.add(new PartialMessage("source", "dest", "text", 4));
        int i = 0;
        for (PartialMessage pmsg : set) {
            i++;
            assertEquals(i, pmsg.getSeqnum());
        }
    }

}
