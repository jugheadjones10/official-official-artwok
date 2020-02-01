package com.example.artwokmabel.profile.uploadlisting;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.artwokmabel.homepage.fragments.Indivlistings.FaqFragment;
import com.example.artwokmabel.homepage.fragments.requestspagestuff.AddRequestDescFragment;

public class UploadListingPagerAdapter extends FragmentStateAdapter {

    public UploadListingPagerAdapter(FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        if(position == 0){
            Fragment fragment = new AddRequestDescFragment();
            return fragment;
        }else if (position == 1){
            Fragment fragment = new AddListingDetailsFragment();
            return fragment;
        }else if (position == 2){
            Fragment fragment = new AddListingDeliveryRefundFragment();
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
