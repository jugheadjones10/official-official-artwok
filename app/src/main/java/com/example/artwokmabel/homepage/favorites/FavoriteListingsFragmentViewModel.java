package com.example.artwokmabel.homepage.favoritestuff;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.artwokmabel.repos.FirestoreRepo;
import com.example.artwokmabel.homepage.models.Listing;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class FavoriteListingsFragmentViewModel extends ViewModel {

    private final LiveData<List<Listing>> favListingsObjectsObeservable;
    private FirebaseAuth mAuth;

    public FavoriteListingsFragmentViewModel() {
        mAuth = FirebaseAuth.getInstance();
        Log.d("thestuffreturned", "my id from view model" + mAuth.getCurrentUser().getUid());
        favListingsObjectsObeservable = FirestoreRepo.getInstance().getUserFavListingsObjects(mAuth.getCurrentUser().getUid());
    }

    public LiveData<List<Listing>> getFavListingsObjectsObservable() {
        return favListingsObjectsObeservable;
    }

}
