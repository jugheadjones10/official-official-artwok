package com.example.artwokmabel;

import android.net.Uri;

import androidx.lifecycle.LiveData;

public interface UploadViewModel {

    void setResultOk(Uri imagePath);
    void setVideoResultOk(Uri videoPath);

    LiveData<Uri> getImagePath();
    LiveData<Uri> getVideoPath();
}
