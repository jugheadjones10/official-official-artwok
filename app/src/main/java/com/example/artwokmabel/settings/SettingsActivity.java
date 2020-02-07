package com.example.artwokmabel.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.artwokmabel.rubbish.LoginActivityOld;
import com.example.artwokmabel.models.SettingsModel;
import com.example.artwokmabel.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    RecyclerView recyclerView;

    ArrayList<SettingsModel> settingsModels;
    SettingsAdapter settingsAdapter;

    String[] settings = {"Edit Personal Details",
            "What is Artwok? FAQ",
            "Contact/Service Support",
            "Terms of Use",
            "Log Out",
            "Deactivate Account Permanently"};
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        listView = findViewById(R.id.settings_list);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (SettingsActivity.this, android.R.layout.simple_list_item_1, settings);

        listView.setAdapter(adapter);

        // Log Out Function
        firebaseAuth = FirebaseAuth.getInstance();

        // implement setOnClick on listView
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // when logout(4) item is clicked
                if(position == 4) {
                    firebaseAuth.signOut();
                    finishAffinity();
                    startActivity(new Intent(SettingsActivity.this, LoginActivityOld.class));
                }
            }
        });

        recyclerView = findViewById(R.id.settings_recyclerview);

        // create Integer array
        Integer[] langLogo = {R.drawable.chocolate_cake, R.drawable.handmade_jewelry, R.drawable.tiger};

        // create String array
        String[] langName = {"Chocolate Cake", "Handmade Jewelry", "Tiger"};

        // Initialize ArrayList
        settingsModels = new ArrayList<>();
        for(int i = 0; i < langLogo.length; i++) {
            SettingsModel model = new SettingsModel(langLogo[i], langName[i]);
            settingsModels.add(model);
        }

        // Design Horizontal Layout
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                SettingsActivity.this, LinearLayoutManager.HORIZONTAL, false
        );
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        // Initialize SettingsAdapter
        settingsAdapter = new SettingsAdapter(SettingsActivity.this, settingsModels);
        // Set SettingsAdapter to Recyclerview
        recyclerView.setAdapter(settingsAdapter);

    }
}

