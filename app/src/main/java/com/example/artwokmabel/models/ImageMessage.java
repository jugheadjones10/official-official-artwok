package com.example.artwokmabel.models;

public class ImageMessage extends Message {

    private String imageUrl;

    public ImageMessage(){

    }

    public ImageMessage(String price, String acceptStatus, String from, String to, String messageID) {

        //TODO: potential set read to false problem here too
        super(from, "null", "image", to, messageID, "null", "null", 0, "false");
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

}