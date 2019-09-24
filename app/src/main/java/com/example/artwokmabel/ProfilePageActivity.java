package com.example.artwokmabel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;

public class ProfilePageActivity extends AppCompatActivity {

    private TabLayout tablayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        tablayout = (TabLayout) findViewById(R.id.profile_tab);
        viewPager = (ViewPager) findViewById(R.id.profile_viewpager);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        // Adding Fragments
        adapter.AddFragment(new ListingsFragment(),"Listings");
        adapter.AddFragment(new PostsFragment(),"Posts");
        adapter.AddFragment(new DashboardFragment(),"Dashboard");

        // Adapter setup
        viewPager.setAdapter(adapter);
        tablayout.setupWithViewPager(viewPager);

    }
}
