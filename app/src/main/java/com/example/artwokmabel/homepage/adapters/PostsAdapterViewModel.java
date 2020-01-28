package com.example.artwokmabel.homepage.adapters;

import android.widget.ImageView;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.artwokmabel.Repositories.FirestoreRepo;
import com.example.artwokmabel.homepage.models.MainPost;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class PostsAdapterViewModel extends ViewModel {

    private final LiveData<List<String>> userFavPostsObservable;
    private FirebaseAuth mAuth;

    public PostsAdapterViewModel() {
        mAuth = FirebaseAuth.getInstance();
        userFavPostsObservable = FirestoreRepo.getInstance().getUserFavPosts(mAuth.getCurrentUser().getUid());
    }

    public LiveData<List<String>> getUserFavPostsObservable() {
        return userFavPostsObservable;
    }

    public void switchUserFavPostsNonObservable(MainPost post, ImageView favorite){
        FirestoreRepo.getInstance().switchUserFavPosts(mAuth.getCurrentUser().getUid(), post, favorite);
    }

}
