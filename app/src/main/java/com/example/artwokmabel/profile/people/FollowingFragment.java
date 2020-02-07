package com.example.artwokmabel.profile.people;

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
import com.example.artwokmabel.databinding.FragmentFollowingBinding;
import com.example.artwokmabel.models.User;

import java.util.List;

public class FollowingFragment extends Fragment {

    private FragmentFollowingBinding binding;
    private PeopleViewModel viewModel;
    private FollowingAdapter followingAdapter;
    //Todo: add horizontal scrollable listings

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_following, container, false);
        binding.recyclerview.setHasFixedSize(true);

        followingAdapter = new FollowingAdapter(getContext());
        binding.recyclerview.setAdapter(followingAdapter);

        viewModel = ViewModelProviders.of(this).get(PeopleViewModel.class);

        observeViewModel(viewModel);

        return binding.getRoot();
    }

//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//
//    }

    private void observeViewModel(PeopleViewModel viewModel) {
        // Update the list when the data changes
        viewModel.getFollowingUsersObservable().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(@Nullable List<User> users) {
                if (users != null) {
                    followingAdapter.setFollowingsList(users);
                }
            }
        });
    }

}
