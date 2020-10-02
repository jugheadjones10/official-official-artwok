package com.example.artwokmabel.models;

import java.io.Serializable;

public class AgreementDetails implements Serializable {

    private String deadline;
    private String sellerRequest;
    private String buyerRequest;

    private long price;
    private String delivery;
    private String refund;

//    private long nanopast;

    public AgreementDetails(long price, String delivery, String refund, String deadline, String sellerRequest, String buyerRequest) {
        this.deadline = deadline;
        this.sellerRequest = sellerRequest;
        this.buyerRequest = buyerRequest;
        this.price = price;
        this.delivery = delivery;
        this.refund = refund;
    }

    public AgreementDetails(){

    }

//    public long getNanopast() {
//        return nanopast;
//    }
//
//    public void setNanopast(long nanopast) {
//        this.nanopast = nanopast;
//    }


    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public String getDelivery() {
        return delivery;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }

    public String getRefund() {
        return refund;
    }

    public void setRefund(String refund) {
        this.refund = refund;
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
