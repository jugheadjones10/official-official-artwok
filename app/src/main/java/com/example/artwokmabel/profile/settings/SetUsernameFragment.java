package com.example.artwokmabel.profile.settings;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.artwokmabel.HomePageActivity;
import com.example.artwokmabel.R;
import com.example.artwokmabel.databinding.FragmentSetUsernameBinding;
import com.example.artwokmabel.repos.FirestoreRepo;

public class SetUsernameFragment extends Fragment {

    private FragmentSetUsernameBinding binding;
    private String username;
    private NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        username = SetUsernameFragmentArgs.fromBundle(getArguments()).getUsername();
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_set_username, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        binding.progressBar.setVisibility(View.GONE);
        binding.setSetUsernameFragment(this);

        binding.usernameEditText.setText(username);
        binding.usernameEditText.requestFocus();

        ((AppCompatActivity)requireActivity()).setSupportActionBar(binding.settingsToolbar);
        ((AppCompatActivity)requireActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void onTickButtonClicked(){
        username = binding.usernameEditText.getText().toString().trim();
        if(username.length() == 0) {
            binding.usernameEditText.setError("Please enter a username");
        }else{
            binding.progressBar.setVisibility(View.VISIBLE);
            FirestoreRepo.getInstance().isUsernameDuplicate(username, (isDuplicate) ->  {
                isUsernameDuplicateCallback(isDuplicate);
            });
        }
    }

    private void isUsernameDuplicateCallback(boolean isUsernameDuplicateDatabase){
        binding.progressBar.setVisibility(View.GONE);

        if(isUsernameDuplicateDatabase){
            binding.usernameEditText.setError("Sorry, this username already exists");
        }else{
            new ViewModelProvider(requireActivity()).get(SettingsActivityViewModel.class)
                    .updateUserUsername(username);
            Toast.makeText(requireActivity(), "Username change successful", Toast.LENGTH_SHORT).show();

            navController.navigateUp();
//            Intent intent = new Intent(SetUsernameActivity.this, HomePageActivity.class);
//            intent.putExtra("fromWho", "editProfile");
//            startActivity(intent);
        }
    }
}
