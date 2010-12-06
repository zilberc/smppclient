package org.bulatnig.smpp.session.impl;

import org.bulatnig.smpp.SmppException;
import org.bulatnig.smpp.net.impl.TcpConnection;
import org.bulatnig.smpp.pdu.CommandId;
import org.bulatnig.smpp.pdu.Pdu;
import org.bulatnig.smpp.pdu.PduException;
import org.bulatnig.smpp.pdu.PduParser;
import org.bulatnig.smpp.pdu.impl.*;
import org.bulatnig.smpp.session.Session;
import org.bulatnig.smpp.session.SessionListener;
import org.bulatnig.smpp.util.ByteBuffer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

import static org.junit.Assert.assertEquals;

/**
 * Comments here.
 * <p/>
 * User: Bulat Nigmatullin
 * Date: Nov 5, 2008
 * Time: 7:17:35 PM
 */
public class SessionBindTest implements SessionListener {

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
    public void bindReceiverTest() throws PduException, IOException {
        Session session = new SessionImpl(new TcpConnection(new InetSocketAddress("localhost", PORT1)));
        BindReceiver bindRequest = new BindReceiver();
        bindRequest.setSystemId("client");
        bindRequest.setPassword("pass");
        bindRequest.setSystemId("client");
        bindRequest.setSystemType("test");
        bindRequest.setInterfaceVersion(0x34);
        Pdu bindResp = session.open(bindRequest);
        session.send(new Unbind());
        session.close();

        assertEquals(0, bindResp.getCommandStatus());
    }

    @Test
    public void bindTransceiverTest() throws PduException, IOException {
        Session session = new SessionImpl(new TcpConnection(new InetSocketAddress("localhost", PORT2)));
        BindTransceiver bindRequest = new BindTransceiver();
        bindRequest.setSystemId("client");
        bindRequest.setPassword("pass");
        bindRequest.setSystemId("client");
        bindRequest.setSystemType("test");
        bindRequest.setInterfaceVersion(0x34);
        Pdu bindResp = session.open(bindRequest);
        session.send(new Unbind());
        session.close();

        assertEquals(0, bindResp.getCommandStatus());
    }

    @Test
    public void bindTransmitterTest() throws PduException, IOException {
        Session session = new SessionImpl(new TcpConnection(new InetSocketAddress("localhost", PORT3)));
        BindTransmitter bindRequest = new BindTransmitter();
        bindRequest.setSystemId("client");
        bindRequest.setPassword("pass");
        bindRequest.setSystemId("client");
        bindRequest.setSystemType("test");
        bindRequest.setInterfaceVersion(0x34);
        Pdu bindResp = session.open(bindRequest);
        session.send(new Unbind());
        session.close();

        assertEquals(0, bindResp.getCommandStatus());
    }

    @Test
    public void bindFailedTest() throws PduException, IOException {
        Session session = new SessionImpl(new TcpConnection(new InetSocketAddress("localhost", PORT4)));
        BindTransmitter bindRequest = new BindTransmitter();
        bindRequest.setSystemId("client");
        bindRequest.setPassword("pass");
        bindRequest.setSystemId("client");
        bindRequest.setSystemType("test");
        bindRequest.setInterfaceVersion(0x34);
        Pdu bindResp = session.open(bindRequest);
        session.close();

        assertEquals(100, bindResp.getSequenceNumber());
    }

    @Test
    public void bindFailedTest2() throws PduException, IOException {
        Session session = new SessionImpl(new TcpConnection(new InetSocketAddress("localhost", PORT5)));
        BindTransmitter bindRequest = new BindTransmitter();
        bindRequest.setSystemId("client");
        bindRequest.setPassword("pass");
        bindRequest.setSystemId("client");
        bindRequest.setSystemType("test");
        bindRequest.setInterfaceVersion(0x34);
        Pdu bindResp = session.open(bindRequest);
        session.close();

        assertEquals(CommandId.BIND_RECEIVER_RESP, bindResp.getCommandId());
    }

    @Override
    public Pdu received(Pdu pdu) {
        return null;
    }

    private static class BindCheckSMPPServer implements Runnable {

        private final PduParser parser = new DefaultPduParser();
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
                Pdu pdu;
                Socket client;
                OutputStream out;
                InputStream in;
                while (run) {
                    client = serverSocket.accept();
                    in = client.getInputStream();
                    out = client.getOutputStream();
                    in.read(buffer);
                    pdu = parser.parse(new ByteBuffer(buffer));
                    Pdu response;
                    if (CommandId.BIND_RECEIVER == pdu.getCommandId()) {
                        assertEquals("00000025000000010000000000000001636c69656e74007061737300746573740034000000", pdu.buffer().hexDump());
                        response = new BindReceiverResp();
                    } else if (CommandId.BIND_TRANSCEIVER == pdu.getCommandId()) {
                        assertEquals("00000025000000090000000000000001636c69656e74007061737300746573740034000000", pdu.buffer().hexDump());
                        response = new BindTransceiverResp();
                    } else {
                        assertEquals("00000025000000020000000000000001636c69656e74007061737300746573740034000000", pdu.buffer().hexDump());
                        response = new BindReceiverResp();
                    }
                    response.setSequenceNumber(pdu.getSequenceNumber());
                    out.write(response.buffer().array());
                    in.read(buffer2);
                    assertEquals("00000010000000060000000000000002", new ByteBuffer(buffer2).hexDump());

                    // TODO refactor
                    Thread.sleep(2000);

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

        private final PduParser parser = new DefaultPduParser();
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
                Pdu pdu;
                Socket client;
                OutputStream out;
                InputStream in;
                while (run) {
                    client = serverSocket.accept();
                    in = client.getInputStream();
                    out = client.getOutputStream();
                    in.read(buffer);
                    pdu = new BindTransmitterResp();
                    pdu.setSequenceNumber(100);
                    out.write(pdu.buffer().array());
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

        private final PduParser parser = new DefaultPduParser();
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
                Pdu pdu;
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
                    out.write(pdu.buffer().array());
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
