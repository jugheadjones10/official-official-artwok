package com.example.artwokmabel.profile.uploadpost;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.artwokmabel.HomePageActivity;
import com.example.artwokmabel.R;
import com.example.artwokmabel.databinding.ActivityUploadPostBinding;
import com.example.artwokmabel.repos.FirestoreRepo;
import com.google.firebase.auth.FirebaseAuth;

public class UploadPostActivity extends AppCompatActivity {

    private ActivityUploadPostBinding binding;
    private FirebaseAuth mAuth;
    private static UploadPostActivity instance;

    public static UploadPostActivity getInstance(){
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;

        binding = DataBindingUtil.setContentView(this, R.layout.activity_upload_post);

        binding.setOnpostupload(new OnPostUpload());
        mAuth = FirebaseAuth.getInstance();
    }

    public void onPostUploaded(){
        Intent intent = new Intent(this, HomePageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public class OnPostUpload{
        public void onPostUpload(){
            String postText = binding.postText.getText().toString();
            FirestoreRepo.getInstance().uploadNewPost(postText, mAuth.getCurrentUser().getUid(), UploadPostActivity.this);
        }
    }
}