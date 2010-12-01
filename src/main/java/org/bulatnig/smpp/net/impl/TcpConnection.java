package org.bulatnig.smpp.net.impl;

import org.bulatnig.smpp.net.Connection;
import org.bulatnig.smpp.pdu.*;
import org.bulatnig.smpp.pdu.impl.DefaultPduParser;
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
 * Not thread safe.
 *
 * @author Bulat Nigmatullin
 */
public class TcpConnection implements Connection {

    private static final Logger logger = LoggerFactory.getLogger(TcpConnection.class);

    private final SocketAddress socketAddress;
    private int bufferSize = DEFAULT_BUFFER_SIZE;
    private PduParser parser = new DefaultPduParser();

    private Socket socket;
    private InputStream in;
    private OutputStream out;

    private byte[] bytes;
    private ByteBuffer bb;

    public TcpConnection(SocketAddress socketAddress) {
        this.socketAddress = socketAddress;
    }

    @Override
    public void setIncomingBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }

    @Override
    public void setParser(PduParser parser) {
        this.parser = parser;
    }

    @Override
    public void open() throws IOException {
        socket = new Socket();
        socket.connect(socketAddress);
        socket.setSoTimeout(0); // block read forever
        in = socket.getInputStream();
        out = socket.getOutputStream();
        bytes = new byte[bufferSize];
        bb = new ByteBuffer();
    }

    @Override
    public Pdu read() throws PduParsingException, PduNotFoundException, IOException {
        Pdu pdu = tryToReadBuffer();
        while (pdu == null) {
            int read = in.read(bytes);
            bb.appendBytes(bytes, read);
            pdu = tryToReadBuffer();
        }
        return pdu;
    }

    @Override
    public void write(Pdu pdu) throws PduParsingException, IOException {
        out.write(pdu.buffer().array());
        logger.info("<<< {}", pdu.buffer().hexDump());
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
        bytes = null;
        bb = null;
    }

    private Pdu tryToReadBuffer() throws PduParsingException, PduNotFoundException {
        if (bb.length() >= Pdu.HEADER_LENGTH) {
            long commandLength = bb.readInt();
            if (bb.length() >= commandLength)
                return parser.parse(new ByteBuffer(bb.removeBytes((int) commandLength)));
        }
        return null;
    }

}
