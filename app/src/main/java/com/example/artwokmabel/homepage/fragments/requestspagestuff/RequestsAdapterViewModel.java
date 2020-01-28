package com.example.artwokmabel.homepage.fragments.requestspagestuff;

import android.widget.ImageView;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.artwokmabel.Repositories.FirestoreRepo;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class RequestsAdapterViewModel extends ViewModel {

    private final LiveData<List<String>> userFavRequestsObservable;
    private FirebaseAuth mAuth;

    public RequestsAdapterViewModel() {
        mAuth = FirebaseAuth.getInstance();
        userFavRequestsObservable = FirestoreRepo.getInstance().getUserFavRequests(mAuth.getCurrentUser().getUid());
    }

    public LiveData<List<String>> getUserFavRequestsObservable() {
        return userFavRequestsObservable;
    }

    public void switchUserFavRequestsNonObservable(Request request, ImageView favorite){
        FirestoreRepo.getInstance().switchUserFavRequests(mAuth.getCurrentUser().getUid(), request, favorite);
    }
}