package com.example.artwokmabel.profile.settings;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.DialogInterface;
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
import com.example.artwokmabel.login.ForgotPasswordActivity;
import com.example.artwokmabel.login.LoginOptionsActivity;
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
        binding.setOnpasswordchange(new OnPasswordChange());
        binding.setOnlogout(new OnLogout());
        binding.setOndeactivate(new OnDeactivate());

        mAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        setSupportActionBar(binding.settingsToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewModel = ViewModelProviders.of(this).get(SettingsActivityViewModel.class);

        observeViewModel();

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

            new ImagePickerCallback(SettingsActivity.this, UploadListingAcitvity.REQUEST_IMAGE).onImagePickerClicked();

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

    public class OnPasswordChange{
        public void onPasswordChange(){

            //Change the below to start using nav

//            Intent intent = new Intent(SettingsActivity.this, ForgotPasswordActivity.class);
//            intent.putExtra("email", user.getEmail());
//            startActivity(intent);
        }
    }

    public class OnLogout{
        public void onLogout(){
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(SettingsActivity.this, LoginOptionsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    public class OnDeactivate{
        public void onDeactivate(){
            AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
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
    }


    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
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
                            Toast.makeText(SettingsActivity.this, "New profile pic failed", Toast.LENGTH_LONG).show();
                        }
                    });

                } else { // no pics selected

                }
            }
        }
    }
}
