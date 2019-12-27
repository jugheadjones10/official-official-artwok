package com.example.artwokmabel.chat.activities;

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
import com.example.artwokmabel.chat.adapters.ChatsViewAdapter;
import com.example.artwokmabel.chat.models.Comment;
import com.example.artwokmabel.databinding.ActivityChatBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class NewChatFragment extends Fragment {

    //Todo: what's different about this fragment that it's faster than ChatActivity?
    private ChatsViewAdapter adapter;

    private ActivityChatBinding binding;
    private ChatActivityViewModel viewModel;

    private String userId;
    public String chatRoomUid;

    private Bundle args;
    private static NewChatFragment instance;

    public static NewChatFragment getInstance(){
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.activity_chat, container, false);
        args = getArguments();

        instance = this;
        chatRoomUid = args.getString("chatRoomId");
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        adapter = new ChatsViewAdapter();
        binding.chatRecyclerview.setAdapter(adapter);
        binding.setCallback(new OnMessageButClicked());

        return binding.getRoot();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(ChatActivityViewModel.class);

        observeViewModel(viewModel);
    }

    private void observeViewModel(ChatActivityViewModel viewModel) {
        // Update the list when the data changes
        viewModel.getRoomMessagesObservable().observe(this, new Observer<List<Comment>>() {
            @Override
            public void onChanged(@Nullable List<Comment> messages) {
                if (messages != null) {
                    adapter.setMessages(messages);

                    binding.chatRecyclerview.scrollToPosition(adapter.getItemCount());
                }
            }
        });
    }

    public class OnMessageButClicked{
        public void onMessageButClicked(){

            //Todo: is there a better way to implement the below?
            Comment newComment = new Comment(
                    userId,
//                    binding.editComment.getText().toString(),
                    "FUCK",
                    System.currentTimeMillis(),
                    args.getString("otherUserPic")
                    ///This is not MY pic - it's the other person's
            );

            Log.d("HUCK", "FRAG" + binding.editComment.getText().toString());
            viewModel.addNewComment(newComment);
            binding.editComment.setText("");

            binding.chatRecyclerview.scrollToPosition(adapter.getItemCount());
        }
    }
}
