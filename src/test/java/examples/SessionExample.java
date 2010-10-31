package examples;

import org.bulatnig.smpp.SmppException;
import org.bulatnig.smpp.pdu.*;
import org.bulatnig.smpp.session.*;
import org.bulatnig.smpp.session.impl.SyncSession;

import java.io.IOException;

/**
 * SyncSession usage example.
 * <p/>
 * User: Bulat Nigmatullin
 * Date: Apr 7, 2009
 * Time: 8:31:47 PM
 */
public class SessionExample {

    public void run() {
        try {
            Session session = new SyncSession.Builder("smschost", 9999).
                    systemId("login").password("pass").
                    addrTon(TON.INTERNATIONAL).addrNpi(NPI.ISDN).
                    pduHandler(new PDUHandlerExampleImpl()).
                    stateListener(new SmppSessionStateListenerExampleImpl()).
                    build();
            SubmitSM submit = new SubmitSM();
            submit.setSourceAddr("1010");
            submit.setDestinationAddr("79011289102");
            submit.setDestAddrTon(TON.INTERNATIONAL);
            submit.setDestAddrNpi(NPI.ISDN);
            submit.setShortMessage("Hello!");
            int attempts = 0;
            boolean recoverable;
            do {
                recoverable = false;
                try {
                    PDU response = session.send(submit);
                    if (response.getCommandStatus() == CommandStatus.ESME_ROK) {
                        System.out.println("Message successfully delivered");
                    } else {
                        // handle error 
                    }
                } catch (IOException e) {
                    // Session failed
                } catch (PDUException e) {
                    // Verify PDU
                } catch (NoResponseException e) {
                    // Try again
                    recoverable = true;
                }
                attempts++;
            } while (recoverable && attempts < 3);
            session.stop();
        } catch (SmppException e) {
            e.printStackTrace();
        }
    }

    private class PDUHandlerExampleImpl implements PDUHandler {

        public PDU received(PDU pdu) {
            switch (pdu.getCommandId()) {
                case DATA_SM:
                    DataSM dataPDU = (DataSM) pdu;
                    if (dataPDU.getMessagePayload() != null) {
                        System.out.println("Data received from " + dataPDU.getSourceAddr() +
                                " to " + dataPDU.getDestinationAddr() +
                                " with message " + dataPDU.getMessagePayload().getValue());
                        return dataPDU.getResponse();
                    } else {
                        PDU response = dataPDU.getResponse();
                        response.setCommandStatus(CommandStatus.ESME_RINVMSGLEN);
                        return response;
                    }
                case DELIVER_SM:
                    DeliverSM deliverPDU = (DeliverSM) pdu;
                    String message = deliverPDU.getShortMessage();
                    if (message == null && deliverPDU.getMessagePayload() != null) {
                        message = deliverPDU.getMessagePayload().getValue();
                    }
                    System.out.println("Message received from " + deliverPDU.getSourceAddr() +
                            " to " + deliverPDU.getDestinationAddr() +
                            " with message " + message);
                default:
                    // null return values handled correctly, it's ok
                    return null;
            }
        }
    }

    private class SmppSessionStateListenerExampleImpl implements SmppSessionStateListener {

        private SmppSessionState lastState;

        public void stateChanged(SmppSessionState state) {
            switch (state) {
                case OK:
                    if (lastState == null) {
                        alert("SMPP Session started");
                    } else {
                        alert("SMPP Session recovered");
                    }
                    break;
                case RECOVERING:
                    alert("WARN: SMPP Session in recovering mode");
                    break;
                case DISCONNECTED:
                    alert("ERROR: SMPP SESSION DISCONNECTED");

            }
            lastState = state;
        }

        private void alert(String message) {
            // Alert via E-mail or another system.
        }

    }

    public static void main(String[] args) {
        new SessionExample().run();
    }

}
