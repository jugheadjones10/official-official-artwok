package com.example.artwokmabel.login;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.artwokmabel.R;
import com.example.artwokmabel.databinding.FragmentCreateAccountPasswordBinding;

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
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);

        binding.passwordEditText.requestFocus();
        InputMethodManager imm = (InputMethodManager)
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(binding.passwordEditText, InputMethodManager.SHOW_IMPLICIT);
        binding.setCreateAccountPasswordFragment(this);
        binding.progressBar.setVisibility(View.GONE);

        registrationViewModel.getRegistrationState().observe(getViewLifecycleOwner(), state -> {
            binding.progressBar.setVisibility(View.GONE);
            switch (state) {
                case REGISTRATION_COMPLETE:
                    loginViewModel.authenticate(registrationViewModel.getEmail(), registrationViewModel.getPassword());
                    navController.navigate(R.id.loginFragment);
                    break;
                case REGISTRATION_FAILED:
                    Toast.makeText(requireActivity(), "Create account failed", Toast.LENGTH_SHORT).show();
                    break;
            }
        });

        binding.upButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().onBackPressed();
            }
        });

        requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Log.d("sweefkin", "Handle on back pressed");
                registrationViewModel.userCancelledPW();
                navController.navigateUp();
            }
        });

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

    public void onPrivacyPolicyClicked(){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW);
        browserIntent.setData(Uri.parse("http://artwok.bss.design/Privacy.html"));
        startActivity(browserIntent);
    }

    public void onTermsOfUseClicked(){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW);
        browserIntent.setData(Uri.parse("http://artwok.bss.design/Terms%20of%20use.html"));
        startActivity(browserIntent);
    }

    public void onLoginClicked(){
        navController.navigate(R.id.loginFragment);
    }

}
