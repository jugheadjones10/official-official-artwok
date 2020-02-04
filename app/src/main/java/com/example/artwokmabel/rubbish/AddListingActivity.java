package com.example.artwokmabel.profile.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.artwokmabel.R;
import com.example.artwokmabel.models.Listing;
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
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageClickListener;
import com.synnapps.carouselview.ImageListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddListingActivity extends AppCompatActivity {

    private StorageReference storageReference;
    private FirebaseFirestore firebaseFirestore;
    private ImageView uploadPic;
    private ImageView boldBtn, italicsBtn, underlineBtn, highlightBtn, leftBtn, centerBtn, rightBtn, fillBtn;
    private TextView newPostDesc, newPostHashtags, newPostTitle, setPrice, returnExchangePolicies, deliveryPolicies;
    private Button uploadBtn, saveBtn, redBtn, pinkBtn, greenBtn, blueBtn, yellowBtn, purpleBtn, blackBtn, darkBlueBtn;
    private ProgressBar uploadProgress;
    private Uri postImageUri = null;
    private ArrayList<String> postImageUris = new ArrayList<String>();
    private boolean more_than_one_img = false;
    private Spinner fontSizeSpinner, fontSelectSpinner;
    private Button selectCategoryBtn;
    private ArrayList<String> finCatList = new ArrayList<String>();
    private HorizontalScrollView colorPalette;
    private boolean hashtagEditable = true;
    CarouselView carouselView;

    //Todo: edit ucrop library to allow for editing of multiple images
    int numPostArray[] = {0};

    public static final int REQUEST_IMAGE = 100;
    String[] imageURIs = {"","","","",""};

    int h_or_t = 0; // 0 is none, 1 is highlight, 2 is text
    static public int h_counter = 0;
    static public int f_counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        postImageUris.clear();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.z_activity_upload_listing);

        // ini
        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();

        fontSelectSpinner = findViewById(R.id.fontSelectSpinnerL);
        fontSizeSpinner = findViewById(R.id.fontSizeSpinnerL);
        selectCategoryBtn = findViewById(R.id.select_categoryL);

        uploadProgress = findViewById(R.id.upload_progressL);
        uploadBtn = findViewById(R.id.upload_btnL);
        newPostDesc = findViewById(R.id.new_post_descL);
        newPostHashtags = findViewById(R.id.new_post_hashtagsL);
        newPostTitle = findViewById(R.id.new_post_titleL);
        setPrice = findViewById(R.id.set_price);
        deliveryPolicies = findViewById(R.id.delivery_policies);
        returnExchangePolicies = findViewById(R.id.return_exchange_policies);

        uploadPic = findViewById(R.id.uploadPicL);
        boldBtn = findViewById(R.id.boldBtnL);
        italicsBtn = findViewById(R.id.italicsBtnL);
        underlineBtn = findViewById(R.id.underlineBtnL);
        highlightBtn = findViewById(R.id.highlightBtnL);
        saveBtn = findViewById(R.id.save_btnL);
        redBtn = findViewById(R.id.redBtnL);
        pinkBtn = findViewById(R.id.pinkBtnL);
        greenBtn = findViewById(R.id.greenBtnL);
        blueBtn = findViewById(R.id.blueBtnL);
        darkBlueBtn = findViewById(R.id.darkBlueBtnL);
        yellowBtn = findViewById(R.id.yellowBtnL);
        purpleBtn = findViewById(R.id.purpleBtnL);
        blackBtn = findViewById(R.id.blackBtnL);
        uploadBtn = findViewById(R.id.upload_btnL);
        fillBtn = findViewById(R.id.fillBtnL);
        leftBtn = findViewById(R.id.alignLeftL);
        centerBtn = findViewById(R.id.alignCenterL);
        rightBtn = findViewById(R.id.alignRightL);

        newPostHashtags.addTextChangedListener(hashtagWatcher);

        colorPalette = findViewById(R.id.color_parlette_viewL);
        colorPalette.setVisibility(View.INVISIBLE);

        carouselView = (CarouselView) findViewById(R.id.carouselView);

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
                ProfileImageClick();
