package com.example.artwokmabel.homepage.request.upload;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.artwokmabel.homepage.listing.FaqFragment;

public class UploadRequestPagerAdapter extends FragmentStateAdapter {


    public UploadRequestPagerAdapter(FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        if(position == 0){
            Fragment fragment = new UploadRequestDescFragment();
            return fragment;
        }else if (position == 1){
            Fragment fragment = new UploadRequestDetailsFragment();
            return fragment;
        }else{
            Fragment fragment = new FaqFragment();
            return fragment;
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }

}
