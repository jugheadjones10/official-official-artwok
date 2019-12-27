package com.example.artwokmabel.profile.fragments;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.artwokmabel.Repositories.FirestoreRepo;
import com.example.artwokmabel.homepage.models.MainPost;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class PostsViewModel extends ViewModel {

    private FirebaseAuth mAuth;
    private final LiveData<List<MainPost>> userPostsObeservable;

    public PostsViewModel() {

        mAuth = FirebaseAuth.getInstance();
        userPostsObeservable = FirestoreRepo.getInstance().getUserPosts(mAuth.getCurrentUser().getUid());
    }

    public LiveData<List<MainPost>> getUserPostsObeservable() {
        return userPostsObeservable;
    }
}
