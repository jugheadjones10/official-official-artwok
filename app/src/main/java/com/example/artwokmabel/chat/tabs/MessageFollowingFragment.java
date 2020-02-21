package com.example.artwokmabel.chat.tabs;

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
import com.example.artwokmabel.databinding.MessageFollowingFragmentBinding;
import com.example.artwokmabel.models.User;

import java.util.List;

public class MessageFollowingFragment extends Fragment {

    private MessageFollowingAdapter adapter;

    private MessageFollowingFragmentBinding binding;
    private MessageFollowingViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.message_following_fragment, container, false);

        adapter = new MessageFollowingAdapter(getActivity());
        binding.friendsFragmentRecyclerview.setAdapter(adapter);

        viewModel = ViewModelProviders.of(this).get(MessageFollowingViewModel.class);
        observeViewModel(viewModel);

        return binding.getRoot();
    }

    private void observeViewModel(MessageFollowingViewModel viewModel) {
        // Update the list when the data changes
        viewModel.getFollowingUsersObservable().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(@Nullable List<User> users) {
                if (users != null) {
                    adapter.setUsersList(users);
                }
            }
        });
    }
}