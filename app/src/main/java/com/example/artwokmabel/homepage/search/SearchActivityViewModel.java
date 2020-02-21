package com.example.artwokmabel.homepage.search;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.artwokmabel.models.Listing;
import com.example.artwokmabel.models.User;
import com.example.artwokmabel.repos.FirestoreRepo;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class SearchActivityViewModel extends ViewModel {

    private FirebaseAuth mAuth;
    private final LiveData<List<User>> followingsObservable;

    public SearchActivityViewModel() {
        mAuth = FirebaseAuth.getInstance();
        followingsObservable = FirestoreRepo.getInstance().getFollowings(mAuth.getCurrentUser().getUid());
    }

    public LiveData<List<Listing>> getSearchedListings(String query) {
        return FirestoreRepo.getInstance().getSearchedListings(query);
    }

    public LiveData<List<User>> getSearchedUsers(String query) {
        return FirestoreRepo.getInstance().getSearchedUsers(query);
    }

    public LiveData<List<User>> getFollowingsObservable() {
        return followingsObservable;
    }

}
