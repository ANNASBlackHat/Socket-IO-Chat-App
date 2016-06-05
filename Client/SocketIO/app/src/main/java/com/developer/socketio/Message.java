package com.developer.socketio;

/**
 * Created by ANNASBlackHat on 6/4/2016.
 */

public class Message {
    public static final int TYPE_MESSAGE = 1;
    public static final int TYPE_CONNECT = 2;
    public static final int TYPE_DISCONNECT = 3;
    private String username;
    private String message;
    private int type;
    private String id;

    public Message(String username, String message, int type) {
        this.username = username;
        this.message = message;
        this.type = type;
    }

    public Message(String username, String message, int type, String id) {
        this.username = username;
        this.message = message;
        this.type = type;
        this.id = id;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
