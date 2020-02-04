package com.example.artwokmabel.homepage.adapters;

import android.widget.ImageView;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.artwokmabel.repos.FirestoreRepo;
import com.example.artwokmabel.models.Listing;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class ListingsAdapterViewModel extends ViewModel {
    private final LiveData<List<String>> userFavListingsObservable;
    private FirebaseAuth mAuth;

    public ListingsAdapterViewModel() {
        mAuth = FirebaseAuth.getInstance();
        userFavListingsObservable = FirestoreRepo.getInstance().getUserFavListings(mAuth.getCurrentUser().getUid());
    }

    public LiveData<List<String>> getUserFavListingsObservable() {
        return userFavListingsObservable;
    }

    public void switchUserFavListingsNonObservable(Listing listing, ImageView favorite){
        FirestoreRepo.getInstance().switchUserFavListings(mAuth.getCurrentUser().getUid(), listing, favorite);
    }
}
