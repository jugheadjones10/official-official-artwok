package com.example.artwokmabel.profile.utils;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import com.example.artwokmabel.HomePageActivity;
import com.example.artwokmabel.R;
import com.example.artwokmabel.UploadViewModel;
import com.example.artwokmabel.homepage.callbacks.ImagePickerCallback;
import com.example.artwokmabel.profile.uploadpost.UploadPostFragment;
import com.example.artwokmabel.profile.uploadpost.UploadPostViewModel;
import com.example.artwokmabel.profile.user.ProfileFragmentViewModel;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.List;

import static androidx.core.content.FileProvider.getUriForFile;


public class ImagePickerActivity extends AppCompatActivity {
    private static final String TAG = ImagePickerActivity.class.getSimpleName();
    public static final String INTENT_IMAGE_PICKER_OPTION = "image_picker_option";
    public static final String INTENT_ASPECT_RATIO_X = "aspect_ratio_x";
    public static final String INTENT_ASPECT_RATIO_Y = "aspect_ratio_Y";
    public static final String INTENT_LOCK_ASPECT_RATIO = "lock_aspect_ratio";
    public static final String INTENT_IMAGE_COMPRESSION_QUALITY = "compression_quality";
    public static final String INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT = "set_bitmap_max_width_height";
    public static final String INTENT_BITMAP_MAX_WIDTH = "max_width";
    public static final String INTENT_BITMAP_MAX_HEIGHT = "max_height";

    public static final String SHOW_IMAGE_OPTIONS_ONLY = "show image options only";
    public static final String SHOW_ALL_OPTIONS = "show all options";

    public static final int REQUEST_IMAGE_CAPTURE = 0;
    public static final int REQUEST_VIDEO_CAPTURE = 2;
    public static final int REQUEST_GALLERY_IMAGE = 1;
    public static final int REQUEST_GALLERY_VIDEO = 3;

    private boolean lockAspectRatio = false, setBitmapMaxWidthHeight = false;
    private int ASPECT_RATIO_X = 16, ASPECT_RATIO_Y = 9, bitmapMaxWidth = 1000, bitmapMaxHeight = 1000;
    private int IMAGE_COMPRESSION = 80;
    public static String fileName;

    private UploadViewModel viewModel;

    public interface PickerOptionListener {
        void onTakeCameraSelected();
        void onChooseGallerySelected();
        void onChooseGalleryVideoSelected();
        void onTakeVideoSelected();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_picker);

        viewModel = ImagePickerCallback.getInstance().viewModel;

        Intent intent = getIntent();
        if (intent == null) {
            Toast.makeText(getApplicationContext(), getString(R.string.toast_image_intent_null), Toast.LENGTH_LONG).show();
            return;
        }

        ASPECT_RATIO_X = intent.getIntExtra(INTENT_ASPECT_RATIO_X, ASPECT_RATIO_X);
        ASPECT_RATIO_Y = intent.getIntExtra(INTENT_ASPECT_RATIO_Y, ASPECT_RATIO_Y);
        IMAGE_COMPRESSION = intent.getIntExtra(INTENT_IMAGE_COMPRESSION_QUALITY, IMAGE_COMPRESSION);
        lockAspectRatio = intent.getBooleanExtra(INTENT_LOCK_ASPECT_RATIO, false);
        setBitmapMaxWidthHeight = intent.getBooleanExtra(INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, false);
        bitmapMaxWidth = intent.getIntExtra(INTENT_BITMAP_MAX_WIDTH, bitmapMaxWidth);
        bitmapMaxHeight = intent.getIntExtra(INTENT_BITMAP_MAX_HEIGHT, bitmapMaxHeight);

