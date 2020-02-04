package com.example.artwokmabel.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.algolia.search.saas.Client;
import com.algolia.search.saas.Index;
import com.example.artwokmabel.R;
import com.example.artwokmabel.rubbish.LoginActivityOld;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class CreateAccountActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "SignUp";

    private EditText inputUserName;
    //private EditText fullName;
    private EditText emailText;
    private EditText passwordText;
    private EditText reEnterPasswordText;
    private Button signUpButton;

    private ProgressDialog mProgressDialog;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;


    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_create_account);
        setContentView(R.layout.activity_signup);

        inputUserName = findViewById(R.id.signupActivity_edittext_name);
        //fullName = findViewById(R.id.input_fullname);
        emailText = findViewById(R.id.signupActivity_edittext_email);
        passwordText = findViewById(R.id.signupActivity_edittext_password);
        reEnterPasswordText = findViewById(R.id.signupActivity_edittext_reenter_password);

        signUpButton = findViewById(R.id.signupActivity_button_signup);
        signUpButton.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }


    private void createAccount(String username, String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validate()) {
            onSignupFailed();
            return;
        }

        showProgressDialog();

        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                                String uid = task.getResult().getUser().getUid();

                                UserProfileChangeRequest userProfileChangeRequest
                                        = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(inputUserName.getText().toString()).build();
                                task.getResult().getUser().updateProfile(userProfileChangeRequest);

                            ArrayList<String> followers = new ArrayList<String>();
                            ArrayList<String> following = new ArrayList<String>();
                            ArrayList<String> chatRooms = new ArrayList<>();
                            ArrayList<String> tab_categories = new ArrayList<>();

                            Map<String, Object> userDoc = new HashMap<>();
                            userDoc.put("username", inputUserName.getText().toString());
                            userDoc.put("profile_url", "https://imagesvc.meredithcorp.io/v3/mm/image?url=https%3A%2F%2Fstatic.onecms.io%2Fwp-content%2Fuploads%2Fsites%2F38%2F2005%2F10%2F12230420%2FZdorovKirillVladimirovich_0.jpg&q=85");
                            userDoc.put("email", emailText.getText().toString());
                            userDoc.put("followers", followers);
                            userDoc.put("following", following);
                            userDoc.put("number_of_posts", 0);
                            userDoc.put("number_of_listings", 0);
                            userDoc.put("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                            userDoc.put("chat_rooms", chatRooms);
                            userDoc.put("tab_categories", tab_categories);

                            db.collection("Users")
                                    .document(uid)
                                    .set(userDoc)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d(TAG, "DocumentSnapshot successfully written!");
                                            PushToAlgolia(inputUserName.getText().toString(), uid);
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w(TAG, "Error writing document", e);
                                        }
                                    });
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(CreateAccountActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // [START_EXCLUDE]
                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
        // [END create_user_with_email]
    }


    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Sign up failed", Toast.LENGTH_LONG).show();
    }


    public boolean validate() {
        boolean valid = true;

        String username = inputUserName.getText().toString().trim();
        String email = emailText.getText().toString().trim();
        String password = passwordText.getText().toString().trim();
        String reEnterPassword = reEnterPasswordText.getText().toString().trim();

        if (username.isEmpty() || username.length() < 3) {
            inputUserName.setError("at least 3 characters");
            valid = false;
        } else {
            inputUserName.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailText.setError("enter a valid email address");
            valid = false;
        } else {
            emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 15) {
            passwordText.setError("between 4 and 15 alphanumeric characters");
            valid = false;
        } else {
            passwordText.setError(null);
        }

        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 4 || reEnterPassword.length() > 15 || !(reEnterPassword.equals(password))) {
            reEnterPasswordText.setError("passwords do not match");
            valid = false;
        } else {
            reEnterPasswordText.setError(null);
        }

        return valid;
    }

    private void PushToAlgolia(String username, String userid){

        Client client = new Client("CTIOUIUY3T", "7f3f4f1b7f3eab10acf7e980b2023a23");
        Index algoliaIndex = client.getIndex("Users");

        JSONObject newData = null;
        try {
            newData = new JSONObject()
                    .put("username", username);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        algoliaIndex.saveObjectAsync(newData, userid, null);
    }

    private void updateUI(FirebaseUser user) {
        hideProgressDialog();
        if (user != null) {
            Intent intent = new Intent(getApplicationContext(), LoginActivityOld.class);
            startActivity(intent);
        } else {
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.signupActivity_button_signup) {
            createAccount(inputUserName.getText().toString().trim(), emailText.getText().toString().trim(), passwordText.getText().toString().trim());
        }
    }
    // sliding animation effect when back key pressed
    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.fromleft, R.anim.toright);
    }
}
