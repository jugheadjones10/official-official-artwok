package com.example.artwokmabel.auth;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.artwokmabel.R;
import com.example.artwokmabel.Repositories.FirestoreRepo;
import com.example.artwokmabel.databinding.ActivityCreateAccountPasswordBinding;
import com.example.artwokmabel.homepage.callbacks.LoginPageCallback;

public class CreateAccountPasswordActivity extends AppCompatActivity {

    private ActivityCreateAccountPasswordBinding binding;
    private static CreateAccountPasswordActivity instance = null;

    public static CreateAccountPasswordActivity getInstance(){
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        instance = this;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_account_password);
        binding.passwordEditText.requestFocus();
        binding.setOncreateacctclicked(new OnCreateAccountClicked());
        binding.progressBar.setVisibility(View.GONE);
        binding.setLoginpagecallback(new LoginPageCallback());
        binding.setContext(this);

        setSupportActionBar(binding.zeroUiToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public class OnCreateAccountClicked {
        public void onCreateAccountClicked(){

            if(binding.passwordEditText.getText().toString().length() == 0){
                binding.retypePasswordEditText.setError("Please enter a password");
            }else if(binding.passwordEditText.getText().toString().length() < 6){
                binding.retypePasswordEditText.setError("Please enter at least 6 characters");
            } else if(!binding.passwordEditText.getText().toString().equals(binding.retypePasswordEditText.getText().toString())){
                binding.retypePasswordEditText.setError("Passwords do not match");
            }else if(!binding.termsCheck.isChecked() || !binding.privacyCheck.isChecked()){
                Toast toast=Toast.makeText(getApplicationContext(),"Please agree to our Privacy Policy & Terms of Use", Toast.LENGTH_SHORT);
                //toast.setMargin(50,50);
                toast.show();
            }else{
                binding.progressBar.setVisibility(View.VISIBLE);

                FirestoreRepo.getInstance().createAccount(
                        getIntent().getStringExtra("email"),
                        getIntent().getStringExtra("username"),
                        binding.passwordEditText.getText().toString());
            }
        }
    }

    public void createAccountCallback(boolean successful){
        binding.progressBar.setVisibility(View.GONE);
        if(successful){
            Intent intent = new Intent(getApplicationContext(), LoginLoginActivity.class);
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
