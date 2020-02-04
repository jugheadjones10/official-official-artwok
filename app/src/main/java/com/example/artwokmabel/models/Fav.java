package com.example.artwokmabel.homepage.models;

import java.util.ArrayList;

public class Fav {
    private String postid;
    private ArrayList<String> photos;

    public Fav(String postid, ArrayList<String> photos) {
        this.postid = postid;
        this.photos = photos;
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public ArrayList<String> getPhotos() {
        return photos;
    }

    public void setPhotos(ArrayList<String> photos) {
        this.photos = photos;
    }
}
