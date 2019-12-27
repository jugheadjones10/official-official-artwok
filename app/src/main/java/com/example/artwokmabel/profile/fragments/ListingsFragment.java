package com.example.artwokmabel.profile.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.artwokmabel.Auth.LoginActivity;
import com.example.artwokmabel.databinding.ActivityIndivListingNewBinding;
import com.example.artwokmabel.databinding.ActivityListingsFragmentBinding;
import com.example.artwokmabel.homepage.adapters.ListingsAdapter;
import com.example.artwokmabel.homepage.fragments.HomeStandardCatViewModel;
import com.example.artwokmabel.homepage.models.Listing;
import com.example.artwokmabel.profile.Activites.AddListingActivity;
import com.example.artwokmabel.R;
import com.example.artwokmabel.profile.Adapters.ProfileListingsAdapter;
import com.example.artwokmabel.profile.Models.ProfileListings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;


public class ListingsFragment extends Fragment {

    private ActivityListingsFragmentBinding binding;
    private ListingsAdapter adapter;
    private ListingsFragmentViewModel viewModel;

    public ListingsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.activity_listings_fragment, container, false);
        binding.setAddlistingcallback(new OnAddListingClicked());

        binding.profileListingsRecyclerview.setHasFixedSize(true);
        binding.profileListingsRecyclerview.setLayoutManager(new GridLayoutManager(getContext(), 2));

        adapter = new ListingsAdapter(getActivity());
        binding.profileListingsRecyclerview.setAdapter(adapter);

        return binding.getRoot();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewModel = ViewModelProviders.of(this).get(ListingsFragmentViewModel.class);

        observeViewModel(viewModel);
    }

    private void observeViewModel(ListingsFragmentViewModel viewModel) {
        // Update the list when the data changes
        viewModel.getListingsObeservable().observe(this, new Observer<List<Listing>>() {
            @Override
            public void onChanged(@Nullable List<Listing> listings) {
                if (listings != null) {
                    adapter.setListingsList(listings);
                }
            }
        });
    }

    public class OnAddListingClicked{
        public void onAddListingClicked(){
            Intent intent = new Intent(getApplicationContext(), AddListingActivity.class);
            startActivity(intent);
        }
    }

}
