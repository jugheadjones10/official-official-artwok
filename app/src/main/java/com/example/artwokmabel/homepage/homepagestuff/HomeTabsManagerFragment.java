package com.example.artwokmabel.homepage.homepagestuff;

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
import com.example.artwokmabel.databinding.AltMainHomeFragmentBinding;
import com.example.artwokmabel.homepage.Activities.FavouritesActivity;
import com.example.artwokmabel.homepage.fragments.TemporarySearchFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

public class HomeTabsManagerFragment extends Fragment {

    private TabsPagerAdapter adapter;
    private AltMainHomeFragmentBinding binding;
    private ArrayList<String> tabCategories;
    private static HomeTabsManagerFragment instance = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.alt_main_home_fragment, container, false);
        instance = this;
        ((AppCompatActivity)getActivity()).setSupportActionBar(binding.toolbar);

        adapter = new TabsPagerAdapter(this);
        binding.viewPager.setAdapter(adapter);

        new TabLayoutMediator(binding.tabs, binding.viewPager,
                (tab, position) -> tab.setText(tabCategories.get(position))
        ).attach();
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

                    tabCategories = categories;
                    adapter.setCategoriesList(categories);

                    binding.viewPager.setCurrentItem(categories.size());
                }
            }
        });
    }


    public static HomeTabsManagerFragment getInstance(){
        return instance;
    }

    public void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void setUpClickListeners(){
        binding.favBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), FavouritesActivity.class);
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
