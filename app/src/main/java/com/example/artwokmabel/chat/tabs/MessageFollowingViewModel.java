package com.example.artwokmabel.chat.tabs;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.artwokmabel.models.User;
import com.example.artwokmabel.repos.FirestoreRepo;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class MessageFollowingViewModel extends ViewModel {
    private FirebaseAuth mAuth;
    private final LiveData<List<User>> followingUsersObservable;
    private LiveData<User> userObservable;


    public MessageFollowingViewModel() {
        mAuth = FirebaseAuth.getInstance();
        followingUsersObservable = FirestoreRepo.getInstance().getFollowings(mAuth.getCurrentUser().getUid());
        userObservable = FirestoreRepo.getInstance().getUser(mAuth.getCurrentUser().getUid());
    }

    public LiveData<User> getUserObservable() {
        return userObservable;
    }

    public LiveData<List<User>> getFollowingUsersObservable() {
        return followingUsersObservable;
    }
}
