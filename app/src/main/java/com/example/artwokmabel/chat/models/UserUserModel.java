package com.example.artwokmabel.chat.models;

import java.util.ArrayList;

public class UserUserModel {

    private String userName;
    private String profileImageUrl;
    private String uid;
    private ArrayList<String> chatrooms;

    public UserUserModel(String userName, String profileImageUrl, String uid, ArrayList<String> chatrooms) {
        this.userName = userName;
        this.profileImageUrl = profileImageUrl;
        this.uid = uid;
        this.chatrooms = chatrooms;
    }

    public ArrayList<String> getChatrooms() {
        return chatrooms;
    }

    public void setChatrooms(ArrayList<String> chatrooms) {
        this.chatrooms = chatrooms;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }


}

