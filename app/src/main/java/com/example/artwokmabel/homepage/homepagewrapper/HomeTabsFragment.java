package com.example.artwokmabel.homepage.homepagewrapper;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.artwokmabel.R;
import com.example.artwokmabel.databinding.FragmentHomeTabsBinding;
import com.example.artwokmabel.homepage.favorites.FavoritesActivity;
import com.example.artwokmabel.homepage.search.TemporarySearchFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

public class HomeTabsFragment extends Fragment {

    private FragmentHomeTabsBinding binding;
    private HomeTabsPagerAdapter adapter;

    private NavController navController;

    private ArrayList<String> tabCategories;
    private static HomeTabsFragment instance = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home_tabs, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        instance = this;
        navController = Navigation.findNavController(view);
        ((AppCompatActivity)getActivity()).setSupportActionBar(binding.toolbar);

        adapter = new HomeTabsPagerAdapter(this);

        binding.viewPager.setAdapter(adapter);
        new TabLayoutMediator(binding.tabs, binding.viewPager,
                (tab, position) -> tab.setText(tabCategories.get(position))
        ).attach();

        binding.tabs.setTabMode(TabLayout.MODE_SCROLLABLE);
        setUpClickListeners();
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

        tabCategories = new ArrayList<>();
        tabCategories.add("feed");
        tabCategories.add("gifts");
        adapter.setCategoriesList(tabCategories);


        // Update the list when the data changes
//        viewModel.getCategoryListObservable().observe(getViewLifecycleOwner(), new Observer<ArrayList<String>>() {
//            @Override
//            public void onChanged(@Nullable ArrayList<String> categories) {
//                if (categories != null) {
//
//                    Log.d("categories", "B4 adding feed and edit :" + categories.toString());
//                    if(!categories.get(0).equals("feed")) {
//                        categories.add(0, "feed");
//                        categories.add("edit");
//                    }
//
//                    //if(viewModel.getCategoriesListMaintainable() == null || !viewModel.getCategoriesListMaintainable().equals(categories)){
//                        tabCategories = categories;
//                        viewModel.setCategoriesListMaintainable(categories);
//                    //}
//
//                    adapter.setCategoriesList(tabCategories);
//                    //binding.viewPager.setCurrentItem(0);
//
//                }
//            }
//        });
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
                navController.navigate(R.id.action_home_graph_to_temporarySearchFragment);
                //loadFragment(new TemporarySearchFragment());
            }
        });
    }
}
