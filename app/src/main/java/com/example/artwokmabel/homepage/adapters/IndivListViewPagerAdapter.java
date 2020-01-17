package com.example.artwokmabel.homepage.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.artwokmabel.homepage.fragments.Indivlistings.DeliveryFragment;
import com.example.artwokmabel.homepage.fragments.Indivlistings.DescFragment;
import com.example.artwokmabel.homepage.fragments.Indivlistings.FaqFragment;
import com.example.artwokmabel.homepage.fragments.Indivlistings.ReviewFragment;

public class IndivListViewPagerAdapter extends FragmentStateAdapter {

    public IndivListViewPagerAdapter(FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        if(position == 0){

            Fragment fragment = new DescFragment();

//            Bundle args = new Bundle();
//            args.putInt("Arg", position + 1);
//            fragment.setArguments(args);

            return fragment;

        }else if(position == 1){

            Fragment fragment = new ReviewFragment();

//            Bundle args = new Bundle();
//            args.putInt("Arg", position + 1);
//            fragment.setArguments(args);

            return fragment;
        }else if(position == 2){

            Fragment fragment = new DeliveryFragment();

//            Bundle args = new Bundle();
//            args.putInt("Arg", position + 1);
//            fragment.setArguments(args);

            return fragment;
        }else{

            Fragment fragment = new FaqFragment();

//            Bundle args = new Bundle();
//            args.putInt("Arg", position + 1);
//            fragment.setArguments(args);

            return fragment;
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
