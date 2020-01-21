package com.example.artwokmabel.auth;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.artwokmabel.R;
import com.example.artwokmabel.databinding.ActivityCreateAccountUsernameBinding;

public class CreateAccountUsernameActivity extends AppCompatActivity {
    private ActivityCreateAccountUsernameBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_account_username);
        binding.usernameEditText.requestFocus();
        binding.setOnnextclicked(new OnUsernameNextClicked());

        setSupportActionBar(binding.zeroUiToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public class OnUsernameNextClicked {
        public void onUsernameNextClicked(){
            Intent intent = new Intent(getApplicationContext(), CreateAccountPasswordActivity.class);

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
