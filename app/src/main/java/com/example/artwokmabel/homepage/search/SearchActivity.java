package com.example.artwokmabel.homepage.search;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.artwokmabel.R;
import com.example.artwokmabel.databinding.ActivitySearchBinding;
import com.example.artwokmabel.homepage.search.tabs.SearchListingsFragment;
import com.example.artwokmabel.homepage.search.tabs.SearchUsersFragment;
import com.example.artwokmabel.models.Listing;
import com.example.artwokmabel.models.User;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private ActivitySearchBinding binding;
    private SearchPagerAdapter adapter;
    private SearchActivityViewModel viewModel;
    private List<User> myFollowings = new ArrayList<>();

    private static SearchActivity instance;

    public static SearchActivity getInstance(){
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_search);

        instance = this;

        setSupportActionBar(binding.searchResultsToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        adapter = new SearchPagerAdapter(this);
        binding.searchViewPager.setAdapter(adapter);

        new TabLayoutMediator(binding.searchResultsTabs, binding.searchViewPager,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(TabLayout.Tab tab, int position) {
                        if(position == 0){
                            tab.setText("Listings");
                        }else if(position == 1){
                            tab.setText("Users");
                        } else{
                            tab.setText("Posts");
                        }
                    }
                }
        ).attach();
        binding.searchResultsTabs.setTabMode(TabLayout.MODE_SCROLLABLE);

        viewModel = ViewModelProviders.of(this).get(SearchActivityViewModel.class);
        //observeViewModel(viewModel);

        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                callSearch(newText, binding.searchViewPager.getCurrentItem());
                return true;
            }
        });

        viewModel.getFollowingsObservable().observe(SearchActivity.this, new Observer<List<User>>() {
            @Override
            public void onChanged(@Nullable List<User> users) {
                if (users != null) {
                    myFollowings = users;
                }
            }
        });

    }

    public void callSearch(String newText, int current){

        if(current == 0){
            viewModel.getSearchedListings(newText).observe(SearchActivity.this, new Observer<List<Listing>>() {
                @Override
                public void onChanged(@Nullable List<Listing> listings) {
                    if (listings != null) {
                        SearchListingsFragment.getInstance().adapter.setListingsList(listings);
                    }
                }
            });
        } else if(current == 1){
            viewModel.getSearchedUsers(newText).observe(SearchActivity.this, new Observer<List<User>>() {
                @Override
                public void onChanged(@Nullable List<User> users) {
                    if (users != null) {
                        SearchUsersFragment.getInstance().adapter.setFollowingsList(myFollowings);
                        SearchUsersFragment.getInstance().adapter.setFollowersList(users);
                    }
                }
            });
        }
    }

}
