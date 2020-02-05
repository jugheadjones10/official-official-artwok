package com.example.artwokmabel;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.artwokmabel.chat.fragments.MessageFragment;
import com.example.artwokmabel.groups.GroupsFragment;
import com.example.artwokmabel.homepage.homepagewrapper.HomeTabsFragment;
import com.example.artwokmabel.notifs.NotifsFragment;
import com.example.artwokmabel.profile.user.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class HomePageActivity extends AppCompatActivity {

    private static HomePageActivity instance = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        instance = this;

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navView.setSelectedItemId(R.id.main_home_fragment);

        if(savedInstanceState == null){
            loadFragment(new HomeTabsFragment());
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
                        fragment = new ProfileFragment();
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