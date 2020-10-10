package com.example.artwokmabel.profile.people;

import android.widget.Button;

import com.example.artwokmabel.models.User;

//Combine all intefaces into one interface?

public class UserItemCallback {
    public OnUserClicked onUserClicked;
    public OnButtonClicked onButtonClicked;
    public OnChatClicked onChatClicked;

    public interface OnUserClicked{
        void onUserClicked(User user);
    }

    public interface OnButtonClicked{
        void onButtonClicked(Button button, User user);
    }

    public interface OnChatClicked{
        void onChatClicked(User user);
    }

    public UserItemCallback(OnUserClicked onUserClicked, OnButtonClicked onButtonClicked, OnChatClicked onChatClicked){
        this.onUserClicked = onUserClicked;
        this.onButtonClicked = onButtonClicked;
        this.onChatClicked = onChatClicked;
    }

}
