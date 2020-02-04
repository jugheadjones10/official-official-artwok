package com.example.artwokmabel.homepage.request.indiv;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.artwokmabel.homepage.listing.DescFragment;
import com.example.artwokmabel.homepage.listing.FaqFragment;
import com.example.artwokmabel.models.Request;

public class RequestPagerAdapter extends FragmentStateAdapter {


    private Request request;

    public RequestPagerAdapter(FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public void setRequest(Request request){
        this.request = request;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        if(position == 0){
            Fragment fragment = new DescFragment();

            Bundle args = new Bundle();
            args.putString("description", request.getDesc());
            fragment.setArguments(args);

            return fragment;
        }else{
            Fragment fragment = new FaqFragment();
            return fragment;
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
    
}
