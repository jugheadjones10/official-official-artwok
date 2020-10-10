package com.example.artwokmabel.models;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NormalChat{

    private String lastMessage;
    private String lastInteractionTime;
    private long rawLastInteractionTime;
    private User user;

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
        this.rawLastInteractionTime = message.getNanopast();
        this.lastInteractionTime = changeNanopastToReadableDate(message.getNanopast());
    }

    public long getRawLastInteractionTime() {
        return rawLastInteractionTime;
    }

    public void setRawLastInteractionTime(long rawLastInteractionTime) {
        this.rawLastInteractionTime = rawLastInteractionTime;
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
