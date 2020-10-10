package com.example.artwokmabel.profile.uploadpost;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.artwokmabel.UploadViewModel;

public class UploadPostViewModel extends ViewModel implements UploadViewModel {

    private MutableLiveData<Uri> imagePath = new MutableLiveData<>();
    private MutableLiveData<Uri> videoPath = new MutableLiveData<>();

    public UploadPostViewModel(){

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

}
