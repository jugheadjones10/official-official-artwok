package com.example.artwokmabel.profile.followingfollowers;

import androidx.lifecycle.ViewModel;

import com.example.artwokmabel.Repositories.FirestoreRepo;

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
