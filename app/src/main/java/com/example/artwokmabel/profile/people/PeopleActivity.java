package com.example.artwokmabel.profile.people;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.artwokmabel.R;
import com.example.artwokmabel.databinding.ActivityPeopleBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class PeopleActivity extends AppCompatActivity {

    private PeoplePagerAdapter adapter;
    private ActivityPeopleBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_people);

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        adapter = new PeoplePagerAdapter(this);
        binding.viewPager.setAdapter(adapter);

        new TabLayoutMediator(binding.tabs, binding.viewPager,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(TabLayout.Tab tab, int position) {
                        if(position == 0){
                            tab.setText("Following");
                        }else{
                            tab.setText("Followers");
                        }
                    }
                }
        ).attach();
        binding.tabs.setTabMode(TabLayout.MODE_SCROLLABLE);
    }
}
