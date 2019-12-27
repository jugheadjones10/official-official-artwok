package com.example.artwokmabel.profile.Activites;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.artwokmabel.Auth.LoginActivity;
import com.example.artwokmabel.R;
import com.example.artwokmabel.homepage.models.MainPost;
import com.github.irshulx.Editor;
import com.github.irshulx.models.EditorTextStyle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageClickListener;
import com.synnapps.carouselview.ImageListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class UploadPostActivity extends AppCompatActivity {

    private final int REQUEST_IMAGE_SELECTOR = 69;
    private StorageReference storageReference;
    private FirebaseFirestore firebaseFirestore;
    private ImageView uploadPic; // change to ViewPager later
    private ImageView boldBtn, italicsBtn, underlineBtn, highlightBtn, leftBtn, centerBtn, rightBtn, fillBtn;
    private TextView newPostDesc, newPostHashtags;
    private Button uploadBtn, saveBtn, redBtn, pinkBtn, greenBtn, blueBtn, darkBlueBtn, yellowBtn, purpleBtn, blackBtn;
    private ProgressBar uploadProgress;
    private Uri postImageUri = null;
    private ArrayList<String> postImageUris = new ArrayList<String>();
    private boolean more_than_one_img = false;
    private Spinner fontSizeSpinner, fontSelectSpinner;
    private HorizontalScrollView colorPalette;
    private boolean hashtagEditable = true;
    private Context context;
    private Editor editor;
    CarouselView carouselView;

    String[] imageURIs = {"","","","",""};

    int numPostArray[] = {0};

    int h_or_t = 0; // 0 is none, 1 is highlight, 2 is text

    static public int h_counter = 0;
    static public int f_counter = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        postImageUris.clear();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_post);

        // ini
        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();

        fontSelectSpinner = findViewById(R.id.fontSelectSpinner);
        fontSizeSpinner = findViewById(R.id.fontSizeSpinner);

        context = this;
        uploadProgress = findViewById(R.id.upload_progress);
        uploadBtn = findViewById(R.id.upload_btn);
        newPostDesc = findViewById(R.id.new_post_desc);
//        newPostHashtags = findViewById(R.id.new_post_hashtags);
        uploadPic = findViewById(R.id.uploadPic);
//        boldBtn = findViewById(R.id.boldBtn);
//        italicsBtn = findViewById(R.id.italicsBtn);
//        underlineBtn = findViewById(R.id.underlineBtn);
//        highlightBtn = findViewById(R.id.highlightBtn);
//        saveBtn = findViewById(R.id.save_btn);
//        redBtn = findViewById(R.id.redBtn);
//        pinkBtn = findViewById(R.id.pinkBtnL);
//        greenBtn = findViewById(R.id.greenBtn);
//        blueBtn = findViewById(R.id.blueBtn);
//        darkBlueBtn = findViewById(R.id.darkBlueBtn);
//        yellowBtn = findViewById(R.id.yellowBtn);
//        purpleBtn = findViewById(R.id.purpleBtn);
//        blackBtn = findViewById(R.id.blackBtn);
//        fillBtn = findViewById(R.id.fillBtn);
//        leftBtn = findViewById(R.id.alignLeft);
//        centerBtn = findViewById(R.id.alignCenter);
//        rightBtn = findViewById(R.id.alignRight);

        //newPostHashtags.addTextChangedListener(hashtagWatcher);
