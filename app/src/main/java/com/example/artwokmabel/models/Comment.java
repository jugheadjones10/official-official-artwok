package com.example.artwokmabel.models;


public class Comment {

    private String comment;
    private String user_id;
    private String username;
    private long date_created;
    private String poster_url;
    private String comment_id;

    public Comment() {

    }

    public Comment(String comment, String user_id, long date_created, String username, String poster_url, String comment_id) {
        this.comment = comment;
        this.user_id = user_id;
        this.date_created = date_created;
        this.username = username;
        this.poster_url = poster_url;
        this.comment_id = comment_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getComment_id() {
        return comment_id;
    }

    public void setComment_id(String comment_id) {
        this.comment_id = comment_id;
    }

    public String getPoster_url() {
        return poster_url;
    }

    public void setPoster_url(String poster_url) {
        this.poster_url = poster_url;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public long getDate_created() {
        return date_created;
    }

    public void setDate_created(long date_created) {
        this.date_created = date_created;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "comment='" + comment + '\'' +
                ", user_id='" + user_id + '\'' +
//                ", likes=" + likes +
                ", date_created='" + date_created + '\'' +
                '}';
    }
}
