package com.example.artwokmabel.login;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.example.artwokmabel.R;
import com.example.artwokmabel.chat.personalchat.ChatActivity;
import com.example.artwokmabel.databinding.FragmentSplashBinding;
import com.example.artwokmabel.homepage.listing.ListingActivity;
import com.example.artwokmabel.homepage.post.PostActivity;
import com.example.artwokmabel.models.Listing;
import com.example.artwokmabel.models.MainPost;
import com.example.artwokmabel.repos.FirestoreRepo;

public class SplashFragment extends Fragment {

    private FragmentSplashBinding binding;
    private LoginViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_splash, container, false);

        requireActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.checkAuthenticationState();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(LoginViewModel.class);

        final NavController navController = Navigation.findNavController(view);
        viewModel.authenticationState.observe(getViewLifecycleOwner(),
            new Observer<LoginViewModel.AuthenticationState>() {
                @Override
                public void onChanged(LoginViewModel.AuthenticationState authenticationState) {
                    switch (authenticationState) {
                        case AUTHENTICATED:
                            navController.navigate(R.id.action_splashFragment_to_homePageActivity);
                            break;
                        case UNAUTHENTICATED:
                            navController.navigate(R.id.action_splashFragment_to_loginOptionsFragment);
                            break;
                    }
                }
            });
    }
}
