package com.example.artwokmabel.auth;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.artwokmabel.R;
import com.example.artwokmabel.Repositories.FirestoreRepo;
import com.example.artwokmabel.databinding.ActivityCreateAccountUsernameBinding;

public class CreateAccountUsernameActivity extends AppCompatActivity {
    private ActivityCreateAccountUsernameBinding binding;
    private String username;
    private static CreateAccountUsernameActivity instance = null;

    public static CreateAccountUsernameActivity getInstance(){
        return instance;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_account_username);
        binding.usernameEditText.requestFocus();
        binding.setOnnextclicked(new OnUsernameNextClicked());
        binding.progressBar.setVisibility(View.GONE);

        setSupportActionBar(binding.zeroUiToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public class OnUsernameNextClicked {
        public void onUsernameNextClicked(){

            username = binding.usernameEditText.getText().toString().trim();

            if(username.length() == 0) {
                binding.usernameEditText.setError("Please enter a username");
            }else{
                binding.progressBar.setVisibility(View.VISIBLE);
                FirestoreRepo.getInstance().isUsernameDuplicate(username);
            }
        }
    }

    public void isUsernameDuplicateCallback(boolean isUsernameDuplicateDatabase){
        binding.progressBar.setVisibility(View.GONE);

        if(isUsernameDuplicateDatabase){
            binding.usernameEditText.setError("Sorry, this username already exists");
        }else{
            Intent intent = new Intent(getApplicationContext(), CreateAccountPasswordActivity.class);

            intent.putExtra("email", getIntent().getStringExtra("email"));
            intent.putExtra("username", username);

            // create an animation effect sliding from left to right
            ActivityOptions activityOptions = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                activityOptions = ActivityOptions.makeCustomAnimation(binding.getRoot().getContext(), R.anim.fromright,R.anim.toleft);
                startActivity(intent,activityOptions.toBundle());
            } else {
                startActivity(intent);
            }
        }
    }

}
