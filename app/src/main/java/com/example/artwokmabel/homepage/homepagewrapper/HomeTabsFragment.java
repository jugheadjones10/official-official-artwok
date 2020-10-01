package com.example.artwokmabel.homepage.homepagewrapper;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.artwokmabel.R;
import com.example.artwokmabel.databinding.FragmentHomeTabsBinding;
import com.example.artwokmabel.homepage.listingstab.ShrinkListingsAnimation;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

public class HomeTabsFragment extends Fragment {

    public FragmentHomeTabsBinding binding;
    private HomeTabsPagerAdapter adapter;
    private NavController navController;

    private ArrayList<String> tabCategories;
    private static HomeTabsFragment instance = null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        instance = this;

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home_tabs, container, false);
        binding.setHomeTabsFragment(this);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        instance = this;
        navController = Navigation.findNavController(view);
        adapter = new HomeTabsPagerAdapter(this);

        binding.viewPager.setAdapter(adapter);
        binding.viewPager.setPageTransformer(new ShrinkListingsAnimation());

        //Prevent reloading of WebView
        binding.viewPager.setOffscreenPageLimit(10);

        binding.toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.search:
                        navController.navigate(R.id.action_home_graph_to_temporarySearchFragment);
                        return true;
                    case R.id.favorite:
                        navController.navigate(R.id.action_home_graph_to_favoritesFragment);
                        return true;
                    default:
                        return false;
                }
            }
        });

        new TabLayoutMediator(binding.tabs, binding.viewPager,
                (tab, position) -> tab.setText(tabCategories.get(position))
        ).attach();

        binding.tabs.setTabMode(TabLayout.MODE_SCROLLABLE);
//        setUpClickListeners();

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                //Somehow adding the below code solved not only the "goes to splash instead of out problem"
                //but also the start screen goes to splash and stays there problem. Why?
                Intent a = new Intent(Intent.ACTION_MAIN);
                a.addCategory(Intent.CATEGORY_HOME);
                a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(a);
            }
        });
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
        tabCategories.add("Feed");
        tabCategories.add("gifts");
        tabCategories.add("handmade");
        tabCategories.add("fashion");
        tabCategories.add("delights");
        tabCategories.add("services");
        tabCategories.add("requests");
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


    public void onFavClicked(){
        navController.navigate(R.id.action_home_graph_to_favoritesFragment);
    }

    public void onSearchClicked(){
        navController.navigate(R.id.action_home_graph_to_temporarySearchFragment);
    }

//    private void setUpClickListeners(){
//        binding.favBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(getActivity(), FavoritesActivity.class);
//                startActivity(i);
//            }
//        });
//
//        binding.mainsearchbar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                navController.navigate(R.id.action_home_graph_to_temporarySearchFragment);
//            }
//        });
//    }
}
