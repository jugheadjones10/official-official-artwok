package com.example.artwokmabel.login;


import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
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

import com.example.artwokmabel.R;
import com.example.artwokmabel.databinding.FragmentLoginBinding;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    private LoginViewModel viewModel;
    private NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(LoginViewModel.class);

        binding.setLoginFragment(this);

        binding.emailEditText.requestFocus();
        binding.progressBar.setVisibility(View.GONE);

        navController = Navigation.findNavController(view);
//        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(),
//            new OnBackPressedCallback(true) {
//                @Override
//                public void handleOnBackPressed() {
//                    viewModel.refuseAuthentication();
//                    navController.popBackStack();
//                }
//            });

        ((AppCompatActivity)requireActivity()).setSupportActionBar(binding.toolbar);
        ((AppCompatActivity)requireActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewModel.authenticationState.observe(getViewLifecycleOwner(),
            new Observer<LoginViewModel.AuthenticationState>() {
                @Override
                public void onChanged(LoginViewModel.AuthenticationState authenticationState) {
                    binding.progressBar.setVisibility(View.GONE);
                    switch (authenticationState) {
                        case AUTHENTICATED:
                            navController.popBackStack(R.id.splashFragment, false);
                            break;
                        case INVALID_AUTHENTICATION:
                        Toast.makeText(requireActivity(), "Login failed", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
            });
    }

    public void onLoginClicked(){
        String email = binding.emailEditText.getText().toString().trim();
        String password = binding.passwordEditText.getText().toString().trim();

        if(email.length() == 0){
            binding.emailEditText.setError("Please enter your email");
        }else if(password.length() == 0){
            binding.passwordEditText.setError("Please enter your password");
        }else{
            binding.progressBar.setVisibility(View.VISIBLE);

            viewModel.authenticate(email, password);
        }
    }

    public void onForgotPasswordClicked(){
        navController.navigate(R.id.forgotPasswordFragment);
    }

}
