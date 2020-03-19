package com.example.artwokmabel.chat.tabs;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.artwokmabel.models.NormalChat;
import com.example.artwokmabel.models.User;
import com.example.artwokmabel.repos.FirestoreRepo;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class MessageChatsViewModel extends ViewModel {
    private FirebaseAuth mAuth;
    private final LiveData<List<NormalChat>> chattingWithObservable;

    public MessageChatsViewModel() {
        mAuth = FirebaseAuth.getInstance();
        chattingWithObservable = FirestoreRepo.getInstance().getChattingWith(mAuth.getCurrentUser().getUid());
    }

    public LiveData<List<NormalChat>> getChattingWithObservable() {
        return chattingWithObservable;
    }

}
