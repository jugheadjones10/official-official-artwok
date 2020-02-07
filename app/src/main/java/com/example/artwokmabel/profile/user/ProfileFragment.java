package com.example.artwokmabel.profile.user;

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
import com.example.artwokmabel.databinding.FragmentProfileBinding;
import com.example.artwokmabel.models.User;
import com.example.artwokmabel.profile.people.PeopleActivity;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment {

    private ProfileFragmentViewModel viewModel;
    private FragmentProfileBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);
        binding.profileTab.bringToFront();
        binding.setOnpeopleclicked(new OnPeopleClicked());

        ProfileFragmentPagerAdapter adapter = new ProfileFragmentPagerAdapter(getChildFragmentManager());
        adapter.AddFragment(new ProfileListingsFragment(),"Listings");
        adapter.AddFragment(new ProfilePostsFragment(),"Posts");
        adapter.AddFragment(new DashboardFragment(),"Dashboard");

        // Adapter setup
        binding.profileViewpager.setAdapter(adapter);
        binding.profileTab.setupWithViewPager(binding.profileViewpager);

        viewModel = ViewModelProviders.of(this).get(ProfileFragmentViewModel.class);
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
            Intent intent = new Intent(getContext(), PeopleActivity.class);
            startActivity(intent);
        }
    }
}
