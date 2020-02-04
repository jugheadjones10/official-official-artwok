package com.example.artwokmabel.homepage.models;

public class Categories {

    private String Title;
    private int Thumbnail;
    private Boolean isChecked;

    public Categories(){

    }

    public Categories(String title, int thumbnail) {
        Title = title;
        Thumbnail = thumbnail;
        isChecked = false;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public int getThumbnail() {
        return Thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        Thumbnail = thumbnail;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

}
