package com.example.artwokmabel.profile.people;

import androidx.lifecycle.ViewModel;

import com.example.artwokmabel.repos.FirestoreRepo;

public class FollowingFollowersAdapterViewModel extends ViewModel {


    public FollowingFollowersAdapterViewModel() {

    }

    public void removeUserFollowing(String myId, String otherUserId) {
        FirestoreRepo.getInstance().removeUserFollowing(myId, otherUserId);
    }

    public void addUserFollowing(String myId, String otherUserId) {
        FirestoreRepo.getInstance().addUserFollowing(myId, otherUserId);
    }

}
