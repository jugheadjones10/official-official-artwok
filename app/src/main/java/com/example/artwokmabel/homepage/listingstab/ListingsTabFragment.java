package com.example.artwokmabel.homepage.listingstab;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.artwokmabel.R;
import com.example.artwokmabel.databinding.FragmentListingsTabBinding;
import com.example.artwokmabel.homepage.adapters.ListingsAdapter;
import com.example.artwokmabel.models.Listing;

import java.util.List;

public class ListingsTabFragment extends Fragment {

    private ListingsTabViewModel viewModel;
    private ListingsAdapter listingsAdapter;
    private FragmentListingsTabBinding binding;
    private String cat;
    //Todo: add horizontal scrollable listings

    private NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_listings_tab, container, false);

        binding.giftRecyclerview.setHasFixedSize(true);
        binding.giftRecyclerview.setLayoutManager(new GridLayoutManager(getContext(), 2));

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Below is a tricky line - take note of it. Why won't it work with the single argument version of findNavController?
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_container);

        listingsAdapter = new ListingsAdapter(getContext(), navController);
        binding.giftRecyclerview.setAdapter(listingsAdapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        cat = getArguments().getString("category");

        ListingsTabViewModelFactory modelFactory = new ListingsTabViewModelFactory(cat);
        viewModel = ViewModelProviders.of(this, modelFactory).get(ListingsTabViewModel.class);
        viewModel.setNavController(navController);

        observeViewModel(viewModel);
    }


    private void observeViewModel(ListingsTabViewModel viewModel) {
        // Update the list when the data changes
        viewModel.getListingsObeservable().observe(getViewLifecycleOwner(), new Observer<List<Listing>>() {
            @Override
            public void onChanged(@Nullable List<Listing> listings) {
                if (listings != null) {
                    listingsAdapter.setListingsList(listings);
                }
            }
        });
    }

}
