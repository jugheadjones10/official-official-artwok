package com.example.artwokmabel.homepage.listing;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.artwokmabel.R;
import com.example.artwokmabel.Utils.TransactFragment;
import com.example.artwokmabel.chat.offerchat.OfferActivity;
import com.example.artwokmabel.databinding.ActivityListingBinding;
import com.example.artwokmabel.homepage.callbacks.ShareClickCallback;
import com.example.artwokmabel.models.Listing;
import com.example.artwokmabel.models.Message;
import com.example.artwokmabel.models.User;
import com.example.artwokmabel.repos.FirestoreRepo;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.ImageListener;

import java.util.ArrayList;

public class ListingActivity extends AppCompatActivity {

    private ActivityListingBinding binding;
    private FirebaseAuth mAuth;
    private Listing listing;
    private ListingPagerAdapter adapter;
    private ListingActivityViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_listing);

        //adapter = new ListingPagerAdapter(this);

        getIncomingIntent();
    }

    private void getIncomingIntent(){

        listing = (Listing)getIntent().getSerializableExtra("listing");
        viewModel = ViewModelProviders.of(this).get(ListingActivityViewModel.class);

        viewModel.getUserObservable(mAuth.getCurrentUser().getUid()).observe(this, new Observer<User>() {
            @Override
            public void onChanged(@Nullable User me) {
                if (me != null) {
//                    me = user;

                    if(listing.getUserid().equals(mAuth.getCurrentUser().getUid())){
                        binding.favoriteButton.setVisibility(View.GONE);
                    }else{
                        ArrayList<String> favs = me.getFav_listings();
                        Log.d("favfav", favs.toString());

                        if(favs != null && favs.contains(listing.getPostid())){
                            binding.favoriteButton.setImageResource(R.drawable.like);
                        }else{
                            binding.favoriteButton.setImageResource(R.drawable.favourite_post);
                        }

                        binding.favoriteButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if(favs != null && favs.contains(listing.getPostid())){
                                    viewModel.removeUserListingFavs(listing.getPostid());
                                }else{
                                    viewModel.addUserListingFavs(listing.getPostid());
                                }
                            }
                        });

                    }
                }
            }
        });

        viewModel.getUserObservable(listing.getUserid()).observe(this, new Observer<User>() {
            @Override
            public void onChanged(@Nullable User user) {

                if (user != null) {
                    binding.setUser(user);
                    Log.d("profileimage", user.getUid() + user.getProfile_url());
                    Picasso.get()
                            .load(user.getProfile_url())
                            .placeholder(R.drawable.user)
                            .error(R.drawable.placeholder_color_new)
                            .into(binding.profilePicture);
                }
            }
        });

        binding.setListing(listing);
        binding.setSharecallback(new ShareClickCallback());
        binding.setOnofferclicked(new OnOfferClicked());
        binding.setOnprofileclicked(new OnProfileClicked());

        ArrayList<String> images = listing.getPhotos();
        Log.d("listing_check", images.toString());
        binding.listingImage.setImageListener(new ImageListener() {
            @Override
            public void setImageForPosition(int position, ImageView imageView) {
                Picasso.get()
                        .load(images.get(position))
                        .placeholder(R.drawable.user)
                        .error(R.drawable.placeholder_color_new)
                        .into(imageView);
            }
        });

        if(images != null){
            binding.listingImage.setPageCount(images.size());
        }

        setSupportActionBar(binding.indivToolbar);

        if(mAuth.getCurrentUser().getUid().equals(listing.getUserid())){
            binding.indivToolbar.inflateMenu(R.menu.indiv_listing_menu_mine);
        }else{
            binding.indivToolbar.inflateMenu(R.menu.indiv_listing_menu_yours);
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

    public class OnOfferClicked{
        public void onOfferClicked(Listing listing){
            Intent offerIntent = new Intent(ListingActivity.this, OfferActivity.class);
            offerIntent.putExtra("orderchat", FirestoreRepo.getInstance().changeListingToMeBuy(listing, new Message("", "", "", "","","", "", 0, "false")));
            startActivity(offerIntent);
        }
    }

    public class OnProfileClicked{
        public void onProfileClicked(User user){
            new TransactFragment().loadFragment(ListingActivity.this, user.getUid());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        if(mAuth.getCurrentUser().getUid().equals(listing.getUserid())){
            inflater.inflate(R.menu.indiv_listing_menu_mine, menu);
        }else{
            inflater.inflate(R.menu.indiv_listing_menu_yours, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.listing_delete:
                return true;
            case R.id.listing_edit:
                return true;
            case R.id.listing_report:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
