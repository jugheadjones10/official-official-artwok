package com.example.artwokmabel.models;

import java.util.ArrayList;

public class ListingPost {

    public ListingPost(String userid, long nanopast, ArrayList<String> photos, String hashtags, String desc, String username, String postid, String timestamp, String return_exchange, long price, String name, String delivery, ArrayList<String> categories) {
        this.userid = userid;
        this.nanopast = nanopast;
        this.photos = photos;
        this.hashtags = hashtags;
        this.desc = desc;
        this.username = username;
        this.postid = postid;
        this.timestamp = timestamp;
        this.return_exchange = return_exchange;
        this.price = price;
        this.name = name;
        this.delivery = delivery;
        this.categories = categories;
    }

    private String userid;
    private long nanopast;
    private ArrayList<String> photos;
    private String hashtags;
    private String desc;
    private String username;
    private String postid;

    //Post specific fields
    private String timestamp;

    //Listing specific fields
    private String return_exchange;
    private long price;
    private String name;
    private String delivery;
    private ArrayList<String> categories;


    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public long getNanopast() {
        return nanopast;
    }

    public void setNanopast(long nanopast) {
        this.nanopast = nanopast;
    }

    public ArrayList<String> getPhotos() {
        return photos;
    }

    public void setPhotos(ArrayList<String> photos) {
        this.photos = photos;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getReturn_exchange() {
        return return_exchange;
    }

    public void setReturn_exchange(String return_exchange) {
        this.return_exchange = return_exchange;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDelivery() {
        return delivery;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }

    public ArrayList<String> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<String> categories) {
        this.categories = categories;
    }

}
