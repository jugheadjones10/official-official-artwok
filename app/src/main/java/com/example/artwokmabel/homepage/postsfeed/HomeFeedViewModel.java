package com.example.artwokmabel.homepage.postsfeed;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.artwokmabel.models.User;
import com.example.artwokmabel.repos.FirestoreRepo;
import com.example.artwokmabel.models.Listing;
import com.example.artwokmabel.models.MainPost;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.Query;

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
    }

    public LiveData<List<MainPost>> getFeedPostsObeservable() {
        return feedPostsObeservable;
    }

    public LiveData<List<Listing>> getFeedListingObservable() {
        return feedListingsObeservable;
    }

    public LiveData<List<String>> getUserFavPostsObservable() {
        return FirestoreRepo.getInstance().getUserFavPosts(mAuth.getCurrentUser().getUid());
    }

    public LiveData<List<String>> getFollowingIds(){
        return FirestoreRepo.getInstance().getFollowingsAsStringsList(mAuth.getCurrentUser().getUid());
    }

    public LiveData<User> getUser(String uid){
        return FirestoreRepo.getInstance().getUser(uid);
    }

    public LiveData<User> getUserOnce(String uid){
        return FirestoreRepo.getInstance().getUserOnce(uid);
    }

    public Query getFeedPostsQuery(List<String> followingIds){
        return FirestoreRepo.getInstance().getListingPostsQuery(followingIds);
    }

}