//                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
//                    if(ContextCompat.checkSelfPermission(AddListingActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
//                        ActivityCompat.requestPermissions(AddListingActivity.this, new String[] {android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
//                    }
//                    else{
//                        postImageUris.clear();
//                    }
//                }
            }
        });

        Typeface oswald = Typeface.createFromAsset(getAssets(),"fonts/Oswald-Regular.ttf");
        Typeface roboto = Typeface.createFromAsset(getAssets(),"fonts/Roboto-Regular.ttf");
        Typeface openSans = Typeface.createFromAsset(getAssets(),"fonts/OpenSans-Regular.ttf");
        Typeface zcoolXiaoWei = Typeface.createFromAsset(getAssets(),"fonts/ZCOOLXiaoWei-Regular.ttf");

        fontSizeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                String selectedItem = parent.getItemAtPosition(position).toString();
                newPostDesc.setTextSize(Integer.parseInt(selectedItem));

            } // to close the onItemSelected
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        fontSelectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                String selectedItem = parent.getItemAtPosition(position).toString();
                if(selectedItem.equals("Oswald")){
                    newPostDesc.setTypeface(oswald);
                }
                else if(selectedItem.equals("Roboto")){
                    newPostDesc.setTypeface(roboto);
                }
                else if(selectedItem.equals("Open Sans")){
                    newPostDesc.setTypeface(openSans);
                }
                else if(selectedItem.equals("ZCOOL XiaoWei")){
                    newPostDesc.setTypeface(zcoolXiaoWei);
                }
            } // to close the onItemSelected
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        highlightBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                h_counter++;

                if(h_counter%2==0) {
                    highlightBtn.setBackgroundColor(Color.WHITE);
                }
                else {
                    if(f_counter%2==1){
                        f_counter++;
                        fillBtn.setBackgroundColor(Color.WHITE);
                    }
                    else{

                    }
                    highlightBtn.setBackgroundColor(Color.LTGRAY);

                }

                if(colorPalette.getVisibility() == View.INVISIBLE) {
                    colorPalette.setVisibility(View.VISIBLE);
                    h_or_t = 1;
                }
                else if(colorPalette.getVisibility() == View.VISIBLE) {
                    if(h_or_t==1){
                        colorPalette.setVisibility(View.INVISIBLE);
                        h_or_t = 0;
                    }
                    else if(h_or_t==2){
                        h_or_t = 1;
                    }
                }
            }
        });

        fillBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                f_counter++;

                if(f_counter%2==0) {
                    fillBtn.setBackgroundColor(Color.WHITE);

                }
                else {
                    if(h_counter%2==1){
                        h_counter++;
                        highlightBtn.setBackgroundColor(Color.WHITE);
                    }
                    else{

                    }
                    fillBtn.setBackgroundColor(Color.LTGRAY);

                }

                if(colorPalette.getVisibility() == View.INVISIBLE) {
                    colorPalette.setVisibility(View.VISIBLE);
                    h_or_t = 2;
                }
                else if(colorPalette.getVisibility() == View.VISIBLE) {
                    if(h_or_t==2){
                        colorPalette.setVisibility(View.INVISIBLE);
                        h_or_t = 0;
                    }
                    else if(h_or_t==1){
                        h_or_t = 2;
                    }
                }
            }
        });


        redBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(h_or_t==2) newPostDesc.setTextColor(Color.parseColor("#ed2423"));
                else if(h_or_t==1) {
                    newPostDesc.setBackgroundColor(Color.parseColor("#ed2423"));
                }
            }
        });


        pinkBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(h_or_t==2) newPostDesc.setTextColor(Color.parseColor("#D893BC"));
                else if(h_or_t==1) {
                    newPostDesc.setBackgroundColor(Color.parseColor("#D893BC"));
                }
            }
        });

        greenBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(h_or_t==2) newPostDesc.setTextColor(Color.parseColor("#2C5F2D"));
                else if(h_or_t==1) {
                    newPostDesc.setBackgroundColor(Color.parseColor("#2C5F2D"));
                }
            }
        });

        yellowBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(h_or_t==2) newPostDesc.setTextColor(Color.parseColor("#F1CE01"));
                else if(h_or_t==1) {
                    newPostDesc.setBackgroundColor(Color.parseColor("#F1CE01"));
                }
            }
        });

        blueBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(h_or_t==2) newPostDesc.setTextColor(Color.parseColor("#87CEEB"));
                else if(h_or_t==1) {
                    newPostDesc.setBackgroundColor(Color.parseColor("#87CEEB"));
                }
            }
        });

        darkBlueBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(h_or_t==2) newPostDesc.setTextColor(Color.parseColor("#3684DD"));
                else if(h_or_t==1) {
                    newPostDesc.setBackgroundColor(Color.parseColor("#3684DD"));
                }
            }
        });

        purpleBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(h_or_t==2) newPostDesc.setTextColor(Color.parseColor("#B19CD9"));
                else if(h_or_t==1) {
                    newPostDesc.setBackgroundColor(Color.parseColor("#B19CD9"));
                }
            }
        });

        blackBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(h_or_t==2) newPostDesc.setTextColor(Color.BLACK);
                else if(h_or_t==1) {
                    newPostDesc.setBackgroundColor(Color.BLACK);
                }
            }
        });


        boldBtn.setOnClickListener(new View.OnClickListener(){
            int counter = 0;
            @Override
            public void onClick(View view){
                counter++;

                if(counter%2==0) boldBtn.setBackgroundColor(Color.WHITE);
                else boldBtn.setBackgroundColor(Color.LTGRAY);
                //newPostDesc.setTypeface(newPostDesc.getTypeface(), Typeface.BOLD);
                if(newPostDesc.getTypeface() == null){
                    newPostDesc.setTypeface(Typeface.create(newPostDesc.getTypeface(), Typeface.NORMAL));

                }else{

                    if(newPostDesc.getTypeface().getStyle() == Typeface.BOLD)  newPostDesc.setTypeface(Typeface.create(newPostDesc.getTypeface(), Typeface.NORMAL));
                    else if(newPostDesc.getTypeface().getStyle() == Typeface.NORMAL) newPostDesc.setTypeface(newPostDesc.getTypeface(), Typeface.BOLD);
                    else if(newPostDesc.getTypeface().getStyle() == (Typeface.ITALIC)) newPostDesc.setTypeface(newPostDesc.getTypeface(), Typeface.BOLD_ITALIC);
                    else if(newPostDesc.getTypeface().getStyle() == (Typeface.BOLD_ITALIC)) newPostDesc.setTypeface(newPostDesc.getTypeface(), Typeface.ITALIC);
                }
            }
        });


        italicsBtn.setOnClickListener(new View.OnClickListener(){
            int counter = 0;
            @Override
            public void onClick(View view){
                counter++;

                if(counter%2==0) italicsBtn.setBackgroundColor(Color.WHITE);
                else italicsBtn.setBackgroundColor(Color.LTGRAY);

                //newPostDesc.setTypeface(newPostDesc.getTypeface(), Typeface.ITALIC);
                if(newPostDesc.getTypeface() == null){
                    newPostDesc.setTypeface(Typeface.create(newPostDesc.getTypeface(), Typeface.NORMAL));

                }else{
                    if(newPostDesc.getTypeface().getStyle() == Typeface.ITALIC)  newPostDesc.setTypeface(Typeface.create(newPostDesc.getTypeface(), Typeface.NORMAL));
                    else if(newPostDesc.getTypeface().getStyle() == Typeface.NORMAL) newPostDesc.setTypeface(newPostDesc.getTypeface(), Typeface.ITALIC);
                    else if(newPostDesc.getTypeface().getStyle() == (Typeface.BOLD)) newPostDesc.setTypeface(newPostDesc.getTypeface(), Typeface.BOLD_ITALIC);
                    else if(newPostDesc.getTypeface().getStyle() == (Typeface.BOLD_ITALIC)) newPostDesc.setTypeface(newPostDesc.getTypeface(), Typeface.BOLD);
                }
            }
        });

        underlineBtn.setOnClickListener(new View.OnClickListener(){
            int counter = 0;
            @Override
            public void onClick(View view){
                counter++;
                if(counter%2==0) underlineBtn.setBackgroundColor(Color.WHITE);
                else underlineBtn.setBackgroundColor(Color.LTGRAY);
                if ((newPostDesc.getPaintFlags() & Paint.UNDERLINE_TEXT_FLAG) > 0){
                    newPostDesc.setPaintFlags(newPostDesc.getPaintFlags() & (~ Paint.UNDERLINE_TEXT_FLAG));
                }
                else{
                    newPostDesc.setPaintFlags(newPostDesc.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                }
            }
        });


        leftBtn.setOnClickListener(new View.OnClickListener(){
            int counter = 0;
            @Override
            public void onClick(View view){
                counter++;

                if(counter%2==0) leftBtn.setBackgroundColor(Color.WHITE);
                else leftBtn.setBackgroundColor(Color.LTGRAY);
                newPostDesc.setGravity(Gravity.LEFT);
            }
        });



        centerBtn.setOnClickListener(new View.OnClickListener(){
            int counter = 0;
            @Override
            public void onClick(View view){
                counter++;

                if(counter%2==0) centerBtn.setBackgroundColor(Color.WHITE);
                else centerBtn.setBackgroundColor(Color.LTGRAY);
                newPostDesc.setGravity(Gravity.CENTER_HORIZONTAL);

            }
        });



        rightBtn.setOnClickListener(new View.OnClickListener(){
            int counter = 0;
            @Override
            public void onClick(View view){
                counter++;

                if(counter%2==0) rightBtn.setBackgroundColor(Color.WHITE);
                else rightBtn.setBackgroundColor(Color.LTGRAY);

                newPostDesc.setGravity(Gravity.RIGHT);
            }
        });


        String [] categoryArray = new String[] {"Gifts", "Services", "Handmade", "cat4", "cat5", "cat6"};
        boolean [] checkedCategoriesArray = new boolean[]{
                false, false, false, false, false, false, false
        };

        selectCategoryBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                finCatList.clear();
                AlertDialog.Builder builder = new AlertDialog.Builder(AddListingActivity.this);

                final List<String> categoryList = Arrays.asList(categoryArray);
                builder.setTitle("Select Categories");
                //builder.setIcon(R.drawable.);

                builder.setMultiChoiceItems(categoryArray, checkedCategoriesArray, new DialogInterface.OnMultiChoiceClickListener(){
                    public void onClick(DialogInterface dialogInterface, int which, boolean isChcked){
                        checkedCategoriesArray[which] = isChcked;
                        String currentItem = categoryList.get(which);
                        Toast.makeText(AddListingActivity.this, currentItem+" "+ isChcked, Toast.LENGTH_LONG);
                    }
                });

                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which){
                        for(int i=0; i<checkedCategoriesArray.length; i++){
                            if(checkedCategoriesArray[i]){
                                finCatList.add(categoryArray[i]);
                            }
                        }
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        newPostHashtags.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(MotionEvent.ACTION_UP == event.getAction() && hashtagEditable==true) {
                    newPostHashtags.setText("#");
                    hashtagEditable = false;
                }

                return false;
            }
        });

        uploadPic.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                ProfileImageClick();

                // opens gallery when button is clicked after checking if app has access to user files
