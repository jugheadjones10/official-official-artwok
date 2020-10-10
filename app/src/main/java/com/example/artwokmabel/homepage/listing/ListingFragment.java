package com.example.artwokmabel.homepage.listing;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.artwokmabel.ChatGraphDirections;
import com.example.artwokmabel.HomeGraphDirections;
import com.example.artwokmabel.ProfileGraphDirections;
import com.example.artwokmabel.R;
import com.example.artwokmabel.databinding.FragmentListingBinding;
import com.example.artwokmabel.homepage.callbacks.ShareClickCallback;
import com.example.artwokmabel.models.Listing;
import com.example.artwokmabel.models.Message;
import com.example.artwokmabel.models.User;
import com.example.artwokmabel.repos.FirestoreRepo;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.ImageListener;

import java.util.ArrayList;

public class ListingFragment extends Fragment {

    private FragmentListingBinding binding;
    private FirebaseAuth mAuth;
    private Listing listing;
    private ListingPagerAdapter adapter;
    private ListingActivityViewModel viewModel;
    private ArrayList<String> currentUserListingsFavs;
    private NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_listing, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new ListingPagerAdapter(this);
        mAuth = FirebaseAuth.getInstance();

        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_container);

        //Prevent going to offer screen
        int currentGraph = navController.getGraph().getId();
        if(currentGraph == R.id.profile_graph){
            binding.offerButton.setVisibility(View.GONE);
        }

        binding.indivToolbar.bringToFront();

        setUpToolbar();
        getIncomingIntent();
    }

    private void setUpToolbar(){
        ((AppCompatActivity)requireActivity()).setSupportActionBar(binding.indivToolbar);
        ((AppCompatActivity)requireActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        setHasOptionsMenu(true);
    }

    private void getIncomingIntent(){

        listing = ListingFragmentArgs.fromBundle(getArguments()).getListing();
        viewModel = ViewModelProviders.of(this).get(ListingActivityViewModel.class);

        if(mAuth.getCurrentUser().getUid().equals(listing.getUserid())){
            binding.offerButton.setVisibility(View.GONE);
        }

        //TODO: Change retrieval of whole user to retrieval of userFavListings. Only retrieve what you need.
        viewModel.getUserObservable(mAuth.getCurrentUser().getUid()).observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(@Nullable User me) {
                if (me != null) {

                    currentUserListingsFavs = me.getFav_listings();

                    MenuItem favoriteButton = binding.indivToolbar.getMenu().findItem(R.id.favorite);
                    if(favoriteButton != null){
                        if(currentUserListingsFavs != null && currentUserListingsFavs.contains(listing.getPostid())){
                            favoriteButton.setIcon(R.drawable.like);
                        }else{
                            favoriteButton.setIcon(R.drawable.heart_button);
                        }
                    }

                }
            }
        });

        viewModel.getUserObservable(listing.getUserid()).observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(@Nullable User user) {
                if (user != null) {
                    binding.setUser(user);
                    Log.d("profileimage", user.getUid() + user.getProfile_url());
                    Picasso.get()
                            .load(user.getProfile_url())
                            .placeholder(R.drawable.placeholder_black_new)
                            .error(R.drawable.placeholder_color_new)
                            .into(binding.profilePicture);
                }
            }
        });

        binding.setListing(listing);
        binding.setSharecallback(new ShareClickCallback());
        binding.setListingFragment(this);

        ArrayList<String> images = listing.getPhotos();
        binding.listingImage.setImageListener(new ImageListener() {
            @Override
            public void setImageForPosition(int position, ImageView imageView) {
                Picasso.get()
                        .load(images.get(position))
                        .placeholder(R.drawable.placeholder_black_new)
                        .error(R.drawable.placeholder_color_new)
                        .into(imageView);
            }
        });

        if(images != null){
            binding.listingImage.setPageCount(images.size());
        }

        adapter.setListing(listing);
        binding.pager.setAdapter(adapter);

        new TabLayoutMediator(binding.tabLayout, binding.pager,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(TabLayout.Tab tab, int position) {
                        if(position == 0){
                            tab.setText("Description");
                        }else if(position == 1){
                            tab.setText("Reviews");
                        }else if(position == 2){
                            tab.setText("Delivery/Refund");
                        }else{
                            tab.setText("FAQ");
                        }
                    }
                }
        ).attach();
    }

    public void onFavClicked(){
        if(currentUserListingsFavs != null && currentUserListingsFavs.contains(listing.getPostid())){
            viewModel.removeUserListingFavs(listing.getPostid());
        }else{
            viewModel.addUserListingFavs(listing.getPostid());
        }
    }

    public void onOfferClicked(Listing listing){
        int currentGraph = navController.getGraph().getId();
        if(currentGraph == R.id.home_graph){
            ListingFragmentDirections.ActionListingFragmentToOfferFragment action =
                    ListingFragmentDirections.actionListingFragmentToOfferFragment((FirestoreRepo.getInstance().changeListingToMeBuy(listing,
                            new Message("", "", "", "","","", "", 0, "false"))));
            navController.navigate(action);
        }
    }

    public void onProfileClicked(User user){
        int currentGraph = navController.getGraph().getId();
        if(currentGraph == R.id.home_graph){
            HomeGraphDirections.ActionGlobalProfileFragment2 action =
                    HomeGraphDirections.actionGlobalProfileFragment2(user.getUid());
            navController.navigate(action);
        }else if(currentGraph == R.id.profile_graph){
            ProfileGraphDirections.ActionProfileGraphSelf action =
                    ProfileGraphDirections.actionProfileGraphSelf(user.getUid());
            navController.navigate(action);
        }else{
            ChatGraphDirections.ActionGlobalProfileFragment3 action =
                    ChatGraphDirections.actionGlobalProfileFragment3(user.getUid());
            navController.navigate(action);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if(mAuth.getCurrentUser().getUid().equals(listing.getUserid())){
            inflater.inflate(R.menu.indiv_listing_menu_mine, menu);
        }else{
            inflater.inflate(R.menu.indiv_listing_menu_yours, menu);
        }
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.listing_delete:
                new MaterialAlertDialogBuilder(getContext())
                    .setTitle("Delete Listing?")
                    .setMessage("This action cannot be reversed!")
                    .setNeutralButton("Cancel", null)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            viewModel.deleteUserListing(listing.getPostid());
                            navController.navigateUp();
                        }
                    })
                    .show();
                return true;
            case R.id.listing_edit:
                return true;
            case R.id.listing_report:
                final EditText taskEditText = new EditText(getContext());
                new MaterialAlertDialogBuilder(getContext())
                        .setTitle("Report this listing")
                        .setMessage("Describe why you feel this listing inappropriate")
                        .setView(taskEditText)
                        .setPositiveButton("Report", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String report = String.valueOf(taskEditText.getText());
                                viewModel.sendListingReport(report, listing.getPostid());
                            }
                        })
                        .setNeutralButton("Cancel", null)
                        .show();
                return true;
            case R.id.favorite:
                onFavClicked();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
