package com.example.artwokmabel.homepage.callbacks;

import android.content.Intent;

import com.example.artwokmabel.homepage.homepagestuff.HomeTabsManagerFragment;

public class ShareClickCallback {
    public void onClick(){
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = "Here is the share content body";
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        HomeTabsManagerFragment.getInstance().startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }
}
