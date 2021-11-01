package ru.java.domain;

public class Message {
    private String messageString;

    public Message() { }

    public Message(String messageString) {
        this.messageString = messageString;
    }

    public String getMessageString() {
        return messageString;
    }

    public void setMessageString(String messageString) {
        this.messageString = messageString;
    }

    @Override
    public String toString() {
        return "Message{" +
                "messageString='" + messageString + '\'' +
                '}';
    }
}
