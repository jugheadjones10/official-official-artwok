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
import com.example.artwokmabel.databinding.FragmentCreateAccountEmailBinding;
import com.example.artwokmabel.homepage.callbacks.LoginPageCallback;
import com.example.artwokmabel.repos.FirestoreRepo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.artwokmabel.login.RegistrationViewModel.RegistrationState.DUPLICATE_EMAIL;

public class CreateAccountEmailFragment extends Fragment {

    private FragmentCreateAccountEmailBinding binding;
    private RegistrationViewModel viewModel;
    private NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_create_account_email, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(RegistrationViewModel.class);
        navController = Navigation.findNavController(view);

        AppBarConfiguration appBarConfiguration =
                new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupWithNavController(
                binding.zeroUiToolbar, navController, appBarConfiguration);

        binding.emailEditText.requestFocus();
        InputMethodManager imm = (InputMethodManager)
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(binding.emailEditText, InputMethodManager.SHOW_IMPLICIT);
        binding.setCreateAccountEmailFragment(this);
        binding.progressBar.setVisibility(View.GONE);

//        ((AppCompatActivity)requireActivity()).setSupportActionBar(binding.zeroUiToolbar);
//        ((AppCompatActivity)requireActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewModel.getRegistrationState().observe(getViewLifecycleOwner(), state -> {
            binding.progressBar.setVisibility(View.GONE);
            switch (state) {
                case DUPLICATE_EMAIL:
                    binding.emailEditText.setError("Sorry, this email already exists");
                    break;
                case COLLECT_USERNAME:
                    navController.navigate(R.id.action_createAccountEmailFragment_to_createAccountUsernameFragment);
                    break;
            }
        });

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                viewModel.userCancelledRegistration();
                //Compare the below two
                //navController.popBackStack(R.id.loginFragment, false);
                navController.popBackStack();
            }
        });
    }

    public void emailOnNextClicked(){
        String email = binding.emailEditText.getText().toString().trim();
        if(email.length() == 0){
            binding.emailEditText.setError("Please enter an email");
        }else if(!isEmailValid(email)){
            binding.emailEditText.setError("Please enter a valid email");
        }else{
            binding.progressBar.setVisibility(View.VISIBLE);
            viewModel.collectUserEmail(email);
        }
    }

    public void onLoginClicked(){
        navController.navigate(R.id.loginFragment);
    }

    private static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
