package com.example.artwokmabel.login;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.artwokmabel.R;
import com.example.artwokmabel.databinding.ActivityForgotPasswordBinding;
import com.example.artwokmabel.repos.FirestoreRepo;

public class ForgotPasswordActivity extends AppCompatActivity {
    ActivityForgotPasswordBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_forgot_password);
        binding.setOnresetpasswordclicked(new OnResetPasswordClicked());

        if(getIntent().getStringExtra("email") != null){
            binding.emailForgotPassword.setText(getIntent().getStringExtra("email"));
            binding.emailForgotPassword.setEnabled(false);
        }else{
            binding.emailForgotPassword.requestFocus();
        }

        setSupportActionBar(binding.zeroUiToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public class OnResetPasswordClicked{
        public void onResetPasswordClicked(){

            FirestoreRepo.getInstance().sendResetPasswordEmail(binding.emailForgotPassword.getText().toString());

            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
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
