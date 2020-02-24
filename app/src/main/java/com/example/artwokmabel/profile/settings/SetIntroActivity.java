package com.example.artwokmabel.profile.settings;


import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.artwokmabel.R;
import com.example.artwokmabel.databinding.ActivitySetIntroBinding;
import com.example.artwokmabel.databinding.CustomSetProfileDetailsBarBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class SetIntroActivity extends AppCompatActivity {

    private ActivitySetIntroBinding binding;
    private String intro;
    private static SetIntroActivity instance;

    public static SetIntroActivity getInstance(){
        return instance;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        instance = this;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_set_intro);
        binding.progressBar.setVisibility(View.GONE);
        //binding.introEditText.setText(SettingsActivity.getInstance().user.getIntro());
        binding.introEditText.setText("placeholder intro");
        binding.introEditText.requestFocus();

        setSupportActionBar(binding.settingsToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binding.tickButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                intro = binding.introEditText.getText().toString().trim();

                if(intro.length() == 0) {
                    binding.introEditText.setError("Please enter an introduction");
                }else{
                    binding.progressBar.setVisibility(View.VISIBLE);
                    SettingsActivity.getInstance().viewModel.updateUserIntroduction(binding.introEditText.getText().toString());
                    binding.progressBar.setVisibility(View.GONE);
                    Toast.makeText(SetIntroActivity.this, "Intro name change successful", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

}