//                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
//                    if(ContextCompat.checkSelfPermission(AddListingActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
//                        ActivityCompat.requestPermissions(AddListingActivity.this, new String[] {android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
//                    }
//                    else{
//                        postImageUris.clear();
//                        openGallery();
//                    }
//                }
            }
        });


        uploadBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View view){
                if(newPostDesc.getText().toString().isEmpty()){
                    Toast.makeText(AddListingActivity.this, "Please fill the description for your listing.", Toast.LENGTH_LONG).show();
                }
                else if(newPostTitle.getText().toString().isEmpty()){
                    Toast.makeText(AddListingActivity.this, "Please set the title for your listing.", Toast.LENGTH_LONG).show();
                }
                else if (setPrice.getText().toString().isEmpty()){
                    Toast.makeText(AddListingActivity.this, "Please set the price for your listing.", Toast.LENGTH_LONG).show();
                }
                else if(finCatList.isEmpty()){
                    Toast.makeText(AddListingActivity.this, "Please select the categories for your listing.", Toast.LENGTH_LONG).show();
                }
                else if(finCatList.size()>3){
                    Toast.makeText(AddListingActivity.this, "You may only select upto 3 categories.", Toast.LENGTH_LONG).show();
                }
                else if(deliveryPolicies.getText().toString().isEmpty()){
                    Toast.makeText(AddListingActivity.this, "Please fill the delivery policies section.", Toast.LENGTH_LONG).show();
                }
                else if(returnExchangePolicies.getText().toString().isEmpty()){
                    Toast.makeText(AddListingActivity.this, "Please fill the return / exchange policies section.", Toast.LENGTH_LONG).show();
                }
                else if(postImageUris.isEmpty() && postImageUri == null){
                    Toast.makeText(AddListingActivity.this, "Please add at least one photo of your listing.", Toast.LENGTH_LONG).show();
                }
                else{
                    String title = newPostTitle.getText().toString();
                    int price = Integer.parseInt(setPrice.getText().toString());
                    String delivery = deliveryPolicies.getText().toString();
                    String returnExchange = returnExchangePolicies.getText().toString();
                    // text (hashtags optional) and picture has been chosen; no empty or null value
                    uploadProgress.setVisibility(View.VISIBLE);

                    getUserPostNumPost();

                    if(more_than_one_img){
//                        Map<String, Object> objectMap = new HashMap<>();
//                        objectMap.put("name", title);
//                        objectMap.put("price", price);
//                        objectMap.put("photos", postImageUris);
//                        objectMap.put("desc", newPostDesc.getText().toString());
//                        objectMap.put("categories", finCatList);
//                        objectMap.put("delivery", delivery);
//                        objectMap.put("return_exchange", returnExchange);
//                        objectMap.put("hashtags", newPostHashtags.getText().toString());
//                        objectMap.put("user_id", "REPLACETHISREPLACETHID");
                        ///////****************************************????????????//////////////
                        ///////****************************************????????????//////////////
                        //Above "ProfilePageActivity.class" should be changed to profileFramgent (next time)


                        DocumentReference newListingRef = firebaseFirestore.collection("Users").document("REPLACETHISREPLACETHID").collection("Listings").document();
                        Listing newListing = new Listing(
                                "REPLACETHISREPLACETHID",
                                returnExchange,
                                price,
                                postImageUris,
                                title,
                                newPostHashtags.getText().toString(),
                                newPostDesc.getText().toString(),
                                delivery,
                                "temporary username",
                                newListingRef.getId(),
                                System.currentTimeMillis(),
                                finCatList);

                        newListingRef.set(newListing)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(AddListingActivity.this, "Successfully uploaded ic_dm.", Toast.LENGTH_LONG).show();
                                    finish();
                                    uploadProgress.setVisibility(View.INVISIBLE);
                                })
                                .addOnFailureListener(e ->
                                        Toast.makeText(AddListingActivity.this, "Failed to upload ic_dm.", Toast.LENGTH_LONG)
                                                .show());

                        firebaseFirestore
                                .collection("Users")
                                .document("REPLACETHISREPLACETHID")
                                .update("number_of_listings", FieldValue.increment(1));


//                        firebaseFirestore.collection("Users").document("REPLACETHISREPLACETHID").collection("Listings").document((numPostArray[0] + 1) + " " + dateToStr).set(objectMap)
//                        firebaseFirestore.collection("Users").document("REPLACETHISREPLACETHID").collection("Listings").document(dateToStr).set(objectMap)
//                                .addOnSuccessListener(aVoid -> {
//                                    Toast.makeText(AddListingActivity.this, "Successfully uploaded ic_dm.", Toast.LENGTH_LONG).show();
//                                    finish();
//                                    uploadProgress.setVisibility(View.INVISIBLE);
//                                })
//                                .addOnFailureListener(e ->
//                                        Toast.makeText(AddListingActivity.this, "Failed to upload ic_dm.", Toast.LENGTH_LONG)
//                                                .show());
//
//                        firebaseFirestore
//                                .collection("Users")
//                                .document("REPLACETHISREPLACETHID")
//                                .update("number_of_listings", FieldValue.increment(1));
                    }else{
                        Log.d("PostImg", "DDDDDDDDDDDDDDD" + postImageUris.toString());
                        DocumentReference newListingRef = firebaseFirestore.collection("Users").document("REPLACETHISREPLACETHID").collection("Listings").document();
                        Listing newListing = new Listing(
                                "REPLACETHISREPLACETHID",
                                returnExchange,
                                price,
                                postImageUris,
                                title,
                                newPostHashtags.getText().toString(),
                                newPostDesc.getText().toString(),
                                delivery,
                                "temporary username",
                                newListingRef.getId(),
                                System.currentTimeMillis(),
                                finCatList);

                        newListingRef.set(newListing)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(AddListingActivity.this, "Successfully uploaded ic_dm.", Toast.LENGTH_LONG).show();
                                    finish();
                                    uploadProgress.setVisibility(View.INVISIBLE);
                                })
                                .addOnFailureListener(e ->
                                        Toast.makeText(AddListingActivity.this, "Failed to upload ic_dm. awd", Toast.LENGTH_LONG)
                                                .show());

                        firebaseFirestore
                                .collection("Users")
                                .document("REPLACETHISREPLACETHID")
                                .update("number_of_posts", FieldValue.increment(1));

