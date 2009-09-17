package org.bulatnig.smpp.session;

import org.bulatnig.smpp.pdu.PDU;

/**
 * Incoming messages handler.
 *
 * User: Bulat Nigmatullin
 * Date: 06.07.2008
 * Time: 19:31:40
 */
public interface PDUHandler {

    /**
     * Handles incoming PDU and return response if required.
     *
     * @param pdu   incoming PDU
     * @return      response PDU (it sends to smsc, only if incoming PDU requires response)
     */
    PDU received(PDU pdu);

}
