package com.example.artwokmabel.chat.activities;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.artwokmabel.Repositories.FirestoreRepo;
import com.example.artwokmabel.chat.models.Comment;

import java.util.List;

public class ChatActivityViewModel extends ViewModel {
    private final LiveData<List<Comment>> roomMessagesObservable;


    public ChatActivityViewModel() {
        // If any transformation is needed, this can be simply done by Transformations class ...
        roomMessagesObservable = FirestoreRepo.getInstance().getChatRoomMessages(NewChatFragment.getInstance().chatRoomUid);
    }

    public LiveData<List<Comment>> getRoomMessagesObservable() {
        return roomMessagesObservable;
    }

    public void addNewComment(Comment comment){
        Log.d("HUCK", "VIEW MODEL" +  comment.getMessage());
        FirestoreRepo.getInstance().addNewMessage(comment, NewChatFragment.getInstance().chatRoomUid);
    }

}
