package com.example.artwokmabel.homepage.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.artwokmabel.R;
import com.example.artwokmabel.databinding.ActivityIndivListingNewNewBinding;
import com.example.artwokmabel.homepage.adapters.IndivListViewPagerAdapter;
import com.example.artwokmabel.homepage.models.Listing;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.ImageListener;

import java.util.ArrayList;

public class IndivListingActivity extends AppCompatActivity {

    private ActivityIndivListingNewNewBinding binding;
    private FirebaseAuth mAuth;
    private Listing listing;
    private IndivListViewPagerAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_indiv_listing_new_new);

        adapter = new IndivListViewPagerAdapter(this);
        //Changed above to passing in fragmentActivity

        getIncomingIntent();
    }

    private void getIncomingIntent(){

        listing = (Listing)getIntent().getSerializableExtra("listing");
        binding.setListing(listing);

        ArrayList<String> images = listing.getPhotos();
        Log.d("listing_check", images.toString());
        binding.listingImage.setImageListener(new ImageListener() {
            @Override
            public void setImageForPosition(int position, ImageView imageView) {
                Picasso.get()
                        .load(images.get(position))
                        .placeholder(R.drawable.user)
                        .error(R.drawable.rick_and_morty)
                        .into(imageView);
            }
        });

        if(images != null){
            binding.listingImage.setPageCount(images.size());
        }


        //Load profile pic into listing
//        Picasso.get()
//                .load(listing.get)
//                .placeholder(R.drawable.user)
//                .error(R.drawable.rick_and_morty)
//                .into(imageView);

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
