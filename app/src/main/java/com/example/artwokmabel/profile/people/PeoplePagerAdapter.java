package com.example.artwokmabel.profile.people;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class PeoplePagerAdapter extends FragmentStateAdapter {

    public PeoplePagerAdapter(Fragment fragment) {
        super(fragment);
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

