package org.bulatnig.smpp.session;

import junit.framework.JUnit4TestAdapter;
import org.bulatnig.smpp.session.impl.SyncSession;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.bulatnig.smpp.SMPPException;
import org.bulatnig.smpp.pdu.PDU;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Comments here.
 * User: Bulat Nigmatullin
 * Date: 20.10.2008
 * Time: 8:54:48
 */
public class SessionConnectTest implements PDUHandler {

// Used for backward compatibility (IDEs, Ant and JUnit 3 text runner)

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(SessionConnectTest.class);
    }

    private static NoResponseSMPPServer server1;
    private static final int PORT1 = 44444;

    @BeforeClass
    public static void startupServer() {
        server1 = new NoResponseSMPPServer(PORT1);
        new Thread(server1).start();

    }

    @AfterClass
    public static void shutdownServer() {
        if (server1 != null) {
            server1.stop();
            server1 = null;
        }
    }

    public PDU received(PDU pdu) {
        System.out.println("pdu received: " + pdu);
        return null;
    }

    @Test(expected = SMPPException.class, timeout = 26000)
    public void noResponseTest() throws SMPPException, Exception {
        long started = System.currentTimeMillis();
        try {
            new SyncSession.Builder("localhost", PORT1).systemId("client").
                    password("pass").systemType("test").pduHandler(this).build();
        } catch (SMPPException e) {
            if (System.currentTimeMillis() - started < 25000) {
                throw new Exception("Wrong timeouts!!! Fix this!", e);
            } else {
                throw e;
            }
        }
    }

    private static class NoResponseSMPPServer implements Runnable {

        private int port;
        private boolean run = true;

        public NoResponseSMPPServer(int port) {
            this.port = port;
        }

        public void run() {
            ServerSocket serverSocket = null;
            try {
                serverSocket = new ServerSocket(port);
                Socket client;
                OutputStream out;
                while (run) {
                    client = serverSocket.accept();
                    out = client.getOutputStream();
                    while (run) {
                        Thread.sleep(10);
                    }
                    out.close();
                    client.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (serverSocket != null) {
                    try {
                        serverSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        public void stop() {
            run = false;
        }
    }

}
