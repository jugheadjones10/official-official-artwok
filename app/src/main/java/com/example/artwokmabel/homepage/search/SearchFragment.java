package com.example.artwokmabel.homepage.search;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.artwokmabel.R;
import com.example.artwokmabel.databinding.FragmentSearchBinding;
import com.example.artwokmabel.homepage.search.tabs.SearchListingsFragment;
import com.example.artwokmabel.homepage.search.tabs.SearchUsersFragment;
import com.example.artwokmabel.models.Listing;
import com.example.artwokmabel.models.User;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    private FragmentSearchBinding binding;
    private SearchPagerAdapter adapter;
    private SearchActivityViewModel viewModel;
    private NavController navController;
    private List<User> myFollowings = new ArrayList<>();

    private static SearchFragment instance;

    public static SearchFragment getInstance() {
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        instance = this;

        navController = Navigation.findNavController(view);

        ((AppCompatActivity)getActivity()).setSupportActionBar(binding.searchResultsToolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
                        }
//                        else{
//                            tab.setText("Posts");
//                        }
                    }
                }
        ).attach();
        binding.searchResultsTabs.setTabMode(TabLayout.MODE_SCROLLABLE);

        viewModel = ViewModelProviders.of(this).get(SearchActivityViewModel.class);

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

        viewModel.getFollowingsObservable().observe(getViewLifecycleOwner(), new Observer<List<User>>() {
            @Override
            public void onChanged(@Nullable List<User> users) {
                if (users != null) {
                    myFollowings = users;
                }
            }
        });

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(),
                new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        navController.navigate(R.id.action_searchFragment_to_home_graph);
                    }
                });

        binding.searchResultsToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_searchFragment_to_home_graph);
            }
        });
    }


    public void callSearch(String newText, int current){

        if(current == 0){
            viewModel.getSearchedListings(newText).observe(SearchFragment.this, new Observer<List<Listing>>() {
                @Override
                public void onChanged(@Nullable List<Listing> listings) {
                    if (listings != null) {
                        SearchListingsFragment.getInstance().adapter.setListingsList(listings);
                    }
                }
            });
        } else if(current == 1){
            viewModel.getSearchedUsers(newText).observe(SearchFragment.this, new Observer<List<User>>() {
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
