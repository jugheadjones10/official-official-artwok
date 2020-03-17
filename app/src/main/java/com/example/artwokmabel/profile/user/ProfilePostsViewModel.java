package com.example.artwokmabel.profile.user;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.artwokmabel.repos.FirestoreRepo;
import com.example.artwokmabel.models.MainPost;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class ProfilePostsViewModel extends ViewModel {

    private FirebaseAuth mAuth;
    //private final LiveData<List<MainPost>> userPostsObeservable;

    public ProfilePostsViewModel() {

        mAuth = FirebaseAuth.getInstance();
        //userPostsObeservable = FirestoreRepo.getInstance().getUserPosts(mAuth.getCurrentUser().getUid());
    }

    public LiveData<List<MainPost>> getUserPostsObeservable(String userId) {
        //return userPostsObeservable;
        return FirestoreRepo.getInstance().getUserPosts(userId);
    }
}
