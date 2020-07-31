package com.example.artwokmabel.models;

import java.io.Serializable;

public class AgreementDetails implements Serializable {

    private String deadline;
    private String sellerRequest;
    private String buyerRequest;
    private String messageID;

    public AgreementDetails(String deadline, String sellerRequest, String buyerRequest, String messageID) {
        this.deadline = deadline;
        this.sellerRequest = sellerRequest;
        this.buyerRequest = buyerRequest;
        this.messageID = messageID;
    }

    public AgreementDetails(){

    }

    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }


    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getSellerRequest() {
        return sellerRequest;
    }

    public void setSellerRequest(String sellerRequest) {
        this.sellerRequest = sellerRequest;
    }

    public String getBuyerRequest() {
        return buyerRequest;
    }

    public void setBuyerRequest(String buyerRequest) {
        this.buyerRequest = buyerRequest;
    }

}
