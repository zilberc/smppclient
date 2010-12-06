package org.bulatnig.smpp.session.impl;

import org.bulatnig.smpp.SmppException;
import org.bulatnig.smpp.net.impl.TcpConnection;
import org.bulatnig.smpp.pdu.Pdu;
import org.bulatnig.smpp.pdu.impl.BindTransceiver;
import org.bulatnig.smpp.session.Session;
import org.bulatnig.smpp.session.SessionListener;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Comments here.
 * User: Bulat Nigmatullin
 * Date: 20.10.2008
 * Time: 8:54:48
 */
public class SessionConnectTest implements SessionListener {

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

    @Override
    public Pdu received(Pdu pdu) {
        System.out.println("pdu received: " + pdu);
        return null;
    }

    @Test(expected = Exception.class, timeout = 2000)
    public void noResponseTest() throws Exception {
        final int smscResponseTimeout = 1000;
        long started = System.currentTimeMillis();
        try {
            Session session = new SessionImpl(new TcpConnection(new InetSocketAddress("localhost", PORT1)));
            session.setSmscResponseTimeout(smscResponseTimeout);
            BindTransceiver bindRequest = new BindTransceiver();
            bindRequest.setSystemId("client");
            bindRequest.setPassword("pass");
            bindRequest.setSystemId("client");
            bindRequest.setSystemType("test");
            session.open(bindRequest);
        } catch (Exception e) {
            if (System.currentTimeMillis() - started < smscResponseTimeout) {
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
