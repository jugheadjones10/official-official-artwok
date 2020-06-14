package com.example.artwokmabel.homepage.favorites;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.artwokmabel.models.Listing;
import com.example.artwokmabel.models.MainPost;
import com.example.artwokmabel.repos.FirestoreRepo;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class FavoritePostsViewModel extends ViewModel {
    private FirebaseAuth mAuth;
    private final LiveData<List<MainPost>> userFavPostsObservable;

    public FavoritePostsViewModel() {
        mAuth = FirebaseAuth.getInstance();
        userFavPostsObservable = FirestoreRepo.getInstance().getUserFavPostsObjects(mAuth.getCurrentUser().getUid());
    }

    public LiveData<List<MainPost>> getUserFavPostsObservable() {
        return userFavPostsObservable;
    }

}
