package com.example.artwokmabel.profile.settings;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.artwokmabel.HomePageActivity;
import com.example.artwokmabel.R;
import com.example.artwokmabel.databinding.FragmentSetIntroBinding;
import com.example.artwokmabel.databinding.FragmentSetUsernameBinding;
import com.example.artwokmabel.repos.FirestoreRepo;

public class SetIntroFragment extends Fragment {

    private FragmentSetIntroBinding binding;
    private String intro;
    private NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Uncomment the below once intros are implemented
        intro = SetIntroFragmentArgs.fromBundle(getArguments()).getIntro();
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_set_intro, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        binding.progressBar.setVisibility(View.GONE);
        binding.setSetIntroFragment(this);

        binding.introEditText.setText(intro);
        binding.introEditText.requestFocus();

        ((AppCompatActivity)requireActivity()).setSupportActionBar(binding.settingsToolbar);
        ((AppCompatActivity)requireActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    public void onTickButtonClicked(){

        intro = binding.introEditText.getText().toString().trim();

        if(intro.length() == 0) {
            binding.introEditText.setError("Please enter an introduction");
        }else{
            binding.progressBar.setVisibility(View.VISIBLE);

            new ViewModelProvider(requireActivity()).get(SettingsActivityViewModel.class)
                    .updateUserIntroduction(binding.introEditText.getText().toString());

            //Put the below in a callback method like in SetUsernameFragment once intros are implemented
            binding.progressBar.setVisibility(View.GONE);
            Toast.makeText(requireActivity(), "Intro name change successful", Toast.LENGTH_SHORT).show();
            navController.navigateUp();
        }
    }
}
