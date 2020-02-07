package com.example.artwokmabel.profile.user;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.artwokmabel.repos.FirestoreRepo;
import com.example.artwokmabel.models.User;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileFragmentViewModel extends ViewModel {

    private LiveData<User> userObservable;
    private LiveData<Integer> numUserPosts;
    private LiveData<Integer> numUserListings;
    private FirebaseAuth mAuth;

    public ProfileFragmentViewModel() {
        mAuth = FirebaseAuth.getInstance();
        userObservable = FirestoreRepo.getInstance().getUser(mAuth.getCurrentUser().getUid());
        numUserPosts = FirestoreRepo.getInstance().getUserNumPosts(mAuth.getCurrentUser().getUid());
        numUserListings = FirestoreRepo.getInstance().getUserNumListings(mAuth.getCurrentUser().getUid());
    }

    public LiveData<User> getUserObservable() {
        return userObservable;
    }

    public LiveData<Integer> getNumUserPosts() {
        return numUserPosts;
    }
    public LiveData<Integer> getNumUserListings() {
        return numUserListings;
    }

}