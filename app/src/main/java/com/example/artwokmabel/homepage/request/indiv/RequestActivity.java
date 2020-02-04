package com.example.artwokmabel.homepage.request.indiv;

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
import com.example.artwokmabel.databinding.ActivityRequestBinding;
import com.example.artwokmabel.homepage.callbacks.ShareClickCallback;
import com.example.artwokmabel.homepage.listing.ListingActivityViewModel;
import com.example.artwokmabel.models.Request;
import com.example.artwokmabel.models.User;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.ImageListener;

import java.util.ArrayList;

public class RequestActivity extends AppCompatActivity {

    private ActivityRequestBinding binding;
    private FirebaseAuth mAuth;
    private Request request;
    private RequestPagerAdapter adapter;
    private ListingActivityViewModel viewModel;
    private User me;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_request);

        adapter = new RequestPagerAdapter(this);

        getIncomingIntent();
    }

    private void getIncomingIntent(){

        request = (Request) getIntent().getSerializableExtra("request");
        viewModel = ViewModelProviders.of(this).get(ListingActivityViewModel.class);

        viewModel.getUserObservable(mAuth.getCurrentUser().getUid()).observe(this, new Observer<User>() {
            @Override
            public void onChanged(@Nullable User user) {
                if (user != null) {
                    me = user;

                    if(request.getUserid().equals(mAuth.getCurrentUser().getUid())){
                        binding.favoriteButton.setVisibility(View.GONE);
                    }else{
                        ArrayList<String> favs = me.getFav_requests();
                        Log.d("favfav", favs.toString());

                        if(favs != null && favs.contains(request.getPostid())){
                            binding.favoriteButton.setImageResource(R.drawable.like);
                        }else{
                            binding.favoriteButton.setImageResource(R.drawable.favourite_post);
                        }

                        binding.favoriteButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if(favs != null && favs.contains(request.getPostid())){
                                    viewModel.removeUserRequestFavs(request.getPostid());
                                }else{
                                    viewModel.addUserRequestFavs(request.getPostid());
                                }
                            }
                        });

                    }
                }
            }
        });

        viewModel.getUserObservable(request.getUserid()).observe(this, new Observer<User>() {
            @Override
            public void onChanged(@Nullable User user) {

                if (user != null) {
                    binding.setUser(user);
                    Log.d("profileimage", user.getUid() + user.getProfile_url());
                    Picasso.get()
                            .load(user.getProfile_url())
                            .placeholder(R.drawable.user)
                            .error(R.drawable.rick_and_morty)
                            .into(binding.profilePicture);
                }
            }
        });

        binding.setRequest(request);
        binding.setSharecallback(new ShareClickCallback());

        ArrayList<String> images = request.getPhotos();
        Log.d("request_check", images.toString());
        binding.requestImage.setImageListener(new ImageListener() {
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
            binding.requestImage.setPageCount(images.size());
        }

        setSupportActionBar(binding.indivToolbar);

        if(mAuth.getCurrentUser().getUid().equals(request.getUserid())){
            binding.indivToolbar.inflateMenu(R.menu.indiv_listing_menu_mine);
        }else{
            binding.indivToolbar.inflateMenu(R.menu.indiv_listing_menu_yours);
        }

        adapter.setRequest(request);
        binding.pager.setAdapter(adapter);

        new TabLayoutMediator(binding.tabLayout, binding.pager,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(TabLayout.Tab tab, int position) {
                        if(position == 0){
                            tab.setText("Description");
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

        if(mAuth.getCurrentUser().getUid().equals(request.getUserid())){
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
