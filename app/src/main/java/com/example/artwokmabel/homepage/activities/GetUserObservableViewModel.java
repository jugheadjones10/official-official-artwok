package com.example.artwokmabel.homepage.activities;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.artwokmabel.Repositories.FirestoreRepo;
import com.example.artwokmabel.homepage.models.User;
import com.google.firebase.auth.FirebaseAuth;

public class GetUserObservableViewModel extends ViewModel {

    private LiveData<User> userObservable;
    private FirebaseAuth mAuth;

    public GetUserObservableViewModel() {
        mAuth = FirebaseAuth.getInstance();
    }

    public LiveData<User> getUserObservable(String uid) {
        userObservable = FirestoreRepo.getInstance().getUser(uid);
        return userObservable;
    }

    public void addUserListingFavs(String listingId){
        FirestoreRepo.getInstance().addUserListingFavs(listingId, mAuth.getCurrentUser().getUid());
    }

    public void removeUserListingFavs(String listingId){
        FirestoreRepo.getInstance().removeUserListingFavs(listingId,  mAuth.getCurrentUser().getUid());
    }
}
