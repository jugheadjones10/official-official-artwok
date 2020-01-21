package com.example.artwokmabel.auth;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.artwokmabel.R;
import com.example.artwokmabel.databinding.ActivityCreateAccountPasswordBinding;

public class CreateAccountPasswordActivity extends AppCompatActivity {

    private ActivityCreateAccountPasswordBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_account_password);
        binding.passwordEditText.requestFocus();
        binding.setOncreateacctclicked(new OnCreateAccountClicked());

        setSupportActionBar(binding.zeroUiToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public class OnCreateAccountClicked {
        public void onCreateAccountClicked(){
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
