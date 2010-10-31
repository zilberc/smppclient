package org.bulatnig.smpp.session.impl;

import org.bulatnig.smpp.SmppException;
import org.bulatnig.smpp.net.Connection;
import org.bulatnig.smpp.net.impl.TCPConnection;
import org.bulatnig.smpp.pdu.*;
import org.bulatnig.smpp.session.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Synchronous SMPP session.
 * Connects on create to SMSC and works until stop() call. If there are no inconming or outcoming messages,
 * then ping packets (Enquire Link) sends. It ping fails, then session moves to recovery mode and trying
 * reconnect to SMSC. All changes in session states are notified to SmppSessionStateListener.
 * If error occurs during message sending, then user responsibility to take a corresponding action: resend or
 * stop current session and create new one.
 * As activity considered only SMSC messages. There is 30 second activity timeout, then ping packet sends to server.
 * If last message from server was received more than 30 seconds ago but user continue tries to send message to server
 * in loop, then send blocked and ping operation started. If ping fails session stopped. If ping OK, then user
 * can continue to send messages through this session.
 *
 * @author Bulat Nigmatullin
 */
public final class SyncSession implements Session {

    /**
     * Logger for this class and subclasses
     */
    static final Logger logger = LoggerFactory.getLogger(SyncSession.class);

    /**
     * Default connection type.
     */
    public static final ConnectionType DEFAULT_CONNECTION_TYPE = ConnectionType.TRANSCEIVER;
    /**
     * SMPP protocol version.
     */
    private static final short INTERFACE_VERSION = 0x34;

    /**
     * Таймаут ответного PDU по-умолчанию.
     */
    private static final int RESPONSE_TIMEOUT = 5000;
    /**
     * Кол-во попыток подключения.
     */
    private static final int MAX_CONNECT_ATTEMPTS = 5;
    /**
     * Таймаут переподключения в случае ошибки ввода/вывода по-умолчанию.
     */
    private static final int RECONNECT_TIMEOUT = 15000;
    /**
     * EnquireLink таймаут. Служит для поддержания сессии активной во время отстутствия сообщений.
     */
    private static final int ENQUIRE_LINK_TIMEOUT = 30000;

    /**
     * SMSC host address.
     */
    private final String host;
    /**
     * SMSC port.
     */
    private final int port;
    /**
     * Соединение с SMSC.
     */
    private Connection connection;
    /**
     * Тип соединения.
     */
    private final ConnectionType connType;
    /**
     * Идентификатор клиента.
     */
    private final String systemId;
    /**
     * Пароль клиента.
     */
    private final String password;
    /**
     * Тип системы клиента.
     */
    private final String systemType;
    /**
     * TON адресов клиента.
     */
    private final TON addrTon;
    /**
     * NPI адресов клиента.
     */
    private final NPI addrNpi;
    /**
     * Диапазон адресов клиента.
     */
    private final String addressRange;
    /**
     * Обработчик входящих сообщений.
     */
    private PDUHandler pduHandler;
    /**
     * Session state listener.
     */
    private SmppSessionStateListener stateListener;

    /**
     * Session state.
     */
    private SmppSessionState state = SmppSessionState.OK;
    /**
     * Время последней активности в миллисекундах.
     * Т.е. время последнего получения PDU.
     */
    private Long lastActivity;
    /**
     * Уникальный в рамках сессии номер PDU.
     */
    private long sequenceNumber = 1;
    /**
     * Таймер, выполняющий задачи поддержания сессии и чтения входных данных.
     */
    private Timer timer;
    /**
     * Блок, используемый для синхронизации досупа к I/O операциям сессии.
     */
    private Lock ioLock = new ReentrantLock();
    /**
     * Session work indicator. It should be verified few time in long operations
     * to faster reply on stop call which only turns work to false.
     * There is no way to continue once stopped/closed session.
     */
    protected boolean work = true;

    private SyncSession(Builder builder) throws SmppException {
        this.host = builder.host;
        this.port = builder.port;
        this.connection = builder.connection;
        this.connType = builder.connType;
        this.systemId = builder.systemId;
        this.password = builder.password;
        this.systemType = builder.systemType;
        this.addrTon = builder.addrTon;
        this.addrNpi = builder.addrNpi;
        this.addressRange = builder.addressRange;
        this.pduHandler = builder.pduHandler;
        this.stateListener = builder.stateListener;
        start();
    }

    public static class Builder {

        private final String host;
        private final int port;
        private final Connection connection;
        private ConnectionType connType = DEFAULT_CONNECTION_TYPE;
        private String systemId;
        private String password;
        private String systemType;
        private TON addrTon = TON.UNKNOWN;
        private NPI addrNpi = NPI.UNKNOWN;
        private String addressRange;
        private PDUHandler pduHandler = new PDUHandlerStub();
        private SmppSessionStateListener stateListener;

        public Builder(String host, int port) {
            this.host = host;
            this.port = port;
            this.connection = null;
        }

