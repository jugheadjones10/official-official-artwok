package com.example.artwokmabel.login;


import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.example.artwokmabel.R;
import com.example.artwokmabel.databinding.FragmentCreateAccountEmailBinding;
import com.example.artwokmabel.databinding.FragmentCreateAccountUsernameBinding;
import com.example.artwokmabel.repos.FirestoreRepo;

public class CreateAccountUsernameFragment extends Fragment {

    private FragmentCreateAccountUsernameBinding binding;
    private RegistrationViewModel viewModel;
    private NavController navController;
    private AppBarConfiguration appBarConfiguration;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_create_account_username, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(RegistrationViewModel.class);
        navController = Navigation.findNavController(view);

        binding.usernameEditText.requestFocus();
        InputMethodManager imm = (InputMethodManager)
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(binding.usernameEditText, InputMethodManager.SHOW_IMPLICIT);
        binding.setCreateAccountUsernameFragment(this);
        binding.progressBar.setVisibility(View.GONE);

        ((AppCompatActivity)requireActivity()).setSupportActionBar(binding.zeroUiToolbar);
        ((AppCompatActivity)requireActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        appBarConfiguration =
                new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupWithNavController(
                binding.zeroUiToolbar, navController, appBarConfiguration);

        viewModel.getRegistrationState().observe(getViewLifecycleOwner(), state -> {
            binding.progressBar.setVisibility(View.GONE);
            switch (state) {
                case DUPLICATE_USERNAME:
                    binding.usernameEditText.setError("Sorry, this username already exists");
                    break;
                case COLLECT_PASSWORD:
                    navController.navigate(R.id.action_createAccountUsernameFragment_to_createAccountPasswordFragment);
                    break;
            }
        });

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                viewModel.userCancelledRegistration();
                navController.popBackStack();
            }
        });
    }

    public void onUsernameNextClicked(){
        String username = binding.usernameEditText.getText().toString().trim();
        if(username.length() == 0) {
            binding.usernameEditText.setError("Please enter a username");
        }else{
            binding.progressBar.setVisibility(View.VISIBLE);
            viewModel.collectUserUsername(username);
        }
    }

    public void onLoginClicked(){
        navController.navigate(R.id.loginFragment);
    }

}
