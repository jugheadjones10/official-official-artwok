package com.example.artwokmabel.homepage.fragments.requestspagestuff;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.artwokmabel.R;
import com.example.artwokmabel.Repositories.FirestoreRepo;
import com.example.artwokmabel.databinding.ActivityUploadListing3Binding;
import com.example.artwokmabel.homepage.callbacks.ImagePickerCallback;
import com.example.artwokmabel.homepage.homepagestuff.HomePageActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.ImageClickListener;
import com.synnapps.carouselview.ImageListener;

import java.util.ArrayList;
import java.util.Random;

public class UploadRequestActivity extends AppCompatActivity {


    private StorageReference storageReference;
    private FirebaseFirestore firebaseFirestore;
    private Uri postImageUri = null;
    private ArrayList<String> postImageUris = new ArrayList<String>();
    private boolean more_than_one_img = false;
    private ArrayList<String> finCatList = new ArrayList<String>();
    private HorizontalScrollView colorPalette;
    private boolean hashtagEditable = true;
    private static UploadRequestActivity instance;

    //Todo: edit ucrop library to allow for editing of multiple images
    int numPostArray[] = {0};

    String[] imageURIs = {"","","","",""};

    private ActivityUploadListing3Binding binding;
    private String currentUserId;
    private FirebaseAuth mAuth;
    private InidvAddRequestPagerAdapter adapter;

    public static UploadRequestActivity getInstance(){
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        postImageUris.clear();

        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_upload_listing_3);

        instance = this;
        adapter = new InidvAddRequestPagerAdapter(this);
        binding.pager.setAdapter(adapter);

        new TabLayoutMediator(binding.tabLayout, binding.pager,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(TabLayout.Tab tab, int position) {
                        if(position == 0){
                            tab.setText("Description");
                        }else if(position == 1){
                            tab.setText("Details");
                        }else{
                            tab.setText("FAQ");
                        }
                    }
                }
        ).attach();

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();

        // ini
        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();

        if(imageURIs[0].equals("")){
            binding.carouselView.setVisibility(View.GONE);
            binding.uploadPicL.setVisibility(View.VISIBLE);

        }
        else {
            binding.carouselView.setVisibility(View.VISIBLE);
            binding.uploadPicL.setVisibility(View.GONE);

            binding.carouselView.setImageListener(imageListener);
            binding.carouselView.setPageCount(imageURIs.length);
        }

        binding.carouselView.setImageClickListener(new ImageClickListener() {
            @Override
            public void onClick(int position) {
                new ImagePickerCallback(UploadRequestActivity.this).onImagePickerClicked();
            }
        });

        String [] categoryArray = new String[] {"Gifts", "Services", "Handmade", "cat4", "cat5", "cat6"};
        boolean [] checkedCategoriesArray = new boolean[]{
                false, false, false, false, false, false, false
        };

