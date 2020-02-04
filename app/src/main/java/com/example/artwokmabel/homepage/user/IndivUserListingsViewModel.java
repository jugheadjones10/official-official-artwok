package com.example.artwokmabel.homepage.user;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.artwokmabel.repos.FirestoreRepo;
import com.example.artwokmabel.models.Listing;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class IndivUserListingsViewModel extends ViewModel {
    private FirebaseAuth mAuth;
    private LiveData<List<Listing>> userListingsObservable;

    public IndivUserListingsViewModel() {
        mAuth = FirebaseAuth.getInstance();
    }

    public LiveData<List<Listing>> getUserListingsObservable(String userId) {
        userListingsObservable = FirestoreRepo.getInstance().getUserListings(userId);
        return userListingsObservable;
    }
}
