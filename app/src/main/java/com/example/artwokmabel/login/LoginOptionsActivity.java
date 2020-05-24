package com.example.artwokmabel.login;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.artwokmabel.R;
import com.example.artwokmabel.databinding.ActivityLoginOptionsBinding;
import com.example.artwokmabel.HomePageActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginOptionsActivity extends AppCompatActivity {

    private final String TAG = "LoginOptionsActivity";
    private ActivityLoginOptionsBinding binding;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login_options);


        binding.setOncreateacctclicked(new OnCreateAccountClicked());
        binding.setOnloginclicked(new OnLoginClicked());
        binding.setOngoogleloginclicked(new OnGoogleLoginClicked());

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        if(currentUser != null || account != null){
            Intent intent = new Intent(getApplicationContext(), HomePageActivity.class);
            startActivity(intent);
        }
    }

    public class OnCreateAccountClicked{
        public void onCreateAccountClicked(){
            Intent intent = new Intent(getApplicationContext(), CreateAccountEmailActivity.class);

            // create an animation effect sliding from left to right
            ActivityOptions activityOptions = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                activityOptions = ActivityOptions.makeCustomAnimation(binding.getRoot().getContext(), R.anim.fromright,R.anim.toleft);
                startActivity(intent,activityOptions.toBundle());
            } else {
                startActivity(intent);
            }
        }
    }

    public class OnLoginClicked{
        public void onLoginClicked(){
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);

            // create an animation effect sliding from left to right
            ActivityOptions activityOptions = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                activityOptions = ActivityOptions.makeCustomAnimation(binding.getRoot().getContext(), R.anim.fromright,R.anim.toleft);
                startActivity(intent,activityOptions.toBundle());
            } else {
                startActivity(intent);
            }
        }
    }

    public class OnGoogleLoginClicked{
        public void onGoogleLoginClicked(){
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, 123);
        }
    }
}
