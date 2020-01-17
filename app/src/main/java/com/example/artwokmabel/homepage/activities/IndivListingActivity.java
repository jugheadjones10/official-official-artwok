package com.example.artwokmabel.homepage.activities;

import android.os.Bundle;
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
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.ImageListener;

import java.util.ArrayList;

public class IndivListingActivity extends AppCompatActivity {

    private ActivityIndivListingNewNewBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_indiv_listing_new_new);

        IndivListViewPagerAdapter adapter = new IndivListViewPagerAdapter(this);
        //Changed above to passing in fragmentActivity

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

        getIncomingIntent();
    }

    private void getIncomingIntent(){

        Listing listing = (Listing)getIntent().getSerializableExtra("listing");


        ArrayList<String> images = listing.getPhotos();
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
    }
}
