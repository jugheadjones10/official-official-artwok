package com.example.artwokmabel.homepage.callbacks;

import android.content.Context;
import android.content.Intent;

import com.example.artwokmabel.auth.LoginLoginActivity;

public class LoginPageCallback {

    public void loginPageCallback(Context context){
        Intent intent = new Intent(context, LoginLoginActivity.class);
        // create an animation effect sliding from left to right
        context.startActivity(intent);
    }
}
