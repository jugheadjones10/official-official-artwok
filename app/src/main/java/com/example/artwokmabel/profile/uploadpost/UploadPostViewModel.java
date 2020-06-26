package com.example.artwokmabel.profile.uploadpost;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.artwokmabel.models.Listing;
import com.example.artwokmabel.models.User;
import com.example.artwokmabel.repos.FirestoreRepo;

import java.util.List;

public class UploadPostViewModel extends ViewModel {

    private MutableLiveData<Uri> imagePath = new MutableLiveData<>();

    public UploadPostViewModel(){
//        imagePath.setValue(null);
    }

    public void setResultOk(Uri imagePath){
        this.imagePath.setValue(imagePath);
    }

//    public void setResultCancelled(){
//
//    }

    public LiveData<Uri> getImagePath() {
        return imagePath;
    }
}
