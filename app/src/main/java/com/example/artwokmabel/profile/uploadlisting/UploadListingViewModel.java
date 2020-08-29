package com.example.artwokmabel.profile.uploadlisting;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.artwokmabel.UploadViewModel;
import com.example.artwokmabel.repos.FirestoreRepo;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class UploadListingViewModel extends ViewModel implements UploadViewModel {

    private MutableLiveData<Uri> imagePath = new MutableLiveData<>();
    private MutableLiveData<Uri> videoPath = new MutableLiveData<>();
    private MutableLiveData<Boolean> uploadSuccess = new MutableLiveData<>();
    private MutableLiveData<String> listingImageUri = new MutableLiveData<>();

    public UploadListingViewModel(){

    }

    public MutableLiveData<Boolean> getUploadSuccess() {
        return uploadSuccess;
    }

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

    public void uploadNewListing(String postTitle, String postDesc, ArrayList<String> categories, Long price, String delivery, String refund, String currentUserId, ArrayList<String> postImageUris){
        FirestoreRepo.getInstance().uploadNewListing(postTitle, postDesc, categories, price, delivery, refund, currentUserId, postImageUris, (success) -> {
            if(success){
                uploadSuccess.setValue(true);
            }else{
                uploadSuccess.setValue(false);
            }
        });
    }

    public void setListingImageUri(String imageUri){
        listingImageUri.setValue(imageUri);
    }

    public LiveData<String> getListingImageUri(){
        return listingImageUri;
    }

    public interface UploadListingCallback{
        void uploadListingCallback(boolean successful);
    }
}
