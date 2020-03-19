package com.example.artwokmabel.chat.tabs;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.artwokmabel.chat.models.ChatChatModel;
import com.example.artwokmabel.R;
import com.example.artwokmabel.databinding.MessageChatsFragmentBinding;
import com.example.artwokmabel.databinding.MessageFollowingFragmentBinding;
import com.example.artwokmabel.models.NormalChat;
import com.example.artwokmabel.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MessageChatsFragment extends Fragment {

    private MessageChatsFragmentBinding binding;
    private MessageChatsAdapter adapter;
    private MessageChatsViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.message_chats_fragment, container, false);

        adapter = new MessageChatsAdapter(getActivity());
        binding.chatsFragmentRecyclerview.setAdapter(adapter);

        viewModel = ViewModelProviders.of(this).get(MessageChatsViewModel.class);
        observeViewModel(viewModel);

        return binding.getRoot();
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
    }

}
