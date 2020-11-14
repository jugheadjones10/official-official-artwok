package com.example.artwokmabel.login;


import android.content.Context;
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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.example.artwokmabel.R;
import com.example.artwokmabel.databinding.FragmentCreateAccountEmailBinding;

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
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);

        binding.emailEditText.requestFocus();
        InputMethodManager imm = (InputMethodManager)
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(binding.emailEditText, InputMethodManager.SHOW_IMPLICIT);
        binding.setCreateAccountEmailFragment(this);
        binding.progressBar.setVisibility(View.GONE);

        if(viewModel.getEmail() != null){
            binding.emailEditText.setText(viewModel.getEmail());
        }

        viewModel.getRegistrationState().observe(getViewLifecycleOwner(), state -> {
            binding.progressBar.setVisibility(View.GONE);
            switch (state) {
                case DUPLICATE_EMAIL:
                    binding.emailEditText.setError("Sorry, this email already exists");
                    break;
                case COLLECT_USERNAME:
                    Log.d("sweefkin", "collected username");
                    navController.navigate(R.id.action_createAccountEmailFragment_to_createAccountUsernameFragment);
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
                String email = binding.emailEditText.getText().toString().trim();
                viewModel.userCancelledEmail(email);
                navController.navigateUp();
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
