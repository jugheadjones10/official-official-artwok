package com.example.artwokmabel.models;

public class Report {

    public static String POST = "POST";
    public static String LISTING = "LISTING";

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public Report(String report, String id, String type) {
        this.report = report;
        this.id = id;
        this.type = type;
    }

    private String report;
    private String id;
    private String type;
}
