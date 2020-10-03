package com.example.artwokmabel.profile.settings;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.artwokmabel.UploadViewModel;
import com.example.artwokmabel.models.User;
import com.example.artwokmabel.repos.FirestoreRepo;
import com.google.firebase.auth.FirebaseAuth;

public class SettingsActivityViewModel extends ViewModel implements UploadViewModel {

    enum IntroLoadingStatus {
        BEFORELOAD,
        LOADING,
        NOTLOADING,
        UNSUCCESSFUL
    }

    private LiveData<User> userObservable;
    private MutableLiveData<IntroLoadingStatus> loadingObservable = new MutableLiveData<>(IntroLoadingStatus.BEFORELOAD);

    private FirebaseAuth mAuth;

    public SettingsActivityViewModel() {
        mAuth = FirebaseAuth.getInstance();
        userObservable = FirestoreRepo.getInstance().getUser(mAuth.getCurrentUser().getUid());
    }

    public LiveData<User> getUserObservable() {
        return userObservable;
    }

    public LiveData<IntroLoadingStatus> getLoadingStatus() {
        return loadingObservable;
    }

    public void setLoadingStatus(IntroLoadingStatus status){
        loadingObservable.setValue(status);
    }

    public void updateUserUsername(String username){
        FirestoreRepo.getInstance().updateUserUsername(username, mAuth.getCurrentUser().getUid());
    }

    public void updateUserIntroduction(String introduction){
        setLoadingStatus(IntroLoadingStatus.LOADING);
        FirestoreRepo.getInstance().updateUserIntro(introduction, mAuth.getCurrentUser().getUid(), (isSuccessful)->{
            if(isSuccessful){
                setLoadingStatus(IntroLoadingStatus.NOTLOADING);
            }else{
                setLoadingStatus(IntroLoadingStatus.UNSUCCESSFUL);
            }
        });
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

    //For the pick image callback
    private MutableLiveData<Uri> imagePath = new MutableLiveData<>();
    private MutableLiveData<Uri> videoPath = new MutableLiveData<>();

    public void setResultOk(Uri imagePath){
        this.imagePath.setValue(imagePath);
    }
    public void setVideoResultOk(Uri videoPath){
        this.videoPath.setValue(videoPath);
    }

    public LiveData<Uri> getImagePath() {
        return imagePath;
    }
    public LiveData<Uri> getVideoPath() {
        return videoPath;
    }


    public interface DataLoadSuccessful{
        void isSuccessful(boolean isSuccessful);
    }
}
