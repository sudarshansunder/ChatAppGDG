package com.example.chat;

/**
 * Created by Sudarshan Sunder on 10/1/2016.
 */

public class Message {

    private String message, sender, timestamp;
    private boolean self;

    public Message(String message, String sender, String timestamp, boolean self) {
        this.message = message;
        this.sender = sender;
        this.timestamp = timestamp;
        this.self = self;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isSelf() {
        return self;
    }

    public void setSelf(boolean self) {
        this.self = self;
    }
}
