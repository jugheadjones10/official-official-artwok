package com.example.artwokmabel.homepage.search.tabs;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.artwokmabel.R;
import com.example.artwokmabel.databinding.FragmentSearchListingsBinding;
import com.example.artwokmabel.homepage.adapters.ListingsAdapter;
import com.example.artwokmabel.homepage.search.SearchActivity;
import com.example.artwokmabel.homepage.search.SearchFragment;

public class SearchListingsFragment extends Fragment {

    private FragmentSearchListingsBinding binding;
    public ListingsAdapter adapter;
    private NavController navController;
    private static SearchListingsFragment instance;

    public static SearchListingsFragment getInstance(){
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_listings, container, false);
        instance = this;

        binding.listingsList.setHasFixedSize(true);
        binding.listingsList.setLayoutManager(new GridLayoutManager(getContext(), 3));

        SearchFragment.getInstance().callSearch("", 0);

        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_container);

        adapter = new ListingsAdapter(getContext(), navController);
        binding.listingsList.setAdapter(adapter);
    }
}
