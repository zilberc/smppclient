package org.bulatnig.smpp.net.impl;

import org.bulatnig.smpp.net.Connection;
import org.bulatnig.smpp.pdu.Pdu;
import org.bulatnig.smpp.pdu.PduException;
import org.bulatnig.smpp.util.ByteBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * TCP/IP connection.
 *
 * @author Bulat Nigmatullin
 */
public class TcpConnection implements Connection {

    private static final Logger logger = LoggerFactory.getLogger(TcpConnection.class);

    private final SocketAddress socketAddress;
    private int bufferSize = DEFAULT_BUFFER_SIZE;

    private Socket socket;
    private InputStream in;
    private OutputStream out;

    private ByteBuffer bb;

    public TcpConnection(SocketAddress socketAddress) {
        this.socketAddress = socketAddress;
    }

    @Override
    public void setIncomingBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }

    @Override
    public void open() throws IOException {
        socket = new Socket();
        socket.connect(socketAddress);
        socket.setSoTimeout(0); // block read forever
        in = socket.getInputStream();
        out = socket.getOutputStream();
        bb = new ByteBuffer();
    }

    @Override
    public Pdu read() throws PduException, IOException {

        return null;
    }

    @Override
    public void write(Pdu pdu) throws PduException, IOException {
    }

    @Override
    public void close() {
        try {
            socket.close();
        } catch (IOException e) {
            logger.trace("Connection closing error.", e);
        }
        socket = null;
        in = null;
        out = null;
        bb = null;
    }
}
