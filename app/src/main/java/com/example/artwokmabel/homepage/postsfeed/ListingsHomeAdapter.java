package com.example.artwokmabel.homepage.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.artwokmabel.R;
import com.example.artwokmabel.databinding.ItemListingsBinding;
import com.example.artwokmabel.homepage.listing.IndivListingActivity;
import com.example.artwokmabel.homepage.user.IndivUserFragment;
import com.example.artwokmabel.homepage.homepagestuff.HomePageActivity;
import com.example.artwokmabel.homepage.models.Listing;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.ImageClickListener;
import com.synnapps.carouselview.ImageListener;

import java.util.ArrayList;
import java.util.List;

public class ListingsHomeAdapter extends RecyclerView.Adapter<ListingsHomeAdapter.homeListingsHolder>{
    private Context mContext;
    private List<Listing> listingsList;
    private FirebaseFirestore db;

    public ListingsHomeAdapter(Context context){
        this.mContext = context;
    }

    @NonNull
    @Override
    public ListingsHomeAdapter.homeListingsHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        ItemListingsBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_listings, parent,false);
        return new ListingsHomeAdapter.homeListingsHolder(binding);
    }


    @Override
    public void onBindViewHolder(@NonNull ListingsHomeAdapter.homeListingsHolder homeListingsHolder, int i) {
        Listing data = listingsList.get(i);

        homeListingsHolder.binding.setListingclickcallback(new ListingsHomeAdapter.OnListingClicked());
        homeListingsHolder.binding.setProfilecallback(new ListingsHomeAdapter.OnProfileClicked());
        homeListingsHolder.binding.setListing(data);

        ArrayList<String> images = data.getPhotos();
        ImageListener imageListener = new ImageListener() {
            @Override
            public void setImageForPosition(int position, ImageView imageView) {
                //imageView.setImageResource(sampleImages[position]);
                Picasso.get()
                        .load(images.get(position))
                        .resize(300, 300)
                        .placeholder(R.drawable.loading_image)
                        .error(R.drawable.rick_and_morty)
                        .into(imageView);
            }
        };

        homeListingsHolder.binding.carouselView.setImageClickListener(new ImageClickListener() {
            @Override
            public void onClick(int position) {
                new ListingsHomeAdapter.OnListingClicked().onListClicked(data);
            }
        });

        if(images != null){
            homeListingsHolder.binding.carouselView.setPageCount(images.size());
            homeListingsHolder.binding.carouselView.setImageListener(imageListener);
        }

        //Todo: replace the below with view model next time
        db = FirebaseFirestore.getInstance();
        db.collection("Users")
                .document(data.getUserid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();

                            Picasso.get()
                                    .load(document.getString("profile_url"))
                                    .placeholder(R.drawable.loading_image)
                                    .error(R.drawable.rick_and_morty)
                                    .into(homeListingsHolder.binding.profile);
                        } else {

                        }
                    }
                });
    }


    //Todo: might need to add on clicked to carousel view
    public class OnListingClicked{
        public void onListClicked(Listing data){
            Intent intent = new Intent(mContext, IndivListingActivity.class);
            intent.putExtra("listing", data);
            mContext.startActivity(intent);
        }
    }

    public class OnProfileClicked{
        public void onProfileClicked(Listing data){
            IndivUserFragment indivUserFrag = new IndivUserFragment();
            Bundle args = new Bundle();
            args.putString("poster_username", data.getUsername());
            indivUserFrag.setArguments(args);
            HomePageActivity.getInstance().loadFragment(indivUserFrag);
        }
    }


    public void setListingsList(final List<Listing> listings) {
        if (this.listingsList == null) {
            this.listingsList = listings;
            notifyItemRangeInserted(0, listings.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return ListingsHomeAdapter.this.listingsList.size();
                }

                @Override
                public int getNewListSize() {
                    return listings.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return ListingsHomeAdapter.this.listingsList.get(oldItemPosition).getPostid().equals(
                            listings.get(newItemPosition).getPostid());
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    return ListingsHomeAdapter.this.listingsList.get(oldItemPosition).getPostid().equals(
                            listings.get(newItemPosition).getPostid());
                }
            });
            this.listingsList = listings;
            result.dispatchUpdatesTo(this);
        }
    }

    @Override
    public int getItemCount() {
        return listingsList == null ? 0 : listingsList.size();
    }

    class homeListingsHolder extends RecyclerView.ViewHolder {
        private ItemListingsBinding binding;

        public homeListingsHolder(ItemListingsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
