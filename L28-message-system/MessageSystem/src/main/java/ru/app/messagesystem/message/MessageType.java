package ru.app.messagesystem.message;

public enum MessageType {
    VOID_MESSAGE("voidMessage"),
    USER_DATA("UserData"),
    GET_CLIENTS("getClients"),
    SAVE_CLIENT("saveClient"),
    DELETE_CLIENT("deleteClient");

    private final String name;

    MessageType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
