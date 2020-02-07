package com.example.artwokmabel.chat.fragments;

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
import com.example.artwokmabel.chat.adapters.MessageFriendsFragmentRecyclerViewAdapter;
import com.example.artwokmabel.chat.models.UserUserModel;
import com.example.artwokmabel.databinding.MessageFriendsFragmentBinding;

import java.util.List;

public class MessageFriendsFragment extends Fragment {

    private MessageFriendsFragmentRecyclerViewAdapter adapter;

    private MessageFriendsFragmentBinding binding;
    private MessageFriendsViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.message_friends_fragment, container, false);

        adapter = new MessageFriendsFragmentRecyclerViewAdapter(getActivity());
        binding.friendsFragmentRecyclerview.setAdapter(adapter);

        return binding.getRoot();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(MessageFriendsViewModel.class);

        observeViewModel(viewModel);
    }

    private void observeViewModel(MessageFriendsViewModel viewModel) {
        // Update the list when the data changes
        viewModel.getFollowingUsersObservable().observe(this, new Observer<List<UserUserModel>>() {
            @Override
            public void onChanged(@Nullable List<UserUserModel> users) {
                if (users != null) {
                    adapter.setUsersList(users);
                }
            }
        });
    }
}
