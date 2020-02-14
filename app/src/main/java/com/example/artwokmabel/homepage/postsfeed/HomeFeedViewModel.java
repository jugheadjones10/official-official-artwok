package com.example.artwokmabel.homepage.postsfeed;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.artwokmabel.repos.FirestoreRepo;
import com.example.artwokmabel.models.Listing;
import com.example.artwokmabel.models.MainPost;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class HomeFeedViewModel extends ViewModel {
    //Todo: change view models to the format in the Android docs

    private FirebaseAuth mAuth;
    private final LiveData<List<MainPost>> feedPostsObeservable;
    private final LiveData<List<Listing>> feedListingsObeservable;

    public HomeFeedViewModel() {

        mAuth = FirebaseAuth.getInstance();
        feedPostsObeservable = FirestoreRepo.getInstance().getFeedPosts(mAuth.getCurrentUser().getUid());
        feedListingsObeservable = FirestoreRepo.getInstance().getFeedListings(mAuth.getCurrentUser().getUid());
//        feedPostsObeservable = FirestoreRepo.getInstance().getFeedPosts("pBw1jicoofgeHp6jNq2TuiAK2Wk1");
//        feedListingsObeservable = FirestoreRepo.getInstance().getFeedListings("pBw1jicoofgeHp6jNq2TuiAK2Wk1");
    }

    public LiveData<List<MainPost>> getFeedPostsObeservable() {
        return feedPostsObeservable;
    }

    public LiveData<List<Listing>> getFeedListingObservable() {
        return feedListingsObeservable;
    }

}
