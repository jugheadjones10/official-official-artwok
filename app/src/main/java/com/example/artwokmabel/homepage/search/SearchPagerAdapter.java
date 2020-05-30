package com.example.artwokmabel.homepage.search;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.artwokmabel.homepage.search.tabs.SearchListingsFragment;
import com.example.artwokmabel.homepage.search.tabs.SearchPostsFragment;
import com.example.artwokmabel.homepage.search.tabs.SearchUsersFragment;

public class SearchPagerAdapter extends FragmentStateAdapter {

    public SearchPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return new SearchListingsFragment();
        }else if (position == 1){
            return new SearchUsersFragment();
        }else{
            return new SearchPostsFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
