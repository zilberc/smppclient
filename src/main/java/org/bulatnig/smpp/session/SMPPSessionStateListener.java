package org.bulatnig.smpp.session;

/**
 * Session state changes listener.
 * <p/>
 * User: Bulat Nigmatullin
 * Date: Apr 5, 2009
 * Time: 2:45:22 PM
 */
public interface SMPPSessionStateListener {

    /**
     * Notifies about state changes.
     *
     * @param state     new session state
     */
    void stateChanged(SMPPSessionState state);

}