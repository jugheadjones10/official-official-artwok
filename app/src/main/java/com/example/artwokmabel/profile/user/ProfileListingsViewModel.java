package com.example.artwokmabel.profile.fragments;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.artwokmabel.repos.FirestoreRepo;
import com.example.artwokmabel.models.Listing;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class ListingsFragmentViewModel extends ViewModel {
    private final LiveData<List<Listing>> listingsObeservable;
    private FirebaseAuth mAuth;

    public ListingsFragmentViewModel() {
        mAuth = FirebaseAuth.getInstance();
        listingsObeservable = FirestoreRepo.getInstance().getUserListings(mAuth.getCurrentUser().getUid());
    }

    public LiveData<List<Listing>> getListingsObeservable() {
        return listingsObeservable;
    }
}
