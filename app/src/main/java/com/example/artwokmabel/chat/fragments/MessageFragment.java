package com.example.artwokmabel.chat.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.artwokmabel.chat.adapters.MessageMainAdapter;
import com.example.artwokmabel.R;
import com.example.artwokmabel.databinding.MainMessageFragmentBinding;

public class MessageFragment extends Fragment {

    private MainMessageFragmentBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.main_message_fragment, container, false);

        MessageMainAdapter adapter = new MessageMainAdapter(getActivity().getSupportFragmentManager());

        // Adding Fragments
        adapter.addFragment(new MessageFriendsFragment(),"Following");
        adapter.addFragment(new MessageChatsFragment(),"Chats");
        adapter.addFragment(new MessageOrdersFragment(),"Orders");

        // Adapter Setup
        binding.messageViewpager.setAdapter(adapter);
        binding.messageTabs.setupWithViewPager(binding.messageViewpager);

        return binding.getRoot();
    }
}