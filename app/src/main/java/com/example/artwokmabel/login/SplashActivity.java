package com.example.artwokmabel.login;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.artwokmabel.BuildConfig;
import com.example.artwokmabel.R;
import com.example.artwokmabel.chat.personalchat.ChatActivity;
import com.example.artwokmabel.homepage.listing.ListingActivity;
import com.example.artwokmabel.homepage.post.PostActivity;
import com.example.artwokmabel.models.Listing;
import com.example.artwokmabel.models.MainPost;
import com.example.artwokmabel.repos.FirestoreRepo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

public class SplashActivity extends AppCompatActivity {

    private LinearLayout linearLayout;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                String value = getIntent().getExtras().getString(key);
                Log.d("intenthunter", "Key: " + key + " Value: " + value);

                if(key.equals("type")){
                    if(value.equals("message")){
                        Intent intent = new Intent(this, ChatActivity.class);

                        intent.putExtra("message_following_id", getIntent().getStringExtra("fromId"));
                        intent.putExtra("message_following_username", getIntent().getStringExtra("fromUsername"));
                        intent.putExtra("message_following_profile_img", getIntent().getStringExtra("fromProfileUrl"));

                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();

                    }else if(value.equals("newListing")){
                        Intent intent = new Intent(this, ListingActivity.class);

                        class OnListingRetrieved implements FirestoreRepo.ListingRetrieved {
                            public void onListingRetrieved (Listing listing){
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.putExtra("listing", listing);
                                startActivity(intent);
                                finish();
                            }
                        }

                        FirestoreRepo.getInstance().getListing(getIntent().getStringExtra("listingId"),
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

                        Log.d("getlistingconfusion", getIntent().getStringExtra("postId"));

                        FirestoreRepo.getInstance().getPost(getIntent().getStringExtra("postId"),
                                new OnPostRetrieved()
                        );
                    }
                }
            }
        }

        linearLayout = findViewById(R.id.splashactivity_linearlayout);
    }

    @Override protected void onResume() {

        super.onResume();

        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build();
        mFirebaseRemoteConfig.setConfigSettings(configSettings);
        mFirebaseRemoteConfig.setDefaults(R.xml.default_config);

        mFirebaseRemoteConfig.fetch(0)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            // Once the config is successfully fetched it must be activated before newly fetched
                            // values are returned.
                            mFirebaseRemoteConfig.activateFetched();
                        } else {

                        }
                        displayMessage();
                    }
                });
    }

    void displayMessage() {
        String splash_background = mFirebaseRemoteConfig.getString("splash_background");
        boolean caps = mFirebaseRemoteConfig.getBoolean("splash_message_caps");
        String splash_message = mFirebaseRemoteConfig.getString("splash_message");

        linearLayout.setBackgroundColor(Color.parseColor(splash_background));

        if(caps){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(splash_message).setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            });

            builder.create().show();

        }else {
            startActivity(new Intent(this, LoginOptionsActivity.class));
        }

    }


}
