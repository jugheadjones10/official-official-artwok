package com.example.artwokmabel.chat.personalchat;

import androidx.lifecycle.ViewModel;
import android.net.Uri;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.artwokmabel.UploadViewModel;

public class ChatViewModel extends ViewModel implements UploadViewModel {

    private MutableLiveData<Uri> imagePath = new MutableLiveData<>();
    private MutableLiveData<Uri> videoPath = new MutableLiveData<>();

    public ChatViewModel() {
    }

    @Override
    public void setResultOk(Uri imagePath) {
        this.imagePath.setValue(imagePath);
    }

    @Override
    public void setVideoResultOk(Uri videoPath) {
        this.videoPath.setValue(videoPath);
    }

    @Override
    public LiveData<Uri> getImagePath() {
        return imagePath;
    }

    @Override
    public LiveData<Uri> getVideoPath() {
        return videoPath;
    }
}
