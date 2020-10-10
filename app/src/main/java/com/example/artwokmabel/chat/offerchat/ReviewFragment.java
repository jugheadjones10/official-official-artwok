package com.example.artwokmabel.chat.offerchat;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.artwokmabel.R;
import com.example.artwokmabel.databinding.FragmentReviewBinding;
import com.example.artwokmabel.models.OrderChat;

public class ReviewFragment extends Fragment {

    private FragmentReviewBinding binding;
    private ReviewViewModel viewModel;
    private OrderChat orderChat;
    private NavController navController;
    private String lastMessageId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_container);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_review, container, false);
        viewModel = ViewModelProviders.of(this).get(ReviewViewModel.class);
        orderChat = ReviewFragmentArgs.fromBundle(getArguments()).getOrderchat();
        lastMessageId = ReviewFragmentArgs.fromBundle(getArguments()).getLastMessageId();

        setUpToolbar();

        return binding.getRoot();
    }

    private void setUpToolbar(){
        ((AppCompatActivity)requireActivity()).setSupportActionBar(binding.reviewToolbar);
        ((AppCompatActivity)requireActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_review, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.submit:
                viewModel.uploadNewReview(binding.ratingBar.getRating(), orderChat.getListing().getPostid(), binding.publicReview.getText().toString(), binding.privateReview.getText().toString());
                viewModel.setOfferAsReviewed(orderChat, lastMessageId);
                navController.navigateUp();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
