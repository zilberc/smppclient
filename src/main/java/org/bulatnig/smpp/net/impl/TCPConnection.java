package org.bulatnig.smpp.net.impl;

import org.bulatnig.smpp.SmppException;
import org.bulatnig.smpp.net.Connection;
import org.bulatnig.smpp.pdu.PDU;
import org.bulatnig.smpp.pdu.PDUException;
import org.bulatnig.smpp.pdu.PDUFactory;
import org.bulatnig.smpp.pdu.PDUFactoryImpl;
import org.bulatnig.smpp.util.SmppByteBuffer;
import org.bulatnig.smpp.util.WrongLengthException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * TCP/IP implementation of Connection. Designed to be immutable.
 * <p/>
 * User: Bulat Nigmatullin
 * Date: 29.06.2008
 * Time: 21:44:24
 */
public final class TCPConnection implements Connection {

    /**
     * Logger for this class and subclasses
     */
    private static final Logger logger = LoggerFactory.getLogger(TCPConnection.class);

    /**
     * Default max length of incoming PDU that will be accepted.
     */
    public static final int DEFAULT_MAX_PDU_LENGTH = 1000;
    /**
     * Default socket SO_TIMEOUT value.
     */
    public static final int DEFAULT_SO_TIMEOUT = 10;
    /**
     * Max reads from input stream. Defined to prevent buffer overflow.
     */
    private static final int MAX_READS = 5;

    /**
     * SMSC address.
     */
    private final String host;
    /**
     * SMSC port.
     */
    private final int port;
    /**
     * Max length of incoming PDU that will be accepted.
     */
    private final int maxAcceptedPduLength;

    private InputStream in;
    private OutputStream out;
    private SmppByteBuffer sbb = new SmppByteBuffer();
    /**
     * PDUFactory actually handles pdu creation from bytes.
     */
    private PDUFactory factory = PDUFactoryImpl.INSTANCE;

    private TCPConnection(Builder builder) throws IOException {
        this.host = builder.host;
        this.port = builder.port;
        this.maxAcceptedPduLength = builder.maxAcceptedPduLength;
        int connectTimeout = builder.connectTimeout;
        int soTimeout = builder.soTimeout;
        Socket connection = TimedSocket.getSocket(host, port, connectTimeout);
        connection.setSoTimeout(soTimeout);
        in = connection.getInputStream();
        out = connection.getOutputStream();
    }

    public static class Builder {

        private final String host;
        private final int port;
        private int connectTimeout = DEFAULT_CONNECT_TIMEOUT;
        private int maxAcceptedPduLength = DEFAULT_MAX_PDU_LENGTH;
        private int soTimeout = DEFAULT_SO_TIMEOUT;

        public Builder(String host, int port) {
            this.host = host;
            this.port = port;
        }

        public Builder connectTimeout(int connectTimeout) {
            this.connectTimeout = connectTimeout;
            return this;
        }

        public Builder maxAcceptedPduLength(int maxAcceptedPduLength) {
            this.maxAcceptedPduLength = maxAcceptedPduLength;
            return this;
        }

        public Builder soTimeout(int soTimeout) {
            this.soTimeout = soTimeout;
            return this;
        }

        public TCPConnection build() throws IOException {
            return new TCPConnection(this);
        }

    }

    /**
     * {@inheritDoc}
     */
    public void close() {
        if (in != null) {
            try {
                in.close();
            } catch (IOException e) {
                logger.error("Input stream close error", e);
            }
        }
        if (out != null) {
            try {
                out.close();
            } catch (IOException e) {
                logger.error("Output stream close error", e);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public List<PDU> read() throws IOException {
        List<PDU> pdus = new ArrayList<PDU>();
        // читаем все доступные данные
        byte[] buffer = new byte[1024];
        int bytesRead;
        int reads = 0;
        try {
            do {
                bytesRead = in.read(buffer);
                if (bytesRead == 0)
                    break;
                sbb.appendBytes(buffer, bytesRead);
                reads++;
            } while (reads < MAX_READS);
        } catch (InterruptedIOException e) {
            // omit it cause it was thrown by soTimeout
        }
        long pduLength;
        PDU pdu;
        // парсим их
        while (sbb.length() > 4) {
            try {
                pduLength = sbb.readInt();
                if (pduLength < PDU.HEADER_LENGTH || pduLength > maxAcceptedPduLength) {
                    logger.warn("Wrong length PDU received. Buffer hex dump: {}", sbb.getHexDump());
                    // there is no certainty that all SmppByteBuffer contain correct values, so invalidate it
                    sbb = new SmppByteBuffer();
                    break;
                } else if (pduLength <= sbb.length()) {
                    try {
                        buffer = sbb.removeBytes((int) pduLength).getBuffer();
                    } catch (SmppException e) {
                        sbb = new SmppByteBuffer();
                        throw new IOException("FATAL ERROR while removing bytes from buffer", e);
                    }
                    try {
                        pdu = factory.parsePDU(buffer);
                        logger.info(">>> {}", new SmppByteBuffer(pdu.getBytes()).getHexDump());
                        pdus.add(pdu);
                    } catch (PDUException e) {
                        logger.warn("PDU parsing failed. Hexdump: {}", new SmppByteBuffer(buffer).getHexDump());
                    }
                } else {
                    // bytes not enough to assemble full PDU, retain them to the next read call
                    break;
                }
            } catch (WrongLengthException e) {
                throw new IOException("FATAL ERROR while reading pdu length", e);
            }
        }
        return pdus;
    }

    /**
     * {@inheritDoc}
     */
    public void write(PDU pdu) throws IOException, PDUException {
        out.write(pdu.getBytes());
        logger.info("<<< {}", new SmppByteBuffer(pdu.getBytes()).getHexDump());
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public int getMaxAcceptedPduLength() {
        return maxAcceptedPduLength;
    }
}
