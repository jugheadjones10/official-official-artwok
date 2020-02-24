package com.example.artwokmabel.profile.settings;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.artwokmabel.R;
import com.example.artwokmabel.databinding.ActivitySetUsernameBinding;
import com.example.artwokmabel.databinding.CustomSetProfileDetailsBarBinding;
import com.example.artwokmabel.login.DuplicateUsernameChecker;
import com.example.artwokmabel.repos.FirestoreRepo;

public class SetUsernameActivity extends AppCompatActivity implements DuplicateUsernameChecker {

    private ActivitySetUsernameBinding binding;
    private String username;
    private static SetUsernameActivity instance;

    public static SetUsernameActivity getInstance(){
        return instance;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        instance = this;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_set_username);
        binding.progressBar.setVisibility(View.GONE);
        binding.usernameEditText.setText(SettingsActivity.getInstance().user.getUsername());
        binding.usernameEditText.requestFocus();

        setSupportActionBar(binding.settingsToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        binding.tickButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                username = binding.usernameEditText.getText().toString().trim();
                if(username.length() == 0) {
                    binding.usernameEditText.setError("Please enter a username");
                }else{
                    binding.progressBar.setVisibility(View.VISIBLE);
                    FirestoreRepo.getInstance().isUsernameDuplicate(username, getInstance());
                }

            }
        });
    }

    @Override
    public void isUsernameDuplicateCallback(boolean isUsernameDuplicateDatabase){
        binding.progressBar.setVisibility(View.GONE);

        if(isUsernameDuplicateDatabase){
            binding.usernameEditText.setError("Sorry, this username already exists");
        }else{
            SettingsActivity.getInstance().viewModel.updateUserUsername(username);
            Toast.makeText(this, "Username change successful", Toast.LENGTH_SHORT).show();
        }
    }



}