        public Builder(Connection connection) {
            this.connection = connection;
            this.host = connection.getHost();
            this.port = connection.getPort();
        }

        public Builder connectionType(ConnectionType connType) {
            this.connType = connType;
            return this;
        }

        public Builder systemId(String systemId) {
            this.systemId = systemId;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder systemType(String systemType) {
            this.systemType = systemType;
            return this;
        }

        public Builder addrTon(TON addrTon) {
            this.addrTon = addrTon;
            return this;
        }

        public Builder addrNpi(NPI addrNpi) {
            this.addrNpi = addrNpi;
            return this;
        }

        public Builder addressRange(String addressRange) {
            this.addressRange = addressRange;
            return this;
        }

        public Builder pduHandler(PDUHandler pduHandler) {
            this.pduHandler = pduHandler;
            return this;
        }

        public Builder stateListener(SmppSessionStateListener stateListener) {
            this.stateListener = stateListener;
            return this;
        }

        public SyncSession build() throws SmppException {
            return new SyncSession(this);
        }

    }

    private void start() throws SmppException {
        ioLock.lock();
        try {
            PDU bindPDU = getBindPDU();
            int connectAttempts = 0;
            PDU bindResponse = null;
            do {
                try {
                    if (connection == null) {
                        connection = new TCPConnection.Builder(host, port).build();
                    }
                    bindResponse = send(bindPDU);
                } catch (NoResponseException e) {
                    connection.close();
                    connection = null;
                    connectAttempts++;
                }
            } while (bindResponse == null && connectAttempts < MAX_CONNECT_ATTEMPTS && work);
            if (bindResponse == null) {
                throw new SmppException("Bind failed. No response received to bind request");
            } else if (bindResponse.getCommandStatus() != CommandStatus.ESME_ROK) {
                throw new SmppException("Bind failed. SMSC response: " + bindResponse.getCommandStatus());
            }
            timer = new Timer();
            timer.schedule(new EnquireLinkTimerTask(), 30000, 1000);
            timer.schedule(new ReadTimerTask(), 1000, 100);
        } catch (IOException e) {
            if (connection != null) {
                connection.close();
                connection = null;
            }
            throw new SmppException("Bind error: " + e.getMessage(), e);
        } finally {
            ioLock.unlock();
        }
    }

