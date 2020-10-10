package com.example.artwokmabel.chat.offerchat;

import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
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
        listing = OfferAgreementFragmentArgs.fromBundle(getArguments()).getListing();
        agreementDetails = OfferAgreementFragmentArgs.fromBundle(getArguments()).getAgreementDetails();

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

        binding.setListing(listing);
        binding.setAgreementDetails(agreementDetails);
        Picasso.get()
                .load(listing.getPhotos().get(0))
                .placeholder(R.drawable.placeholder_black_new)
                .error(R.drawable.placeholder_color_new)
                .into(binding.listingPic);

//        viewModel.getAgreementDetails().observe(getViewLifecycleOwner(), new Observer<AgreementDetails>() {
//            @Override
//            public void onChanged(AgreementDetails agreementDetails) {
//                if(agreementDetails != null){
//                    binding.setAgreementDetails(agreementDetails);
//                    viewModel.getAgreementDetails().removeObserver(this);
//                }
//            }
//        });

//        viewModel.getListing(listingId).observe(getViewLifecycleOwner(), new Observer<Listing>() {
//            @Override
//            public void onChanged(Listing liveListing) {
//                if(liveListing != null){
//                    listing = liveListing;
//
//                    binding.setListing(listing);
//                    Picasso.get()
//                            .load(listing.getPhotos().get(0))
//                            .placeholder(R.drawable.placeholder_black_new)
//                            .error(R.drawable.placeholder_color_new)
//                            .into(binding.listingPic);
//
//                    viewModel.getListing(listingId).removeObserver(this);
//                }
//            }
//        });
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

            //Below is commented because we aren't actually changing the original listing details here.
            //We're only setting the details for this transaction
//            viewModel.updateListing(
//                    Integer.parseInt(binding.priceEditText.getText().toString()),
//                    binding.deliveryEditText.getText().toString(),
//                    binding.refundEditText.getText().toString(),
//                    listing
//            );

            String price = binding.priceEditText.getText().toString();
            String delivery = binding.deliveryEditText.getText().toString();
            String refund = binding.refundEditText.getText().toString();

            if(price.isEmpty()){
                Toast.makeText(requireContext(), "Please input a price", Toast.LENGTH_LONG).show();
            }else if(delivery.isEmpty()){
                Toast.makeText(requireContext(), "Please input delivery details", Toast.LENGTH_LONG).show();
            }else if(refund.isEmpty()){
                Toast.makeText(requireContext(), "Please input refund details", Toast.LENGTH_LONG).show();
            }else{
                viewModel.updateOfferDetails(
                    new AgreementDetails(
                        Double.parseDouble(price),
                        delivery,
                        refund,
                        binding.shipmentDeadlineEditText.getText().toString(),
                        binding.sellerRequestEditText.getText().toString(),
                        binding.buyerRequestEditText.getText().toString()
                    )
                );

                navController.navigateUp();
            }

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
