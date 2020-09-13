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
import android.widget.Toast;

import com.example.artwokmabel.R;
import com.example.artwokmabel.databinding.FragmentCreateAccountPasswordBinding;
import com.example.artwokmabel.databinding.FragmentCreateAccountUsernameBinding;
import com.example.artwokmabel.repos.FirestoreRepo;

public class CreateAccountPasswordFragment extends Fragment {

    private FragmentCreateAccountPasswordBinding binding;
    private RegistrationViewModel registrationViewModel;
    private LoginViewModel loginViewModel;
    private NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_create_account_password, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewModelProvider provider = new ViewModelProvider(requireActivity());
        registrationViewModel = provider.get(RegistrationViewModel.class);
        loginViewModel = provider.get(LoginViewModel.class);
        navController = Navigation.findNavController(view);

//        AppBarConfiguration appBarConfiguration =
//                new AppBarConfiguration.Builder(navController.getGraph()).build();
//        NavigationUI.setupWithNavController(
//                binding.zeroUiToolbar, navController, appBarConfiguration);

        binding.passwordEditText.requestFocus();
        InputMethodManager imm = (InputMethodManager)
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(binding.passwordEditText, InputMethodManager.SHOW_IMPLICIT);
        binding.setCreateAccountPasswordFragment(this);
        binding.progressBar.setVisibility(View.GONE);

//        ((AppCompatActivity)requireActivity()).setSupportActionBar(binding.zeroUiToolbar);
//        ((AppCompatActivity)requireActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        registrationViewModel.getRegistrationState().observe(getViewLifecycleOwner(), state -> {
            binding.progressBar.setVisibility(View.GONE);
            switch (state) {
                case REGISTRATION_COMPLETE:
                    loginViewModel.authenticate(registrationViewModel.getEmail(), registrationViewModel.getPassword());
                    //navController.popBackStack(R.id.loginFragment, false);
                    navController.navigate(R.id.loginFragment);
                    break;
                case REGISTRATION_FAILED:
                    Toast.makeText(requireActivity(), "Create account failed", Toast.LENGTH_SHORT).show();
                    break;
            }
        });

//        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
//            @Override
//            public void handleOnBackPressed() {
//                registrationViewModel.userCancelledRegistration();
//                //Compare the below two
//                //navController.popBackStack(R.id.loginFragment, false);
//                navController.navigateUp();
//            }
//        });
    }

    public void onCreateAccountClicked(){
        String password = binding.passwordEditText.getText().toString().trim();
        if(password.length() == 0){
            binding.retypePasswordEditText.setError("Please enter a password");
        }else if(password.length() < 6){
            binding.retypePasswordEditText.setError("Please enter at least 6 characters");
        } else if(!password.equals(binding.retypePasswordEditText.getText().toString())){
            binding.retypePasswordEditText.setError("Passwords do not match");
        }else if(!binding.termsCheck.isChecked() || !binding.privacyCheck.isChecked()){
            Toast toast = Toast.makeText(requireActivity(),"Please agree to our Privacy Policy & Terms of Use", Toast.LENGTH_SHORT);
            toast.show();
        }else{
            binding.progressBar.setVisibility(View.VISIBLE);
            registrationViewModel.createAccountAndLogin(password);
        }
    }

    public void onLoginClicked(){
        navController.navigate(R.id.loginFragment);
    }

}
