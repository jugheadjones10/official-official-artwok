package com.example.artwokmabel.profile.uploadlisting;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.artwokmabel.homepage.listing.FaqFragment;
import com.example.artwokmabel.homepage.request.upload.UploadRequestDescFragment;

public class UploadListingPagerAdapter extends FragmentStateAdapter {

    public UploadListingPagerAdapter(FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        if(position == 0){
            Fragment fragment = new UploadRequestDescFragment();
            return fragment;
        }else if (position == 1){
            Fragment fragment = new UploadListingDetailsFragment();
            return fragment;
        }else if (position == 2){
            Fragment fragment = new UploadListingDeliveryFragment();
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