//        binding.selectCategoryL.setOnClickListener(new View.OnClickListener(){
//            public void onClick(View view){
//                finCatList.clear();
//                AlertDialog.Builder builder = new AlertDialog.Builder(UploadRequestActivity.this);
//
//                final List<String> categoryList = Arrays.asList(categoryArray);
//                builder.setTitle("Select Categories");
//                //builder.setIcon(R.drawable.);
//
//                builder.setMultiChoiceItems(categoryArray, checkedCategoriesArray, new DialogInterface.OnMultiChoiceClickListener(){
//                    public void onClick(DialogInterface dialogInterface, int which, boolean isChcked){
//                        checkedCategoriesArray[which] = isChcked;
//                        String currentItem = categoryList.get(which);
//                        Toast.makeText(UploadRequestActivity.this, currentItem+" "+ isChcked, Toast.LENGTH_LONG);
//                    }
//                });
//
//                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener(){
//                    public void onClick(DialogInterface dialog, int which){
//                        for(int i=0; i<checkedCategoriesArray.length; i++){
//                            if(checkedCategoriesArray[i]){
//                                finCatList.add(categoryArray[i]);
//                            }
//                        }
//                    }
//                });
//
//                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                });
//                AlertDialog dialog = builder.create();
//                dialog.show();
//            }
//        });

        binding.uploadPicL.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                new ImagePickerCallback(UploadRequestActivity.this).onImagePickerClicked();
            }
        });


        binding.uploadBtnL.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View view){

                String postDesc = AddRequestDescFragment.getInstance().getDesc();
                String postTitle = AddRequestDescFragment.getInstance().getTitle();

                String category = AddRequestDetailsFragment.getInstance().getCategory();
                long budget = AddRequestDetailsFragment.getInstance().getBudget();

                if(postDesc.isEmpty()){
                    Toast.makeText(UploadRequestActivity.this, "Please fill the description for your listing.", Toast.LENGTH_LONG).show();
                }
                else if(postTitle.isEmpty()){
                    Toast.makeText(UploadRequestActivity.this, "Please fill the title for your listing.", Toast.LENGTH_LONG).show();
                }
                else if(category == null){
                    Toast.makeText(UploadRequestActivity.this, "Please select the category for your request.", Toast.LENGTH_LONG).show();
                }
                else if(postImageUris.isEmpty() && postImageUri == null){
                    Toast.makeText(UploadRequestActivity.this, "Please add at least one photo of your listing.", Toast.LENGTH_LONG).show();
                }else{

//                    int price = Integer.parseInt(setPrice.getText().toString());
//                    String delivery = deliveryPolicies.getText().toString();
//                    String returnExchange = returnExchangePolicies.getText().toString();
                    // text (hashtags optional) and picture has been chosen; no empty or null value


                    binding.uploadProgressL.setVisibility(View.VISIBLE);
                    getUserPostNumPost();
//                    if(more_than_one_img){
//
//                        DocumentReference newListingRef = firebaseFirestore.collection("Users").document(LoginActivity.uid).collection("Listings").document();
//                        Listing newListing = new Listing(
//                                LoginActivity.uid,
//                                returnExchange,
//                                price,
//                                postImageUris,
//                                title,
//                                newPostHashtags.getText().toString(),
//                                newPostDesc.getText().toString(),
//                                delivery,
//                                "temporary username",
//                                newListingRef.getId(),
//                                System.currentTimeMillis(),
//                                finCatList);
//
//                        newListingRef.set(newListing)
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
//                                .document(LoginActivity.uid)
//                                .update("number_of_listings", FieldValue.increment(1));
//
//                    }else{

                    FirestoreRepo.getInstance().uploadNewRequest(postTitle, postDesc, category, budget, currentUserId, postImageUris, UploadRequestActivity.this);
                }}
//            }
        });
    }

    public void onUploaded(){
        binding.uploadProgressL.setVisibility(View.INVISIBLE);

        Intent intent = new Intent(this, HomePageActivity.class);
        startActivity(intent);
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
//        DocumentReference docRef = firebaseFirestore.collection("Users").document(currentUserId);
//
//        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//                    DocumentSnapshot document = task.getResult();
//                    if (document.exists()) {
//                        Log.d("LOG: ", "DocumentSnapshot data: " + document.getData());
////                        String name = "number_of_posts";
//                        String name = "number_of_listings";
////                        YJ - changed number of posts to number of listings
//                        numPostArray[0] = ((Long) document.get(name)).intValue();
//
//                    } else {
//                        Log.d("LOG: ", "No such document");
//                        getUserPostNumPost();
//                    }
//                } else {
//                    Log.d("LOG: ", "get failed with ", task.getException());
//                }
//            }
//        });
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (int i = 0; i < 5; i++) {
            imageURIs[i] = "";
        }

        if (data != null) {
            Uri fileUri = data.getParcelableExtra("path");
            //Uri fileUri = data.getData();

            //String fileName = getFileName(fileUri);

            //StorageReference fileToUpload = storageReference.child("Images").child(currentUserId).child(fileName); // randomize name
            Random random = new Random();
            int randomNumber = random.nextInt();
            String fileName = Integer.toString(randomNumber);

            StorageReference fileToUpload = storageReference.child("Images").child(currentUserId).child(fileName); // randomize name

            fileToUpload.putFile(fileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    storageReference.child("Images").child(currentUserId).child(fileName).getDownloadUrl().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            postImageUris.add(task.getResult().toString());
                            Log.d("LOG: ", "postImageUris " + task.getResult().toString());

                            for (int i = 0; i < postImageUris.size(); i++) {
                                imageURIs[i] = postImageUris.get(i);
                            }
                            binding.carouselView.setVisibility(View.VISIBLE);
                            binding.uploadPicL.setVisibility(View.GONE);

                            int len = 0;
                            for (int i = 0; i < postImageUris.size(); i++) {
                                if (!imageURIs[i].equals("")) {
                                    len++;
                                }
                            }
                            binding.carouselView.setImageListener(imageListener);
                            binding.carouselView.setPageCount(len);
                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UploadRequestActivity.this, "failed", Toast.LENGTH_LONG).show();
                }
            });

        } else { // no pics selected
            binding.carouselView.setVisibility(View.GONE);
            binding.uploadPicL.setVisibility(View.VISIBLE);
        }
    }


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
