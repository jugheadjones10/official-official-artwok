package com.example.artwokmabel.models;

import java.io.Serializable;
import java.util.ArrayList;

public class OrderChat extends Listing implements Serializable {

    private Message lastMessage;
    private String buyerId;

//    public OrderChat(String userid, String return_exchange, long price, ArrayList<String> photos, String name, String hashtags, String desc, String delivery, String username, String postid, long nanopast, ArrayList<String> categories, Message lastMessage) {
//        super(userid, return_exchange, price, photos, name, hashtags, desc, delivery, username, postid, nanopast, categories);
//        this.lastMessage = lastMessage;
//    }

    public OrderChat(String userid, String return_exchange, long price, ArrayList<String> photos, String name, String hashtags, String desc, String delivery, String username, String postid, long nanopast, ArrayList<String> categories, Message lastMessage, String buyerId) {
        super(userid, return_exchange, price, photos, name, hashtags, desc, delivery, username, postid, nanopast, categories);
        this.lastMessage = lastMessage;
        this.buyerId = buyerId;
    }

    public OrderChat(){

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
