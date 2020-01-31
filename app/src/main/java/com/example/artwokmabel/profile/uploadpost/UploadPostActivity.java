package com.example.artwokmabel.profile.uploadpost;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.artwokmabel.R;
import com.example.artwokmabel.databinding.ActivityUploadPostBinding;

public class UploadPostActivity extends AppCompatActivity {

    private ActivityUploadPostBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_upload_post);

//        Aztec.with(binding.visual, binding.source, binding.formattingToolbar,)
//                .setImageGetter(GlideImageLoader(context))
//                .setVideoThumbnailGetter(GlideVideoThumbnailLoader(context));
    }
}