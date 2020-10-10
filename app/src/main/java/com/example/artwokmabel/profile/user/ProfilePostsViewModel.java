package com.example.artwokmabel.profile.user;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.artwokmabel.models.User;
import com.example.artwokmabel.repos.FirestoreRepo;
import com.google.firebase.firestore.Query;

public class ProfilePostsViewModel extends ViewModel {

    public ProfilePostsViewModel() {
    }

    public LiveData<User> getUserOnce(String uid){
        return FirestoreRepo.getInstance().getUserOnce(uid);
    }

    public Query getSortedUserPostsQuery(String userId){
        return FirestoreRepo.getInstance().getSortedUserPostsQuery(userId);
    }
}
