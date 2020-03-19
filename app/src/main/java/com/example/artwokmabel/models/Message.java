package com.example.artwokmabel.models;

import java.io.Serializable;

public class Message implements Serializable
{
    private String from, message, type, to, messageID, time, date;

    private long nanopast;

    public Message() {

    }

//    String name,
    public Message(String from, String message, String type, String to, String messageID, String time, String date, long nanopast) {
        this.from = from;
        this.message = message;
        this.type = type;
        this.to = to;
        this.messageID = messageID;
        this.time = time;
        this.date = date;
        //this.name = name;
        this.nanopast = nanopast;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }

    public long getNanopast() {
        return nanopast;
    }

    public void setNanopast(long nanopast) {
        this.nanopast = nanopast;
    }

}