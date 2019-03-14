package com.cruz.fyp.virtualassistant;

public class Message {

    public enum MessageType {
        ONE_ITEM, TWO_ITEM
    }

    private String msg;
    private MessageType type;

    Message(String msg, MessageType type) {
        this.msg = msg;
        this.type = type;
    }

    public MessageType getType() {
        return type;
    }

    public String getMsg() {
        return msg;
    }
}

