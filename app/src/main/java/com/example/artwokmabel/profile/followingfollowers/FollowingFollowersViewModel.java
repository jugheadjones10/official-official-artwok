package com.example.artwokmabel.profile.followingfollowers;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.artwokmabel.Repositories.FirestoreRepo;
import com.example.artwokmabel.homepage.models.User;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class FollowingFollowersViewModel extends ViewModel {

    private FirebaseAuth mAuth;
    private final LiveData<List<User>> followingUsersObservable;
    private final LiveData<List<User>> followersUsersObservable;

    public FollowingFollowersViewModel() {
        mAuth = FirebaseAuth.getInstance();
        followingUsersObservable = FirestoreRepo.getInstance().getUserFollowings(mAuth.getCurrentUser().getUid());
        followersUsersObservable = FirestoreRepo.getInstance().getUserFollowers(mAuth.getCurrentUser().getUid());

    }

    public LiveData<List<User>> getFollowingUsersObservable() {
        return followingUsersObservable;
    }
    public LiveData<List<User>> getFollowersUsersObservable() {
        return followersUsersObservable;
    }

}
