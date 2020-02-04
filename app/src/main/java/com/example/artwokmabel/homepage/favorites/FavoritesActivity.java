package com.example.artwokmabel.homepage.favorites;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.artwokmabel.R;
import com.google.android.material.tabs.TabLayout;

public class FavoritesActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        tabLayout = findViewById(R.id.fav_tabs);
        viewPager = findViewById(R.id.fav_viewpager);

        FavoritesPagerAdapter adapter = new FavoritesPagerAdapter(this.getSupportFragmentManager());

        // Adding Fragments
        adapter.addFragment(new FavoriteListingsFragment(),"Listings");
        adapter.addFragment(new FavoritePostsFragment(),"Posts");
        adapter.addFragment(new FavoriteRequestsFragment(),"Requests");

        // Adapter Setup
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

    }

}
