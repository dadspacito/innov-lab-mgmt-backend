package websocket;

import jakarta.ejb.Singleton;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import javax.websocket.Session;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import java.io.IOException;
import java.util.HashMap;


/**
 * WebSocket Notifier class that handles WebSocket connections and messages.
 */
@Singleton
@ServerEndpoint("/websocket/notifier/{token}")
public class Notifier {
    HashMap<String, Session> sessions = new HashMap<String, Session>();

    /**
     * Sends a message to the WebSocket session identified by the token.
     *
     * @param token the token identifying the WebSocket session
     * @param msg   the message to be sent
     */
    public void send(String token, String msg) {
        Session session = sessions.get(token);
        if (session != null) {
            System.out.println("sending.......... " + msg);
            try {
                session.getBasicRemote().sendText(msg);
            } catch (IOException e) {
                System.out.println("Something went wrong!");
            }
        }
    }

    /**
     * Called when a new WebSocket connection is opened.
     *
     * @param session the WebSocket session
     * @param token   the token identifying the WebSocket session
     */
    @OnOpen
    public void toDoOnOpen(Session session, @PathParam("token") String token) {
        System.out.println("A new WebSocket session is opened for client with token: " + token);
        sessions.put(token, session);
    }

    /**
     * Called when a WebSocket connection is closed.
     *
     * @param session the WebSocket session
     * @param reason  the reason for the closure
     */
    @OnClose
    public void toDoOnClose(Session session, CloseReason reason) {
        System.out.println("WebSocket session is closed with CloseCode: " + reason.getCloseCode() + ": " + reason.getReasonPhrase());
        sessions.entrySet().removeIf(entry -> entry.getValue().equals(session));
    }

    /**
     * Called when a message is received from the WebSocket client.
     *
     * @param session the WebSocket session
     * @param msg     the message received
     */
    @OnMessage
    public void toDoOnMessage(Session session, String msg) {
        System.out.println("A new message is received: " + msg);

        try {
            session.getBasicRemote().sendText("ack");
        } catch (IOException e) {
            System.out.println("Something went wrong!");
        }
    }
}
