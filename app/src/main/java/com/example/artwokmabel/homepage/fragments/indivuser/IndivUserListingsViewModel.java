package com.example.artwokmabel.homepage.fragments.indivuser;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.artwokmabel.Repositories.FirestoreRepo;
import com.example.artwokmabel.homepage.models.Listing;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class IndivUserListingsViewModel extends ViewModel {
    private FirebaseAuth mAuth;
    private final LiveData<List<Listing>> userListingsObservable;

    public IndivUserListingsViewModel() {
        mAuth = FirebaseAuth.getInstance();
        userListingsObservable = FirestoreRepo.getInstance().getUserListings(mAuth.getCurrentUser().getUid());
    }

    public LiveData<List<Listing>> getUserListingsObservable() {
        return userListingsObservable;
    }
}
