package com.example.artwokmabel.models;

import java.io.Serializable;
import java.util.ArrayList;

public class Request implements Serializable {

    private String userid;
    private long nanopast;
    private long price;
    private ArrayList<String> photos;
    private String name;
    private String hashtags;
    private String desc;
    private String username;
    private String postid;
    private String categories;

    public Request(String userid, long price, ArrayList<String> photos, String name, String hashtags, String desc, String username, String postid, long nanopast, String categories) {
        this.userid = userid;
        this.price = price;
        this.photos = photos;
        this.name = name;
        this.hashtags = hashtags;
        this.desc = desc;
        this.username = username;
        this.postid = postid;
        this.nanopast = nanopast;
        this.categories = categories;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public long getNanopast() {
        return nanopast;
    }

    public void setNanopast(long nanopast) {
        this.nanopast = nanopast;
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public ArrayList<String> getPhotos() {
        return photos;
    }

    public void setPhotos(ArrayList<String> photos) {
        this.photos = photos;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHashtags() {
        return hashtags;
    }

    public void setHashtags(String hashtags) {
        this.hashtags = hashtags;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

}
