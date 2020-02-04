package com.example.artwokmabel.homepage.user;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.artwokmabel.repos.FirestoreRepo;
import com.example.artwokmabel.models.MainPost;

import java.util.List;

public class IndivUserPostsViewModel extends ViewModel {

    private final LiveData<List<MainPost>> userPostsObeservable;

    public IndivUserPostsViewModel() {
        userPostsObeservable = FirestoreRepo.getInstance().getUserPosts(IndivUserPostsFragment.getInstance().indivUserId);
    }

    public LiveData<List<MainPost>> getUserPostsObeservable() {
        return userPostsObeservable;
    }
}
