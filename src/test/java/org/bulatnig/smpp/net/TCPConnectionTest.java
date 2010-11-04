package org.bulatnig.smpp.net;

import junit.framework.JUnit4TestAdapter;
import org.bulatnig.smpp.net.impl.TCPConnection;
import org.bulatnig.smpp.util.SmppByteBuffer;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.BeforeClass;
import org.junit.Test;
import org.bulatnig.smpp.pdu.CommandId;
import org.bulatnig.smpp.pdu.CommandStatus;
import org.bulatnig.smpp.pdu.PDU;
import org.bulatnig.smpp.pdu.PDUException;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

/**
 * Comment here.
 * User: Bulat Nigmatullin
 * Date: 04.07.2008
 * Time: 7:13:13
 */
public class TCPConnectionTest {

    private static SMPPServer server1;
    private static NoResponseSMPPServer server2;
    private static FloodingSMPPServer server3;
    private static NullLengthSMPPServer server4;
    private static final int PORT1 = 55555;
    private static final int PORT2 = 55556;
    private static final int PORT3 = 55557;
    private static final int PORT4 = 55558;

    // Used for backward compatibility (IDEs, Ant and JUnit 3 text runner)

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(TCPConnectionTest.class);
    }

    @BeforeClass
    public static void startupServer() {
        server1 = new SMPPServer(PORT1);
        new Thread(server1).start();
        server2 = new NoResponseSMPPServer(PORT2);
        new Thread(server2).start();
        server3 = new FloodingSMPPServer(PORT3);
        new Thread(server3).start();
        server4 = new NullLengthSMPPServer(PORT4);
        new Thread(server4).start();

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
    }

    @Test
    public void connectionNormalTest() throws Exception {
        Connection connection = null;
        try {
            connection = new TCPConnection.Builder("localhost", PORT1).build();
            List<PDU> pdus = connection.read();
            PDU pdu = pdus.get(0);
            assertEquals(CommandId.ENQUIRE_LINK, pdu.getCommandId());
            assertEquals(5L, pdu.getSequenceNumber());
            pdu = pdus.get(1);
            assertEquals(CommandId.GENERIC_NACK, pdu.getCommandId());
            assertEquals(CommandStatus.ESME_RINVCMDID, pdu.getCommandStatus());
            pdu = pdus.get(2);
            assertEquals(CommandId.BIND_RECEIVER_RESP, pdu.getCommandId());
            assertEquals(CommandStatus.ESME_RDELIVERYFAILURE, pdu.getCommandStatus());
        } finally {
            if (connection != null)
                connection.close();
        }
    }

    @Test(expected = IOException.class)
    public void targetPortClosedTest() throws IOException {
        Connection connection = null;
        try {
            connection = new TCPConnection.Builder("localhost", 65000).build();
        } finally {
            if (connection != null)
                connection.close();
        }
    }

    @Test(expected = IOException.class)
    public void unknownHostnameTest() throws IOException {
        Connection connection = null;
        try {
            connection = new TCPConnection.Builder("noname.com", 65000).build();
        } finally {
            if (connection != null)
                connection.close();
        }
    }

    @Test(expected = IOException.class, timeout = 3300)
    public void targetIPUnreachableTest() throws IOException {
        Connection connection = null;
        try {
            connection = new TCPConnection.Builder("1.0.0.0", 65000).build();
        } finally {
            if (connection != null)
                connection.close();
        }
    }

    @Test(timeout = 200)
    public void readTimerTest() throws IOException, PDUException {
        Connection connection = null;
        try {
            connection = new TCPConnection.Builder("localhost", PORT2).build();
            connection.read();
        } finally {
            if (connection != null)
                connection.close();
        }
    }

    @Test(timeout = 500)
    public void floodTest() throws Exception {
        Connection connection = null;
        try {
            connection = new TCPConnection.Builder("localhost", PORT3).build();
            List<PDU> pdus = connection.read();
            assertEquals(1, pdus.size());
            PDU pdu = pdus.get(0);
            assertEquals(CommandId.ENQUIRE_LINK, pdu.getCommandId());
            assertEquals(5L, pdu.getSequenceNumber());
            do {
                pdus = connection.read();
            } while (pdus.isEmpty());
            pdu = pdus.get(0);
            assertEquals(CommandId.GENERIC_NACK, pdu.getCommandId());
            assertEquals(CommandStatus.ESME_RINVCMDID, pdu.getCommandStatus());
            pdu = pdus.get(1);
            assertEquals(CommandId.BIND_RECEIVER_RESP, pdu.getCommandId());
            assertEquals(CommandStatus.ESME_RDELIVERYFAILURE, pdu.getCommandStatus());
        } finally {
            if (connection != null)
                connection.close();
        }
    }

    @Test
    public void nullLengthTest() throws Exception {
        Connection connection = null;
        try {
            connection = new TCPConnection.Builder("localhost", PORT4).build();
            assertEquals(0, connection.read().size());
        } finally {
            if (connection != null)
                connection.close();
        }
    }

    private static class SMPPServer implements Runnable {

        private int port;
        private boolean run = true;

        public SMPPServer(int port) {
            this.port = port;
        }

        public void run() {
            ServerSocket serverSocket = null;
            try {
                serverSocket = new ServerSocket(port);
                Socket client;
                OutputStream out;
                SmppByteBuffer sbb = new SmppByteBuffer();
                sbb.appendInt(16);
                sbb.appendInt(0x00000015L);
                sbb.appendInt(0);
                sbb.appendInt(5);
                sbb.appendInt(16);
                sbb.appendInt(0x80000000L);
                sbb.appendInt(0x00000003L);
                sbb.appendInt(0);
                sbb.appendInt(17);
                sbb.appendInt(0x80000001L);
                sbb.appendInt(0x000000FEL);
                sbb.appendInt(0);
                sbb.appendByte((byte) 0);
                while (run) {
                    client = serverSocket.accept();
                    out = client.getOutputStream();
                    out.write(sbb.array());
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

    private static class FloodingSMPPServer implements Runnable {

        private int port;
        private boolean run = true;

        public FloodingSMPPServer(int port) {
            this.port = port;
        }

        public void run() {
            ServerSocket serverSocket = null;
            try {
                serverSocket = new ServerSocket(port);
                Socket client;
                OutputStream out;
                SmppByteBuffer sbb = new SmppByteBuffer();
                sbb.appendInt(16);
                sbb.appendInt(0x00000015L);
                sbb.appendInt(0);
                sbb.appendInt(5);
                sbb.appendInt(16);
                SmppByteBuffer sbb2 = new SmppByteBuffer();
                sbb2.appendInt(0x00000005L);
                sbb2.appendInt(0x00000000L);
                sbb2.appendInt(4);
                sbb2.appendInt(16);
                sbb2.appendInt(0x80000000L);
                sbb2.appendInt(0x00000003L);
                sbb2.appendInt(0);
                sbb2.appendInt(17);
                sbb2.appendInt(0x80000001L);
                sbb2.appendInt(0x000000FEL);
                sbb2.appendInt(0);
                sbb2.appendByte((byte) 0);
                while (run) {
                    client = serverSocket.accept();
                    out = client.getOutputStream();
                    out.write(sbb.array());
                    out.flush();
                    Thread.sleep(150);
                    out.write(sbb2.array());
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

    private static class NullLengthSMPPServer implements Runnable {

        private int port;
        private boolean run = true;

        public NullLengthSMPPServer(int port) {
            this.port = port;
        }

        public void run() {
            ServerSocket serverSocket = null;
            try {
                serverSocket = new ServerSocket(port);
                Socket client;
                OutputStream out;
                SmppByteBuffer sbb = new SmppByteBuffer();
                sbb.appendInt(0);
                sbb.appendInt(0x00000015L);
                sbb.appendInt(0);
                sbb.appendInt(5);
                while (run) {
                    client = serverSocket.accept();
                    out = client.getOutputStream();
                    out.write(sbb.array());
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
