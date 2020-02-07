package com.example.artwokmabel.chat.models;

public class ChatChatModel {

    public ChatChatModel(String otherUser, String otherUserPic, String chatRoomId, String lastMsg) {
        this.otherUser = otherUser;
        this.otherUserPic = otherUserPic;
        this.chatRoomid = chatRoomId;
        this.lastMsg = lastMsg;
    }

    private String otherUser;
    private String otherUserPic;
    private String chatRoomid;
    public String lastMsg;

    public String getLastMsg() {
        return lastMsg;
    }

    public void setLastMsg(String lastMsg) {
        this.lastMsg = lastMsg;
    }

    public String getOtherUser() {
        return otherUser;
    }

    public void setOtherUser(String otherUser) {
        this.otherUser = otherUser;
    }

    public String getOtherUserPic() {
        return otherUserPic;
    }

    public void setOtherUserPic(String otherUserPic) {
        this.otherUserPic = otherUserPic;
    }

    public String getChatRoomid() {
        return chatRoomid;
    }

    public void setChatRoomid(String chatRoomid) {
        this.chatRoomid = chatRoomid;
    }
}

