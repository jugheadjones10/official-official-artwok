package com.example.artwokmabel.models;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

//extends Listing
public class OrderChat implements Serializable {

    private Message lastMessage;
    private String buyerId;
    private Listing listing;

//    public OrderChat(String userid, String return_exchange, long price, ArrayList<String> photos, String name, String hashtags, String desc, String delivery, String username, String postid, long nanopast, ArrayList<String> categories, Message lastMessage) {
//        super(userid, return_exchange, price, photos, name, hashtags, desc, delivery, username, postid, nanopast, categories);
//        this.lastMessage = lastMessage;
//    }

//    public OrderChat(String userid, String return_exchange, long price, ArrayList<String> photos, String name, String hashtags, String desc, String delivery, String username, String postid, long nanopast, ArrayList<String> categories, Message lastMessage, String buyerId) {
//        super(userid, return_exchange, price, photos, name, hashtags, desc, delivery, username, postid, nanopast, categories);
//        this.lastMessage = lastMessage;
//        this.buyerId = buyerId;
//    }

    public OrderChat(Listing listing, Message lastMessage, String buyerId) {
        this.listing = listing;
        this.lastMessage = lastMessage;
        this.buyerId = buyerId;
    }

    public OrderChat(){

    }

    public String getLastInteractionTime(){
        long nanopast = lastMessage.getNanopast();
        String finalString;
        SimpleDateFormat sdf = new SimpleDateFormat("MM dd");

        long diff = System.currentTimeMillis() - nanopast;

        if(diff < 86400000 ){
            if(sdf.format(new Date(nanopast)).equals(sdf.format(new Date(System.currentTimeMillis())))){
                SimpleDateFormat finalFormat = new SimpleDateFormat("HH:mm");
                finalString = finalFormat.format(new Date(nanopast));
            }else {
                finalString = "yesterday";
            }
        }else{
            SimpleDateFormat finalFormat = new SimpleDateFormat("dd/MM/yyyy");
            finalString = finalFormat.format(new Date(nanopast));
        }

        return finalString;
    }

    public Listing getListing() {
        return listing;
    }

    public void setListing(Listing listing) {
        this.listing = listing;
    }

    public Message getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(Message lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

}
