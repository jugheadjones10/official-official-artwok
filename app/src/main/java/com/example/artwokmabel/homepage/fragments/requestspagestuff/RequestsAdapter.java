package com.example.artwokmabel.homepage.fragments.requestspagestuff;

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
import com.example.artwokmabel.Request.IndivRequestActivity;
import com.example.artwokmabel.databinding.ItemRequestsBinding;
import com.example.artwokmabel.homepage.fragments.indivuser.IndivUserFragment;
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

public class RequestsAdapter extends RecyclerView.Adapter<RequestsAdapter.myHolder>{

    private Context mContext;
    private List<Request> requestsList;
    private FirebaseFirestore db;

    public RequestsAdapter(Context context){
        this.mContext = context;
    }

    @NonNull
    @Override
    public RequestsAdapter.myHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        ItemRequestsBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_requests, parent, false);
        return new RequestsAdapter.myHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull RequestsAdapter.myHolder myHolder, int i) {
        Request data = requestsList.get(i);

        myHolder.binding.setRequestclickcallback(new RequestsAdapter.OnRequestClicked());
        myHolder.binding.setProfilecallback(new RequestsAdapter.OnProfileClicked());
        myHolder.binding.setRequest(data);

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
                new RequestsAdapter.OnRequestClicked().onRequestClicked(data);
            }
        });

        if(images != null){
            myHolder.binding.carouselView.setPageCount(images.size());
            myHolder.binding.carouselView.setImageListener(imageListener);
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
                                    .into(myHolder.binding.profile);
                        } else {

                        }
                    }
                });
    }


    //Todo: might need to add on clicked to carousel view
    public class OnRequestClicked{
        public void onRequestClicked(Request data){
            Intent intent = new Intent(mContext, IndivRequestActivity.class);

            intent.putExtra("request", data);

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

    public void setRequestsList(final List<Request> listings) {

        if (this.requestsList == null) {

            this.requestsList = listings;
            notifyItemRangeInserted(0, listings.size());

        } else {

            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return RequestsAdapter.this.requestsList.size();
                }

                @Override
                public int getNewListSize() {
                    return listings.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return RequestsAdapter.this.requestsList.get(oldItemPosition).getPostid().equals(
                            listings.get(newItemPosition).getPostid());
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    return RequestsAdapter.this.requestsList.get(oldItemPosition).getPostid().equals(
                            listings.get(newItemPosition).getPostid());
                }
            });

            this.requestsList = listings;
            result.dispatchUpdatesTo(this);
        }
    }



    @Override
    public int getItemCount() {
        return requestsList == null ? 0 : requestsList.size();
    }

    class myHolder extends RecyclerView.ViewHolder {
        private ItemRequestsBinding binding;

        public myHolder(ItemRequestsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
