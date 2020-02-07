package com.example.artwokmabel.models;

import androidx.fragment.app.Fragment;

public class TabDetailsModel {
    private String tabName;
    private Fragment fragment;

    public TabDetailsModel(String tabName, Fragment fragment) {
        this.tabName = tabName;
        this.fragment = fragment;
    }

    public String getTabName() {
        return tabName;
    }

    public void setTabName(String tabName) {
        this.tabName = tabName;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }

}
