package com.example.artwokmabel.homepage.favorites;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.artwokmabel.profile.people.FollowersFragment;
import com.example.artwokmabel.profile.people.FollowingFragment;

import java.util.ArrayList;
import java.util.List;

public class FavoritesPagerAdapter extends FragmentStateAdapter {

    public FavoritesPagerAdapter(Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override public Fragment createFragment(int position) {
        if(position == 0){
            return new FavoriteListingsFragment();
        }else if(position == 1){
            return new FavoritePostsFragment();
        }else{
            return new FavoriteRequestsFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }

}