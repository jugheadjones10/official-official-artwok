package com.example.artwokmabel.homepage.favorites;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.artwokmabel.R;
import com.example.artwokmabel.databinding.FragmentFavoriteRequestsBinding;
import com.example.artwokmabel.models.Request;
import com.example.artwokmabel.homepage.request.tab.RequestsAdapter;

import java.util.List;

public class FavoriteRequestsFragment extends Fragment {
    private RequestsAdapter adapter;

    private FragmentFavoriteRequestsBinding binding;
    private FavoriteRequestsFragmentViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_favorite_requests, container, false);

//        binding.favRequestsRecyclerview.setHasFixedSize(true);
//        binding.favRequestsRecyclerview.setLayoutManager(new GridLayoutManager(getActivity(), 2));
//
//        adapter = new RequestsAdapter(getContext());
//        binding.favRequestsRecyclerview.setAdapter(adapter);

        viewModel = ViewModelProviders.of(this).get(FavoriteRequestsFragmentViewModel.class);
        observeViewModel(viewModel);

        return binding.getRoot();
    }



    private void observeViewModel(FavoriteRequestsFragmentViewModel viewModel) {
        // Update the list when the data changes
        viewModel.getFavRequestsObjectsObservable().observe(this, new Observer<List<Request>>() {
            @Override
            public void onChanged(@Nullable List<Request> requests) {
                if (requests != null) {
//                    if(listings.size() != 0){
//                    adapter.setRequestsList(requests);
//                    }else{
//                        //List<Listing> altListings = new ArrayList<>(0);
//                        adapter.setListingsList(listings);
//                        adapter.notifyItemChanged(0);
//                    }
                    Log.d("checkRequests", Integer.toString(requests.size()));

                }
            }
        });
    }
}