    /**
     * Возвращает Bind PDU, соответствующую типу соединения.
     *
     * @return bind PDU
     */
    private PDU getBindPDU() {
        switch (connType) {
            case RECEIVER:
                BindReceiver receiver = new BindReceiver();
                receiver.setSystemId(systemId);
                receiver.setPassword(password);
                receiver.setSystemType(systemType);
                receiver.setInterfaceVersion(INTERFACE_VERSION);
                receiver.setAddrTon(addrTon);
                receiver.setAddrNpi(addrNpi);
                receiver.setAddressRange(addressRange);
                return receiver;
            case TRANSMITTER:
                BindTransmitter transmitter = new BindTransmitter();
                transmitter.setSystemId(systemId);
                transmitter.setPassword(password);
                transmitter.setSystemType(systemType);
                transmitter.setInterfaceVersion(INTERFACE_VERSION);
                transmitter.setAddrTon(addrTon);
                transmitter.setAddrNpi(addrNpi);
                transmitter.setAddressRange(addressRange);
                return transmitter;
            case TRANSCEIVER:
                BindTransceiver transceiver = new BindTransceiver();
                transceiver.setSystemId(systemId);
                transceiver.setPassword(password);
                transceiver.setSystemType(systemType);
                transceiver.setInterfaceVersion(INTERFACE_VERSION);
                transceiver.setAddrTon(addrTon);
                transceiver.setAddrNpi(addrNpi);
                transceiver.setAddressRange(addressRange);
                return transceiver;
            default:
                return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    public void stop() {
        work = false;
        // if session is blocked by long running operation it should look up work = false and free ioLock
        ioLock.lock();
        if (connection != null) {
            Unbind unbind = new Unbind();
            unbind.setSequenceNumber(nextSequenceNumber());
            try {
                connection.write(unbind);
            } catch (IOException e) {
                logger.error("IOError during Unbind", e);
            } catch (PDUException e) {
                logger.error("FATAL ERROR: wrong pdu constructed", e);
            }
            connection.close();
            connection = null;
        }
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        ioLock.unlock();
    }

    private void reconnect() {
        stateChanged(SmppSessionState.RECOVERING);
        stop();
        work = true;
        try {
            Thread.sleep(RECONNECT_TIMEOUT);
            start();
            stateChanged(SmppSessionState.OK);
        } catch (Exception e) {
            work = false;
            stateChanged(SmppSessionState.DISCONNECTED);
            logger.error("Reconnect failed");
        }
    }

    /**
     * {@inheritDoc}
     */
    public PDU send(PDU send) throws IOException, PDUException, NoResponseException {
        PDU response = null;
        ioLock.lock();
        try {
            if (work) {
                long seqNum = nextSequenceNumber();
                send.setSequenceNumber(seqNum);
                connection.write(send);
                long sentTime = System.currentTimeMillis();
                if (send instanceof Responsable) {
                    CommandId responseType = ((Responsable) send).getResponse().getCommandId();
                    List<PDU> pdus;
                    PDU pdu;
                    while ((System.currentTimeMillis() - sentTime) < RESPONSE_TIMEOUT && response == null && work) {
                        pdus = connection.read();
                        if (!pdus.isEmpty()) {
                            lastActivity = System.currentTimeMillis();
                            Iterator<PDU> it = pdus.iterator();
                            while (it.hasNext()) {
                                pdu = it.next();
                                if (seqNum == pdu.getSequenceNumber() &&
                                        (responseType == pdu.getCommandId() || CommandId.GENERIC_NACK == pdu.getCommandId())) {
                                    response = pdu;
                                    it.remove();
                                }
                            }
                            if (!pdus.isEmpty()) {
                                handle(pdus);
                            }
                        }
                    }
                    if (response == null) {
                        throw new NoResponseException("No response to " + send + " during " + RESPONSE_TIMEOUT + " ms.");
                    } else {
                        return response;
                    }
                } else {
                    return null;
                }
            } else {
                throw new IOException("SMPP session stopped");
            }
        } finally {
            ioLock.unlock();
        }
    }

    /**
     * Обрабатывает входящие сообщения.
     *
     * @param pdus список входящих PDU
     * @throws IOException  ошибка ввода/вывода
     * @throws PDUException ошибка разбора PDU
     */
    private void handle(List<PDU> pdus) throws IOException, PDUException {
        PDU response;
        boolean processingError;
        for (PDU pdu : pdus) {
            response = null;
            processingError = false;
            try {
                response = pduHandler.received(pdu);
            } catch (Exception e) {
                logger.error("Error during incoming message processing", e);
                processingError = true;
            }
            if (pdu instanceof Responsable) {
                if (response == null) {
                    response = ((Responsable) pdu).getResponse();
                    if (processingError)
                        response.setCommandStatus(CommandStatus.ESME_RSYSERR);
                }
                connection.write(response);
            }
        }
    }

    protected long nextSequenceNumber() {
        return sequenceNumber++;
    }

    private void stateChanged(SmppSessionState newState) {
        state = newState;
        if (stateListener != null) {
            stateListener.stateChanged(newState);
        }
    }

    public void setPDUHandler(PDUHandler pduHandler) {
        this.pduHandler = pduHandler;
    }

    public void setSMPPSessionStateListener(SmppSessionStateListener listener) {
        this.stateListener = listener;
    }

    public SmppSessionState getState() {
        return state;
    }

    public ConnectionType getConnType() {
        return connType;
    }

    public String getSystemId() {
        return systemId;
    }

    public String getPassword() {
        return password;
    }

    public String getSystemType() {
        return systemType;
    }

    public short getInterfaceVersion() {
        return INTERFACE_VERSION;
    }

    public TON getAddrTon() {
        return addrTon;
    }

    public NPI getAddrNpi() {
        return addrNpi;
    }

    public String getAddressRange() {
        return addressRange;
    }

    private final class EnquireLinkTimerTask extends TimerTask {
        public void run() {
            if ((System.currentTimeMillis() - lastActivity) > ENQUIRE_LINK_TIMEOUT) {
                ioLock.lock();
                // lock acquiring may take very long time so we should check timeout again
                if ((System.currentTimeMillis() - lastActivity) > ENQUIRE_LINK_TIMEOUT) {
                    EnquireLink ping = new EnquireLink();
                    try {
                        PDU resp = send(ping);
                        if (CommandStatus.ESME_ROK != resp.getCommandStatus()) {
                            logger.warn("Enquire link returned status: " + resp.getCommandStatus());
                        }
                    } catch (IOException e) {
                        logger.error("I/O error during enquire link request. Going to reconnect.", e);
                        reconnect();
                    } catch (PDUException e) {
                        logger.warn("PDU parsing error during enquire link request", e);
                    } catch (NoResponseException e) {
                        logger.error("No response to enquire link request. Going to reconnect", e);
                        reconnect();
                    }
                }
                ioLock.unlock();
            }
        }
    }

    private final class ReadTimerTask extends TimerTask {
        public void run() {
            if (work && ioLock.tryLock()) {
                try {
                    List<PDU> pdus = connection.read();
                    if (!pdus.isEmpty()) {
                        lastActivity = System.currentTimeMillis();
                        handle(pdus);
                    }
                } catch (IOException e) {
                    logger.error("I/O error during reading incoming pdus. Going to reconnect.", e);
                    reconnect();
                } catch (PDUException e) {
                    logger.warn("PDU parsing error during read", e);
                } finally {
                    ioLock.unlock();
                }
            }
        }
    }

}
