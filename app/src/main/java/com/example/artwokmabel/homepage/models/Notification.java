package com.example.artwokmabel.homepage.models;

public class Notification {
    private String protagPic;
    private String protagUsername;

    public Notification(String protagPic, String protagUsername) {
        this.protagPic = protagPic;
        this.protagUsername = protagUsername;
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
