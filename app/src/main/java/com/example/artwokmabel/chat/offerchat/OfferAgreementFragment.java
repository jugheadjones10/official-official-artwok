package com.example.artwokmabel.chat.offerchat;

import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.artwokmabel.R;
import com.example.artwokmabel.Utils.DecimalDigitsInputFilter;
import com.example.artwokmabel.databinding.FragmentOfferAgreementBinding;
import com.example.artwokmabel.models.AgreementDetails;
import com.example.artwokmabel.models.Listing;
import com.squareup.picasso.Picasso;

public class OfferAgreementFragment extends Fragment {

    private FragmentOfferAgreementBinding binding;
    private String listingId;
    private Listing listing;
    private AgreementDetails agreementDetails;
    private OfferViewModel viewModel;
    private NavController navController;

    public OfferAgreementFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_offer_agreement, container, false);
        binding.priceEditText.setFilters(new InputFilter[]{new DecimalDigitsInputFilter()});

        viewModel = new ViewModelProvider(requireActivity()).get(OfferViewModel.class);
        listingId = OfferAgreementFragmentArgs.fromBundle(getArguments()).getListingId();

        setHasOptionsMenu(true);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_container);

        ((AppCompatActivity)requireActivity()).setSupportActionBar(binding.toolbar);
        ((AppCompatActivity)requireActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)requireActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);

        viewModel.getAgreementDetails().observe(getViewLifecycleOwner(), new Observer<AgreementDetails>() {
            @Override
            public void onChanged(AgreementDetails agreementDetails) {
                if(agreementDetails != null){
                    binding.setAgreementDetails(agreementDetails);
                    viewModel.getAgreementDetails().removeObserver(this);
                }
            }
        });

        viewModel.getListing(listingId).observe(getViewLifecycleOwner(), new Observer<Listing>() {
            @Override
            public void onChanged(Listing liveListing) {
                if(liveListing != null){
                    listing = liveListing;

                    binding.setListing(listing);
                    Picasso.get()
                            .load(listing.getPhotos().get(0))
                            .placeholder(R.drawable.placeholder_black_new)
                            .error(R.drawable.placeholder_color_new)
                            .into(binding.listingPic);

                    viewModel.getListing(listingId).removeObserver(this);
                }
            }
        });
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.offer_agreement_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.save) {
            viewModel.updateListing(
                    Integer.parseInt(binding.priceEditText.getText().toString()),
                    binding.deliveryEditText.getText().toString(),
                    binding.refundEditText.getText().toString(),
                    listing
            );

            viewModel.updateOfferDetails(
                    binding.shipmentDeadlineEditText.getText().toString(),
                    binding.sellerRequestEditText.getText().toString(),
                    binding.buyerRequestEditText.getText().toString()
            );

            navController.navigateUp();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
