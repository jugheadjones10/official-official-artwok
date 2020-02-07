package com.example.artwokmabel.chat.fragments;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.artwokmabel.chat.models.UserUserModel;
import com.example.artwokmabel.Repositories.FirestoreRepo;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class MessageFriendsViewModel extends ViewModel {
    private FirebaseAuth mAuth;
    private final LiveData<List<UserUserModel>> followingUsersObservable;

    public MessageFriendsViewModel() {
        mAuth = FirebaseAuth.getInstance();
        followingUsersObservable = FirestoreRepo.getInstance().getFollowings(mAuth.getCurrentUser().getUid());
    }

    public LiveData<List<UserUserModel>> getFollowingUsersObservable() {
        return followingUsersObservable;
    }
}
