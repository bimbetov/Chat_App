package com.example.chatapp;

import android.widget.ImageView;

public class Room {
    private String chatName;

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
