package com.example.artwokmabel.models;

import java.util.ArrayList;

public class User {

    public User(String username, String uid, String profile_url, ArrayList<String> following, ArrayList<String> followers, ArrayList<String> chatrooms, ArrayList<String> fav_listings, ArrayList<String> fav_posts, ArrayList<String> fav_requests, ArrayList<String> tab_categories, String email) {
        this.username = username;
        this.uid = uid;
        this.profile_url = profile_url;
        this.following = following;
        this.followers = followers;
        this.chatrooms = chatrooms;
        this.fav_listings = fav_listings;
        this.fav_posts = fav_posts;
        this.fav_requests = fav_requests;
        this.tab_categories = tab_categories;
        this.email = email;
    }

    public String username;
    public String uid;
    public String profile_url;
    public ArrayList<String> following;
    public ArrayList<String> followers;
    public ArrayList<String> chatrooms;
    public ArrayList<String> fav_listings;
    public ArrayList<String> fav_posts;
    public ArrayList<String> fav_requests;
    public ArrayList<String> tab_categories;
    public String email;

    public User(){

    }

    public String getProfile_url() {
        return profile_url;
    }

    public void setProfile_url(String profile_url) {
        this.profile_url = profile_url;
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

    public ArrayList<String> getFav_listings() {
        return fav_listings;
    }

    public void setFav_listings(ArrayList<String> fav_listings) {
        this.fav_listings = fav_listings;
    }

    public ArrayList<String> getFav_posts() {
        return fav_posts;
    }

    public void setFav_posts(ArrayList<String> fav_posts) {
        this.fav_posts = fav_posts;
    }

    public ArrayList<String> getFav_requests() {
        return fav_requests;
    }

    public void setFav_requests(ArrayList<String> fav_requests) {
        this.fav_requests = fav_requests;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<String> getTab_categories() {
        return tab_categories;
    }

    public void setTab_categories(ArrayList<String> tab_categories) {
        this.tab_categories = tab_categories;
    }
}
