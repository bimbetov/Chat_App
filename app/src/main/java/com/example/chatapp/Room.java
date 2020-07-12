package com.example.chatapp;

public class Room {
    String chatName;

    Room() {}

    public Room(String chatName) {
        this.chatName = chatName;
    }

    public String getChatName() {
        return chatName;
    }

    public void setChatName(String chatName) {
        this.chatName = chatName;
    }
}
