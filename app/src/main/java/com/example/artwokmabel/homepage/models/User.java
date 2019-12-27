package com.example.artwokmabel.homepage.models;

import java.util.ArrayList;

public class User {
    public String username;
    public String uid;
    public String profileUrl;
    public ArrayList<String> following;
    public ArrayList<String> followers;
    public ArrayList<String> chatrooms;
    public String email;

    public User(){

    }

    public User(String username, String uid, String profileUrl, ArrayList<String> following, ArrayList<String> followers, ArrayList<String> chatrooms, String email) {
        this.username = username;
        this.uid = uid;
        this.profileUrl = profileUrl;
        this.following = following;
        this.followers = followers;
        this.chatrooms = chatrooms;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public ArrayList<String> getFollowing() {
        return following;
    }

    public void setFollowing(ArrayList<String> following) {
        this.following = following;
    }

    public ArrayList<String> getFollowers() {
        return followers;
    }

    public void setFollowers(ArrayList<String> followers) {
        this.followers = followers;
    }

    public ArrayList<String> getChatrooms() {
        return chatrooms;
    }

    public void setChatrooms(ArrayList<String> chatrooms) {
        this.chatrooms = chatrooms;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
