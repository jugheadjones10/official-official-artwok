package com.example.artwokmabel.chat.offerchat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.artwokmabel.R;
import com.example.artwokmabel.databinding.FragmentOfferAgreementBinding;
import com.example.artwokmabel.homepage.adapters.ListingsAdapter;
import com.example.artwokmabel.models.Listing;
import com.example.artwokmabel.profile.settings.SetUsernameFragmentArgs;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class OfferAgreementFragment extends Fragment {

    private FragmentOfferAgreementBinding binding;
    private Listing listing;

    public OfferAgreementFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_offer_agreement, container, false);
        listing =  OfferAgreementFragmentArgs.fromBundle(getArguments()).getListing();
        binding.setListing(listing);

        Picasso.get()
            .load(listing.getPhotos().get(0))
            .placeholder(R.drawable.loading_image_rounded_50)
            .error(R.drawable.rick_and_morty)
            .into(binding.listingPic);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((AppCompatActivity)requireActivity()).setSupportActionBar(binding.toolbar);
        ((AppCompatActivity)requireActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
