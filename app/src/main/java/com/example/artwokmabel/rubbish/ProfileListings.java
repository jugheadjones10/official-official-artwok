package com.example.artwokmabel.profile.Models;

public class ProfileListings {

    private String Title;
    private String Price;
    private int Thumbnail;

    public ProfileListings(){

    }

    public ProfileListings(String title, String price, int thumbnail) {
        Title = title;
        Price = price;
        Thumbnail = thumbnail;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public int getThumbnail() {
        return Thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        Thumbnail = thumbnail;
    }

}
