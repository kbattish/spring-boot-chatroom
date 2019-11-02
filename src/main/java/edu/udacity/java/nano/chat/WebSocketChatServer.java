package edu.udacity.java.nano.chat;

import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static edu.udacity.java.nano.chat.Message.*;

/**
 * WebSocket Server
 *
 * @see ServerEndpoint WebSocket Client
 * @see Session   WebSocket Session
 */

@Component
@ServerEndpoint("/chat/{username}")
public class WebSocketChatServer {

    private Session session;
    private static Set<WebSocketChatServer> chatEndpoints = new CopyOnWriteArraySet<WebSocketChatServer>();
    private static HashMap<String, String> users = new HashMap<>();
    private static Map<String, Session>  sessionMap = new ConcurrentHashMap<>();

    private static void sendMessageToAll(Session session, Message message) throws IOException, EncodeException {
        message.setSender(users.get(session.getId()));
        broadcast(message);
    }
    private static void sendMessageToAll(Message message) throws IOException, EncodeException {
        message.setSender(message.getSender());
        broadcast(message);
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) throws IOException, EncodeException {
        this.session = session;
        chatEndpoints.add(this);
        users.put(session.getId(), username );

        sessionMap.put(session.getId(), session);

        Message message = new Message();
        message.setSender(username);
        message.setContent("Connected!");
        message.setContent("Disconnected!");

        broadcast(message);
    }

    @OnMessage
    public void onMessage(Session session,  String  message) throws IOException, EncodeException {
        Message messages = new Message(message);
        sendMessageToAll(messages);
    }

    @OnClose
    public void onClose(Session session) throws IOException, EncodeException {
        //TODO: add close connection.
        chatEndpoints.remove(this);
        sessionMap.remove(session.getId());
        Message message = new Message();
        message.setSender(users.get(session.getId()));
        message.setContent("Disconnected!");
        broadcast(message);
    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }
    private static void broadcast(Message message)
            throws IOException, EncodeException {

        chatEndpoints.forEach(endpoint -> {
            synchronized (endpoint) {
                try {
                    endpoint.session.getBasicRemote().
                            sendObject(message);
                } catch (EncodeException | IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
