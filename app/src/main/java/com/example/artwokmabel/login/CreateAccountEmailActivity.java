package com.example.artwokmabel.login;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.artwokmabel.R;
import com.example.artwokmabel.repos.FirestoreRepo;
import com.example.artwokmabel.databinding.ActivityCreateAccountEmailBinding;
import com.example.artwokmabel.homepage.callbacks.LoginPageCallback;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateAccountEmailActivity extends AppCompatActivity {
    private ActivityCreateAccountEmailBinding binding;
    private String email;
    private static CreateAccountEmailActivity instance = null;

    public static CreateAccountEmailActivity getInstance(){
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_account_email);
        binding.emailEditText.requestFocus();
        binding.setEmailonnextclicked(new EmailOnNextClicked());
        binding.setLoginpagecallback(new LoginPageCallback());
        binding.setContext(this);
        binding.progressBar.setVisibility(View.GONE);

        setSupportActionBar(binding.zeroUiToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public class EmailOnNextClicked {
        public void emailOnNextClicked(){
            email = binding.emailEditText.getText().toString().trim();
            if(email.length() == 0){
                binding.emailEditText.setError("Please enter an email");
            }else if(!isEmailValid(email)){
                binding.emailEditText.setError("Please enter a valid email");
            }else{
                binding.progressBar.setVisibility(View.VISIBLE);
                FirestoreRepo.getInstance().isEmailDuplicate(email);
            }
        }
    }

    public void isEmailDuplicateCallback(boolean isEmailDuplicateDatabase){
        binding.progressBar.setVisibility(View.GONE);

        if(isEmailDuplicateDatabase){
            binding.emailEditText.setError("Sorry, this email already exists");
        }else{

            Intent intent = new Intent(getApplicationContext(), CreateAccountUsernameActivity.class);
            intent.putExtra("email", email);

            ActivityOptions activityOptions = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                activityOptions = ActivityOptions.makeCustomAnimation(binding.getRoot().getContext(), R.anim.fromright,R.anim.toleft);
                startActivity(intent,activityOptions.toBundle());
            } else {
                startActivity(intent);
            }
        }
    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

}