//
//        colorPalette = findViewById(R.id.color_parlette_view);
//        colorPalette.setVisibility(View.INVISIBLE);

        carouselView = (CarouselView) findViewById(R.id.carouselView);


        //EDITOR

        editor = (Editor) findViewById(R.id.editor);
        findViewById(R.id.action_h1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.updateTextStyle(EditorTextStyle.H1);
            }
        });

        findViewById(R.id.action_h2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.updateTextStyle(EditorTextStyle.H2);
            }
        });

        findViewById(R.id.action_h3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.updateTextStyle(EditorTextStyle.H3);
            }
        });

        findViewById(R.id.action_bold).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.updateTextStyle(EditorTextStyle.BOLD);
            }
        });

        findViewById(R.id.action_Italic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.updateTextStyle(EditorTextStyle.ITALIC);
            }
        });

        findViewById(R.id.action_indent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.updateTextStyle(EditorTextStyle.INDENT);
            }
        });

        findViewById(R.id.action_outdent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.updateTextStyle(EditorTextStyle.OUTDENT);
            }
        });

        findViewById(R.id.action_bulleted).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.insertList(false);
            }
        });

        findViewById(R.id.action_color).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.updateTextColor("#FF3333");
            }
        });

        findViewById(R.id.action_unordered_numbered).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.insertList(true);
            }
        });

        findViewById(R.id.action_hr).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.insertDivider();
            }
        });

        findViewById(R.id.action_insert_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.openImagePicker();
            }
        });

        findViewById(R.id.action_insert_link).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.insertLink();
            }
        });


        findViewById(R.id.action_erase).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.clearAllContents();
            }
        });

        findViewById(R.id.action_blockquote).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.updateTextStyle(EditorTextStyle.BLOCKQUOTE);
            }
        });

        editor.render();
        //EDITOR

        if(imageURIs[0].equals("")){
            carouselView.setVisibility(View.GONE);
            uploadPic.setVisibility(View.VISIBLE);

        }
        else {
            carouselView.setVisibility(View.VISIBLE);
            uploadPic.setVisibility(View.GONE);

            carouselView.setImageListener(imageListener);
            carouselView.setPageCount(imageURIs.length);
        }

        carouselView.setImageClickListener(new ImageClickListener() {
            @Override
            public void onClick(int position) {

//                final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
//
//                AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                builder.setTitle("Choose your post image");
//
//                builder.setItems(options, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int item) {
//
//                        if (options[item].equals("Take Photo")) {
//                            Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//                            startActivityForResult(takePicture, 0);
//
//                        } else if (options[item].equals("Choose from Gallery")) {
//                            Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                            startActivityForResult(pickPhoto , 1);
//
//                        } else if (options[item].equals("Cancel")) {
//                            dialog.dismiss();
//                        }
//                    }
//                });
//                builder.show();


                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if(ContextCompat.checkSelfPermission(UploadPostActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(UploadPostActivity.this, new String[] {android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                    }
                    else{
                        postImageUris.clear();
                        openGallery();
                    }
                }
            }
        });

        Typeface oswald = Typeface.createFromAsset(getAssets(),"fonts/Oswald-Regular.ttf");
        Typeface roboto = Typeface.createFromAsset(getAssets(),"fonts/Roboto-Regular.ttf");
        Typeface openSans = Typeface.createFromAsset(getAssets(),"fonts/OpenSans-Regular.ttf");
        Typeface zcoolXiaoWei = Typeface.createFromAsset(getAssets(),"fonts/ZCOOLXiaoWei-Regular.ttf");

//
//        fontSizeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
//        {
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
//            {
//                String selectedItem = parent.getItemAtPosition(position).toString();
//                newPostDesc.setTextSize(Integer.parseInt(selectedItem));
//
//            } // to close the onItemSelected
//            public void onNothingSelected(AdapterView<?> parent)
//            {
//
//            }
//        });
//
//        fontSelectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
//        {
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
//            {
//                String selectedItem = parent.getItemAtPosition(position).toString();
//                if(selectedItem.equals("Oswald")){
//                    newPostDesc.setTypeface(oswald);
//                }
//                else if(selectedItem.equals("Roboto")){
//                    newPostDesc.setTypeface(roboto);
//                }
//                else if(selectedItem.equals("Open Sans")){
//                    newPostDesc.setTypeface(openSans);
//                }
//                else if(selectedItem.equals("ZCOOL XiaoWei")){
//                    newPostDesc.setTypeface(zcoolXiaoWei);
//                }
//            } // to close the onItemSelected
//            public void onNothingSelected(AdapterView<?> parent)
//            {
//
//            }
//        });
//        highlightBtn.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view){
//                h_counter++;
//
//                if(h_counter%2==0) {
//                    highlightBtn.setBackgroundColor(Color.WHITE);
//                }
//                else {
//                    if(f_counter%2==1){
//                        f_counter++;
//                        fillBtn.setBackgroundColor(Color.WHITE);
//                    }
//                    else{
//
//                    }
//                    highlightBtn.setBackgroundColor(Color.LTGRAY);
//
//                }
//
//                if(colorPalette.getVisibility() == View.INVISIBLE) {
//                    colorPalette.setVisibility(View.VISIBLE);
//                    h_or_t = 1;
//                }
//                else if(colorPalette.getVisibility() == View.VISIBLE) {
//                    if(h_or_t==1){
//                        colorPalette.setVisibility(View.INVISIBLE);
//                        h_or_t = 0;
//                    }
//                    else if(h_or_t==2){
//                        h_or_t = 1;
//                    }
//                }
//            }
//        });
//
//        fillBtn.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view){
//                f_counter++;
//
//                if(f_counter%2==0) {
//                    fillBtn.setBackgroundColor(Color.WHITE);
//
//                }
//                else {
//                    if(h_counter%2==1){
//                        h_counter++;
//                        highlightBtn.setBackgroundColor(Color.WHITE);
//                    }
//                    else{
//
//                    }
//                    fillBtn.setBackgroundColor(Color.LTGRAY);
//
//                }
//
//                if(colorPalette.getVisibility() == View.INVISIBLE) {
//                    colorPalette.setVisibility(View.VISIBLE);
//                    h_or_t = 2;
//                }
//                else if(colorPalette.getVisibility() == View.VISIBLE) {
//                    if(h_or_t==2){
//                        colorPalette.setVisibility(View.INVISIBLE);
//                        h_or_t = 0;
//                    }
//                    else if(h_or_t==1){
//                        h_or_t = 2;
//                    }
//                }
//            }
//        });
//
//        redBtn.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view){
//                if(h_or_t==2) newPostDesc.setTextColor(Color.parseColor("#ed2423"));
//                else if(h_or_t==1) {
//                    newPostDesc.setBackgroundColor(Color.parseColor("#ed2423"));
//                }
//            }
//        });
//
//        pinkBtn.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view){
//                if(h_or_t==2) newPostDesc.setTextColor(Color.parseColor("#D893BC"));
//                else if(h_or_t==1) {
//                    newPostDesc.setBackgroundColor(Color.parseColor("#D893BC"));
//                }
//            }
//        });
//
//        greenBtn.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view){
//                if(h_or_t==2) newPostDesc.setTextColor(Color.parseColor("#2C5F2D"));
//                else if(h_or_t==1) {
//                    newPostDesc.setBackgroundColor(Color.parseColor("#2C5F2D"));
//                }
//            }
//        });
//
//        yellowBtn.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view){
//                if(h_or_t==2) newPostDesc.setTextColor(Color.parseColor("#F1CE01"));
//                else if(h_or_t==1) {
//                    newPostDesc.setBackgroundColor(Color.parseColor("#F1CE01"));
//                }
//            }
//        });
//
//        blueBtn.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view){
//                if(h_or_t==2) newPostDesc.setTextColor(Color.parseColor("#87CEEB"));
//                else if(h_or_t==1) {
//                    newPostDesc.setBackgroundColor(Color.parseColor("#87CEEB"));
//                }
//            }
//        });
//
//        darkBlueBtn.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view){
//                if(h_or_t==2) newPostDesc.setTextColor(Color.parseColor("#3684DD"));
//                else if(h_or_t==1) {
//                    newPostDesc.setBackgroundColor(Color.parseColor("#3684DD"));
//                }
//            }
//        });
//
//        purpleBtn.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view){
//                if(h_or_t==2) newPostDesc.setTextColor(Color.parseColor("#B19CD9"));
//                else if(h_or_t==1) {
//                    newPostDesc.setBackgroundColor(Color.parseColor("#B19CD9"));
//                }
//            }
//        });
//
//        blackBtn.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view){
//                if(h_or_t==2) newPostDesc.setTextColor(Color.BLACK);
//                else if(h_or_t==1) {
//                    newPostDesc.setBackgroundColor(Color.BLACK);
//                }
//            }
//        });
//
//
//        boldBtn.setOnClickListener(new View.OnClickListener(){
//            int counter = 0;
//            @Override
//            public void onClick(View view){
//
//                counter++;
//
//                if(counter%2==0) boldBtn.setBackgroundColor(Color.WHITE);
//                else boldBtn.setBackgroundColor(Color.LTGRAY);
//
//                //newPostDesc.setTypeface(newPostDesc.getTypeface(), Typeface.BOLD);
//                if(newPostDesc.getTypeface() == null){
//                    newPostDesc.setTypeface(Typeface.create(newPostDesc.getTypeface(), NORMAL));
//
//                }else{
//
//                    if(newPostDesc.getTypeface().getStyle() == Typeface.BOLD)  newPostDesc.setTypeface(Typeface.create(newPostDesc.getTypeface(), NORMAL));
//                    else if(newPostDesc.getTypeface().getStyle() == NORMAL) newPostDesc.setTypeface(newPostDesc.getTypeface(), Typeface.BOLD);
//                    else if(newPostDesc.getTypeface().getStyle() == (Typeface.ITALIC)) newPostDesc.setTypeface(newPostDesc.getTypeface(), Typeface.BOLD_ITALIC);
//                    else if(newPostDesc.getTypeface().getStyle() == (Typeface.BOLD_ITALIC)) newPostDesc.setTypeface(newPostDesc.getTypeface(), Typeface.ITALIC);
//                }
//            }
//        });
//
//
//        italicsBtn.setOnClickListener(new View.OnClickListener(){
//            int counter = 0;
//            @Override
//            public void onClick(View view){
//
//                counter++;
//
//                if(counter%2==0) italicsBtn.setBackgroundColor(Color.WHITE);
//                else italicsBtn.setBackgroundColor(Color.LTGRAY);
//                //newPostDesc.setTypeface(newPostDesc.getTypeface(), Typeface.ITALIC);
//                if(newPostDesc.getTypeface() == null){
//                    newPostDesc.setTypeface(Typeface.create(newPostDesc.getTypeface(), NORMAL));
//
//                }else{
//                    if(newPostDesc.getTypeface().getStyle() == Typeface.ITALIC)  newPostDesc.setTypeface(Typeface.create(newPostDesc.getTypeface(), NORMAL));
//                    else if(newPostDesc.getTypeface().getStyle() == NORMAL) newPostDesc.setTypeface(newPostDesc.getTypeface(), Typeface.ITALIC);
//                    else if(newPostDesc.getTypeface().getStyle() == (Typeface.BOLD)) newPostDesc.setTypeface(newPostDesc.getTypeface(), Typeface.BOLD_ITALIC);
//                    else if(newPostDesc.getTypeface().getStyle() == (Typeface.BOLD_ITALIC)) newPostDesc.setTypeface(newPostDesc.getTypeface(), Typeface.BOLD);
//                }
//            }
//        });
//
//
//        underlineBtn.setOnClickListener(new View.OnClickListener(){
//            int counter = 0;
//            @Override
//            public void onClick(View view){
//                counter++;
//                if(counter%2==0) underlineBtn.setBackgroundColor(Color.WHITE);
//                else underlineBtn.setBackgroundColor(Color.LTGRAY);
//                if ((newPostDesc.getPaintFlags() & Paint.UNDERLINE_TEXT_FLAG) > 0){
//                    newPostDesc.setPaintFlags(newPostDesc.getPaintFlags() & (~ Paint.UNDERLINE_TEXT_FLAG));
//                }
//                else{
//                    newPostDesc.setPaintFlags(newPostDesc.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
//                }
//            }
//        });
//
//
//        leftBtn.setOnClickListener(new View.OnClickListener(){
//            int counter = 0;
//            @Override
//            public void onClick(View view){
//
//                counter++;
//                if(counter%2==0) leftBtn.setBackgroundColor(Color.WHITE);
//                else leftBtn.setBackgroundColor(Color.LTGRAY);
//                newPostDesc.setGravity(Gravity.LEFT);
//            }
//        });
//
//
//
//        centerBtn.setOnClickListener(new View.OnClickListener(){
//            int counter = 0;
//            @Override
//            public void onClick(View view){
//                counter++;
//                if(counter%2==0) centerBtn.setBackgroundColor(Color.WHITE);
//                else centerBtn.setBackgroundColor(Color.LTGRAY);
//                newPostDesc.setGravity(Gravity.CENTER_HORIZONTAL);
//            }
//        });
//
//        rightBtn.setOnClickListener(new View.OnClickListener(){
//            int counter = 0;
//            @Override
//            public void onClick(View view){
//                counter++;
//                if(counter%2==0) rightBtn.setBackgroundColor(Color.WHITE);
//                else rightBtn.setBackgroundColor(Color.LTGRAY);
//                newPostDesc.setGravity(Gravity.RIGHT);
//            }
//        });


        uploadPic.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                // opens gallery when button is clicked after checking if app has access to user files
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if(ContextCompat.checkSelfPermission(UploadPostActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(UploadPostActivity.this, new String[] {android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                    }
                    else{
                        postImageUris.clear();
                        openGallery();
                    }
                }
            }
        });

//        newPostHashtags.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if(MotionEvent.ACTION_UP == event.getAction() && hashtagEditable==true) {
//                    newPostHashtags.setText("#");
//                    hashtagEditable = false;
//                }
//
//                return false; // return is important...
//            }
//        });

        uploadBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View view){
                if(newPostDesc.getText().toString().isEmpty()){
                    Toast.makeText(UploadPostActivity.this, "Please fill the description for your listing.", Toast.LENGTH_LONG).show();
                }
                else if(postImageUris.isEmpty() && postImageUri == null) {
                    Toast.makeText(UploadPostActivity.this, "Please add at least one photo of your listing.", Toast.LENGTH_LONG).show();
                }
                else{
                        // text (hashtags optional) and picture has been chosen; no empty or null value
                        uploadProgress.setVisibility(View.VISIBLE);

                        Date today = Calendar.getInstance().getTime();
                        SimpleDateFormat format = new SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH);
                        String dateToStr = format.format(today);
                        Log.d("Dwa", dateToStr);

                        getUserPostNumPost();
                        //Sets numPostArray[0] to be the number of posts that the current user has

                        if(more_than_one_img){
//                            Map<String, Object> objectMap = new HashMap<>();
//                            objectMap.put("photos", postImageUris);
//                            objectMap.put("desc", newPostDesc.getText().toString());
//                            objectMap.put("hashtags", newPostHashtags.getText().toString());
//                            objectMap.put("user_id", LoginActivity.uid);

//                            firebaseFirestore.collection("Users").document(LoginActivity.uid).collection("Posts").document((numPostArray[0] + 1) + " " + dateToStr).set(objectMap)
                            DocumentReference newUserRef = firebaseFirestore.collection("Users").document(LoginActivity.uid).collection("Posts").document();
                            MainPost newPost = new MainPost(
                                    LoginActivity.uid,
                                    newPostDesc.getText().toString(),
                                    newPostHashtags.getText().toString(),
                                    newUserRef.getId(),
                                    "placeholder username",
                                    postImageUris,
                                    dateToStr,
                                    System.currentTimeMillis());

                            newUserRef.set(newPost)
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(UploadPostActivity.this, "Successfully uploaded ic_dm.", Toast.LENGTH_LONG).show();
                                        finish();
                                        uploadProgress.setVisibility(View.INVISIBLE);
                                    })
                                    .addOnFailureListener(e ->
                                            Toast.makeText(UploadPostActivity.this, "Failed to upload ic_dm.", Toast.LENGTH_LONG)
                                                    .show());

