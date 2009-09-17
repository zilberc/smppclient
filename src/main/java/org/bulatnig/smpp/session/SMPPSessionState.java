package org.bulatnig.smpp.session;

/**
 * SMSCSession state.
 *
 * User: Bulat Nigmatullin
 * Date: 06.07.2008
 * Time: 19:28:27
 */
public enum SMPPSessionState {
    /**
     * Connected. All ok.
     */
    OK,
    /**
     * Session stopped.
     */
    DISCONNECTED,
    /**
     * Session in recovery mode, i.e. trying to reconnect to SMSC.
     */
    RECOVERING

}
