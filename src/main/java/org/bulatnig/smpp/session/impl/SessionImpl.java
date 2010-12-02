package org.bulatnig.smpp.session.impl;

import org.bulatnig.smpp.net.Connection;
import org.bulatnig.smpp.pdu.Pdu;
import org.bulatnig.smpp.pdu.PduNotFoundException;
import org.bulatnig.smpp.pdu.PduParsingException;
import org.bulatnig.smpp.session.Session;
import org.bulatnig.smpp.session.SessionListener;

import java.io.IOException;
import java.util.concurrent.Future;

/**
 * Sync and async session implementation.
 *
 * @author Bulat Nigmatullin
 */
public class SessionImpl implements Session {

    private final Connection conn;

    public SessionImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void setSessionListener(SessionListener sessionListener) {
    }

    @Override
    public Pdu open(Pdu pdu) throws PduParsingException, PduNotFoundException, IOException {
        conn.open();

        return null;
    }

    @Override
    public Future<Pdu> send(Pdu pdu) {
        return null;
    }

    @Override
    public void close() {
    }

}
