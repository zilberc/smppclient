package org.bulatnig.smpp.pdu.tlv;

import org.bulatnig.smpp.pdu.EsmClass;

import java.util.List;

/**
 * Создает TLV соответствующий байткоду.
 * <p/>
 * User: Bulat Nigmatullin
 * Date: 29.10.2008
 * Time: 9:47:44
 */
public interface TLVFactory {

    /**
     * Парсит полученный массив байтов и возвращает TLV им соответствующую.
     *
     * @param bytes      массив байтов
     * @param esmClass   PDU esm_class
     * @param dataCoding PDU data_coding
     * @return TLV
     * @throws TLVException байтов  ошибка разбора TLV
     */
    public TLV parseTLV(byte[] bytes, EsmClass esmClass, short dataCoding) throws TLVException;

    /**
     * Парсит полученный массив байтов и возвращает список TLV ему соответствующий.
     * Если TLV не известен системе, то он опускается.
     *
     * @param bytes      byte array
     * @param esmClass   PDU esm_class
     * @param dataCoding PDU data_coding
     * @return TLV list
     * @throws TLVException error during parsing optional parameters
     */
    public List<TLV> parseTLVs(byte[] bytes, EsmClass esmClass, short dataCoding) throws TLVException;

}