        int requestCode = intent.getIntExtra(INTENT_IMAGE_PICKER_OPTION, -1);
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            takeCameraImage();
        } else if(requestCode == REQUEST_GALLERY_IMAGE){
            chooseImageFromGallery();
        }else if(requestCode == REQUEST_GALLERY_VIDEO){
            chooseVideoFromGallery();
        }else{
            takeVideo();
        }
    }

    public static void showImagePickerOptions(Context context,  String showOptions, PickerOptionListener listener) {
        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.title_dialog_choose_option));

        // add a list
        String[] options;
        if(showOptions == SHOW_IMAGE_OPTIONS_ONLY){
            options = new String[]{context.getString(R.string.option_dialog_photo), context.getString(R.string.option_dialog_gallery)};
        }else{
            //When the option is SHOW_ALL_OPTIONS
            options = new String[]{context.getString(R.string.option_dialog_photo), context.getString(R.string.option_dialog_gallery), context.getString(R.string.option_dialog_video), context.getString(R.string.option_dialog_gallery_video)};
        }
        builder.setItems(options, (dialog, which) -> {
            switch (which) {
                case 0:
                    listener.onTakeCameraSelected();
                    break;
                case 1:
                    listener.onChooseGallerySelected();
                    break;
                case 2:
                    listener.onTakeVideoSelected();
                    break;
                case 3:
                    listener.onChooseGalleryVideoSelected();
                    break;
            }
        });

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        try {
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void takeCameraImage() {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            fileName = System.currentTimeMillis() + ".jpg";
                            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, getCacheImagePath(fileName));
                            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                            }
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    private void takeVideo(){
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        }
    }

    private void chooseImageFromGallery() {
        Dexter.withActivity(this)
            .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .withListener(new MultiplePermissionsListener() {
                @Override
                public void onPermissionsChecked(MultiplePermissionsReport report) {
                    if (report.areAllPermissionsGranted()) {
                        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(pickPhoto, REQUEST_GALLERY_IMAGE);
                    }
                }

                @Override
                public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                    token.continuePermissionRequest();
                }
            }).check();
    }

    private void chooseVideoFromGallery(){
        Intent pickVideo = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickVideo, REQUEST_GALLERY_VIDEO);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_IMAGE_CAPTURE:
                if (resultCode == RESULT_OK) {
                    cropImage(getCacheImagePath(fileName));
                } else {
                    setResultCancelled();
                }
                break;
            case REQUEST_VIDEO_CAPTURE:
                if (resultCode == RESULT_OK) {
                    //cropImage(getCacheImagePath(fileName));
                    Uri videoUri = data.getData();
                    setVideoResultOk(videoUri);
                } else {
                    setResultCancelled();
                }
                break;
            case REQUEST_GALLERY_IMAGE:
                if (resultCode == RESULT_OK) {

                    Log.d("chooseImage", "We have received A OK from request gallery image on Activity Result");

                    Uri imageUri = data.getData();
                    cropImage(imageUri);
                } else {
                    setResultCancelled();
                }
                break;
            case REQUEST_GALLERY_VIDEO:
                if (resultCode == RESULT_OK) {
                    Uri videoUri = data.getData();
                    setVideoResultOk(videoUri);
                } else {
                    setResultCancelled();
                }
                break;
            case UCrop.REQUEST_CROP:
                if (resultCode == RESULT_OK) {
                    Log.d("chooseImage", "We have received AOK from UCrop result OK");
                    handleUCropResult(data);
                } else {
                    Log.d("chooseImage", "Result of REQUEST_CROP is not RESULT_OK");
                    setResultCancelled();
                }
                break;
            case UCrop.RESULT_ERROR:
                final Throwable cropError = UCrop.getError(data);
                Log.e(TAG, "Crop error: " + cropError);
                Log.d("chooseImage", "Crop error: " + cropError);
                setResultCancelled();
                break;
            default:
                setResultCancelled();
        }
    }

    private void cropImage(Uri sourceUri) {

        Log.d("chooseImage", "We have entered cropImage");

        Uri destinationUri = Uri.fromFile(new File(getCacheDir(), queryName(getContentResolver(), sourceUri)));
        UCrop.Options options = new UCrop.Options();
        options.setCompressionQuality(IMAGE_COMPRESSION);

        // applying UI theme
        options.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        options.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        options.setActiveWidgetColor(ContextCompat.getColor(this, R.color.colorPrimary));

        if (lockAspectRatio)
            options.withAspectRatio(ASPECT_RATIO_X, ASPECT_RATIO_Y);

        if (setBitmapMaxWidthHeight)
            options.withMaxResultSize(bitmapMaxWidth, bitmapMaxHeight);

        Log.d("chooseImage", "We are right before UCrop start");

        UCrop.of(sourceUri, destinationUri)
                .withOptions(options)
                .start(this);
    }

    private void handleUCropResult(Intent data) {
        if (data == null) {
            Log.d("chooseImage", "Data from UCROP is null");
            setResultCancelled();
            return;
        }
        final Uri resultUri = UCrop.getOutput(data);
        setResultOk(resultUri);
    }

    private void setResultOk(Uri imagePath) {
//        Intent intent = new Intent();
//        intent.putExtra("path", imagePath);
//        setResult(Activity.RESULT_OK, intent);
        Log.d("chooseImage", "Result is all ok, on the way to sending to view model");

        viewModel.setResultOk(imagePath);
        finish();
    }

    private void setVideoResultOk(Uri videoPath){
        viewModel.setVideoResultOk(videoPath);
        finish();
    }

    private void setResultCancelled() {
//        Intent intent = new Intent();
//        setResult(Activity.RESULT_CANCELED, intent);

//        viewModel.setResultCancelled();
        Log.d("chooseImage", "Result has been cancelled");
        finish();
    }

    private Uri getCacheImagePath(String fileName) {
        File path = new File(getExternalCacheDir(), "camera");
        if (!path.exists()) path.mkdirs();
        File image = new File(path, fileName);
        return getUriForFile(ImagePickerActivity.this, getPackageName() + ".provider", image);
    }

    private static String queryName(ContentResolver resolver, Uri uri) {
        Cursor returnCursor =
                resolver.query(uri, null, null, null, null);
        assert returnCursor != null;
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        returnCursor.moveToFirst();
        String name = returnCursor.getString(nameIndex);
        returnCursor.close();
        return name;
    }

    /**
     * Calling this will delete the images from cache directory
     * useful to clear some memory
     */
    public static void clearCache(Context context) {
        File path = new File(context.getExternalCacheDir(), "camera");
        if (path.exists() && path.isDirectory()) {
            for (File child : path.listFiles()) {
                child.delete();
            }
        }
    }
}