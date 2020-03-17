package com.example.artwokmabel.homepage.homepagewrapper;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.artwokmabel.R;
import com.example.artwokmabel.databinding.FragmentHomeTabsBinding;
import com.example.artwokmabel.homepage.favorites.FavoritesActivity;
import com.example.artwokmabel.homepage.search.TemporarySearchFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

public class HomeTabsFragment extends Fragment {

    private HomeTabsPagerAdapter adapter;
    private FragmentHomeTabsBinding binding;
    private ArrayList<String> tabCategories;
    private static HomeTabsFragment instance = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home_tabs, container, false);
        instance = this;
        ((AppCompatActivity)getActivity()).setSupportActionBar(binding.toolbar);

        adapter = new HomeTabsPagerAdapter(this);
        binding.viewPager.setAdapter(adapter);


        new TabLayoutMediator(binding.tabs, binding.viewPager,
                (tab, position) -> tab.setText(tabCategories.get(position))
        ).attach();

//        new TabLayoutMediator(binding.tabs, binding.viewPager,
//                new TabLayoutMediator.TabConfigurationStrategy() {
//                    @Override
//                    public void onConfigureTab(TabLayout.Tab tab, int position) {
//                        if(position == 0){
//                            tab.setText("Following");
//                        }else{
//                            tab.setText("Followers");
//                        }
//                    }
//                }
//        ).attach();
//        new TabLayoutMediator(binding.tabs, binding.viewPager,
//                (tab, position) -> tab.setText(tabCategories.get(position))
//        ).attach();

        binding.tabs.setTabMode(TabLayout.MODE_SCROLLABLE);
        setUpClickListeners();

        return binding.getRoot();
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final HomeTabsViewModel viewModel =
                ViewModelProviders.of(this).get(HomeTabsViewModel.class);

        observeViewModel(viewModel);
        //binding.tabs.setupWithViewPager(binding.viewPager);
        //        //binding.tabs.getTabAt(mSectionsPagerAdapter.getCount() - 1).setIcon(R.drawable.add);
    }

    private void observeViewModel(HomeTabsViewModel viewModel) {
        // Update the list when the data changes
        viewModel.getCategoryListObservable().observe(this, new Observer<ArrayList<String>>() {
            @Override
            public void onChanged(@Nullable ArrayList<String> categories) {
                if (categories != null) {

                    categories.add(0, "feed");
                    categories.add("edit");

//                    for(String cat : categories){
//                        Log.d("catfight", "From data base  " + cat);
//                    }
//
//                    if(viewModel.getCategoriesListMaintainable() != null){
//                        for(String cat :  viewModel.getCategoriesListMaintainable()){
//                            Log.d("catfight", "From view model " + cat);
//                        }
//                        Log.d("catfight", "are they indeed equal  " + categories.equals(viewModel.getCategoriesListMaintainable()));
//                        Log.d("catfight", "\n");
//                    }

                    if(viewModel.getCategoriesListMaintainable() == null || !viewModel.getCategoriesListMaintainable().equals(categories)){

                        tabCategories = categories;
                        viewModel.setCategoriesListMaintainable(categories);

                        adapter.setCategoriesList(categories);
                        binding.viewPager.setCurrentItem(0);
                    }

                }
            }
        });
    }

    public static HomeTabsFragment getInstance(){
        return instance;
    }

    public void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        //transaction.add(R.id.container, fragment);
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss();
    }

    private void setUpClickListeners(){
        binding.favBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), FavoritesActivity.class);
                startActivity(i);
            }
        });

        binding.mainsearchbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new TemporarySearchFragment());
            }
        });
    }
}
