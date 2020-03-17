package com.example.artwokmabel.profile.people;

import android.os.Bundle;

import com.example.artwokmabel.profile.user.ProfileFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import android.view.View;

import com.example.artwokmabel.R;

public class ViewOtherUserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_other_user);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.profile_fragment_holder, new ProfileFragment(getIntent().getStringExtra("userId")));
//        transaction.addToBackStack(null);
        transaction.commit();
    }

}
