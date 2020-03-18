package com.example.artwokmabel.models;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class NormalChat{

    private String lastMessage;
    private String lastInteractionTime;
    private User user;

//    public NormalChat(String username, String uid, String profile_url, ArrayList<String> following, ArrayList<String> followers, ArrayList<String> chatrooms, ArrayList<String> fav_listings, ArrayList<String> fav_posts, ArrayList<String> fav_requests, ArrayList<String> tab_categories, String email, String introduction, String token, String lastMessage, String lastInteractionTime) {
//        super(username, uid, profile_url, following, followers, chatrooms, fav_listings, fav_posts, fav_requests, tab_categories, email, introduction, token);
//        this.lastMessage = lastMessage;
//        this.lastInteractionTime = lastInteractionTime;
//    }

    private String changeNanopastToReadableDate(long nanopast){

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

    public NormalChat(User user, Message message){
        this.user = user;
        this.lastMessage = message.getMessage();
        this.lastInteractionTime = changeNanopastToReadableDate(message.getNanopast());
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getLastInteractionTime() {
        return lastInteractionTime;
    }

    public void setLastInteractionTime(String lastInteractionTime) {
        this.lastInteractionTime = lastInteractionTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
