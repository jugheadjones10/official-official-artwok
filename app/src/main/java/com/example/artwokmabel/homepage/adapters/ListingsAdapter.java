package com.example.artwokmabel.homepage.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.artwokmabel.R;
import com.example.artwokmabel.databinding.ItemListingsNormalBinding;
import com.example.artwokmabel.homepage.activities.IndivListingActivity;
import com.example.artwokmabel.homepage.models.Listing;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.ImageClickListener;
import com.synnapps.carouselview.ImageListener;

import java.util.ArrayList;
import java.util.List;

public class ListingsAdapter extends RecyclerView.Adapter<ListingsAdapter.myHolder> {
    private Context mContext;
    private List<Listing> listingsList;
    private FirebaseFirestore db;
    private ListingsAdapterViewModel viewModel;
    private FirebaseAuth mAuth;


    public ListingsAdapter(Context context){
        this.mContext = context;
        this.mAuth = FirebaseAuth.getInstance();
        viewModel = ViewModelProviders.of((FragmentActivity)context).get(ListingsAdapterViewModel.class);
    }

    @NonNull
    @Override
    public myHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        ItemListingsNormalBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_listings_normal, parent,false);
        return new ListingsAdapter.myHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull myHolder myHolder, int i) {
        Listing data = listingsList.get(i);

        myHolder.binding.setListingclickcallback(new OnListingClicked());
        //myHolder.binding.setProfilecallback(new OnProfileClicked());
        myHolder.binding.setListing(data);

        myHolder.binding.setFavorite(myHolder.binding.normalListingLike);

        myHolder.binding.normalListingLike.bringToFront();

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

        myHolder.binding.carouselView.setImageClickListener(new ImageClickListener() {
            @Override
            public void onClick(int position) {
                new OnListingClicked().onListClicked(data);
            }
        });

        if(images != null){
            myHolder.binding.carouselView.setPageCount(images.size());
            myHolder.binding.carouselView.setImageListener(imageListener);
        }

        //Todo: replace the below with view model next time
//        db = FirebaseFirestore.getInstance();
//        db.collection("Users")
//                .document(data.getUserid())
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                        if (task.isSuccessful()) {
//                            DocumentSnapshot document = task.getResult();
//
//                            Picasso.get()
//                                    .load(document.getString("profile_url"))
//                                    .placeholder(R.drawable.loading_image)
//                                    .error(R.drawable.rick_and_morty)
//                                    .into(myHolder.binding.profile);
//                        } else {
//
//                        }
//                    }
//                });

        if(data.getUserid().equals(mAuth.getCurrentUser().getUid())){
            myHolder.binding.normalListingLike.setImageResource(R.drawable.menu);
        }else {
            myHolder.binding.setOnfavlistingclicked(new OnFavListingClicked());
            viewModel.getUserFavListingsObservable().observe((FragmentActivity) mContext, new Observer<List<String>>() {
                @Override
                public void onChanged(@Nullable List<String> favListings) {
                    if (favListings != null) {
                        if (favListings.contains(data.getPostid())) {
                            myHolder.binding.normalListingLike.setImageResource(R.drawable.like);
                        } else {
                            myHolder.binding.normalListingLike.setImageResource(R.drawable.favourite_post);
                        }
                    }
                }
            });
        }
    }


    //Todo: might need to add on clicked to carousel view
    public class OnListingClicked{
        public void onListClicked(Listing data){
            Intent intent = new Intent(mContext, IndivListingActivity.class);

//            intent.putExtra("username", data.getUsername());
//            intent.putExtra("postid", data.getPostid());
//            intent.putExtra("itemname", data.getName());
//            intent.putExtra("price", data.getPrice());
//            intent.putExtra("itemDesc", data.getDesc());
//            intent.putExtra("photos", data.getPhotos());
//            intent.putExtra("refund_exxhange", data.getReturn_exchange());
//            intent.putExtra("delivery", data.getDelivery());
//            intent.putExtra("userid", data.getUserid());
            intent.putExtra("listing", data);

            mContext.startActivity(intent);
        }
    }

//    public class OnProfileClicked{
//        public void onProfileClicked(Listing data){
//            IndivUserFragment indivUserFrag = new IndivUserFragment();
//            Bundle args = new Bundle();
//            args.putString("poster_username", data.getUsername());
//            indivUserFrag.setArguments(args);
//            HomePageActivity.getInstance().loadFragment(indivUserFrag);
//        }
//    }

    public class OnFavListingClicked{
        public void onFavListingClicked(Listing listing, ImageView favorite){
            viewModel.switchUserFavListingsNonObservable(listing, favorite);
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
                    return ListingsAdapter.this.listingsList.size();
                }

                @Override
                public int getNewListSize() {
                    return listings.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return ListingsAdapter.this.listingsList.get(oldItemPosition).getPostid().equals(
                            listings.get(newItemPosition).getPostid());
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    return ListingsAdapter.this.listingsList.get(oldItemPosition).getPostid().equals(
                            listings.get(newItemPosition).getPostid());
                }
            });
            this.listingsList = listings;
            notifyDataSetChanged();
            result.dispatchUpdatesTo(this);
        }
    }


    @Override
    public int getItemCount() {
        return listingsList == null ? 0 : listingsList.size();
    }

    class myHolder extends RecyclerView.ViewHolder {
        private ItemListingsNormalBinding binding;

        public myHolder(ItemListingsNormalBinding binding) {
            super(binding.getRoot());
           this.binding = binding;
        }
    }
}