//                             firebaseFirestore.collection("Users").document(LoginActivity.uid).collection("Posts").add(objectMap)
//                                    .addOnSuccessListener(aVoid -> {
//                                        Toast.makeText(UploadPostActivity.this, "Successfully uploaded ic_dm.", Toast.LENGTH_LONG).show();
//                                        finish();
//                                        uploadProgress.setVisibility(View.INVISIBLE);
//                                    })
//                                    .addOnFailureListener(e ->
//                                            Toast.makeText(UploadPostActivity.this, "Failed to upload ic_dm.", Toast.LENGTH_LONG)
//                                                    .show());

                            firebaseFirestore
                                    .collection("Users")
                                    .document(LoginActivity.uid)
                                    .update("number_of_posts", FieldValue.increment(1));
                        }

                        else{
                            Log.d("PostImg", "DDDDDDDDDDDDDDD" + postImageUris.toString());
                            DocumentReference newUserRef = firebaseFirestore.collection("Users").document(LoginActivity.uid).collection("Posts").document();
                            MainPost newPost = new MainPost(
                                    LoginActivity.uid,
                                    newPostDesc.getText().toString(),
                                    newPostHashtags.getText().toString(),
                                    newUserRef.getId(),
                                    "placeholder username",
                                    postImageUris,
                                    dateToStr,
                                    System.currentTimeMillis());

                            newUserRef.set(newPost)
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(UploadPostActivity.this, "Successfully uploaded ic_dm.", Toast.LENGTH_LONG).show();
                                        finish();
                                        uploadProgress.setVisibility(View.INVISIBLE);
                                    })
                                    .addOnFailureListener(e ->
                                            Toast.makeText(UploadPostActivity.this, "Failed to upload ic_dm. awd", Toast.LENGTH_LONG)
                                                    .show());

                            firebaseFirestore
                                    .collection("Users")
                                    .document(LoginActivity.uid)
                                    .update("number_of_posts", FieldValue.increment(1));

