package com.example.artwokmabel.profile.Activites;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.artwokmabel.R;

public class GroupProfileActivity extends AppCompatActivity {

    private View view;
    private Button postButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_profile);

        postButton = findViewById(R.id.btn_group_profile_post);
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(GroupProfileActivity.this, UploadGroupPostActivity.class);
                startActivity(i);
            }
        });
    }
}
