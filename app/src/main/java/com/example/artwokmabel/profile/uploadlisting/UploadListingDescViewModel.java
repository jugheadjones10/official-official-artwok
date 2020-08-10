package com.example.artwokmabel.profile.uploadlisting;

import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.artwokmabel.UploadViewModel;

public class UploadListingDescViewModel extends ViewModel implements UploadViewModel {

    private MutableLiveData<Uri> imagePath = new MutableLiveData<>();
    private MutableLiveData<Uri> videoPath = new MutableLiveData<>();

    private MutableLiveData<String> htmlContent = new MutableLiveData<>();

    public UploadListingDescViewModel(){

    }

    public void setResultOk(Uri imagePath){
        this.imagePath.setValue(imagePath);
    }
    public void setVideoResultOk(Uri videoPath){
        this.videoPath.setValue(videoPath);
    }


    public void setHtmlContent(String html){
        this.htmlContent.setValue(html);
        Log.d("insideview", htmlContent.getValue());
    }


    public LiveData<Uri> getImagePath() {
        return imagePath;
    }

    public LiveData<Uri> getVideoPath() {
        return videoPath;
    }

    public LiveData<String> getHtmlContent(){
        return htmlContent;
    }

}
