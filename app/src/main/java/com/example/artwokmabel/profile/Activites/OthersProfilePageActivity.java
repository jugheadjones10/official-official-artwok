package com.example.artwokmabel.profile.Activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.artwokmabel.auth.LoginActivity;
import com.example.artwokmabel.profile.ProfileViewPagerAdapter;
import com.example.artwokmabel.profile.fragments.DashboardFragment;
import com.example.artwokmabel.profile.fragments.ListingsFragment;
import com.example.artwokmabel.profile.fragments.PostsFragment;
import com.example.artwokmabel.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class OthersProfilePageActivity extends AppCompatActivity {


    private TabLayout tablayout;
    private ViewPager viewPager;
    private ImageButton profileGroup, report;
    private Button messageBtn, followBtn;
    private TextView profileDesc;

    private RecyclerView postRecyclerView;
    private DatabaseReference postDatabaseRef;
    private FirebaseFirestore firebaseFirestore;
    FirebaseAuth mAuth;

    FirebaseUser currentUser;

    private ArrayList<String> following;
    private ArrayList<String> followers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_others_profile_fragment);

        tablayout = (TabLayout) findViewById(R.id.profile_tab);
        viewPager = (ViewPager) findViewById(R.id.profile_viewpager);
        profileGroup = findViewById(R.id.profile_group);
        profileDesc = findViewById(R.id.profile_desc);
        report = findViewById(R.id.report);
        messageBtn = findViewById(R.id.messageBtn);
        followBtn = findViewById(R.id.followBtn);

        mAuth = FirebaseAuth.getInstance();

        ProfileViewPagerAdapter adapter = new ProfileViewPagerAdapter(getSupportFragmentManager());
        profileDesc.setText(retrieveDesc());

        // Adding Fragments
        adapter.AddFragment(new ListingsFragment(),"Listings");
        adapter.AddFragment(new PostsFragment(),"Posts");
        adapter.AddFragment(new DashboardFragment(),"Dashboard");

        // Adapter setup
        viewPager.setAdapter(adapter);
        tablayout.setupWithViewPager(viewPager);

        profileGroup.setVisibility(View.INVISIBLE);
        profileGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {}
        });

        report.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

            }
        });

        messageBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

            }
        });

        followBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String target = "";

                DocumentReference docRef = firebaseFirestore.collection("Users").document(LoginActivity.uid);
                DocumentReference docRef2 = firebaseFirestore.collection("Users").document(target);

                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d("LOG: ", "DocumentSnapshot data: " + document.getData());
                                following = (ArrayList<String>) document.get("following");
                                following.add(target);
                            } else {
                                Log.d("LOG: ", "No such document");
                            }
                        } else {
                            Log.d("LOG: ", "get failed with ", task.getException());
                        }
                    }
                });

                docRef2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d("LOG: ", "DocumentSnapshot data: " + document.getData());
                                followers = (ArrayList<String>) document.get("followers");
                                followers.add(LoginActivity.uid);
                            } else {
                                Log.d("LOG: ", "No such document");
                            }
                        } else {
                            Log.d("LOG: ", "get failed with ", task.getException());
                        }
                    }
                });

                firebaseFirestore
                        .collection("Users")
                        .document(LoginActivity.uid)
                        .update("following", following);

                firebaseFirestore
                        .collection("Users")
                        .document(LoginActivity.uid)
                        .update("followers", followers);
            }
        });
    }

    protected String retrieveDesc() {
        if (false) {
            return "Firebase string";
        } else {
            return "I'm an avid user of Artwok! Come check out my awesome listings and posts." +
                    " Feel free to drop me a message to contact me :)";
        }
    }
}
