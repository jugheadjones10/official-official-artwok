package com.example.artwokmabel.models;

import java.io.Serializable;

public class Notification implements Serializable {

    public static final int COMMENT = 1;
    public static final int OTHERS_UPLOAD_POST = 2;
    public static final int OTHERS_UPLOAD_LISTING = 3;
    public static final int FOLLOWED = 4;

    private String protagPic;
    private String protagUsername;
    private String protagId;
    private int action;
    private double timeInMillis;
    private String notifId;

    public Notification(){

    }

    public Notification(String protagPic, String protagUsername, int action, double timeInMillis, String notifId, String protagId) {
        this.protagPic = protagPic;
        this.protagUsername = protagUsername;
        this.action = action;
        this.timeInMillis = timeInMillis;
        this.notifId = notifId;
        this.protagId = protagId;
    }

    public String getActionText() {
        if (action == COMMENT) {
            return " commented on your post!";
        } else if (action == OTHERS_UPLOAD_POST) {
            return " uploaded a new post!";
        } else if (action == OTHERS_UPLOAD_LISTING) {
            return " uploaded a new listing!";
        } else if (action == FOLLOWED){
            return " started following you";
        }else{
            return "error!!!!";
        }
    }

    public String getProtagId() {
        return protagId;
    }

    public void setProtagId(String protagId) {
        this.protagId = protagId;
    }

    public String getNotifId() {
        return notifId;
    }

    public void setNotifId(String notifId) {
        this.notifId = notifId;
    }

    public double getTimeInMillis() {
        return timeInMillis;
    }

    public void setTimeInMillis(double timeInMillis) {
        this.timeInMillis = timeInMillis;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }


    public String getProtagPic() {
        return protagPic;
    }

    public void setProtagPic(String protagPic) {
        this.protagPic = protagPic;
    }

    public String getProtagUsername() {
        return protagUsername;
    }

    public void setProtagUsername(String protagUsername) {
        this.protagUsername = protagUsername;
    }
}
