package com.example.artwokmabel.profile.uploadlisting;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.artwokmabel.R;
import com.example.artwokmabel.Repositories.FirestoreRepo;
import com.example.artwokmabel.databinding.ActivityUploadListing4Binding;
import com.example.artwokmabel.homepage.callbacks.ImagePickerCallback;
import com.example.artwokmabel.homepage.fragments.requestspagestuff.AddRequestDescFragment;
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

public class UploadListingAcitvity extends AppCompatActivity {

    private StorageReference storageReference;
    private Uri postImageUri = null;
    private ArrayList<String> postImageUris = new ArrayList<String>();
    private static UploadListingAcitvity instance;
    private FirebaseFirestore db;
    public static final int REQUEST_IMAGE = 100;

    //Todo: edit ucrop library to allow for editing of multiple images
    String[] imageURIs = {"","","","",""};

    private ActivityUploadListing4Binding binding;
    private String currentUserId;
    private FirebaseAuth mAuth;
    private UploadListingPagerAdapter adapter;

    public static UploadListingAcitvity getInstance(){
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //postImageUris.clear();

        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_upload_listing_4);

        instance = this;
        adapter = new UploadListingPagerAdapter(this);
        binding.pager.setAdapter(adapter);

        new TabLayoutMediator(binding.tabLayout, binding.pager,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(TabLayout.Tab tab, int position) {
                            if(position == 0){
                                tab.setText("Description");
                            }else if(position == 1){
                                tab.setText("Details");
                            }else if(position == 2){
                                tab.setText("Delivery/Refund");
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
        db = FirebaseFirestore.getInstance();

//        if(imageURIs[0].equals("")){
            binding.carouselView.setVisibility(View.GONE);
            binding.uploadPicL.setVisibility(View.VISIBLE);
//        }
//        else {
//            binding.carouselView.setVisibility(View.VISIBLE);
//            binding.uploadPicL.setVisibility(View.GONE);
//
//            binding.carouselView.setImageListener(imageListener);
//            binding.carouselView.setPageCount(imageURIs.length);
//        }

        binding.carouselView.setImageClickListener(new ImageClickListener() {
            @Override
            public void onClick(int position) {
                new ImagePickerCallback(UploadListingAcitvity.this, REQUEST_IMAGE).onImagePickerClicked();
            }
        });

        binding.uploadPicL.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                new ImagePickerCallback(UploadListingAcitvity.this, REQUEST_IMAGE).onImagePickerClicked();
            }
        });

        binding.uploadBtnL.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View view){

                String postDesc = AddRequestDescFragment.getInstance().getDesc();
                String postTitle = AddRequestDescFragment.getInstance().getTitle();

                ArrayList<String> categories = AddListingDetailsFragment.getInstance().getCategories();
                long budget = AddListingDetailsFragment.getInstance().getBudget();

                String deliveryDetails = AddListingDeliveryRefundFragment.getInstance().getDelivery();
                String refundPolicy = AddListingDeliveryRefundFragment.getInstance().getRefund();

                if(postDesc.isEmpty()){
                    Toast.makeText(UploadListingAcitvity.this, "Please fill the description for your listing.", Toast.LENGTH_LONG).show();
                } else if(postTitle.isEmpty()){
                    Toast.makeText(UploadListingAcitvity.this, "Please fill the title for your listing.", Toast.LENGTH_LONG).show();
                }else if(deliveryDetails.isEmpty()){
                    Toast.makeText(UploadListingAcitvity.this, "Please enter delivery details.", Toast.LENGTH_LONG).show();
                } else if(refundPolicy.isEmpty()){
                    Toast.makeText(UploadListingAcitvity.this, "Please enter your refund policy", Toast.LENGTH_LONG).show();
                } else if(categories == null || categories.size() == 0){
                    Toast.makeText(UploadListingAcitvity.this, "Please select the category for your request.", Toast.LENGTH_LONG).show();
                } else if(postImageUris.isEmpty() && postImageUri == null){
                    Toast.makeText(UploadListingAcitvity.this, "Please add at least one photo of your listing.", Toast.LENGTH_LONG).show();
                }else{
                    binding.uploadProgressL.setVisibility(View.VISIBLE);
                    FirestoreRepo.getInstance().uploadNewListing(postTitle, postDesc, categories, budget, deliveryDetails, refundPolicy, currentUserId, postImageUris, UploadListingAcitvity.this);
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

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check which request we're responding to
        if (requestCode == REQUEST_IMAGE) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
//                for (int i = 0; i < 5; i++) {
//                    imageURIs[i] = "";
//                }

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

//                                    for (int i = 0; i < postImageUris.size(); i++) {
//                                        imageURIs[i] = postImageUris.get(i);
//                                    }
                                    binding.carouselView.setVisibility(View.VISIBLE);
                                    binding.uploadPicL.setVisibility(View.GONE);

//                                    int len = 0;
//                                    for (int i = 0; i < postImageUris.size(); i++) {
//                                        if (!imageURIs[i].equals("")) {
//                                            len++;
//                                        }
//                                    }
                                    binding.carouselView.setImageListener(imageListener);
                                    binding.carouselView.setPageCount(1);
                                }
                            });

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(UploadListingAcitvity.this, "failed", Toast.LENGTH_LONG).show();
                        }
                    });

                } else { // no pics selected
                    binding.carouselView.setVisibility(View.GONE);
                    binding.uploadPicL.setVisibility(View.VISIBLE);
                }
            }
        }
    }
}