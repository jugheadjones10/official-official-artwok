package com.example.artwokmabel.homepage.listing;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.artwokmabel.R;
import com.example.artwokmabel.databinding.FragmentListingReviewsBinding;
import com.example.artwokmabel.homepage.post.CommentsAdapter;
import com.example.artwokmabel.models.Listing;
import com.example.artwokmabel.models.Review;

import java.util.List;

public class ListingReviewsFragment extends Fragment {

    private FragmentListingReviewsBinding binding;
    private ListingReviewsAdapter adapter;
    private ListingActivityViewModel viewModel;
    private Listing listing;

    public ListingReviewsFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_listing_reviews, container, false);
        viewModel = ViewModelProviders.of(this).get(ListingActivityViewModel.class);

        listing = (Listing)getArguments().getSerializable("listing");

        adapter = new ListingReviewsAdapter(getActivity());
        binding.reviewsRecyclerView.setAdapter(adapter);

        viewModel.getListingReviews(listing).observe(getActivity(), new Observer<List<Review>>() {
            @Override
            public void onChanged(List<Review> reviews) {
                if(reviews != null){
                    adapter.setReviewsList(reviews);
                }
            }
        });


        return binding.getRoot();
    }
}