//                        StorageReference filePath = storageReference.child("Images").child("REPLACETHISREPLACETHID").child(dateToStr); // currently one post per user; will change numbering system according to current number of posts
//
//                        UploadTask objectUploadTask = filePath.putFile(postImageUri);
//                        objectUploadTask.continueWithTask(task -> {
//                            if(!task.isSuccessful()){
//                                throw task.getException();
//                            }
//                            return filePath.getDownloadUrl();
//                        }).addOnCompleteListener(task -> {
//                            if(task.isSuccessful()){
//                                Map<String, Object> objectMap = new HashMap<>();
//                                postImageUris.add(task.getResult().toString());
//                                objectMap.put("name", title);
//                                objectMap.put("price", price);
//                                objectMap.put("photos", postImageUris);
//                                objectMap.put("desc", newPostDesc.getText().toString());
//                                objectMap.put("categories", finCatList);
//                                objectMap.put("delivery", delivery);
//                                objectMap.put("return_exchange", returnExchange);
//                                objectMap.put("hashtags", newPostHashtags.getText().toString());
//                                objectMap.put("user_id", "REPLACETHISREPLACETHID");
//
//                                firebaseFirestore.collection("Users").document("REPLACETHISREPLACETHID").collection("Listings").document((numPostArray[0] + 1) + " " + dateToStr).set(objectMap)
//                                        .addOnSuccessListener(aVoid -> {
//                                            Toast.makeText(AddListingActivity.this, "Successfully uploaded ic_dm.", Toast.LENGTH_LONG).show();
//                                            finish();
//                                            uploadProgress.setVisibility(View.INVISIBLE);
//                                        })
//                                        .addOnFailureListener(e ->
//                                                Toast.makeText(AddListingActivity.this, "Failed to upload ic_dm.", Toast.LENGTH_LONG)
//                                                        .show());
//                            }
//                            else if(!task.isSuccessful()) {
//                                Toast.makeText(AddListingActivity.this, task.getException().toString(), Toast.LENGTH_LONG).show();
//                            }
//                        });
//                        firebaseFirestore
//                                .collection("Users")
//                                .document("REPLACETHISREPLACETHID")
//                                .update("number_of_listings",  FieldValue.increment(1));
                    }}
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

