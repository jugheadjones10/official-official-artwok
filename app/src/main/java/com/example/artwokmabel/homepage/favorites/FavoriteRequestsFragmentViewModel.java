package com.example.artwokmabel.homepage.favoritestuff;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.artwokmabel.repos.FirestoreRepo;
import com.example.artwokmabel.homepage.fragments.requestspagestuff.Request;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class FavoriteRequestsFragmentViewModel extends ViewModel {

    private final LiveData<List<Request>> favRequestsObjectsObeservable;
    private FirebaseAuth mAuth;

    public FavoriteRequestsFragmentViewModel() {
        mAuth = FirebaseAuth.getInstance();
        Log.d("thestuffreturned", "my id from view model" + mAuth.getCurrentUser().getUid());
        favRequestsObjectsObeservable = FirestoreRepo.getInstance().getUserFavRequestsObjects(mAuth.getCurrentUser().getUid());
    }

    public LiveData<List<Request>> getFavRequestsObjectsObservable() {
        return favRequestsObjectsObeservable;
    }

}