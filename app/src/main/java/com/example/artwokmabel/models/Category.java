package com.example.artwokmabel.homepage.models;

public class Category {

    private String name;
    private String photo;
    private Boolean isChecked;

    public Category(){

    }

    public Category(String name, String photo) {
        this.name = name;
        this.photo = photo;
        this.isChecked = false;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getName() {
        return name;
    }

    public void setName(String title) {
        this.name = name;
    }

    public String getCategoryPhoto() {
        return photo;
    }

    public void setCategoryPhoto(int distanceFromSun) {
        this.photo = photo;
    }

}
