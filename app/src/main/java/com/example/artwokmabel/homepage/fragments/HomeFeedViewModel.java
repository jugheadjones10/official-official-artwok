package com.example.artwokmabel.homepage.fragments;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.artwokmabel.Repositories.FirestoreRepo;
import com.example.artwokmabel.homepage.models.MainPost;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class HomeFeedViewModel extends ViewModel {
    //Todo: change view models to the format in the Android docs

    private FirebaseAuth mAuth;
    private final LiveData<List<MainPost>> feedPostsObeservable;

    public HomeFeedViewModel() {

        mAuth = FirebaseAuth.getInstance();
        feedPostsObeservable = FirestoreRepo.getInstance().getFeedPosts(mAuth.getCurrentUser().getUid());
    }

    public LiveData<List<MainPost>> getFeedPostsObeservable() {
        return feedPostsObeservable;
    }
}
