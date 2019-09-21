package com.example.artwokmabel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ProfilePageActivity extends AppCompatActivity {

    private Button upload_post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        upload_post = findViewById(R.id.upload_button);


        upload_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(getApplicationContext(), UploadPostActivity.class);
                startActivity(intent);
            }
        });
    }
}
