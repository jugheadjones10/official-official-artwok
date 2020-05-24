package com.example.artwokmabel.login;

import android.app.ActivityOptions;
import android.content.Intent;
import android.view.View;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.artwokmabel.HomePageActivity;
import com.example.artwokmabel.R;
import com.example.artwokmabel.repos.FirestoreRepo;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginViewModel extends ViewModel {

    public enum AuthenticationState {
        UNAUTHENTICATED,        // Initial state, the user needs to authenticate
        AUTHENTICATED,          // The user has authenticated successfully
        INVALID_AUTHENTICATION  // Authentication failed
    }

    final MutableLiveData<AuthenticationState> authenticationState = new MutableLiveData<>();
    String email;

    public LoginViewModel() {

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        //Uncomment when starting to integrate google accounts
        //GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        //Add the following condition to the below if : || account != null
        if(currentUser != null){
           authenticationState.setValue(AuthenticationState.AUTHENTICATED);
        }else{
            authenticationState.setValue(AuthenticationState.UNAUTHENTICATED);
        }

        email = "";
    }

    public void authenticate(String email, String password) {
        FirestoreRepo.getInstance().logIntoAccount(email, password, (successful) -> {
            if(successful){
                this.email = email;
                authenticationState.setValue(AuthenticationState.AUTHENTICATED);
            }else{
                authenticationState.setValue(AuthenticationState.INVALID_AUTHENTICATION);
            }
        });
    }

    public void refuseAuthentication() {
        authenticationState.setValue(AuthenticationState.UNAUTHENTICATED);
    }

    //Thin about where to place interfaces
    public interface LoginCallback{
        void loginCallback(boolean successful);
    }

//    Intent intent = new Intent(requireActivity(), HomePageActivity.class);
//    // create an animation effect sliding from left to right
//    ActivityOptions activityOptions = null;
//                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
//        activityOptions = ActivityOptions.makeCustomAnimation(binding.getRoot().getContext(), R.anim.fromright,R.anim.toleft);
//        startActivity(intent,activityOptions.toBundle());
//    } else {
//        startActivity(intent);
//    }
}
