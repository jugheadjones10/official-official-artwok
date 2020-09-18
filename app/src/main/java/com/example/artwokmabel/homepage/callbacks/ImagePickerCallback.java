package com.example.artwokmabel.homepage.callbacks;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.provider.MediaStore;

import androidx.lifecycle.ViewModel;

import com.example.artwokmabel.UploadViewModel;
import com.example.artwokmabel.profile.uploadpost.UploadPostViewModel;
import com.example.artwokmabel.profile.utils.ImagePickerActivity;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

public class ImagePickerCallback {

    private Activity activity;
    private int requestCode;
    public UploadViewModel viewModel;
    private static ImagePickerCallback instance;
    private String showOptions;

    //No overlap with other image picker callbacks should occur
    public static ImagePickerCallback getInstance(){
        return instance;
    }

    public ImagePickerCallback(Activity activity, int requestCode, UploadViewModel viewModel, String showOptions){
        instance = this;
        this.activity = activity;
        this.requestCode = requestCode;
        this.viewModel = viewModel;
        this.showOptions = showOptions;
    }

    public void onImagePickerClicked(){
        Dexter.withActivity(activity)
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {

                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            showImagePickerOptions();
                        }else{
                            //Todo: need to account for possibility that not all permissions are granted (go to Dexter docs and implement the snackbar)
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    private void showImagePickerOptions(){
        ImagePickerActivity.showImagePickerOptions(activity, showOptions, new ImagePickerActivity.PickerOptionListener() {
            @Override
            public void onTakeCameraSelected() {
                launchCameraIntent();
            }

            @Override
            public void onTakeVideoSelected() {
                launchTakeVideoIntent();
            }

            @Override
            public void onChooseGallerySelected() {
                launchGalleryIntent();
            }

            @Override
            public void onChooseGalleryVideoSelected() {
                launchGalleryVideoIntent();
            }
        });
    }

    private void launchCameraIntent() {
        Intent intent = new Intent(activity, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);

        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000);

        activity.startActivityForResult(intent, requestCode);
    }

    private void launchTakeVideoIntent(){
        Intent intent = new Intent(activity, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_VIDEO_CAPTURE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);

        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000);

        activity.startActivityForResult(intent, requestCode);
    }

    private void launchGalleryIntent() {
        Intent intent = new Intent(activity, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);

        activity.startActivityForResult(intent, requestCode);
    }

    private void launchGalleryVideoIntent(){
        Intent intent = new Intent(activity, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_VIDEO);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);

        activity.startActivityForResult(intent, requestCode);
    }
}
