package com.example.artwokmabel.models;

public class OfferMessage extends Message{

    private String price;
    private String acceptStatus;

    public OfferMessage(){

    }

    public OfferMessage(String price, String acceptStatus, String from, String to, String messageID) {

        super(from, "null", "null", to, messageID, "null", "null", 0);
        this.price = price;
        this.acceptStatus = acceptStatus;
    }

    public String getAcceptStatus() {
        return acceptStatus;
    }

    public void setAcceptStatus(String acceptStatus) {
        this.acceptStatus = acceptStatus;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
