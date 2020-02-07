package com.example.artwokmabel.homepage.models;

import java.util.ArrayList;

public class Listing {

    private String userid;
    private long nanopast;
    private String return_exchange;
    private long price;
    private ArrayList<String> photos;
    private String name;
    private String hashtags;
    private String desc;
    private String delivery;
    private String username;
    private String postid;
    private ArrayList<String> categories;

    public Listing(String userid, String return_exchange, long price, ArrayList<String> photos, String name, String hashtags, String desc, String delivery, String username, String postid, long nanopast, ArrayList<String> categories) {
        this.userid = userid;
        this.return_exchange = return_exchange;
        this.price = price;
        this.photos = photos;
        this.name = name;
        this.hashtags = hashtags;
        this.desc = desc;
        this.delivery = delivery;
        this.username = username;
        this.postid = postid;
        this.nanopast = nanopast;
        this.categories = categories;
    }

    public ArrayList<String> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<String> categories) {
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

    public String getReturn_exchange() {
        return return_exchange;
    }

    public void setReturn_exchange(String return_exchange) {
        this.return_exchange = return_exchange;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(int price) {
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

    public String getDelivery() {
        return delivery;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }

}
