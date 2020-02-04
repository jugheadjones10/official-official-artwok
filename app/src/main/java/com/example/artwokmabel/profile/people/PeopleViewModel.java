package com.example.artwokmabel.profile.people;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.artwokmabel.repos.FirestoreRepo;
import com.example.artwokmabel.models.User;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class PeopleViewModel extends ViewModel {

    private FirebaseAuth mAuth;
    private final LiveData<List<User>> followingUsersObservable;
    private final LiveData<List<User>> followersUsersObservable;

    public PeopleViewModel() {
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
