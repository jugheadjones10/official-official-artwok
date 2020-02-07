package com.example.artwokmabel.homepage.postsfeed;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.example.artwokmabel.R;
import com.example.artwokmabel.databinding.FragmentHomeFeedBinding;
import com.example.artwokmabel.homepage.adapters.PostsAdapter;
import com.example.artwokmabel.models.Listing;
import com.example.artwokmabel.models.MainPost;
import com.yarolegovich.discretescrollview.InfiniteScrollAdapter;
import com.yarolegovich.discretescrollview.transform.Pivot;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import java.util.List;

public class HomeFeedFragment extends Fragment {

    private FragmentHomeFeedBinding binding;
    private HomeFeedViewModel viewModel;
    private PostsAdapter postsAdapter;
    private ListingsHomeAdapter listingsAdapter;
    //Todo: add horizontal scrollable listings

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home_feed, container, false);

        binding.recyclerview.setHasFixedSize(false);


        postsAdapter = new PostsAdapter(getContext());
        binding.recyclerview.setAdapter(postsAdapter);

        //Todo: bring back listings recycler view
        listingsAdapter = new ListingsHomeAdapter(getContext());

        InfiniteScrollAdapter wrapper = InfiniteScrollAdapter.wrap(listingsAdapter);
        binding.horizontalRecyclerViewListings.setAdapter(wrapper);
        binding.horizontalRecyclerViewListings.setItemTransformer(new ScaleTransformer.Builder()
                .setMaxScale(1.05f)
                .setMinScale(0.8f)
                .setPivotX(Pivot.X.CENTER) // CENTER is a default one
                .setPivotY(Pivot.Y.BOTTOM) // CENTER is a default one
                .build());
        binding.horizontalRecyclerViewListings.setSlideOnFling(true);
        binding.horizontalRecyclerViewListings.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                int action = e.getAction();
                switch (action) {
                    case MotionEvent.ACTION_MOVE:
                        rv.getParent().requestDisallowInterceptTouchEvent(true);
                        break;
                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

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