package com.example.artwokmabel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class HomePageActivity extends AppCompatActivity {

    private ImageButton profile_button;
    private EditText searchBar;
    private Button searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);

        profile_button = findViewById(R.id.profile_button);
        searchBar = findViewById(R.id.et_mainsearchbar);
        searchButton = findViewById(R.id.btn_mainsearch);

        profile_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(getApplicationContext(), ProfilePageActivity.class);
                startActivity(intent);
            }
        });
        searchBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(getApplicationContext(), SearchDefaultActivity.class);
                startActivity(intent);
            }
        });
    }
}
