package junit4.smpp.session;

import junit.framework.JUnit4TestAdapter;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.bulatnig.smpp.SMPPException;
import org.bulatnig.smpp.pdu.*;
import org.bulatnig.smpp.session.ConnectionType;
import org.bulatnig.smpp.session.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Comment here.
 * <p/>
 * User: Bulat Nigmatullin
 * Date: Nov 6, 2008
 * Time: 3:43:48 PM
 */
public class  SessionSendTest implements PDUHandler {

// Used for backward compatibility (IDEs, Ant and JUnit 3 text runner)

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(SessionSendTest.class);
    }

    private static SuccessfulSMPPServer server1;
    private static final int PORT1 = 33331;
    private static WrongSeqNumSMPPServer server2;
    private static final int PORT2 = 33332;
    private static WrongPDUSMPPServer server3;
    private static final int PORT3 = 33333;

    @BeforeClass
    public static void startupServer() {
        server1 = new SuccessfulSMPPServer(PORT1);
        new Thread(server1).start();
        server2 = new WrongSeqNumSMPPServer(PORT2);
        new Thread(server2).start();
        server3 = new WrongPDUSMPPServer(PORT3);
        new Thread(server3).start();
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
    }

    public PDU received(PDU pdu) {
        return null;
    }

    @Test
    public void successfulSendTest() throws IOException, SMPPException {
        SMPPSession session = new SyncSMPPSession.Builder("localhost", PORT1).systemId("client").
                password("pass").systemType("test").pduHandler(this).build();
        session.send(new SubmitSM());
        session.stop();
    }

    @Test(expected = NoResponseException.class)
    public void wrongSequenceNumberTest() throws IOException, SMPPException {
        SMPPSession session = new SyncSMPPSession.Builder("localhost", PORT2).systemId("client").
                password("pass").systemType("test").pduHandler(this).build();
        session.send(new SubmitSM());
        session.stop();
    }

    @Test(expected = NoResponseException.class)
    public void wrongPDUTest() throws IOException,SMPPException {
        SMPPSession session = new SyncSMPPSession.Builder("localhost", PORT3).systemId("client").
                password("pass").systemType("test").pduHandler(this).build();
        session.send(new SubmitSM());
        session.stop();
    }

    private static class SuccessfulSMPPServer implements Runnable {

        private final PDUFactory factory = PDUFactoryImpl.INSTANCE;
        private int port;
        private boolean run = true;

        public SuccessfulSMPPServer(int port) {
            this.port = port;
        }

        public void run() {
            ServerSocket serverSocket = null;
            try {
                serverSocket = new ServerSocket(port);
                byte[] buffer = new byte[37];
                byte[] buffer2 = new byte[33];
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
                    out.write(((Responsable) pdu).getResponse().getBytes());
                    in.read(buffer2);
                    pdu = factory.parsePDU(buffer2);
                    out.write(((Responsable) pdu).getResponse().getBytes());
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
                byte[] buffer2 = new byte[33];
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
                    out.write(((Responsable) pdu).getResponse().getBytes());
                    in.read(buffer2);
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
                byte[] buffer2 = new byte[33];
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
                    out.write(((Responsable) pdu).getResponse().getBytes());
                    in.read(buffer2);
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
