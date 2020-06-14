package com.example.artwokmabel.profile.people;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.artwokmabel.R;
import com.example.artwokmabel.databinding.FragmentFollowingBinding;
import com.example.artwokmabel.models.User;

import java.util.List;

public class FollowingFragment extends Fragment {

    private FragmentFollowingBinding binding;
    private PeopleViewModel viewModel;
    private FollowingAdapter followingAdapter;
    private NavController navController;
    //Todo: add horizontal scrollable listings

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_following, container, false);
        binding.recyclerview.setHasFixedSize(true);


        viewModel = ViewModelProviders.of(this).get(PeopleViewModel.class);

        observeViewModel(viewModel);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Below is a tricky line - take note of it. Why won't it work with the single argument version of findNavController?
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_container);
        followingAdapter = new FollowingAdapter(getContext(), navController);
        binding.recyclerview.setAdapter(followingAdapter);
    }

    private void observeViewModel(PeopleViewModel viewModel) {
        // Update the list when the data changes
        viewModel.getFollowingUsersObservable().observe(getViewLifecycleOwner(), new Observer<List<User>>() {
            @Override
            public void onChanged(@Nullable List<User> users) {
                if (users != null) {
                    followingAdapter.setFollowingsList(users);
                }
            }
        });
    }

}
