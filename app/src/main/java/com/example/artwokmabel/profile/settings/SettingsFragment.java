package com.example.artwokmabel.profile.settings;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.artwokmabel.R;
import com.example.artwokmabel.databinding.FragmentSettingsBinding;
import com.example.artwokmabel.homepage.callbacks.ImagePickerCallback;
import com.example.artwokmabel.login.ForgotPasswordActivity;
import com.example.artwokmabel.login.LoginOptionsActivity;
import com.example.artwokmabel.models.User;
import com.example.artwokmabel.profile.uploadlisting.UploadListingAcitvity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Random;

import static android.app.Activity.RESULT_OK;

public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;
    private SettingsActivityViewModel viewModel;
    private NavController navController;

    private StorageReference storageReference;
    private FirebaseAuth mAuth;

    public User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false);
        binding.setSettingsFragment(this);

        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        viewModel = new ViewModelProvider(requireActivity()).get(SettingsActivityViewModel.class);

        mAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        ((AppCompatActivity)requireActivity()).setSupportActionBar(binding.settingsToolbar);
        ((AppCompatActivity)requireActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        observeViewModel();
    }

    private void observeViewModel(){
        viewModel.getUserObservable().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                binding.setUser(user);

                SettingsFragment.this.user = user;
                Picasso.get().load(user.getProfile_url()).into(binding.profilePicture);
            }
        });
    }

    public void onProfilePicChange(){
        new ImagePickerCallback(requireActivity(), UploadListingAcitvity.REQUEST_IMAGE).onImagePickerClicked();
    }

    public void onUsernameChange(){
        SettingsFragmentDirections.ActionSettingsFragmentToSetUsernameFragment action =
                SettingsFragmentDirections.actionSettingsFragmentToSetUsernameFragment(user.getUsername());
        navController.navigate(action);
    }

    public void onIntroChange(){
        //Replace the placeholder below eventually with User.getIntroduction() - currently the call is null
        SettingsFragmentDirections.ActionSettingsFragmentToSetIntroFragment action =
                SettingsFragmentDirections.actionSettingsFragmentToSetIntroFragment("yadeehoo placeholder intro");
        navController.navigate(action);
    }

    public void onPasswordChange(){

        //Change the below to start using nav

//        Intent intent = new Intent(SettingsActivity.this, ForgotPasswordActivity.class);
//        intent.putExtra("email", user.getEmail());
//        startActivity(intent);
    }

    public void onLogout(){
//        FirebaseAuth.getInstance().signOut();
//        Intent intent = new Intent(SettingsActivity.this, LoginOptionsActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(intent);
    }

    public void onDeactivate(){
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setMessage("This will delete every post, listing, comment, review, message by you and you will be unable to recover them! Deactive anyway?")
                .setPositiveButton("Deactivate", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        viewModel.deleteUser();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertdialog = builder.create();

        alertdialog.setOnShowListener( new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                alertdialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.red));
            }
        });
        alertdialog.show();

    }

    @Override
    public void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UploadListingAcitvity.REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {

                if (data != null) {
                    Uri fileUri = data.getParcelableExtra("path");

                    Random random = new Random();
                    int randomNumber = random.nextInt();
                    String fileName = Integer.toString(randomNumber);

                    StorageReference fileToUpload = storageReference.child("Images").child(mAuth.getCurrentUser().getUid()).child(fileName); // randomize name

                    fileToUpload.putFile(fileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            storageReference.child("Images").child(mAuth.getCurrentUser().getUid()).child(fileName).getDownloadUrl().addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Log.d("LOG: ", "postImageUris " + task.getResult().toString());

                                    Picasso.get().load(task.getResult().toString()).into(binding.profilePicture);
                                    viewModel.updateUserProfileUrl(task.getResult().toString());
                                }
                            });

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(requireActivity(), "New profile pic failed", Toast.LENGTH_LONG).show();
                        }
                    });

                } else { // no pics selected

                }
            }
        }
    }
}