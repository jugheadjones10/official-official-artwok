package com.example.artwokmabel.homepage.models;


import java.util.ArrayList;

import javax.annotation.Nullable;

public class MainPost{
    private String user_id;
    private String desc;
    private String hashtags;
    private String postid;
    private String username;
    private ArrayList<String> photos;
    private String timestamp;
    private long nanopast;

    public long getNanopast() {
        return nanopast;
    }

    public void setNanopast(long nanoPast) {
        this.nanopast = nanoPast;
    }

    public String getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getUser_id() {
        return user_id;
    }
    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getDesc() {
        return desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getHashtags() {
        return hashtags;
    }
    public void setHashtags(String hashtags) {
        this.hashtags = hashtags;
    }

    public String getPostId() {
        return postid;
    }
    public void setPostId(String postid) {
        this.postid = postid;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String postid) {
        this.username = username;
    }

    public ArrayList<String> getPhotos() {
        return photos;
    }
    public void setPhotos(ArrayList<String> photos) {
        this.photos = photos;
    }


    public MainPost(String user_id, String desc, String hashtags, String postid, String username, ArrayList<String> photos, String timestamp, @Nullable long nanopast) {
        this.user_id = user_id;
        this.desc = desc;
        this.hashtags = hashtags;
        this.postid = postid;
//        Post id is date and time it was posted
        this.username = username;
        this.photos = photos;
        this.timestamp = timestamp;
        this.nanopast = nanopast;
    }

}