//        Intent intent = new Intent();
//        intent.setType("ic_dm/*");
//        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent,"Select Picture"), 1);
    }

    public void getUserPostNumPost(){
        DocumentReference docRef = firebaseFirestore.collection("Users").document("AWDAWD");

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("LOG: ", "DocumentSnapshot data: " + document.getData());
//                        String name = "number_of_posts";
                        String name = "number_of_listings";
//                        YJ - changed number of posts to number of listings
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
        for (int i = 0; i < 5; i++) {
            imageURIs[i] = "";
        }
//        if (requestCode == REQUEST_IMAGE) {
//            if (resultCode == Activity.RESULT_OK) {
//                Uri uri = data.getParcelableExtra("path");
//                try {
//                    // You can update this bitmap to your server
//                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
//                    Log.d("uri ",uri.toString());
//                    // loading profile image from l
//                    imageURIs[0]=uri.toString();
//                    carouselView.setImageListener(imageListener);
//                    carouselView.setPageCount(1);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }else{
            if (data != null) {
//                if (data.getClipData() != null) {
//                    more_than_one_img = true;
//                    int totalItemsSelected = data.getClipData().getItemCount();
//                    if (totalItemsSelected > 5) {
//                        Toast.makeText(AddListingActivity.this, "You can only upload 5 images at a time. The first 5 have been automatically selected.", Toast.LENGTH_LONG).show();
//                    }
//
//                    for (int i = 0; i < (totalItemsSelected > 5 ? 5 : totalItemsSelected); i++) {
//                        Uri fileUri = data.getClipData().getItemAt(i).getUri();
//
//                        String fileName = getFileName(fileUri);
//
//                        StorageReference fileToUpload = storageReference.child("Images").child("REPLACETHISREPLACETHID").child(fileName); // randomize name
//                        fileToUpload.putFile(fileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                            @Override
//                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                                storageReference.child("Images").child("REPLACETHISREPLACETHID").child(fileName).getDownloadUrl().addOnCompleteListener(task -> {
//                                    if (task.isSuccessful()) {
//                                        postImageUris.add(task.getResult().toString());
//                                        Log.d("LOG: ", "postImageUris " + task.getResult().toString());
//
//                                        for (int i = 0; i < postImageUris.size(); i++) {
//                                            imageURIs[i] = postImageUris.get(i);
//                                        }
//                                        carouselView.setVisibility(View.VISIBLE);
//                                        uploadPic.setVisibility(View.GONE);
//
//                                        int len = 0;
//                                        for (int i = 0; i < postImageUris.size(); i++) {
//                                            if (!imageURIs[i].equals("")) {
//                                                len++;
//                                            }
//                                        }
//                                        carouselView.setImageListener(imageListener);
//                                        carouselView.setPageCount(len);
//                                    }
//                                });
//
//                            }
//                        }).addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Toast.makeText(AddListingActivity.this, "failed", Toast.LENGTH_LONG).show();
//                            }
//                        });
//                    }
//                } else {
                    if (data != null) {
                        Uri fileUri = data.getParcelableExtra("path");
                        String fileName = getFileName(fileUri);
//                        Uri fileUri = data.getData();
//                        String fileName = getFileName(fileUri);

                        StorageReference fileToUpload = storageReference.child("Images").child("777").child(fileName); // randomize name
                        fileToUpload.putFile(fileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                storageReference.child("Images").child("REPLACETHISREPLACETHID").child(fileName).getDownloadUrl().addOnCompleteListener(task -> {
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
                                Toast.makeText(AddListingActivity.this, "failed", Toast.LENGTH_LONG).show();
                            }
                        });


    //                    postImageUri = data.getData();
    //                    imageURIs[0] = postImageUri.toString();
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
//            }
//        }
    }


    private final TextWatcher hashtagWatcher = new TextWatcher() {
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


    //ImagePicker Code

    void ProfileImageClick() {
        // opens gallery when button is clicked after checking if app has access to user files
//                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
//                    if(ContextCompat.checkSelfPermission(AddListingActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
//                        ActivityCompat.requestPermissions(AddListingActivity.this, new String[] {android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
//                    }
//                    else{
//                        postImageUris.clear();
//                        openGallery();
//                    }
//                }

        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            showImagePickerOptions();
                        }
                        //Todo: need to account for possibility that not all permissions are granted (go to Dexter docs and implement the snackbar)
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    private void showImagePickerOptions() {
        ImagePickerActivity.showImagePickerOptions(this, new ImagePickerActivity.PickerOptionListener() {
            @Override
            public void onTakeCameraSelected() {
                launchCameraIntent();
            }

            @Override
            public void onChooseGallerySelected() {
                launchGalleryIntent();
            }
        });
    }

    private void launchCameraIntent() {
        Intent intent = new Intent(this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);

        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000);

        startActivityForResult(intent, REQUEST_IMAGE);
    }

    private void launchGalleryIntent() {
        Intent intent = new Intent(this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        startActivityForResult(intent, REQUEST_IMAGE);
    }



}

