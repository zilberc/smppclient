package org.bulatnig.smpp.session;

import junit.framework.JUnit4TestAdapter;
import org.bulatnig.smpp.SmppException;
import org.bulatnig.smpp.session.impl.SyncSession;
import org.bulatnig.smpp.util.SmppByteBuffer;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.BeforeClass;
import org.junit.Test;
import org.bulatnig.smpp.pdu.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Comments here.
 * <p/>
 * User: Bulat Nigmatullin
 * Date: Nov 5, 2008
 * Time: 7:17:35 PM
 */
public class SessionBindTest implements PDUHandler {

// Used for backward compatibility (IDEs, Ant and JUnit 3 text runner)

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(SessionBindTest.class);
    }

    private static BindCheckSMPPServer server1;
    private static final int PORT1 = 54001;
    private static BindCheckSMPPServer server2;
    private static final int PORT2 = 54002;
    private static BindCheckSMPPServer server3;
    private static final int PORT3 = 54003;
    private static WrongSeqNumSMPPServer server4;
    private static final int PORT4 = 54004;
    private static WrongPDUSMPPServer server5;
    private static final int PORT5 = 54005;

    @BeforeClass
    public static void startupServer() {
        server1 = new BindCheckSMPPServer(PORT1);
        new Thread(server1).start();
        server2 = new BindCheckSMPPServer(PORT2);
        new Thread(server2).start();
        server3 = new BindCheckSMPPServer(PORT3);
        new Thread(server3).start();
        server4 = new WrongSeqNumSMPPServer(PORT4);
        new Thread(server4).start();
        server5 = new WrongPDUSMPPServer(PORT5);
        new Thread(server5).start();

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
        if (server3 != null) {
            server3.stop();
            server3 = null;
        }
        if (server4 != null) {
            server4.stop();
            server4 = null;
        }
        if (server5 != null) {
            server5.stop();
            server5 = null;
        }
    }

    @Test
    public void bindReceiverTest() throws SmppException {
        Session session = new SyncSession.Builder("localhost", PORT1).connectionType(ConnectionType.RECEIVER).
                systemId("client").password("pass").systemType("test").pduHandler(this).build();
        session.stop();
    }

    @Test
    public void bindTransceiverTest() throws SmppException {
        Session session = new SyncSession.Builder("localhost", PORT2).
                systemId("client").password("pass").systemType("test").pduHandler(this).build();
        session.stop();
    }

    @Test
    public void bindTransmitterTest() throws SmppException {
        Session session = new SyncSession.Builder("localhost", PORT1).connectionType(ConnectionType.TRANSMITTER).
                systemId("client").password("pass").systemType("test").pduHandler(this).build();
        session.stop();
    }

    @Test(expected = SmppException.class)
    public void bindFailedTest() throws SmppException {
        new SyncSession.Builder("localhost", PORT4).connectionType(ConnectionType.TRANSMITTER).
                systemId("client").password("pass").systemType("test").pduHandler(this).build();
    }

    @Test(expected = SmppException.class)
    public void bindFailedTest2() throws SmppException {
        new SyncSession.Builder("localhost", PORT5).connectionType(ConnectionType.TRANSMITTER).
                systemId("client").password("pass").systemType("test").pduHandler(this).build();
    }

    public PDU received(PDU pdu) {
        return null;
    }

    private static class BindCheckSMPPServer implements Runnable {

        private final PDUFactory factory = PDUFactoryImpl.INSTANCE;
        private int port;
        private boolean run = true;

        public BindCheckSMPPServer(int port) {
            this.port = port;
        }

        public void run() {
            ServerSocket serverSocket = null;
            try {
                serverSocket = new ServerSocket(port);
                byte[] buffer = new byte[37];
                byte[] buffer2 = new byte[16];
                PDU pdu;
                Socket client;
                OutputStream out;
                InputStream in;
                while (run) {
                    client = serverSocket.accept();
                    in = client.getInputStream();
                    out = client.getOutputStream();
                    in.read(buffer);
                    pdu = factory.parsePDU(buffer);
                    switch (pdu.getCommandId()) {
                        case BIND_RECEIVER:
                            assertEquals("00000025000000010000000000000001636c69656e74007061737300746573740034000000", new SmppByteBuffer(pdu.getBytes()).getHexDump());
                            break;
                        case BIND_TRANSCEIVER:
                            assertEquals("00000025000000090000000000000001636c69656e74007061737300746573740034000000", new SmppByteBuffer(pdu.getBytes()).getHexDump());
                            break;
                        case BIND_TRANSMITTER:
                            assertEquals("00000025000000020000000000000001636c69656e74007061737300746573740034000000", new SmppByteBuffer(pdu.getBytes()).getHexDump());
                            break;
                    }
                    out.write(((Responsable) pdu).getResponse().getBytes());
                    in.read(buffer2);
                    assertEquals("00000010000000060000000000000002", new SmppByteBuffer(buffer2).getHexDump());
                    out.close();
                    in.close();
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

    private static class WrongSeqNumSMPPServer implements Runnable {

        private final PDUFactory factory = PDUFactoryImpl.INSTANCE;
        private int port;
        private boolean run = true;

        public WrongSeqNumSMPPServer(int port) {
            this.port = port;
        }

        public void run() {
            ServerSocket serverSocket = null;
            try {
                serverSocket = new ServerSocket(port);
                byte[] buffer = new byte[37];
                PDU pdu;
                Socket client;
                OutputStream out;
                InputStream in;
                while (run) {
                    client = serverSocket.accept();
                    in = client.getInputStream();
                    out = client.getOutputStream();
                    in.read(buffer);
                    pdu = ((Responsable)factory.parsePDU(buffer)).getResponse();
                    pdu.setSequenceNumber(100);
                    out.write(pdu.getBytes());
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

    private static class WrongPDUSMPPServer implements Runnable {

        private final PDUFactory factory = PDUFactoryImpl.INSTANCE;
        private int port;
        private boolean run = true;

        public WrongPDUSMPPServer(int port) {
            this.port = port;
        }

        public void run() {
            ServerSocket serverSocket = null;
            try {
                serverSocket = new ServerSocket(port);
                byte[] buffer = new byte[37];
                PDU pdu;
                Socket client;
                OutputStream out;
                InputStream in;
                while (run) {
                    client = serverSocket.accept();
                    in = client.getInputStream();
                    out = client.getOutputStream();
                    in.read(buffer);
                    pdu = new BindReceiverResp();
                    pdu.setSequenceNumber(2);
                    out.write(pdu.getBytes());
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
