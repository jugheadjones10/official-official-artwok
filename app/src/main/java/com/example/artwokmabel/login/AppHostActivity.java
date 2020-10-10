package com.example.artwokmabel.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.artwokmabel.HomePageActivity;
import com.example.artwokmabel.R;
import com.example.artwokmabel.models.Listing;
import com.example.artwokmabel.models.MainPost;
import com.example.artwokmabel.repos.FirestoreRepo;


public class AppHostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_host);

        //Check for intent so that when user clicks on push notification, goes immediately to the relevant view in the app

        if (getIntent().getBooleanExtra("LOGOUT", false)) {
            finish();
        }

        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                String value = getIntent().getExtras().getString(key);
                Log.d("intenthunter", "Key: " + key + " Value: " + value);

                if(key.equals("type")){
                    if(value.equals("message")){

                        String userId = getIntent().getStringExtra("fromId");
                        String userUsername = getIntent().getStringExtra("fromUsername");
                        String userProfileImg = getIntent().getStringExtra("fromProfileUrl");

                        Intent intent = new Intent(this, HomePageActivity.class);
                        intent.putExtra("type", "message");
                        intent.putExtra("fromId", userId);
                        intent.putExtra("fromUsername", userUsername);
                        intent.putExtra("fromProfileUrl", userProfileImg);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();


                    }else if(value.equals("newListing")){

                        String listingId = getIntent().getStringExtra("listingId");
                        Intent intent = new Intent(this, HomePageActivity.class);

                        class OnListingRetrieved implements FirestoreRepo.ListingRetrieved {
                            public void onListingRetrieved (Listing listing){

                                intent.putExtra("type", "listing");
                                intent.putExtra("listing", listing);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }
                        }

                        FirestoreRepo.getInstance().getListing(listingId,
                                new OnListingRetrieved()
                        );

                    } else if(value.equals("newComment")){
//                        Intent intent = new Intent(this, PostActivity.class);
//
//                        FirestoreRepo.getInstance().getPost(getIntent().getStringExtra("postId"),
//                            (MainPost post) -> {
//                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                intent.putExtra("post", post);
//                                startActivity(intent);
//                                finish();
//                        });
                        Intent intent = new Intent(this, HomePageActivity.class);
                        FirestoreRepo.getInstance().getPost(getIntent().getStringExtra("postId"),
                                (MainPost post) -> {
                                    intent.putExtra("type", "comment");
                                    intent.putExtra("post", post);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
                                });

                    }else if(value.equals("newFollower")){

                    }else if(value.equals("newPost")){
                        Intent intent = new Intent(this, HomePageActivity.class);

                        FirestoreRepo.getInstance().getPost(getIntent().getStringExtra("postId"),
                            (MainPost post) -> {
                                intent.putExtra("type", "post");
                                intent.putExtra("post", post);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            });
                    }
                }
            }
        }
    }
}
