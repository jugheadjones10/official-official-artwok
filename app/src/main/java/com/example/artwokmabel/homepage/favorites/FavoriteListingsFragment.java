package com.example.artwokmabel.homepage.favorites;

import android.os.Bundle;
import android.util.Log;
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
import com.example.artwokmabel.databinding.FragmentFavoriteListingsBinding;
import com.example.artwokmabel.homepage.adapters.ListingsAdapter;
import com.example.artwokmabel.models.Listing;

import java.util.List;

public class FavoriteListingsFragment extends Fragment {

    private ListingsAdapter adapter;
    private NavController navController;
    private FragmentFavoriteListingsBinding binding;
    private FavoriteListingsFragmentViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_favorite_listings, container, false);

        binding.favListingRecyclerView.setHasFixedSize(true);
        binding.favListingRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        viewModel = ViewModelProviders.of(this).get(FavoriteListingsFragmentViewModel.class);
        observeViewModel(viewModel);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_container);

        adapter = new ListingsAdapter(getContext(), navController);
        binding.favListingRecyclerView.setAdapter(adapter);
    }

    private void observeViewModel(FavoriteListingsFragmentViewModel viewModel) {
        // Update the list when the data changes
        viewModel.getFavListingsObjectsObservable().observe(getViewLifecycleOwner(), new Observer<List<Listing>>() {
            @Override
            public void onChanged(@Nullable List<Listing> listings) {
                if (listings != null) {
//                    if(listings.size() != 0){
                    adapter.setListingsList(listings);
//                    }else{
//                        //List<Listing> altListings = new ArrayList<>(0);
//                        adapter.setListingsList(listings);
//                        adapter.notifyItemChanged(0);
//                    }
                    Log.d("checklistings", Integer.toString(listings.size()));

                }
            }
        });
    }
}
