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
import androidx.navigation.ui.AppBarConfiguration;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.example.artwokmabel.R;
import com.example.artwokmabel.databinding.FragmentCreateAccountUsernameBinding;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);

        binding.usernameEditText.requestFocus();
        InputMethodManager imm = (InputMethodManager)
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(binding.usernameEditText, InputMethodManager.SHOW_IMPLICIT);
        binding.setCreateAccountUsernameFragment(this);
        binding.progressBar.setVisibility(View.GONE);

        if(viewModel.getUsername() != null){
            binding.usernameEditText.setText(viewModel.getUsername());
        }

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
                String username = binding.usernameEditText.getText().toString().trim();
                viewModel.userCancelledUsername(username);
                navController.navigateUp();
            }
        });
    }


    public void onUsernameNextClicked(){
        String username = binding.usernameEditText.getText().toString().trim();

        Pattern pattern = Pattern.compile("\\s");
        Matcher matcher = pattern.matcher(username);
        boolean found = matcher.find();

        if(username.length() == 0) {
            binding.usernameEditText.setError("Please enter a username");
        }else if(found){
            binding.usernameEditText.setError("Username cannot contain any spaces");
        }else{
            binding.progressBar.setVisibility(View.VISIBLE);
            viewModel.collectUserUsername(username);
        }
    }

    public void onLoginClicked(){
        navController.navigate(R.id.loginFragment);
    }

}
