package com.example.artwokmabel.profile.settings;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
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
    private SettingsActivityViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Uncomment the below once intros are implemented
        intro = SetIntroFragmentArgs.fromBundle(getArguments()).getIntro();
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_set_intro, container, false);

        viewModel = new ViewModelProvider(requireActivity()).get(SettingsActivityViewModel.class);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        binding.setSetIntroFragment(this);

        binding.introEditText.setText(intro);
        binding.introEditText.requestFocus();

        ((AppCompatActivity)requireActivity()).setSupportActionBar(binding.settingsToolbar);
        ((AppCompatActivity)requireActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewModel.getLoadingStatus().observe(getViewLifecycleOwner(), new Observer<SettingsActivityViewModel.IntroLoadingStatus>() {
            @Override
            public void onChanged(SettingsActivityViewModel.IntroLoadingStatus loadingStatus) {
                if(loadingStatus == SettingsActivityViewModel.IntroLoadingStatus.LOADING){
                    binding.progressBar.setVisibility(View.VISIBLE);
                }else if(loadingStatus == SettingsActivityViewModel.IntroLoadingStatus.UNSUCCESSFUL || loadingStatus == SettingsActivityViewModel.IntroLoadingStatus.BEFORELOAD){
                    binding.progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "Change of introduction not successful", Toast.LENGTH_SHORT).show();
                }else if(loadingStatus == SettingsActivityViewModel.IntroLoadingStatus.NOTLOADING){
                    binding.progressBar.setVisibility(View.GONE);
                    Toast.makeText(requireActivity(), "Intro name change successful", Toast.LENGTH_SHORT).show();
                    navController.navigateUp();
                    viewModel.setLoadingStatus(SettingsActivityViewModel.IntroLoadingStatus.BEFORELOAD);
                }
            }
        });
    }

    public void onTickButtonClicked(){
        intro = binding.introEditText.getText().toString().trim();
        if(intro.length() == 0) {
            binding.introEditText.setError("Please enter an introduction");
        }else{
            viewModel.updateUserIntroduction(binding.introEditText.getText().toString());
        }
    }
}
