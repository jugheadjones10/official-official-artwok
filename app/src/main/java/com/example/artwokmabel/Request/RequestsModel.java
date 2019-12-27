package com.example.artwokmabel.Request;

class RequestsModel {

    Integer requestImages;
    String requestName;
    String requestUsername;
    String requestPrice;

    public RequestsModel(Integer requestImages, String requestName, String requestUsername, String requestPrice) {
        this.requestImages = requestImages;
        this.requestName = requestName;
        this.requestUsername = requestUsername;
        this.requestPrice = requestPrice;
    }

    public Integer getRequestImages() {
        return requestImages;
    }

    public void setRequestImages(Integer requestImages) {
        this.requestImages = requestImages;
    }

    public String getRequestName() {
        return requestName;
    }

    public void setRequestName(String requestName) {
        this.requestName = requestName;
    }

    public String getRequestUsername() {
        return requestUsername;
    }

    public void setRequestUsername(String requestUsername) {
        this.requestUsername = requestUsername;
    }

    public String getRequestPrice() {
        return requestPrice;
    }

    public void setRequestPrice(String requestPrice) {
        this.requestPrice = requestPrice;
    }
}
