package com.example.artwokmabel;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.artwokmabel.chat.MessageFragment;
import com.example.artwokmabel.homepage.homepagewrapper.HomeTabsFragment;
import com.example.artwokmabel.notifs.NotifsFragment;
import com.example.artwokmabel.profile.user.ProfileFragment;
import com.example.artwokmabel.repos.FirestoreRepo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firestore.v1.FirestoreGrpc;


public class HomePageActivity extends AppCompatActivity {

    private static HomePageActivity instance = null;
    public BottomNavigationView navView;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        instance = this;
        mAuth = FirebaseAuth.getInstance();

        navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navView.setSelectedItemId(R.id.main_home_fragment);

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("westchester", "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        Log.d("westchester", token);
                        FirestoreRepo.getInstance().addTokenToFirestore(token);
                    }
                });

        if(savedInstanceState == null){
            loadFragment(new HomeTabsFragment());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(getIntent().getStringExtra("fromWho") != null) {
            if (getIntent().getStringExtra("fromWho").equals("editProfile")){
                navView.setSelectedItemId(R.id.main_profile_fragment);
            }
        }
    }

    public static HomePageActivity getInstance(){
        return instance;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
                Fragment fragment = null;
                switch (item.getItemId()) {
                    case R.id.main_chat_fragment:
                        fragment=new MessageFragment();
                        loadFragment(fragment);
                        return true;
                    case R.id.main_home_fragment:
                        fragment=new HomeTabsFragment();
                        loadFragment(fragment);
                        return true;
                    case R.id.main_notifs_fragment:
                        fragment=new NotifsFragment();
                        loadFragment(fragment);
                        return true;
                    case R.id.main_profile_fragment:
                        fragment = new ProfileFragment(mAuth.getCurrentUser().getUid());
                        loadFragment(fragment);
                        return true;
                }
                return false;
            };

    public void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}