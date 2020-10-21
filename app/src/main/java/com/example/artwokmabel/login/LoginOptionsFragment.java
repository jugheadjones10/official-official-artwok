package com.example.artwokmabel.login;


import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.artwokmabel.R;
import com.example.artwokmabel.databinding.FragmentLoginOptionsBinding;

public class LoginOptionsFragment extends Fragment {

    private FragmentLoginOptionsBinding binding;
    private LoginViewModel viewModel;
    private NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login_options, container, false);

        OnBackPressedCallback callback = new OnBackPressedCallback(true ) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(getActivity(), AppHostActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("LOGOUT", true);
                startActivity(intent);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(LoginViewModel.class);
        navController = Navigation.findNavController(view);

        binding.setLoginOptionsFragment(this);
        //Uncomment when Google time comes
        //binding.setOngoogleloginclicked(new OnGoogleLoginClicked());

    }

    public void onLoginClicked(){
        navController.navigate(R.id.action_loginOptionsFragment_to_loginFragment);
    }

    public void onCreateAccountClicked(){
        navController.navigate(R.id.action_loginOptionsFragment_to_createAccountEmailFragment);
    }

    //Uncomment when Google time comes
//    public class OnGoogleLoginClicked{
//        public void onGoogleLoginClicked(){
//            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
//            startActivityForResult(signInIntent, 123);
//        }
//    }

}
