package com.example.artwokmabel.Utils;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

import com.example.artwokmabel.profile.people.ViewOtherUserActivity;

public class TransactFragment {
    public void loadFragment(Context mContext, String userId){
        Intent intent = new Intent(mContext, ViewOtherUserActivity.class);
        intent.putExtra("userId", userId);
        mContext.startActivity(intent);
    }

}
