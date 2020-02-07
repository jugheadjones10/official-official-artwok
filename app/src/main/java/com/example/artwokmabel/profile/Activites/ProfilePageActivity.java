package com.example.artwokmabel.profile.Activites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.artwokmabel.Auth.LoginActivity;
import com.example.artwokmabel.profile.ProfileViewPagerAdapter;
import com.example.artwokmabel.profile.fragments.DashboardFragment;
import com.example.artwokmabel.profile.fragments.ListingsFragment;
import com.example.artwokmabel.profile.fragments.PostsFragment;
import com.example.artwokmabel.R;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfilePageActivity extends AppCompatActivity {

    private TabLayout tablayout;
    private ViewPager viewPager;
    private TextView profileDesc;
    private ImageView profilePic;
    private RecyclerView postRecyclerView;
    private DatabaseReference postDatabaseRef;
    private FirebaseFirestore firebaseFirestore;
    private String[] profileuid = {""};
    FirebaseAuth mAuth;
    FirebaseFirestore db;

    FirebaseUser currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_profile_fragment);

        tablayout = (TabLayout) findViewById(R.id.profile_tab);
        viewPager = (ViewPager) findViewById(R.id.profile_viewpager);
        profileDesc = findViewById(R.id.profile_desc);
        mAuth = FirebaseAuth.getInstance();
        profilePic = findViewById(R.id.profile_picture);

        FirebaseUser currentUser = mAuth.getCurrentUser();

        DocumentReference docRef = firebaseFirestore.collection("Users").document(LoginActivity.uid);

        /*
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("LOG: ", "DocumentSnapshot data: " + document.getData());
                        String name = "profile_uid";
                        profileuid[0] = ((String) document.get(name));
                        Picasso.get().load(profileuid[0]).into(profilePic);

                    } else {
                        Log.d("LOG: ", "No such document");
                    }
                } else {
                    Log.d("LOG: ", "get failed with ", task.getException());
                }
            }
        });
*/
        ProfileViewPagerAdapter adapter = new ProfileViewPagerAdapter(getSupportFragmentManager());
        profileDesc.setText(retrieveDesc());

        // Adding Fragments
        adapter.AddFragment(new ListingsFragment(),"Listings");
        adapter.AddFragment(new PostsFragment(),"Posts");
        adapter.AddFragment(new DashboardFragment(),"Dashboard");

        // Adapter setup
        viewPager.setAdapter(adapter);
        tablayout.setupWithViewPager(viewPager);

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
