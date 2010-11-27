package org.bulatnig.smpp.net.impl;

import org.bulatnig.smpp.net.Connection;
import org.bulatnig.smpp.pdu.CommandId;
import org.bulatnig.smpp.pdu.CommandStatus;
import org.bulatnig.smpp.pdu.GenericNack;
import org.bulatnig.smpp.pdu.Pdu;
import org.bulatnig.smpp.testutil.SmscStub;
import org.bulatnig.smpp.testutil.UniquePortGenerator;
import org.bulatnig.smpp.util.ByteBuffer;
import org.junit.Test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * TcpConnection test.
 *
 * @author Bulat Nigmatullin
 */
public class TcpConnectionTest {

    @Test(expected = IOException.class)
    public void targetPortClosedTest() throws IOException {
        Connection conn = new TcpConnection(new InetSocketAddress("localhost", UniquePortGenerator.generate()));
        conn.open();
    }

    @Test(expected = IOException.class)
    public void unknownHostnameTest() throws IOException {
        Connection conn = new TcpConnection(new InetSocketAddress("noname.com", UniquePortGenerator.generate()));
        conn.open();
    }

    @Test(expected = IOException.class)
    public void targetIPUnreachableTest() throws IOException {
        Connection conn = new TcpConnection(new InetSocketAddress("1.0.0.0", UniquePortGenerator.generate()));
        conn.open();
    }

    @Test
    public void connect() throws Exception {
        final int port = UniquePortGenerator.generate();
        SmscStub smsc = new SmscStub(port);
        smsc.start();
        try {
            Connection conn = new TcpConnection(new InetSocketAddress("localhost", port));
            conn.open();
            conn.close();
        } finally {
            smsc.stop();
        }
    }

    @Test
    public void write() throws Exception {
        final int port = UniquePortGenerator.generate();
        final Pdu pdu = new GenericNack();
        SmscStub smsc = new SmscStub(port);
        smsc.start();
        try {
            Connection conn = new TcpConnection(new InetSocketAddress("localhost", port));
            conn.open();
            conn.write(pdu);
            Thread.sleep(20);
            conn.close();
        } finally {
            smsc.stop();
        }

        assertArrayEquals(pdu.buffer().array(), smsc.input.get(0));
    }

    @Test
    public void read() throws Exception {
        final int port = UniquePortGenerator.generate();
        final Pdu pdu = new GenericNack();
        pdu.setCommandStatus(500);
        pdu.setSequenceNumber(1034234);
        Pdu read;
        SmscStub smsc = new SmscStub(port);
        smsc.start();
        try {
            Connection conn = new TcpConnection(new InetSocketAddress("localhost", port));
            conn.open();
            conn.write(new GenericNack());
            smsc.write(pdu.buffer().array());
            read = conn.read();
            conn.close();
        } finally {
            smsc.stop();
        }

        assertEquals(pdu.getCommandId(), read.getCommandId());
        assertEquals(500, read.getCommandStatus());
        assertEquals(1034234, read.getSequenceNumber());
        assertArrayEquals(pdu.buffer().array(), read.buffer().array());
    }

    @Test
    public void readDelayed() throws Exception {
        final int port = UniquePortGenerator.generate();
        final Pdu pdu = new GenericNack();
        pdu.setCommandStatus(123098);
        pdu.setSequenceNumber(0);
        final SmscStub smsc = new SmscStub(port);
        smsc.start();
        Pdu read;
        try {
            Connection conn = new TcpConnection(new InetSocketAddress("localhost", port));
            conn.open();
            conn.write(new GenericNack());
            Thread sendThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(100);
                        smsc.write(pdu.buffer().array());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            sendThread.start();
            read = conn.read();
            conn.close();
        } finally {
            smsc.stop();
        }

        assertEquals(pdu.getCommandId(), read.getCommandId());
        assertEquals(123098, read.getCommandStatus());
        assertEquals(0, read.getSequenceNumber());
        assertArrayEquals(pdu.buffer().array(), read.buffer().array());
    }

    @Test
    public void readInTwoParts() throws Exception {
        final int port = UniquePortGenerator.generate();
        final long lenght = Pdu.HEADER_LENGTH;
        final long commandId = CommandId.GENERIC_NACK;
        final long commandStatus = CommandStatus.ESME_RINVDLNAME;
        final long sequenceNumber = 123456789;
        final byte[] part1 = new ByteBuffer().appendInt(lenght).appendInt(commandId).array();
        final byte[] part2 = new ByteBuffer().appendInt(commandStatus).appendInt(sequenceNumber).array();
        final Pdu pdu = new GenericNack();
        pdu.setCommandStatus(commandStatus);
        pdu.setSequenceNumber(sequenceNumber);

        final SmscStub smsc = new SmscStub(port);
        smsc.start();
        Pdu read;
        try {
            Connection conn = new TcpConnection(new InetSocketAddress("localhost", port));
            conn.open();
            conn.write(new GenericNack());
            Thread sendThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(100);
                        smsc.write(part1);
                        smsc.write(part2);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            sendThread.start();
            read = conn.read();
            conn.close();
        } finally {
            smsc.stop();
        }

        assertEquals(commandId, read.getCommandId());
        assertEquals(commandStatus, read.getCommandStatus());
        assertEquals(sequenceNumber, read.getSequenceNumber());
        assertArrayEquals(pdu.buffer().array(), read.buffer().array());
    }

}
