package com.example.artwokmabel.rubbish;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.artwokmabel.HomePageActivity;
import com.example.artwokmabel.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

//import android.support.constraint.ConstraintLayout;

public class LaunchPageActivity extends AppCompatActivity implements View.OnClickListener{

    static final int GOOGLE_SIGN_IN = 123;
    FirebaseAuth mAuth;
    ImageButton google_login;
    GoogleSignInClient mGoogleSignInClient;
    //private LoginButton fbLoginButton;
    private Button loginButton;
    private CallbackManager callbackManager;
    private EditText searchBar;
    //LoginButton fbLoginButton;
    SignInButton googleSignInButton;

    TextView createAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.z_activity_launch_page);

        //Below is all Facebook
        callbackManager = CallbackManager.Factory.create();

        createAccount = findViewById(R.id.create_account);
        createAccount.setOnClickListener(this);
        //fbLoginButton = (LoginButton) findViewById(R.id.facebook_button);


        loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(getApplicationContext(), LoginActivityUNUSED.class);
                startActivity(intent);
            }
        });


        //fbLoginButton.setReadPermissions("email", "public_profile");
        //fbLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//                //Log.d(TAG, "facebook:onSuccess:" + loginResult);
//                handleFacebookAccessToken(loginResult.getAccessToken());
//            }
//
//            @Override
//            public void onCancel() {
//                // App code
//            }
//
//            @Override
//            public void onError(FacebookException exception) {
//                // App code
//            }
//        });
        //Above for Facebook

        //_________________________________//
        mAuth = FirebaseAuth.getInstance();
        //^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^//

        googleSignInButton = findViewById(R.id.google_sign_in_button);
        googleSignInButton.setOnClickListener(this);
        //Above calls onClick function at the bottom

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                //Above requestIdToken is for Firebase
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    protected void onStart(){
        super.onStart();
        //GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        //Replaced the above with the below code (in order to use Firebase)
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser user) {
        if(user != null){
            Intent intent = new Intent(getApplicationContext(), HomePageActivity.class);
            startActivity(intent);
        }else{
            //Display google sign in button
            //setContentView(R.layout.activity_main);
            //Try putting the above here?
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        //Above for Facebook

        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == GOOGLE_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            //______________________________//
            firebaseAuthWithGoogle(account);
            //^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^//

            // Signed in successfully, show authenticated UI.
            //updateUI(account);
            //Deleted above beacuse of fireBaseAuthWithGoogle
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            //Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }

    private void handleFacebookAccessToken(AccessToken token) {
        //Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LaunchPageActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    //____________________________________________________________________________________________________________//
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        //Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Snackbar.make(findViewById(R.id.artwok_load_page), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }
    //^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^//


    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, GOOGLE_SIGN_IN);
        //Above prompts user to select a Google account to sign in with
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.google_sign_in_button:
                //Intent intent = new Intent(getApplicationContext(), HomePageActivity.class);
                //startActivity(intent);
                signIn();
                break;
            case R.id.create_account:
                Intent intentTwo = new Intent(getApplicationContext(), CreateAccountActivity.class);
                startActivity(intentTwo);
        }
    }


