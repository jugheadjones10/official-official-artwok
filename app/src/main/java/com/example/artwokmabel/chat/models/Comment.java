package com.example.artwokmabel.chat.models;

public class Comment {

    private String senderId;
    private String message;
    private Long timestamp;
    private String otherUserPic;

    public String getOtherUserPic() {
        return otherUserPic;
    }

    public void setOtherUserPic(String otherUserPic) {
        this.otherUserPic = otherUserPic;
    }


    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Comment(String senderId, String message, Long timestamp, String otherUserPic) {
        this.senderId = senderId;
        this.message = message;
        this.timestamp = timestamp;
        this.otherUserPic = otherUserPic;
    }

}
