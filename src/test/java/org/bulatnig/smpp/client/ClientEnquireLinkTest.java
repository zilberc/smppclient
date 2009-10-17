package org.bulatnig.smpp.client;

import junit.framework.JUnit4TestAdapter;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.bulatnig.smpp.client.Message;
import org.bulatnig.smpp.pdu.*;
import org.bulatnig.smpp.session.ConnectionType;
import org.bulatnig.smpp.session.SMPPSession;
import org.bulatnig.smpp.session.SyncSMPPSession;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Comment here.
 * <p/>
 * User: Bulat Nigmatullin
 * Date: Dec 24, 2008
 * Time: 1:31:44 PM
 */
public class ClientEnquireLinkTest extends MessageHandler {

// Used for backward compatibility (IDEs, Ant and JUnit 3 text runner)

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(ClientEnquireLinkTest.class);
    }

    private static EnquireLinkTimerServer server1;
    private static EnquireLinkVerifServer server2;
    private static final int PORT1 = 19801;
    private static final int PORT2 = 19802;

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

    @Test(timeout = 45000)
    public void clientEnquireLinkTimerTest() throws Exception {
        SMPPSession session = new SyncSMPPSession.Builder("localhost", PORT1).
                connectionType(ConnectionType.TRANSCEIVER).
                systemId("client").
                password("pass").
                systemType("test").
                addrTon(TON.UNKNOWN).
                addrNpi(NPI.UNKNOWN).
                build();
        SMPPClient client = new SMPPClientImpl(session, this, false);
        Thread.sleep(40 * 1000);
        client.send(new Message("", "", ""));
        client.stop();
    }

    @Test(timeout = 45000)
    public void clientEnquireLinkVerifTest() throws Exception {

        SMPPSession session = new SyncSMPPSession.Builder("localhost", PORT2).
                connectionType(ConnectionType.TRANSCEIVER).
                systemId("client").
                password("pass").
                systemType("test").
                addrTon(TON.UNKNOWN).
                addrNpi(NPI.UNKNOWN).
                build();
        SMPPClient client = new SMPPClientImpl(session, this, false);
        Thread.sleep(40 * 1000);
        client.send(new Message("", "", ""));
        client.stop();
    }

    public void handle(Message message) throws ProcessingFailedException {
    }

    public void deliveredToDest(String messageId) throws ProcessingFailedException {
    }

    private static class EnquireLinkTimerServer implements Runnable {

        private final PDUFactory factory = PDUFactoryImpl.INSTANCE;
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
                PDU pdu;
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
                    pdu = factory.parsePDU(buffer);
                    out.write(((Responsable) pdu).getResponse().getBytes());
                    time = System.currentTimeMillis();
                    in.read(buffer2);
                    if ((System.currentTimeMillis() - time) > 30000 &&
                            (System.currentTimeMillis() - time) < 33000) {
                        pdu = factory.parsePDU(buffer2);
                        out.write(((Responsable) pdu).getResponse().getBytes());
                        in.read(buffer3);
                        pdu = factory.parsePDU(buffer3);
                        out.write(((Responsable) pdu).getResponse().getBytes());
                    } else {
                        out.write(new SubmitSMResp().getBytes());
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

    private static class EnquireLinkVerifServer implements Runnable {

        private final PDUFactory factory = PDUFactoryImpl.INSTANCE;
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
                PDU pdu;
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
                    pdu = factory.parsePDU(buffer);
                    out.write(((Responsable) pdu).getResponse().getBytes());
                    out.write(new DeliverSM().getBytes());
                    in.read(buffer2);
                    pdu = factory.parsePDU(buffer2);
                    if (pdu instanceof DeliverSMResp) {
                        long time = System.currentTimeMillis();
                        in.read(buffer3);
                        if ((System.currentTimeMillis() - time) > 30000 &&
                                (System.currentTimeMillis() - time) < 33000) {
                            pdu = factory.parsePDU(buffer3);
                            out.write(((Responsable) pdu).getResponse().getBytes());
                            in.read(buffer4);
                            pdu = factory.parsePDU(buffer4);
                            out.write(((Responsable) pdu).getResponse().getBytes());
                        } else {
                            out.write(new SubmitSMResp().getBytes());
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
