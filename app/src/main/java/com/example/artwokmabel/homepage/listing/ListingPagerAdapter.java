package com.example.artwokmabel.homepage.adapters;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.artwokmabel.homepage.listing.DeliveryFragment;
import com.example.artwokmabel.homepage.listing.DescFragment;
import com.example.artwokmabel.homepage.listing.FaqFragment;
import com.example.artwokmabel.homepage.listing.ReviewFragment;
import com.example.artwokmabel.homepage.models.Listing;

public class IndivListViewPagerAdapter extends FragmentStateAdapter {

    private Listing listing;

    public IndivListViewPagerAdapter(FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public void setListing(Listing listing){
        this.listing = listing;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        if(position == 0){
            Fragment fragment = new DescFragment();

            Bundle args = new Bundle();
            args.putString("description", listing.getDesc());
            fragment.setArguments(args);

            return fragment;
        }else if(position == 1){
            Fragment fragment = new ReviewFragment();

            Bundle args = new Bundle();
            args.putString("review", "reviews. Make reviews xml");
            fragment.setArguments(args);

            return fragment;
        }else if(position == 2){
            Fragment fragment = new DeliveryFragment();

            Bundle args = new Bundle();
            args.putString("delivery", listing.getDelivery());
            args.putString("refund", listing.getReturn_exchange());
            fragment.setArguments(args);

            return fragment;
        }else{
            Fragment fragment = new FaqFragment();
            return fragment;
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
