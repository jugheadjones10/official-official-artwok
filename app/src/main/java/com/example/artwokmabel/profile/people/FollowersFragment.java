package com.example.artwokmabel.profile.followingfollowers;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.artwokmabel.R;
import com.example.artwokmabel.databinding.FragmentFollowingBinding;
import com.example.artwokmabel.models.User;

import java.util.List;

public class FollowersFragment  extends Fragment {

    private FragmentFollowingBinding binding;
    private FollowingFollowersViewModel viewModel;
    private FollowersAdapter followersAdapter;
    //Todo: add horizontal scrollable listings

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_following, container, false);
        binding.recyclerview.setHasFixedSize(true);

        followersAdapter = new FollowersAdapter(getContext());
        binding.recyclerview.setAdapter(followersAdapter);

        viewModel = ViewModelProviders.of(this).get(FollowingFollowersViewModel.class);

        observeViewModel(viewModel);

        return binding.getRoot();
    }


    private void observeViewModel(FollowingFollowersViewModel viewModel) {
        // Update the list when the data changes
        viewModel.getFollowersUsersObservable().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(@Nullable List<User> users) {
                if (users != null) {
                    Log.d("followerscheck", Integer.toString(users.size()));
                    followersAdapter.setFollowersList(users);
                }
            }
        });

        viewModel.getFollowingUsersObservable().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(@Nullable List<User> users) {
                if (users != null) {
                    followersAdapter.setFollowingsList(users);
                }
            }
        });

    }

}
