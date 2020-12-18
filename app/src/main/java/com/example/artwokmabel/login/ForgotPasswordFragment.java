package com.example.artwokmabel.login;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.artwokmabel.R;
import com.example.artwokmabel.databinding.FragmentForgotPasswordBinding;
import com.example.artwokmabel.repos.FirestoreRepo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ForgotPasswordFragment extends Fragment {

    private FragmentForgotPasswordBinding binding;
    private NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_forgot_password, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        binding.setForgotPasswordFragment(this);
        //Uncomment the below when settings has been converted to navigation
//        if(getIntent().getStringExtra("email") != null){
//            binding.emailForgotPassword.setText(getIntent().getStringExtra("email"));
//            binding.emailForgotPassword.setEnabled(false);
//        }else{
//            binding.emailForgotPassword.requestFocus();
//        }
        //binding.emailForgotPassword.requestFocus();

        //No better way to do the below?

        ((AppCompatActivity)requireActivity()).setSupportActionBar(binding.zeroUiToolbar);
        ((AppCompatActivity)requireActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public interface OnResetPasswordSent{
        void onResetPasswordSent(boolean isSuccessful);
    }

    public void onResetPasswordClicked(){

        String email = binding.emailForgotPassword.getText().toString().trim();
        if(email.isEmpty()) {
            binding.emailForgotPassword.setError("Please enter an email");
        }else if(!isEmailValid(email)){
            binding.emailForgotPassword.setError("Please enter a valid email");
        }else{
            FirestoreRepo.getInstance().sendResetPasswordEmail(binding.emailForgotPassword.getText().toString(),
                    isSuccessful -> {
                        if(isSuccessful){
                            Toast.makeText(getActivity(), "Reset password email has been sent.", Toast.LENGTH_LONG).show();
                            navController.popBackStack();
                        }
                    }
            );
        }
    }

    private static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

}
