package com.example.artwokmabel.chat.offerchat;


import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.artwokmabel.R;
import com.example.artwokmabel.databinding.FragmentReviewBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReviewFragment extends Fragment {

    private FragmentReviewBinding binding;
    private ReviewViewModel viewModel;


    public ReviewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_review, container, false);
        viewModel = ViewModelProviders.of(this).get(ReviewViewModel.class);

        binding.reviewSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.uploadNewReview(binding.ratingBar.getRating(),((OfferActivity)getActivity()).getListingId(), binding.publicReview.getText().toString(), binding.privateReview.getText().toString());
                getFragmentManager().popBackStack();
            }
        });

        return binding.getRoot();
    }

}
