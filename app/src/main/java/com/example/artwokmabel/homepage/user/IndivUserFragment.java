package com.example.artwokmabel.homepage.user;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.artwokmabel.profile.user.DashboardFragment;
import com.example.artwokmabel.profile.user.ProfileFragmentPagerAdapter;
import com.example.artwokmabel.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

public class IndivUserFragment extends Fragment {

    private TabLayout tablayout;
    private ViewPager viewPager;
    private TextView profileDesc, userUsername;
    private ImageView profilePic;
    private  FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private ProfileFragmentPagerAdapter adapter;

    private String userProfileUrl;
    private String userProfileDesc;
    private String retrievedUsername;
    private String userId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.fragment_indiv_user, container, false);

        db = FirebaseFirestore.getInstance();
        tablayout = view.findViewById(R.id.profile_tab);
        viewPager =  view.findViewById(R.id.profile_viewpager);
        profilePic = view.findViewById(R.id.profile_picture);
        profileDesc = view.findViewById(R.id.profile_desc);
        userUsername = view.findViewById(R.id.user_username);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.indiv_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = this.getArguments();
        retrievedUsername = bundle.getString("poster_username");
        userUsername.setText(retrievedUsername);
        retrieveFirebase(retrievedUsername);

        adapter = new ProfileFragmentPagerAdapter(getChildFragmentManager());

        return view;
    }


    protected void retrieveFirebase(String posterUsername) {
        Log.d("hello", posterUsername);

        db.collection("Users")
                .whereEqualTo("username", posterUsername)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("hello", document.getData().toString());
                                Log.d("know", document.toString());
                                userId = document.getId();

                                if(document.getData().get("profile_desc") != null){
                                    userProfileDesc = document.getData().get("profile_desc").toString();
                                    profileDesc.setText(userProfileDesc);
                                }else{
                                    profileDesc.setText("FUCK FUCKF UC");
                                }

                                if(document.getData().get("profile_url") != null){
                                    userProfileUrl = document.getData().get("profile_url").toString();
                                    Picasso.get().load(userProfileUrl).into(profilePic);
                                }
                            }

                            // Adding Fragments
                            IndivUserListingsFragment myListingsFragment = new IndivUserListingsFragment();
                            IndivUserPostsFragment myPostsFragment = new IndivUserPostsFragment();

                            Bundle args = new Bundle();
                            args.putString("indiv_user_id", userId);
                            args.putString("indiv_username", posterUsername);

                            myListingsFragment.setArguments(args);
                            adapter.AddFragment(myListingsFragment,"Listings");

                            myPostsFragment.setArguments(args);
                            adapter.AddFragment(myPostsFragment, "Posts");
                            adapter.AddFragment(new DashboardFragment(),"Dashboard");

                            // Adapter setup
                            viewPager.setAdapter(adapter);
                            tablayout.setupWithViewPager(viewPager);
                        } else {
                            Log.d("Edit Categories", "get failed with ", task.getException());
                        }
                    }
                });
    }
}
