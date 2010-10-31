package examples;

import org.bulatnig.smpp.SmppException;
import org.bulatnig.smpp.client.*;
import org.bulatnig.smpp.pdu.NPI;
import org.bulatnig.smpp.pdu.TON;
import org.bulatnig.smpp.session.Session;
import org.bulatnig.smpp.session.impl.SyncSession;

/**
 * SmppClient usage example.
 * User: Bulat Nigmatullin
 * Date: 02.06.2009
 * Time: 11:20:43
 */
public class ClientExample {

    public void run() {
        SmppClient client = null;
        try {
            // create Session with SMSC
            Session session = new SyncSession.Builder("smschost", 9999).
                    systemId("login").password("pass").
                    addrTon(TON.INTERNATIONAL).addrNpi(NPI.ISDN).
                    build();
            // create Client
            client = new SmppClientImpl(session, new MessageHandler() {

                @Override
                public void handle(Message message) throws ProcessingFailedException {
                    // Store incoming message to database for farther use
                }

                @Override
                public void deliveredToDest(String messageId) throws ProcessingFailedException {
                    // Find message with such smscMessageId in database and set it state to DELIVERED
                }

            }, true);

            // Send message
            // Use can send as long messages as you want: library automatically split large message on small chunks
            String smscMessageId = client.send(new Message("1234", "79140231456",
                    "You have new mail. Mail subject: \"Lets go to the cinema evening tomorrow!\" " +
                            "Check it here: http://webservice.org/user1/mail?id=125"));

            System.out.println("Sent message smscMessageId: " + smscMessageId);

            // Store smscMessageId in database to update message state on delivery

        } catch (SmppException e) {
            e.printStackTrace();
        } finally {
            // Close client connection
            if (client != null) {
                client.stop();
            }
        }
    }

    public static void main(String[] args) {
        new ClientExample().run();
    }
}
