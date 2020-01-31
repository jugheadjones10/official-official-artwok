package com.example.artwokmabel.profile.uploadlisting;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.artwokmabel.homepage.fragments.Indivlistings.FaqFragment;
import com.example.artwokmabel.homepage.fragments.requestspagestuff.AddRequestDescFragment;

public class UploadListingPagerAdapter extends FragmentStateAdapter {

    Fragment descFragment;
    Fragment detailsFragment;
    Fragment refundFragment;
    Fragment faqFragment;

    public UploadListingPagerAdapter(FragmentActivity fragmentActivity) {
        super(fragmentActivity);
        descFragment = new AddRequestDescFragment();
        detailsFragment = new AddListingDetailsFragment();
        refundFragment = new AddListingDeliveryRefundFragment();
        faqFragment = new FaqFragment();
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        if(position == 0){
            return descFragment;
        }else if (position == 1){
            return detailsFragment;
        }else if (position == 2){
            return refundFragment;
        }else{
            return faqFragment;
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }

}
