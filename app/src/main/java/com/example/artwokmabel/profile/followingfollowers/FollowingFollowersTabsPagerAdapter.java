package com.example.artwokmabel.profile.followingfollowers;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class FollowingFollowersTabsPagerAdapter extends FragmentStateAdapter {

    public FollowingFollowersTabsPagerAdapter(FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override public Fragment createFragment(int position) {
       if(position == 0){
           return new FollowingFragment();
       }else{
           return new FollowersFragment();
       }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}

