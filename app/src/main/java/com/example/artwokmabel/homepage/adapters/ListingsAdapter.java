package com.example.artwokmabel.homepage.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.artwokmabel.HomeGraphDirections;
import com.example.artwokmabel.ProfileGraphDirections;
import com.example.artwokmabel.R;
import com.example.artwokmabel.databinding.ItemNormalListingBinding;
import com.example.artwokmabel.models.Listing;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.ImageClickListener;
import com.synnapps.carouselview.ImageListener;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class ListingsAdapter extends RecyclerView.Adapter<ListingsAdapter.myHolder> {
    private Context mContext;
    private List<Listing> listingsList;
    private FirebaseFirestore db;
    private ListingsAdapterViewModel viewModel;
    private NavController navController;
    private FirebaseAuth mAuth;


    public ListingsAdapter(Context context, NavController navController){
        this.mContext = context;
        this.mAuth = FirebaseAuth.getInstance();
        this.navController = navController;
        viewModel = ViewModelProviders.of((FragmentActivity)context).get(ListingsAdapterViewModel.class);
    }

    @NonNull
    @Override
    public myHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        ItemNormalListingBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_normal_listing, parent,false);
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
                        .transform(new RoundedCornersTransformation(10, 0))
                        .resize(300, 300)
                        .placeholder(R.drawable.placeholder_black_new)
                        .error(R.drawable.placeholder_color_new)
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
//                                    .error(R.drawable.placeholder_color_new)
//                                    .into(myHolder.binding.profile);
//                        } else {
//
//                        }
//                    }
//                });

        if(data.getUserid().equals(mAuth.getCurrentUser().getUid())){
            //myHolder.binding.normalListingLike.setImageResource(R.drawable.ic_menu);
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
            if(navController.getCurrentDestination().getId() == R.id.profile_graph){
                ProfileGraphDirections.ActionGlobalListingFragment2 action =
                        ProfileGraphDirections.actionGlobalListingFragment2(data);
                navController.navigate(action);
            }else{
                HomeGraphDirections.ActionGlobalListingFragment action =
                        HomeGraphDirections.actionGlobalListingFragment(data);
                navController.navigate(action);
            }
        }
    }

    public class OnProfileClicked{
        public void onProfileClicked(Listing data){
            //Uncomment if we eventually decide to bring back the profile photo in listings outside
//            int currentDestination = navController.getCurrentDestination().getId();
////            if(currentDestination == R.id.home_graph || currentDestination == R.id.favoritesFragment){
////                HomeGraphDirections.ActionGlobalProfileFragment action =
////                        HomeGraphDirections.actionGlobalProfileFragment(data.getUserid());
////                navController.navigate(action);
////            }else if(currentDestination == R.id.profile_graph){
////                ProfileGraphDirections.ActionProfileGraphSelf action =
////                        ProfileGraphDirections.actionProfileGraphSelf(data.getUserid());
////                navController.navigate(action);
//            }
        }
    }

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
        private ItemNormalListingBinding binding;

        public myHolder(ItemNormalListingBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
