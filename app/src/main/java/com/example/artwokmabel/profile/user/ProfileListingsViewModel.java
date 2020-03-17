package com.example.artwokmabel.profile.user;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.artwokmabel.repos.FirestoreRepo;
import com.example.artwokmabel.models.Listing;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class ProfileListingsViewModel extends ViewModel {
    // final LiveData<List<Listing>> listingsObeservable;
    private FirebaseAuth mAuth;

    public ProfileListingsViewModel() {
        mAuth = FirebaseAuth.getInstance();
        //listingsObeservable = FirestoreRepo.getInstance().getUserListings(mAuth.getCurrentUser().getUid());
    }

    public LiveData<List<Listing>> getListingsObeservable(String userId) {
        //return listingsObeservable;
        return FirestoreRepo.getInstance().getUserListings(userId);
    }
}
