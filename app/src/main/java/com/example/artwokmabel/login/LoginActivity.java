package com.example.artwokmabel.login;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.artwokmabel.R;
import com.example.artwokmabel.databinding.ActivityLoginBinding;
import com.example.artwokmabel.HomePageActivity;
import com.example.artwokmabel.repos.FirestoreRepo;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private static LoginActivity instance = null;

    public static LoginActivity getInstance(){
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        binding.setOnloginclicked(new OnLoginClicked());
        binding.setOnforgotpasswordclicked(new OnForgotPasswordClicked());
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

    public class OnForgotPasswordClicked {
        public void onForgotPasswordClicked(){
            Intent intent = new Intent(getApplicationContext(), ForgotPasswordActivity.class);
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

    public void loginCallback(boolean successful){

        binding.progressBar.setVisibility(View.GONE);

        if(successful){
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
}
