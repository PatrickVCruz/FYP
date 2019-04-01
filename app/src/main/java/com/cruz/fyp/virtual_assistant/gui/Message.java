package com.cruz.fyp.virtual_assistant.gui;

public class Message {

    public enum MessageType {
        SENT, RECEIVED
    }

    private String msg;
    private MessageType type;

    public Message(String msg, MessageType type) {
        this.msg = msg;
        this.type = type;
    }

    MessageType getType() {
        return type;
    }

    public String getMsg() {
        return msg;
    }
}

