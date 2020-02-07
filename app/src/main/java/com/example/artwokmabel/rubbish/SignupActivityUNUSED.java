//package com.example.artwokmabel.RubbishReference;
//
//import android.content.Intent;
//import android.graphics.Color;
//import android.net.Uri;
//import android.os.Build;
//import android.os.Bundle;
//import android.provider.MediaStore;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.ProgressBar;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.example.artwokmabel.Chat.Models.UserUserModel;
//import com.example.artwokmabel.R;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.AuthResult;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.UserProfileChangeRequest;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
//import com.google.firebase.storage.FirebaseStorage;
//import com.google.firebase.storage.UploadTask;
//
//public class SignupActivityUNUSED extends AppCompatActivity {
//    private static final int PICK_FROM_ALBUM = 10;
//    private EditText email;
//    private EditText name;
//    private EditText password;
//    private Button signup;
//    private String splash_background;
//    private ImageView profile;
//    private Uri imageUri;
//    private ProgressBar progressBar;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.z_activity_signup);
//        FirebaseRemoteConfig mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
//        splash_background = mFirebaseRemoteConfig.getString(getString(R.string.rc_color));
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            getWindow().setStatusBarColor(Color.parseColor(splash_background));
//        }
//
//        profile = (ImageView) findViewById(R.id.signupActivity_imageview_profile);
//        profile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(Intent.ACTION_PICK);
//                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
//                startActivityForResult(intent, PICK_FROM_ALBUM);
//            }
//        });
//
//        email = (EditText) findViewById(R.id.signupActivity_edittext_email);
//        name = (EditText) findViewById(R.id.signupActivity_edittext_name);
//        password = (EditText) findViewById(R.id.signupActivity_edittext_password);
//        signup = (Button) findViewById(R.id.signupActivity_button_signup);
//        signup.setBackgroundColor(Color.parseColor(splash_background));
//        progressBar = findViewById(R.id.signupActivity_progressbar);
//
//        signup.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                progressBar.setVisibility(View.VISIBLE);
//
//                if (email.getText().toString() == null || name.getText().toString() == null || password.getText().toString() == null || imageUri == null) {
//                    return;
//                }
//
//                FirebaseAuth.getInstance()
//                        .createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
//                        .addOnCompleteListener(SignupActivityUNUSED.this, new OnCompleteListener<AuthResult>() {
//                            @Override
//                            public void onComplete(@NonNull Task<AuthResult> task) {
//                                progressBar.setVisibility(View.INVISIBLE);
//                                final String uid = task.getResult().getUser().getUid();
//                                UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(name.getText().toString()).build();
//
//                                task.getResult().getUser().updateProfile(userProfileChangeRequest);
//
//
//                                FirebaseStorage.getInstance().getReference().child("userImages").child(uid).putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
//                                        @SuppressWarnings("VisibleForTests")
////                                        String imageUrl = task.getResult().getDownloadUrl().toString();
//
//                                        Task<Uri> imageUrl = task.getResult().getStorage().getDownloadUrl();
//                                        while(!imageUrl.isComplete());
//
//                                        UserUserModel useruserModel = new UserUserModel();
//                                        useruserModel.userName = name.getText().toString();
////                                        userModel.profileImageUrl = imageUrl;
//                                        useruserModel.profileImageUrl = imageUrl.getResult().toString();
//                                        useruserModel.uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
//
//                                        FirebaseDatabase.getInstance().getReference().child("users").child(uid).setValue(useruserModel).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                            @Override
//                                            public void onSuccess(Void aVoid) {
//                                                SignupActivityUNUSED.this.finish();
//                                            }
//                                        });
//
//                                    }
//                                });
//
//                            }
//                        });
//            }
//        });
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == PICK_FROM_ALBUM && resultCode == RESULT_OK) {
//            profile.setImageURI(data.getData()); // 가운데 뷰를 바꿈
//            imageUri = data.getData();// 이미지 경로 원본
//        }
//    }
//}
//
