package com.example.artwokmabel.profile.fragments;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.artwokmabel.homepage.Activities.SettingsActivity;
import com.example.artwokmabel.profile.ProfileViewPagerAdapter;
import com.example.artwokmabel.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment {
    private TabLayout tablayout;
    private ViewPager viewPager;
    private TextView profileDesc, userUsername;
    private ImageView profilePic;
    private View view;
    private ImageButton settingsbtn;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.main_profile_fragment, container, false);

        tablayout = (TabLayout) view.findViewById(R.id.profile_tab);
        viewPager = (ViewPager) view.findViewById(R.id.profile_viewpager);
        profileDesc = view.findViewById(R.id.profile_desc);
        profilePic = view.findViewById(R.id.profile_picture);
        userUsername = view.findViewById(R.id.user_username);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        settingsbtn = view.findViewById(R.id.profile_settings);

        ProfileViewPagerAdapter adapter = new ProfileViewPagerAdapter(getChildFragmentManager());
        profileDesc.setText(retrieveDesc());

        db.collection("Users")
                .document(currentUser.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            // Document found in the offline cache
                            DocumentSnapshot document = task.getResult();
                            Picasso.get().load(document.get("profile_url").toString()).into(profilePic);
                            userUsername.setText(document.get("username").toString());
                        } else {
                        }
                    }
                });



        // Adding Fragments
        adapter.AddFragment(new ListingsFragment(),"Listings");
        adapter.AddFragment(new PostsFragment(),"Posts");
        adapter.AddFragment(new DashboardFragment(),"Dashboard");

        // Adapter setup
        viewPager.setAdapter(adapter);
        tablayout.setupWithViewPager(viewPager);

        settingsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // when the user presses on one of the items from friend list, a profile activity pops up
                Intent intent = new Intent(view.getContext(), SettingsActivity.class);
                // create an animation effect sliding from left to right
                ActivityOptions activityOptions = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    activityOptions = ActivityOptions.makeCustomAnimation(view.getContext(), R.anim.fromtop, R.anim.tobottom);
                    startActivity(intent, activityOptions.toBundle());
                } else {
                    startActivity(intent);
                }
            }
        });

        return view;
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
