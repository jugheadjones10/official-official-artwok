package com.example.artwokmabel.profile.Activites;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.artwokmabel.R;
import com.example.artwokmabel.databinding.MainProfileFragment2Binding;
import com.example.artwokmabel.homepage.models.User;
import com.example.artwokmabel.profile.ProfileViewPagerAdapter;
import com.example.artwokmabel.profile.fragments.DashboardFragment;
import com.example.artwokmabel.profile.fragments.ListingsFragment;
import com.example.artwokmabel.profile.fragments.PostsFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class ProfilePageFragment2 extends Fragment {

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

    private ProfilePageActivityViewModel viewModel;
    private MainProfileFragment2Binding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.main_profile_fragment_2, container, false);
        binding.profileTab.bringToFront();

        ProfileViewPagerAdapter adapter = new ProfileViewPagerAdapter(getChildFragmentManager());
        adapter.AddFragment(new ListingsFragment(),"Listings");
        adapter.AddFragment(new PostsFragment(),"Posts");
        adapter.AddFragment(new DashboardFragment(),"Dashboard");

        // Adapter setup
        binding.profileViewpager.setAdapter(adapter);
        binding.profileTab.setupWithViewPager(binding.profileViewpager);

        viewModel = ViewModelProviders.of(this).get(ProfilePageActivityViewModel.class);
        viewModel.getUserObservable().observe(this, new Observer<User>() {
            @Override
            public void onChanged(@Nullable User user) {
                binding.setUser(user);
                Picasso.get().load(user.getProfile_url()).into(binding.profilePicture);
            }
        });

        viewModel.getNumUserListings().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer numListings) {
                binding.setNumlistings(numListings);
            }
        });

        viewModel.getNumUserPosts().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer numPosts) {
                binding.setNumposts(numPosts);
            }
        });

        return binding.getRoot();
    }
}
