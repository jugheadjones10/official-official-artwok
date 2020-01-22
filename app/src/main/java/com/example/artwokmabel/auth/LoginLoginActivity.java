package com.example.artwokmabel.auth;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.artwokmabel.R;
import com.example.artwokmabel.Repositories.FirestoreRepo;
import com.example.artwokmabel.databinding.ActivityLoginLoginBinding;
import com.example.artwokmabel.homepage.homepagestuff.HomePageActivity;

public class LoginLoginActivity extends AppCompatActivity {

    private ActivityLoginLoginBinding binding;
    private static LoginLoginActivity instance = null;

    public static LoginLoginActivity getInstance(){
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login_login);
        binding.setOnloginclicked(new OnLoginClicked());
        binding.emailEditText.requestFocus();
        binding.progressBar.setVisibility(View.GONE);

    }

    public class OnLoginClicked {
        public void onLoginClicked(){

            String email = binding.emailEditText.getText().toString().trim();
            String password = binding.passwordEditText.getText().toString().trim();

            if(email.length() == 0){
                binding.emailEditText.setError("Please enter your email");
            }else if(password.length() == 0){
                binding.passwordEditText.setError("Please enter your password");
            }else{
                binding.progressBar.setVisibility(View.VISIBLE);

                FirestoreRepo.getInstance().logIntoAccount(
                        email,
                        password
                );
            }
        }
    }

    public void loginCallback(){
        binding.progressBar.setVisibility(View.GONE);

        Intent intent = new Intent(getApplicationContext(), HomePageActivity.class);
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
