package com.example.artwokmabel.homepage.models;

public class Review {

    private String review;
    private String username;
    private String posterid;
    private String posterurl;

    public Review(String review, String username, String posterid, String posterurl) {
        this.review = review;
        this.username = username;
        this.posterid = posterid;
        this.posterurl = posterurl;
    }


    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPosterid() {
        return posterid;
    }

    public void setPosterid(String posterid) {
        this.posterid = posterid;
    }

    public String getPosterurl() {
        return posterurl;
    }

    public void setPosterurl(String posterurl) {
        this.posterurl = posterurl;
    }


}
