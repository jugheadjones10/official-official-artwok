package com.example.artwokmabel.homepage.postsfeed;

import android.widget.Button;
import android.widget.ImageView;

import com.example.artwokmabel.models.MainPost;
import com.example.artwokmabel.models.User;
import com.example.artwokmabel.profile.people.UserItemCallback;

public class PostCallback {

    public OnPostClicked onPostClicked;
    public OnProfileClicked onProfileClicked;
    public OnShareClicked onShareClicked;
    public OnFavClicked onFavClicked;

    public interface OnPostClicked{
        void onPostClicked(MainPost post);
    }

    public interface OnProfileClicked{
        void onProfileClicked(MainPost post);
    }

    public interface OnShareClicked{
        void onShareClicked(MainPost post);
    }

    public interface OnFavClicked{
        void onFavClicked(MainPost post, ImageView favorite);
    }

    public PostCallback(OnPostClicked onPostClicked, OnProfileClicked onProfileClicked, OnShareClicked onShareClicked, OnFavClicked onFavClicked){
        this.onPostClicked = onPostClicked;
        this.onProfileClicked = onProfileClicked;
        this.onShareClicked = onShareClicked;
        this.onFavClicked = onFavClicked;
    }

}
