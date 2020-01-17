package com.example.artwokmabel.homepage.activities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.artwokmabel.homepage.adapters.FavouritesAdapter;
import com.example.artwokmabel.homepage.fragments.FavoritesFragmentListings;
import com.example.artwokmabel.homepage.fragments.FavoritesFragmentPosts;
import com.example.artwokmabel.homepage.fragments.FavoritesFragmentRequests;
import com.example.artwokmabel.R;
import com.google.android.material.tabs.TabLayout;

public class FavouritesActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);

        tabLayout = findViewById(R.id.fav_tabs);
        viewPager = findViewById(R.id.fav_viewpager);

        FavouritesAdapter adapter = new FavouritesAdapter(this.getSupportFragmentManager());

        // Adding Fragments
        adapter.addFragment(new FavoritesFragmentListings(),"Listings");
        adapter.addFragment(new FavoritesFragmentPosts(),"Posts");
        adapter.addFragment(new FavoritesFragmentRequests(),"Requests");

        // Adapter Setup
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

    }

}
