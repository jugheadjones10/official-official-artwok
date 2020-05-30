package com.example.artwokmabel.profile.settings;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.artwokmabel.models.User;
import com.example.artwokmabel.repos.FirestoreRepo;
import com.google.firebase.auth.FirebaseAuth;

public class SettingsActivityViewModel extends ViewModel {

    private LiveData<User> userObservable;
    private FirebaseAuth mAuth;

    public SettingsActivityViewModel() {
        mAuth = FirebaseAuth.getInstance();
        userObservable = FirestoreRepo.getInstance().getUser(mAuth.getCurrentUser().getUid());
    }

    public LiveData<User> getUserObservable() {
        return userObservable;
    }

    public void updateUserUsername(String username){
        FirestoreRepo.getInstance().updateUserUsername(username, mAuth.getCurrentUser().getUid());
    }

    public void updateUserIntroduction(String introduction){
//        FirestoreRepo.getInstance().updateUserIntro(introduction, mAuth.getCurrentUser().getUid());
    }

    public void updateUserProfileUrl(String profile_url){
        FirestoreRepo.getInstance().updateUserProfileUrl(profile_url, mAuth.getCurrentUser().getUid());
    }

    public void updateUserEmail(String email){
        FirestoreRepo.getInstance().updateUserEmail(email, mAuth.getCurrentUser().getUid());
    }

    public void deleteUser(){
        FirestoreRepo.getInstance().deleteCurrentUser();

    }
}
