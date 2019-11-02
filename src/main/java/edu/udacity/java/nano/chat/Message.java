package edu.udacity.java.nano.chat;

import com.alibaba.fastjson.JSON;

/**
 * WebSocket message model
 */
public class Message {

    private String type;
    private String username;
    private String message;
    private int onlineCount;

    public Message(String type, String username, String message, int onlineCount) {

        this.type = type;
        this.username = username;
        this.message = message;
        this.onlineCount = onlineCount;
    }

    public Message(){

    }

    public static String encode(Message message) throws EncodeException {
        return JSON.toJSONString(message);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getOnlineCount() {
        return onlineCount;
    }

    public void setOnlineCount(int onlineCount) {
        this.onlineCount = onlineCount;
    }

    public enum MessageType {
        ENTER,
        SPEAK,
        QUIT
    }


}
