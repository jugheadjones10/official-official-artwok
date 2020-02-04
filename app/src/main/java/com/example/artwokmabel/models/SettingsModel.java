package com.example.artwokmabel.homepage.models;

public class SettingsModel {
    Integer langLogo;
    String langName;

    public Integer getLangLogo() {
        return langLogo;
    }

    public void setLangLogo(Integer langLogo) {
        this.langLogo = langLogo;
    }

    public String getLangName() {
        return langName;
    }

    public void setLangName(String langName) {
        this.langName = langName;
    }

    public SettingsModel(Integer langLogo, String langName) {
        this.langLogo = langLogo;
        this.langName = langName;
    }
}
