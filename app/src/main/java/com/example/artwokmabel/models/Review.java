package com.example.artwokmabel.models;

public class Review {

    private String listingId;
    private String review;
    private String privateReview;
    private String username;
    private String posterid;
    private String posterurl;
    private float rating;
    private String reviewId;
    private long nanopast;

    public Review(){}

    public Review(String review, String username, String posterid, String posterurl, String listingId, String privateReview, float rating, String reviewId, long nanopast) {
        this.review = review;
        this.username = username;
        this.posterid = posterid;
        this.posterurl = posterurl;
        this.listingId = listingId;
        this.privateReview = privateReview;
        this.rating = rating;
        this.reviewId = reviewId;
        this.nanopast = nanopast;
    }

    public long getNanopast() {
        return nanopast;
    }

    public void setNanopast(long nanopast) {
        this.nanopast = nanopast;
    }

    public String getReviewId() {
        return reviewId;
    }

    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getPrivateReview() {
        return privateReview;
    }

    public void setPrivateReview(String privateReview) {
        this.privateReview = privateReview;
    }

    public String getListingId() {
        return listingId;
    }

    public void setListingId(String listingId) {
        this.listingId = listingId;
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
