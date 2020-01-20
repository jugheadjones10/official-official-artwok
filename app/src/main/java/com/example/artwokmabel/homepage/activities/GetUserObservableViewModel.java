package com.example.artwokmabel.homepage.activities;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.artwokmabel.Repositories.FirestoreRepo;
import com.example.artwokmabel.homepage.models.User;

public class GetUserObservableViewModel extends ViewModel {

    private LiveData<User> userObservable;

    public GetUserObservableViewModel() { }

    public LiveData<User> getUserObservable(String uid) {
        userObservable = FirestoreRepo.getInstance().getUser(uid);
        return userObservable;
    }
}
