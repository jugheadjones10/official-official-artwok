package com.example.artwokmabel.chat.offerchat;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.artwokmabel.R;
import com.example.artwokmabel.chat.personalchat.ChatFragmentArgs;
import com.example.artwokmabel.databinding.FragmentReviewBinding;

public class ReviewFragment extends Fragment {

    private FragmentReviewBinding binding;
    private ReviewViewModel viewModel;
    private String listingId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_review, container, false);
        viewModel = ViewModelProviders.of(this).get(ReviewViewModel.class);
        listingId = ReviewFragmentArgs.fromBundle(getArguments()).getListingId();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.reviewSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.uploadNewReview(binding.ratingBar.getRating(), listingId, binding.publicReview.getText().toString(), binding.privateReview.getText().toString());
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_container);
                navController.navigateUp();
            }
        });

        ((AppCompatActivity) getActivity()).setSupportActionBar(binding.reviewToolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
