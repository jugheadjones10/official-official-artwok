package com.example.artwokmabel.profile.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.artwokmabel.R;
import com.example.artwokmabel.databinding.MainProfileFragment2Binding;
import com.example.artwokmabel.homepage.models.User;
import com.example.artwokmabel.profile.ProfileViewPagerAdapter;
import com.example.artwokmabel.profile.followingfollowers.FollowingFollowersActivity;
import com.example.artwokmabel.profile.fragments.DashboardFragment;
import com.example.artwokmabel.profile.fragments.ListingsFragment;
import com.example.artwokmabel.profile.fragments.PostsFragment;
import com.squareup.picasso.Picasso;

public class ProfilePageFragment2 extends Fragment {

    private ProfilePageActivityViewModel viewModel;
    private MainProfileFragment2Binding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.main_profile_fragment_2, container, false);
        binding.profileTab.bringToFront();
        binding.setOnpeopleclicked(new OnPeopleClicked());

        ProfileViewPagerAdapter adapter = new ProfileViewPagerAdapter(getChildFragmentManager());
        adapter.AddFragment(new ListingsFragment(),"Listings");
        adapter.AddFragment(new PostsFragment(),"Posts");
        adapter.AddFragment(new DashboardFragment(),"Dashboard");

        // Adapter setup
        binding.profileViewpager.setAdapter(adapter);
        binding.profileTab.setupWithViewPager(binding.profileViewpager);

        viewModel = ViewModelProviders.of(this).get(ProfilePageActivityViewModel.class);
        viewModel.getUserObservable().observe(this, new Observer<User>() {
            @Override
            public void onChanged(@Nullable User user) {
                binding.setUser(user);
                Picasso.get().load(user.getProfile_url()).into(binding.profilePicture);
            }
        });

        viewModel.getNumUserListings().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer numListings) {
                binding.setNumlistings(numListings);
            }
        });

        viewModel.getNumUserPosts().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer numPosts) {
                binding.setNumposts(numPosts);
            }
        });

        return binding.getRoot();
    }

    public class OnPeopleClicked{
        public void onPeopleClicked(){
            Intent intent = new Intent(getContext(), FollowingFollowersActivity.class);
            startActivity(intent);
        }
    }
}