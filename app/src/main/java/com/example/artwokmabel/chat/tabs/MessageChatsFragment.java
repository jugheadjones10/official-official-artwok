package com.example.artwokmabel.chat.tabs;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
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

import com.example.artwokmabel.chat.MessageFragment;
import com.example.artwokmabel.R;
import com.example.artwokmabel.databinding.MessageChatsFragmentBinding;
import com.example.artwokmabel.models.NormalChat;
import com.google.android.material.badge.BadgeDrawable;

import java.util.List;

public class MessageChatsFragment extends Fragment {

    public MessageChatsFragmentBinding binding;
    private MessageChatsAdapter adapter;
    private MessageChatsViewModel viewModel;
    private NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.message_chats_fragment, container, false);
        viewModel = ViewModelProviders.of(this).get(MessageChatsViewModel.class);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Below is a tricky line - take note of it. Why won't it work with the single argument version of findNavController?
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_container);

        adapter = new MessageChatsAdapter(getActivity(), navController);
        binding.chatsFragmentRecyclerview.setAdapter(adapter);
        observeViewModel(viewModel);
    }


    private void observeViewModel(MessageChatsViewModel viewModel) {
        viewModel.getChattingWithObservable().observe(this, new Observer<List<NormalChat>>() {
            @Override
            public void onChanged(@Nullable List<NormalChat> chatHeads) {
                Log.d("chattingwith", Integer.toString(chatHeads.size()));

                if (chatHeads != null) {
                    adapter.setNormalChatsList(chatHeads);
                }
            }
        });

        viewModel.getNumOfUnreadInChatsTab().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                BadgeDrawable badge = MessageFragment.getInstance().binding.messageTabs.getTabAt(1).getOrCreateBadge();
                badge.setBadgeTextColor(Color.WHITE);
                if(integer > 0){
                    badge.setVisible(true);
                    badge.setNumber(integer);
                }else{
                    badge.setVisible(false);
                }
            }
        });

    }

}
