package com.example.artwokmabel.homepage.request.tab;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

import com.example.artwokmabel.HomePageActivity;
import com.example.artwokmabel.R;
import com.example.artwokmabel.databinding.ItemRequestBinding;
import com.example.artwokmabel.models.Request;
import com.example.artwokmabel.homepage.request.indiv.RequestActivity;
import com.example.artwokmabel.homepage.user.IndivUserFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
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
    private FirebaseAuth mAuth;
    private RequestsAdapterViewModel viewModel;

    public RequestsAdapter(Context context){
        this.mContext = context;
        this.mAuth = FirebaseAuth.getInstance();
        viewModel = ViewModelProviders.of((FragmentActivity)context).get(RequestsAdapterViewModel.class);
    }

    @NonNull
    @Override
    public RequestsAdapter.myHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        ItemRequestBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_request, parent, false);
        return new RequestsAdapter.myHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull RequestsAdapter.myHolder myHolder, int i) {
        Request data = requestsList.get(i);

        myHolder.binding.requestFavorite.bringToFront();
        myHolder.binding.setRequestclickcallback(new RequestsAdapter.OnRequestClicked());
        myHolder.binding.setProfilecallback(new RequestsAdapter.OnProfileClicked());
        myHolder.binding.setRequest(data);

        myHolder.binding.setFavorite(myHolder.binding.requestFavorite);


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

        if(data.getUserid().equals(mAuth.getCurrentUser().getUid())){
            myHolder.binding.requestFavorite.setImageResource(R.drawable.menu);
        }else {
            myHolder.binding.setOnfavrequestclicked(new OnFavRequestClicked());

            viewModel.getUserFavRequestsObservable().observe((FragmentActivity) mContext, new Observer<List<String>>() {
                @Override
                public void onChanged(@Nullable List<String> favRequests) {
                    if (favRequests != null) {
                        if (favRequests.contains(data.getPostid())) {
                            myHolder.binding.requestFavorite.setImageResource(R.drawable.like);
                        } else {
                            myHolder.binding.requestFavorite.setImageResource(R.drawable.favourite_post);
                        }
                    }
                }
            });
        }
    }


    //Todo: might need to add on clicked to carousel view
    public class OnRequestClicked{
        public void onRequestClicked(Request data){
            Intent intent = new Intent(mContext, RequestActivity.class);

            intent.putExtra("request", data);

            mContext.startActivity(intent);
        }
    }

    public class OnProfileClicked{
        public void onProfileClicked(Request data){
            IndivUserFragment indivUserFrag = new IndivUserFragment();
            Bundle args = new Bundle();
            args.putString("poster_username", data.getUsername());
            indivUserFrag.setArguments(args);
            HomePageActivity.getInstance().loadFragment(indivUserFrag);
        }
    }

    public class OnFavRequestClicked{
        public void onFavRequestClicked(Request request, ImageView favorite){
            viewModel.switchUserFavRequestsNonObservable(request, favorite);
        }
    }


    public void setRequestsList(final List<Request> requests) {

        if (this.requestsList == null) {

            this.requestsList = requests;
            notifyItemRangeInserted(0, requests.size());

        } else {

            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return RequestsAdapter.this.requestsList.size();
                }

                @Override
                public int getNewListSize() {
                    return requests.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return RequestsAdapter.this.requestsList.get(oldItemPosition).getPostid().equals(
                            requests.get(newItemPosition).getPostid());
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    return RequestsAdapter.this.requestsList.get(oldItemPosition).getPostid().equals(
                            requests.get(newItemPosition).getPostid());
                }
            });

            this.requestsList = requests;
            notifyDataSetChanged();
            result.dispatchUpdatesTo(this);
        }
    }



    @Override
    public int getItemCount() {
        return requestsList == null ? 0 : requestsList.size();
    }

    class myHolder extends RecyclerView.ViewHolder {
        private ItemRequestBinding binding;

        public myHolder(ItemRequestBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
