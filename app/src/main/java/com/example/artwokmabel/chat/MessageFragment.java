package com.example.artwokmabel.chat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.artwokmabel.R;
import com.example.artwokmabel.chat.tabs.MessageChatsFragment;
import com.example.artwokmabel.chat.tabs.MessageOrdersFragment;
import com.example.artwokmabel.chat.tabs.MessageFollowingFragment;
import com.example.artwokmabel.databinding.MainMessageFragmentBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MessageFragment extends Fragment {

    private MainMessageFragmentBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.main_message_fragment, container, false);

        MessageFragmentPagerAdapter adapter = new MessageFragmentPagerAdapter(this);

        // Adding Fragments
//        adapter.addFragment(new MessageFollowingFragment(),"Following");
//        adapter.addFragment(new MessageChatsFragment(),"Chats");
//        adapter.addFragment(new MessageOrdersFragment(),"Orders");

        // Adapter Setup
        binding.messageViewpager.setAdapter(adapter);
        //binding.messageTabs.setupWithViewPager(binding.messageViewpager);
        new TabLayoutMediator(binding.messageTabs, binding.messageViewpager,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        if(position == 0){
                            tab.setText("Following");
                        }else if(position == 1){
                            tab.setText("Chats");
                        }else{
                            tab.setText("Orders");
                        }
                    }
                }
        ).attach();

        return binding.getRoot();
    }
}