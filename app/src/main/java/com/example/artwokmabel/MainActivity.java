package com.example.artwokmabel;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
//import android.support.constraint.ConstraintLayout;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    static final int GOOGLE_SIGN = 123;
    FirebaseAuth mAuth;
    ImageButton google_login;
    GoogleSignInClient mGoogleSignInClient;
    private LoginButton fbLoginButton;
    private Button loginButton;
    private CallbackManager callbackManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launch_page);

        fbLoginButton = findViewById(R.id.facebook_button);
        google_login = findViewById(R.id.google_button);
        loginButton = findViewById(R.id.login_button);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                setContentView(R.layout.activity_login);
            }
        });

///////////////////////////////////////DO NOT TOUCH BELOW -- IT'S ALL AUTHENTICATION CODE //////////////////////////////////////
///////////////////////////////////////DO NOT TOUCH BELOW -- IT'S ALL AUTHENTICATION CODE //////////////////////////////////////
///////////////////////////////////////DO NOT TOUCH BELOW -- IT'S ALL AUTHENTICATION CODE //////////////////////////////////////
///////////////////////////////////////DO NOT TOUCH BELOW -- IT'S ALL AUTHENTICATION CODE //////////////////////////////////////
///////////////////////////////////////DO NOT TOUCH BELOW -- IT'S ALL AUTHENTICATION CODE //////////////////////////////////////
///////////////////////////////////////DO NOT TOUCH BELOW -- IT'S ALL AUTHENTICATION CODE //////////////////////////////////////
///////////////////////////////////////DO NOT TOUCH BELOW -- IT'S ALL AUTHENTICATION CODE //////////////////////////////////////
///////////////////////////////////////DO NOT TOUCH BELOW -- IT'S ALL AUTHENTICATION CODE //////////////////////////////////////
///////////////////////////////////////DO NOT TOUCH BELOW -- IT'S ALL AUTHENTICATION CODE //////////////////////////////////////
///////////////////////////////////////DO NOT TOUCH BELOW -- IT'S ALL AUTHENTICATION CODE //////////////////////////////////////

        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions
                .Builder()
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);
        google_login.setOnClickListener(v -> SignInGoogle());

        if(mAuth.getCurrentUser() != null){
            FirebaseUser user = mAuth.getCurrentUser();
            updateUI(user);
        }

        callbackManager = CallbackManager.Factory.create();
        fbLoginButton.setReadPermissions(Arrays.asList("email", "public_profile"));

        fbLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GOOGLE_SIGN){
            Task<GoogleSignInAccount> task = GoogleSignIn
                    .getSignedInAccountFromIntent(data);

            try{
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if(account != null) firebaseAuthWithGoogle(account);
            } catch(ApiException e){
                e.printStackTrace();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        Log.d("TAG", "firebaseAuthWithGoogle: " + account.getId());

        AuthCredential credential = GoogleAuthProvider
                .getCredential(account.getIdToken(), null);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                 if(task.isSuccessful()){
                     Log.d("TAG", "signin sucess");
                     FirebaseUser user = mAuth.getCurrentUser();
                     updateUI(user);
                 }else{
                     Log.w("TAG", "signin failure", task.getException());

                     Toast.makeText(this, "SignIn Failed!", Toast.LENGTH_SHORT);
                     updateUI(null);
                 }
                });
    }

    private void updateUI(FirebaseUser user) {
        if(user != null){

//            String name = user.getDisplayName();
//            String email = user.getEmail();
//            String photo = String.valueOf(user.getPhotoUrl());
        }else{

        }
    }

    void Logout(){
        FirebaseAuth.getInstance().signOut();
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this,
                        task -> {updateUI(null);
                });
    }

    AccessTokenTracker tokenTracker = new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
            if(currentAccessToken == null){
//                txtName.setText("");
//                txtEmail.setText("");
                Toast.makeText(MainActivity.this, "User Logged out", Toast.LENGTH_LONG).show();
            }else{
                loadUserProfile(currentAccessToken);
            }
        }
    };

    private void loadUserProfile(AccessToken newAccessToken){
        GraphRequest request = GraphRequest.newMeRequest(newAccessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    String first_name = object.getString("first");
                    String last_name = object.getString("last_name");
                    String email = object.getString("email");
                    String id = object.getString("id");

//                    txtEmail.setText(email);
//                    txtName.setText(first_name +" "+ last_name);
                    RequestOptions requestOptions = new RequestOptions();
                    requestOptions.dontAnimate();

//                    Glide.with(MainActivity.this).load(image_url).into(circleImageView);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "first_name,last_name,email,id,");
        request.setParameters(parameters);
        request.executeAsync();
    }

    void SignInGoogle(){
        Intent signIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signIntent, GOOGLE_SIGN);
    }

}
