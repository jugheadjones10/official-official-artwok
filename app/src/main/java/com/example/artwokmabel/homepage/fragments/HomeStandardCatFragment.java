package com.example.artwokmabel.homepage.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.artwokmabel.R;
import com.example.artwokmabel.databinding.MainGiftsFragmentBinding;
import com.example.artwokmabel.homepage.adapters.ListingsAdapter;
import com.example.artwokmabel.homepage.models.Listing;

import java.util.List;

public class HomeStandardCatFragment extends Fragment {

    private HomeStandardCatViewModel viewModel;
    private ListingsAdapter listingsAdapter;
    private MainGiftsFragmentBinding binding;
    private String cat;
    //Todo: add horizontal scrollable listings

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.main_gifts_fragment, container, false);


        binding.giftRecyclerview.setHasFixedSize(true);
        binding.giftRecyclerview.setLayoutManager(new GridLayoutManager(getContext(), 2));

        listingsAdapter = new ListingsAdapter(getContext());
        binding.giftRecyclerview.setAdapter(listingsAdapter);

        return binding.getRoot();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        cat = getArguments().getString("category");

        HomeStandardCatFactory modelFactory = new HomeStandardCatFactory(cat);
        viewModel = ViewModelProviders.of(this, modelFactory).get(HomeStandardCatViewModel.class);

        observeViewModel(viewModel);
    }

    private void observeViewModel(HomeStandardCatViewModel viewModel) {
        // Update the list when the data changes
        viewModel.getListingsObeservable().observe(this, new Observer<List<Listing>>() {
            @Override
            public void onChanged(@Nullable List<Listing> listings) {
                if (listings != null) {
                    listingsAdapter.setListingsList(listings);
                }
            }
        });
    }

}