//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.z_activity_launch_page);
//
//        //fbLoginButton = findViewById(R.id.facebook_button);
//        google_login = findViewById(R.id.google_button);
//        loginButton = findViewById(R.id.login_button);
//        searchBar = findViewById(R.id.et_mainsearchbar);
//
//        loginButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view)
//            {
//                Intent intent = new Intent(getApplicationContext(), LoginActivityOld.class);
//                startActivity(intent);
//            }
//        });
//
/////////////////////////////////////////DO NOT TOUCH BELOW -- IT'S ALL AUTHENTICATION CODE //////////////////////////////////////
/////////////////////////////////////////DO NOT TOUCH BELOW -- IT'S ALL AUTHENTICATION CODE //////////////////////////////////////
/////////////////////////////////////////DO NOT TOUCH BELOW -- IT'S ALL AUTHENTICATION CODE //////////////////////////////////////
/////////////////////////////////////////DO NOT TOUCH BELOW -- IT'S ALL AUTHENTICATION CODE //////////////////////////////////////
/////////////////////////////////////////DO NOT TOUCH BELOW -- IT'S ALL AUTHENTICATION CODE //////////////////////////////////////
/////////////////////////////////////////DO NOT TOUCH BELOW -- IT'S ALL AUTHENTICATION CODE //////////////////////////////////////
/////////////////////////////////////////DO NOT TOUCH BELOW -- IT'S ALL AUTHENTICATION CODE //////////////////////////////////////
/////////////////////////////////////////DO NOT TOUCH BELOW -- IT'S ALL AUTHENTICATION CODE //////////////////////////////////////
/////////////////////////////////////////DO NOT TOUCH BELOW -- IT'S ALL AUTHENTICATION CODE //////////////////////////////////////
/////////////////////////////////////////DO NOT TOUCH BELOW -- IT'S ALL AUTHENTICATION CODE //////////////////////////////////////
//
//        mAuth = FirebaseAuth.getInstance();
//
//        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions
//                .Builder()
//                .requestIdToken(getString(R.string.default_web_client_id))
//                .requestEmail()
//                .build();
//
//        mGoogleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);
//        google_login.setOnClickListener(v -> SignInGoogle());
//
//        if(mAuth.getCurrentUser() != null){
//            FirebaseUser user = mAuth.getCurrentUser();
//            updateUI(user);
//        }
//
//        callbackManager = CallbackManager.Factory.create();
//        fbLoginButton.setReadPermissions(Arrays.asList("email", "public_profile"));
//
//        fbLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//
//            }
//
//            @Override
//            public void onCancel() {
//
//            }
//
//            @Override
//            public void onError(FacebookException error) {
//
//            }
//        });
//
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
//        callbackManager.onActivityResult(requestCode, resultCode, data);
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if(requestCode == GOOGLE_SIGN){
//            Task<GoogleSignInAccount> task = GoogleSignIn
//                    .getSignedInAccountFromIntent(data);
//
//            try{
//                GoogleSignInAccount account = task.getResult(ApiException.class);
//                if(account != null) firebaseAuthWithGoogle(account);
//            } catch(ApiException e){
//                e.printStackTrace();
//            }
//        }
//    }
//
//    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
//        Log.d("TAG", "firebaseAuthWithGoogle: " + account.getId());
//
//        AuthCredential credential = GoogleAuthProvider
//                .getCredential(account.getIdToken(), null);
//
//        mAuth.signInWithCredential(credential)
//                .addOnCompleteListener(this, task -> {
//                 if(task.isSuccessful()){
//                     Log.d("TAG", "signin sucess");
//                     FirebaseUser user = mAuth.getCurrentUser();
//                     updateUI(user);
//                 }else{
//                     Log.w("TAG", "signin failure", task.getException());
//
//                     Toast.makeText(this, "SignIn Failed!", Toast.LENGTH_SHORT);
//                     updateUI(null);
//                 }
//                });
//    }
//
//    private void updateUI(FirebaseUser user) {
//        if(user != null){
//              Intent intent = new Intent(getApplicationContext(), HomePageActivity.class);
//              startActivity(intent);
//    //         String name = user.getDisplayName();
////            String email = user.getEmail();
////            String photo = String.valueOf(user.getPhotoUrl());
//        }else{
//
//        }
//    }
//
//    void Logout(){
//        FirebaseAuth.getInstance().signOut();
//        mGoogleSignInClient.signOut()
//                .addOnCompleteListener(this,
//                        task -> {updateUI(null);
//                });
//    }
//
//    AccessTokenTracker tokenTracker = new AccessTokenTracker() {
//        @Override
//        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
//            if(currentAccessToken == null){
////                txtName.setText("");
////                txtEmail.setText("");
//                Toast.makeText(LaunchPageActivity.this, "User Logged out", Toast.LENGTH_LONG).show();
//            }else{
//                loadUserProfile(currentAccessToken);
//            }
//        }
//    };
//
//    private void loadUserProfile(AccessToken newAccessToken){
//        GraphRequest request = GraphRequest.newMeRequest(newAccessToken, new GraphRequest.GraphJSONObjectCallback() {
//            @Override
//            public void onCompleted(JSONObject object, GraphResponse response) {
//                try {
//                    String first_name = object.getString("first");
//                    String last_name = object.getString("last_name");
//                    String email = object.getString("email");
//                    String id = object.getString("id");
//
////                    txtEmail.setText(email);
////                    txtName.setText(first_name +" "+ last_name);
//                    RequestOptions requestOptions = new RequestOptions();
//                    requestOptions.dontAnimate();
//
////                    Glide.with(LaunchPageActivity.this).load(image_url).into(circleImageView);
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//
//        Bundle parameters = new Bundle();
//        parameters.putString("fields", "first_name,last_name,email,id,");
//        request.setParameters(parameters);
//        request.executeAsync();
//    }
//
//    void SignInGoogle(){
//        Intent signIntent = mGoogleSignInClient.getSignInIntent();
//        startActivityForResult(signIntent, GOOGLE_SIGN);
//    }

}
