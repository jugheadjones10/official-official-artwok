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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.SnapHelper;

import com.example.artwokmabel.R;
import com.example.artwokmabel.databinding.MainFeedFragmentBinding;
import com.example.artwokmabel.homepage.adapters.ListingsAdapter;
import com.example.artwokmabel.homepage.adapters.PostsAdapter;
import com.example.artwokmabel.homepage.models.Listing;
import com.example.artwokmabel.homepage.models.MainPost;

import java.util.List;

public class HomeFeedFragment extends Fragment {

    private MainFeedFragmentBinding binding;
    private HomeFeedViewModel viewModel;
    private PostsAdapter postsAdapter;
    private ListingsAdapter listingsAdapter;
    //Todo: add horizontal scrollable listings

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.main_feed_fragment, container, false);

        binding.recyclerview.setHasFixedSize(true);

        postsAdapter = new PostsAdapter(getContext());
        binding.recyclerview.setAdapter(postsAdapter);

        //Todo: bring back listings recycler view
        listingsAdapter = new ListingsAdapter(getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        binding.listingsRecycler.setLayoutManager(layoutManager);
        binding.listingsRecycler.setAdapter(listingsAdapter);

        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(binding.listingsRecycler);

        return binding.getRoot();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(HomeFeedViewModel.class);

        observeViewModel(viewModel);
    }

    private void observeViewModel(HomeFeedViewModel viewModel) {
        // Update the list when the data changes
        viewModel.getFeedPostsObeservable().observe(this, new Observer<List<MainPost>>() {
            @Override
            public void onChanged(@Nullable List<MainPost> posts) {
                if (posts != null) {
                    postsAdapter.setPostsList(posts);
                }
            }
        });

        viewModel.getFeedListingObservable().observe(this, new Observer<List<Listing>>() {
            @Override
            public void onChanged(@Nullable List<Listing> listings) {
                if (listings != null) {
                    listingsAdapter.setListingsList(listings);
                }
            }
        });
    }

}
