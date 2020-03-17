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

    public ProfileFragmentViewModel() {
    }

    public LiveData<User> getUserObservable(String userId) {
        userObservable = FirestoreRepo.getInstance().getUser(userId);
        return userObservable;
    }

    public LiveData<Integer> getNumUserPosts(String userId) {
        numUserPosts = FirestoreRepo.getInstance().getUserNumPosts(userId);
        return numUserPosts;
    }
    public LiveData<Integer> getNumUserListings(String userId) {
        numUserListings = FirestoreRepo.getInstance().getUserNumListings(userId);
        return numUserListings;
    }

    public void removeUserFollowing(String myId, String otherUserId) {
        FirestoreRepo.getInstance().removeUserFollowing(myId, otherUserId);
    }

    public void addUserFollowing(String myId, String otherUserId) {
        FirestoreRepo.getInstance().addUserFollowing(myId, otherUserId);
    }


}
