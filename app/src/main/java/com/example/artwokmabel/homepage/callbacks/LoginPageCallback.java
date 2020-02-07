package com.example.artwokmabel.homepage.callbacks;

import android.content.Context;
import android.content.Intent;

import com.example.artwokmabel.login.LoginActivity;

public class LoginPageCallback {

    public void loginPageCallback(Context context){
        Intent intent = new Intent(context, LoginActivity.class);
        // create an animation effect sliding from left to right
        context.startActivity(intent);
    }
}
