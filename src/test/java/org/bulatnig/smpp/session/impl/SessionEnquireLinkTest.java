package org.bulatnig.smpp.session.impl;

import junit.framework.JUnit4TestAdapter;
import org.bulatnig.smpp.net.impl.TcpConnection;
import org.bulatnig.smpp.pdu.Pdu;
import org.bulatnig.smpp.pdu.PduParser;
import org.bulatnig.smpp.pdu.impl.*;
import org.bulatnig.smpp.session.Session;
import org.bulatnig.smpp.session.SessionListener;
import org.bulatnig.smpp.util.ByteBuffer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * TODO refactor terrible tests.
 * <p/>
 * User: Bulat Nigmatullin
 * Date: Dec 24, 2008
 * Time: 1:31:44 PM
 */
public class SessionEnquireLinkTest implements SessionListener {

// Used for backward compatibility (IDEs, Ant and JUnit 3 text runner)

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(SessionEnquireLinkTest.class);
    }

    private static EnquireLinkTimerServer server1;
    private static EnquireLinkVerifServer server2;
    private static final int PORT1 = 19001;
    private static final int PORT2 = 19002;

    @BeforeClass
    public static void startupServer() {
        server1 = new EnquireLinkTimerServer(PORT1);
        new Thread(server1).start();
        server2 = new EnquireLinkVerifServer(PORT2);
        new Thread(server2).start();
    }

    @AfterClass
    public static void shutdownServer() {
        if (server1 != null) {
            server1.stop();
            server1 = null;
        }
        if (server2 != null) {
            server2.stop();
            server2 = null;
        }
    }

    @Test(timeout = 900)
    public void enquireLinkTimerTest() throws Exception {
        Session session = new SessionImpl(new TcpConnection(new InetSocketAddress("localhost", PORT1)));
        session.setPingTimeout(500);
        session.setSmscResponseTimeout(50);
        BindTransceiver bindRequest = new BindTransceiver();
        bindRequest.setSystemId("client");
        bindRequest.setPassword("pass");
        bindRequest.setSystemId("client");
        bindRequest.setSystemType("test");
        bindRequest.setInterfaceVersion(0x34);
        session.open(bindRequest);
        Thread.sleep(600);
        session.send(new SubmitSm());
        session.close();
    }

    @Ignore
    @Test(timeout = 900)
    public void enquireLinkVerifTest() throws Exception {
        Session session = new SessionImpl(new TcpConnection(new InetSocketAddress("localhost", PORT2)));
        session.setPingTimeout(500);
        session.setSmscResponseTimeout(50);
        BindTransceiver bindRequest = new BindTransceiver();
        bindRequest.setSystemId("client");
        bindRequest.setPassword("pass");
        bindRequest.setSystemId("client");
        bindRequest.setSystemType("test");
        bindRequest.setInterfaceVersion(0x34);
        session.open(bindRequest);
        Thread.sleep(600);
        session.send(new SubmitSm());
        session.close();
    }

    @Override
    public Pdu received(Pdu pdu) {
        return null;
    }

    private static class EnquireLinkTimerServer implements Runnable {

        private final PduParser parser = new DefaultPduParser();
        private int port;
        private boolean run = true;

        public EnquireLinkTimerServer(int port) {
            this.port = port;
        }

        public void run() {
            ServerSocket serverSocket = null;
            try {
                serverSocket = new ServerSocket(port);
                byte[] buffer = new byte[37];
                byte[] buffer2 = new byte[16];
                byte[] buffer3 = new byte[33];
                Pdu pdu;
                Socket client;
                OutputStream out;
                InputStream in;
                long time = 0;
                while (run) {
                    client = serverSocket.accept();
                    if (time != 0) {
                        client.close();
                        break;
                    }
                    in = client.getInputStream();
                    out = client.getOutputStream();
                    in.read(buffer);
                    pdu = new BindReceiverResp();
                    pdu.setSequenceNumber(1);
                    out.write(pdu.buffer().array());
                    time = System.currentTimeMillis();
                    in.read(buffer2);
                    if ((System.currentTimeMillis() - time) > 450 &&
                            (System.currentTimeMillis() - time) < 600) {
                        pdu = new EnquireLinkResp();
                        pdu.setSequenceNumber(2);
                        out.write(pdu.buffer().array());
                        in.read(buffer3);
                        pdu = new SubmitSmResp();
                        pdu.setSequenceNumber(3);
                        out.write(pdu.buffer().array());
                    } else {
                        pdu = new SubmitSmResp();
                        pdu.setSequenceNumber(2);
                        out.write(pdu.buffer().array());
                    }
                    in.close();
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
            System.out.println("done");
        }

        public void stop() {
            run = false;
        }
    }

    private static class EnquireLinkVerifServer implements Runnable {

        private final PduParser parser = new DefaultPduParser();
        private int port;
        private boolean run = true;

        public EnquireLinkVerifServer(int port) {
            this.port = port;
        }

        public void run() {
            ServerSocket serverSocket = null;
            try {
                serverSocket = new ServerSocket(port);
                byte[] buffer = new byte[37];
                byte[] buffer2 = new byte[17];
                byte[] buffer3 = new byte[16];
                byte[] buffer4 = new byte[33];
                Pdu pdu;
                Socket client;
                OutputStream out;
                InputStream in;
                boolean firstTime = true;
                while (run) {
                    client = serverSocket.accept();
                    if (!firstTime) {
                        client.close();
                        break;
                    } else {
                        firstTime = false;
                    }
                    in = client.getInputStream();
                    out = client.getOutputStream();
                    in.read(buffer);
                    pdu = new BindReceiverResp();
                    pdu.setSequenceNumber(1);
                    out.write(pdu.buffer().array());
                    out.write(new DeliverSm().buffer().array());
                    in.read(buffer2);
                    pdu = parser.parse(new ByteBuffer(buffer2));
                    if (pdu instanceof DeliverSmResp) {
                        long time = System.currentTimeMillis();
                        in.read(buffer3);
                        if ((System.currentTimeMillis() - time) > 450 &&
                                (System.currentTimeMillis() - time) < 600) {
                            pdu = new EnquireLinkResp();
                            pdu.setSequenceNumber(2);
                            out.write(pdu.buffer().array());
                            in.read(buffer3);
                            pdu = new SubmitSmResp();
                            pdu.setSequenceNumber(3);
                            out.write(pdu.buffer().array());
                        } else {
                            pdu = new SubmitSmResp();
                            pdu.setSequenceNumber(2);
                            out.write(pdu.buffer().array());
                        }
                    }
                    in.close();
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