//////
//                            StorageReference filePath = storageReference.child("Images").child(LoginActivity.uid).child(dateToStr); // currently one post per user; will change numbering system according to current number of posts
//
//                            UploadTask objectUploadTask = filePath.putFile(postImageUri);
//
//                            objectUploadTask.continueWithTask(task -> {
//                                if(!task.isSuccessful()){
//                                    throw task.getException();
//                                }
//                                return filePath.getDownloadUrl();
//                            }).addOnCompleteListener(task -> {
//                                if(task.isSuccessful()){
//
//                                    Map<String, Object> objectMap = new HashMap<>();
//                                    postImageUris.add(task.getResult().toString());
//                                    objectMap.put("photos", postImageUris);
//                                    objectMap.put("desc", newPostDesc.getText().toString());
//                                    objectMap.put("hashtags", newPostHashtags.getText().toString());
//                                    objectMap.put("user_id", LoginActivity.uid);
//
//                                    firebaseFirestore.collection("Users").document(LoginActivity.uid).collection("Posts").document((numPostArray[0] + 1) + " " + dateToStr).set(objectMap)
//                                            .addOnSuccessListener(aVoid -> {
//                                                Toast.makeText(UploadPostActivity.this, "Successfully uploaded ic_dm.", Toast.LENGTH_LONG).show();
//                                                finish();
//                                                uploadProgress.setVisibility(View.INVISIBLE);
//                                            })
//                                            .addOnFailureListener(e ->
//                                                    Toast.makeText(UploadPostActivity.this, "Failed to upload ic_dm.", Toast.LENGTH_LONG)
//                                                            .show());
//                                }
//                                else if(!task.isSuccessful()) {
//                                    Toast.makeText(UploadPostActivity.this, task.getException().toString(), Toast.LENGTH_LONG).show();
//                                }
//                            });
//
//                            firebaseFirestore
//                                    .collection("Users")
//                                    .document(LoginActivity.uid)
//                                    .update("number_of_posts",  FieldValue.increment(1));
//
                        }
                }
            }
        });
    }


    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            Picasso.get().load(imageURIs[position]).into(imageView);
        }
    };

    private void openGallery(){ // opens gallery intent and waits for user to pick an ic_dm

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        //intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);

        // Launching the Intent
        startActivityForResult(intent,1);
    }

    public void getUserPostNumPost(){
        DocumentReference docRef = firebaseFirestore.collection("Users").document(LoginActivity.uid);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("LOG: ", "DocumentSnapshot data: " + document.getData());
                        String name = "number_of_posts";
                        numPostArray[0] = ((Long) document.get(name)).intValue();

                    } else {
                        Log.d("LOG: ", "No such document");
                        getUserPostNumPost();
                    }
                } else {
                    Log.d("LOG: ", "get failed with ", task.getException());
                }
            }
        });
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Log.d("onAct", data.getData().toString());
        for(int i =0; i< 5; i++){
            imageURIs[i] = "";
        }

        if(data!=null) {
            if (data.getClipData() != null) {
                more_than_one_img = true;
                int totalItemsSelected = data.getClipData().getItemCount();
                if (totalItemsSelected > 5) {
                    Toast.makeText(UploadPostActivity.this, "You can only upload 5 images at a time. The first 5 have been automatically selected.", Toast.LENGTH_LONG).show();
                }

                for (int i = 0; i < (totalItemsSelected > 5 ? 5 : totalItemsSelected); i++) {
                    Uri fileUri = data.getClipData().getItemAt(i).getUri();

                    String fileName = getFileName(fileUri);

                    StorageReference fileToUpload = storageReference.child("Images").child(LoginActivity.uid).child(fileName); // randomize name
                    fileToUpload.putFile(fileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            storageReference.child("Images").child(LoginActivity.uid).child(fileName).getDownloadUrl().addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    postImageUris.add(task.getResult().toString());
                                    Log.d("LOG: ", "postImageUris " + task.getResult().toString());

                                    for (int i = 0; i < postImageUris.size(); i++) {
                                        imageURIs[i] = postImageUris.get(i);
                                    }
                                    carouselView.setVisibility(View.VISIBLE);
                                    uploadPic.setVisibility(View.GONE);

                                    int len = 0;
                                    for (int i = 0; i < postImageUris.size(); i++) {
                                        if (!imageURIs[i].equals("")) {
                                            len++;
                                        }
                                    }
                                    carouselView.setImageListener(imageListener);
                                    carouselView.setPageCount(len);
                                }
                            });

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(UploadPostActivity.this, "failed", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            } else {
                if (data != null) {

                    Uri fileUri = data.getData();
                    String fileName = getFileName(fileUri);

                    StorageReference fileToUpload = storageReference.child("Images").child(LoginActivity.uid).child(fileName); // randomize name
                    fileToUpload.putFile(fileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            storageReference.child("Images").child(LoginActivity.uid).child(fileName).getDownloadUrl().addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    postImageUris.add(task.getResult().toString());
                                    Log.d("LOG: ", "postImageUris " + task.getResult().toString());

                                    for (int i = 0; i < postImageUris.size(); i++) {
                                        imageURIs[i] = postImageUris.get(i);
                                    }
                                    carouselView.setVisibility(View.VISIBLE);
                                    uploadPic.setVisibility(View.GONE);

                                    int len = 0;
                                    for (int i = 0; i < postImageUris.size(); i++) {
                                        if (!imageURIs[i].equals("")) {
                                            len++;
                                        }
                                    }
                                    carouselView.setImageListener(imageListener);
                                    carouselView.setPageCount(len);
                                }
                            });

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(UploadPostActivity.this, "failed", Toast.LENGTH_LONG).show();
                        }
                    });

//                    postImageUri = data.getData();
//                    imageURIs[0] = postImageUri.toString();
//
//                    //uploadPic.setImageURI(postImageUri);
//                    more_than_one_img = false;
//
//                    carouselView.setVisibility(View.VISIBLE);
//                    uploadPic.setVisibility(View.GONE);
//
//                    carouselView.setImageListener(imageListener);
//                    carouselView.setPageCount(1);
                } else { // no pics selected
                    carouselView.setVisibility(View.GONE);
                    uploadPic.setVisibility(View.VISIBLE);
                }
            }
        }
    }


    private final TextWatcher hashtagWatcher = new TextWatcher() { // adding hashtag to every word entered in newPostHashtags (automatic after every space)
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
        public void afterTextChanged(Editable s) { }
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(newPostHashtags.getText().toString().length() == 0){

            }
            else if(s.charAt(s.length() - 1) == ' '){ // if the last character is a space and , auto add hashtag after space
                newPostHashtags.append("#");
            }
        }
    };

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }
}

