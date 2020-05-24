package com.example.artwokmabel.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.artwokmabel.R;
import com.example.artwokmabel.chat.personalchat.ChatActivity;
import com.example.artwokmabel.databinding.ActivityAppHostBinding;
import com.example.artwokmabel.homepage.listing.ListingActivity;
import com.example.artwokmabel.homepage.post.PostActivity;
import com.example.artwokmabel.models.Listing;
import com.example.artwokmabel.models.MainPost;
import com.example.artwokmabel.repos.FirestoreRepo;

public class AppHostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_host);

        //Check for intent so that when user clicks on push notification, goes immediately to the relevant view in the app
        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                String value = getIntent().getExtras().getString(key);
                Log.d("intenthunter", "Key: " + key + " Value: " + value);

                if(key.equals("type")){
                    if(value.equals("message")){
                        Intent intent = new Intent(this, ChatActivity.class);

                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();

                    }else if(value.equals("newListing")){

                        String listingId = getIntent().getStringExtra("listingId");

                        Intent intent = new Intent(this, ListingActivity.class);

                        class OnListingRetrieved implements FirestoreRepo.ListingRetrieved {
                            public void onListingRetrieved (Listing listing){
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.putExtra("listing", listing);
                                startActivity(intent);
                                finish();
                            }
                        }

                        FirestoreRepo.getInstance().getListing(listingId,
                                new OnListingRetrieved()
                        );

                    } else if(value.equals("newComment")){

                        Intent intent = new Intent(this, PostActivity.class);

                        class OnPostRetrieved implements FirestoreRepo.PostRetrieved {
                            public void onPostRetrieved (MainPost post){

                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.putExtra("post", post);
                                startActivity(intent);
                                finish();
                            }
                        }

                        FirestoreRepo.getInstance().getPost(getIntent().getStringExtra("postId"),
                                new OnPostRetrieved()
                        );

                    }else if(value.equals("newFollower")){

                    }
                }
            }
        }
    }
}
