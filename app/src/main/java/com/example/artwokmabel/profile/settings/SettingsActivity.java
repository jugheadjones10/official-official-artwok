package com.example.artwokmabel.profile.settings;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.example.artwokmabel.HomePageActivity;
import com.example.artwokmabel.R;
import com.example.artwokmabel.databinding.ActivitySettingsBinding;
import com.example.artwokmabel.homepage.callbacks.ImagePickerCallback;
import com.example.artwokmabel.models.User;
import com.example.artwokmabel.profile.uploadlisting.UploadListingAcitvity;
import com.example.artwokmabel.profile.user.ProfileFragmentViewModel;
import com.google.android.gms.dynamic.SupportFragmentWrapper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Random;

public class SettingsActivity extends AppCompatActivity {

    private StorageReference storageReference;
    private FirebaseAuth mAuth;
    public static final int REQUEST_IMAGE = 100;
    public ActivitySettingsBinding binding;
    public SettingsActivityViewModel viewModel;
    public User user;

    private static SettingsActivity instance;

    public static SettingsActivity getInstance(){
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        instance = this;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_settings);
        binding.setOnprofilepicchange(new OnProfilePicChange());
        binding.setOnusernamechange(new OnUsernameChange());
        binding.setOnintrochange(new OnIntroChange());

        mAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        setSupportActionBar(binding.settingsToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewModel = ViewModelProviders.of(this).get(SettingsActivityViewModel.class);

        //setEditTextListeners();
        observeViewModel();


//        binding.editUsername.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//
//                new OnUsernameChange().onUsernameChange();
//                return true;
//            }
//        });
//
//        binding.editIntroduction.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//
//                new OnIntroChange().onIntroChange();
//                return true;
//            }
//        });
    }

    private void observeViewModel(){
        viewModel.getUserObservable().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                binding.setUser(user);
                Log.d("clicky", user.getUsername());

                SettingsActivity.this.user = user;
                Picasso.get().load(user.getProfile_url()).into(binding.profilePicture);
            }
        });
    }

    public class OnProfilePicChange{
        public void onProfilePicChange(){

            new ImagePickerCallback(SettingsActivity.this, REQUEST_IMAGE).onImagePickerClicked();

        }
    }

    public class OnUsernameChange{
        public void onUsernameChange(){

            Log.d("clicky", "username clicked");
            Intent intent = new Intent(SettingsActivity.this, SetUsernameActivity.class);
            startActivity(intent);

        }
    }

    public class OnIntroChange{
        public void onIntroChange(){
            Log.d("clicky", "intro clicked");

            Intent intent = new Intent(SettingsActivity.this, SetIntroActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
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
                            Toast.makeText(SettingsActivity.this, "New profile pic failed", Toast.LENGTH_LONG).show();
                        }
                    });

                } else { // no pics selected

                }
            }
        }
    }


}
