package org.bulatnig.smpp.pdu;

/**
 * Comment here.
 * <p/>
 * User: Bulat Nigmatullin
 * Date: 04.05.2009
 * Time: 10:40:10
 */
public interface DataCodingStrategy {

    public String getCharsetName(short dataCoding);

}
