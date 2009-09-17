package org.bulatnig.smpp.client;

import java.util.regex.Pattern;

/**
 * Comment here.
 * <p/>
 * User: Bulat Nigmatullin
 * Date: 27.04.2009
 * Time: 19:50:34
 */
public enum MessageCodingHelper {
    INSTANCE;

    public final Pattern ASCII_PATTERN = Pattern.compile("[\\p{ASCII}]*");
    public final Pattern LATIN1_PATTERN = Pattern.compile("[\\p{ASCII}\\u0080-\\u00ff]*");

    public MessageCoding getDataCoding(String text) {
        if (ASCII_PATTERN.matcher(text).matches()) {
            return MessageCoding.ASCII;
        } else if (LATIN1_PATTERN.matcher(text).matches()) {
            return MessageCoding.LATIN1;
        } else {
            return MessageCoding.UCS2;
        }
    }

}
