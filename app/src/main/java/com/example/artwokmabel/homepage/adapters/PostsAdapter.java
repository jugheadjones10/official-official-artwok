package com.example.artwokmabel.homepage.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.artwokmabel.R;
import com.example.artwokmabel.databinding.ItemPostsBinding;
import com.example.artwokmabel.homepage.activities.IndivPostActivity;
import com.example.artwokmabel.homepage.fragments.indivuser.IndivUserFragment;
import com.example.artwokmabel.homepage.callbacks.MainPostClickCallback;
import com.example.artwokmabel.homepage.homepagestuff.HomePageActivity;
import com.example.artwokmabel.homepage.homepagestuff.HomeTabsManagerFragment;
import com.example.artwokmabel.homepage.models.MainPost;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.ImageClickListener;
import com.synnapps.carouselview.ImageListener;

import java.util.ArrayList;
import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.myHolder> {
    private Context mContext;

    private List<MainPost> postList;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

//    public PostsAdapter(Context context, List<MainPost> postList) {
//        this.postList = postList;
//        this.mContext = context;
//    }

    public PostsAdapter(Context context){
        this.mAuth = FirebaseAuth.getInstance();
        this.mContext = context;
    }

    @NonNull
    @Override
    public myHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        ItemPostsBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_posts, parent,false);

        return new myHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull myHolder holder, int i) {
        MainPost data = postList.get(i);
        holder.binding.setPost(data);
        holder.binding.setPostclickcallback(postClickCallback);
        holder.binding.setProfilecallback(profileImageClickCallback);
        holder.binding.setSharecallback(shareClickCallback);

        ArrayList<String> images = data.getPhotos();
        ImageListener imageListener = new ImageListener() {
            @Override
            public void setImageForPosition(int position, ImageView imageView) {
                Picasso.get()
                .load(images.get(position))
                .placeholder(R.drawable.loading_image)
                .error(R.drawable.rick_and_morty)
                .into(imageView);
            }
        };

        holder.binding.carouselView.setImageClickListener(new ImageClickListener() {
            @Override
            public void onClick(int position) {
                postClickCallback.onClick(data);
            }
        });

        //Change this later on by adding profile url at the point of upload
        db = FirebaseFirestore.getInstance();
        db.collection("Users")
                .document(data.getUser_id())
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
                                    .into(holder.binding.profile);
                        } else {

                        }
                    }
                });

        if(images != null){
            holder.binding.carouselView.setPageCount(images.size());
            if(images.size() == 1){
                holder.binding.carouselView.setRadius(0);
            }
            holder.binding.carouselView.setImageListener(imageListener);
        }


        //CHECK THIS PART AFTER MAKING BOTH YOURS AND OTHERS POSTS APPEAR
        //Todo: huge rearchitecture of favorites functionality
        if(data.getUser_id().equals(mAuth.getCurrentUser().getUid())){
            holder.binding.favorite.setImageResource(R.drawable.menu);
        }else{
            db = FirebaseFirestore.getInstance();
            db.collection("Users")
                    .document(mAuth.getCurrentUser().getUid())
                    .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                            if (e != null) {
                                Log.w("TAG", "Listen failed.", e);
                                return;
                            }
                            if(snapshot.exists()){
                                ArrayList<String> favs = (ArrayList<String>) snapshot.get("fav_posts");
                                if(favs.contains(data.getPostId())){
                                    holder.binding.favorite.setImageResource(R.drawable.like);
                                }else{
                                    holder.binding.favorite.setImageResource(R.drawable.favourite_post);
                                }
                            }
                        }
                    });

            holder.binding.favorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    db.collection("Users")
                            .document(mAuth.getCurrentUser().getUid())
                            .get()
                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    ArrayList<String> favs = (ArrayList<String>) documentSnapshot.get("fav_posts");
                                    if(favs.contains(data.getPostId())){
                                        holder.binding.favorite.setImageResource(R.drawable.favourite_post);
                                        db.collection("Users")
                                            .document( mAuth.getCurrentUser().getUid())
                                            .update("fav_posts", FieldValue.arrayRemove(data.getPostId()));
                                    }else{
                                        holder.binding.favorite.setImageResource(R.drawable.like);
                                        db.collection("Users")
                                            .document( mAuth.getCurrentUser().getUid())
                                            .update("fav_posts", FieldValue.arrayUnion(data.getPostId()));
                                    }
                                }
                            });
                }
            });
        }
        //Todo: do i need to set on click listener for carousel view too?
    }

    public void setPostsList(final List<MainPost> posts) {
        if (this.postList == null) {
            this.postList = posts;
            notifyItemRangeInserted(0, posts.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return PostsAdapter.this.postList.size();
                }

                @Override
                public int getNewListSize() {
                    return posts.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return PostsAdapter.this.postList.get(oldItemPosition).getPostId() ==
                            posts.get(newItemPosition).getPostId();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    return PostsAdapter.this.postList.get(oldItemPosition).getPostId() ==
                            posts.get(newItemPosition).getPostId();
                }
            });
            this.postList = posts;
            result.dispatchUpdatesTo(this);
        }
    }


    public final MainPostClickCallback profileImageClickCallback = new MainPostClickCallback() {
        @Override
        public void onClick(MainPost post) {
            IndivUserFragment indivUserFrag = new IndivUserFragment();
            Bundle args = new Bundle();
            args.putString("poster_username", post.getUsername());
            indivUserFrag.setArguments(args);
            HomePageActivity.getInstance().loadFragment(indivUserFrag);
        }
    };

    public final MainPostClickCallback shareClickCallback = new MainPostClickCallback() {
        @Override
        public void onClick(MainPost post) {
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody = "Here is the share content body";
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            HomeTabsManagerFragment.getInstance().startActivity(Intent.createChooser(sharingIntent, "Share via"));
        }
    };

    public final MainPostClickCallback postClickCallback = new MainPostClickCallback() {
        @Override
        public void onClick(MainPost post) {
            Intent intent = new Intent(mContext, IndivPostActivity.class);
            intent.putExtra("description", post.getDesc());
            intent.putExtra("hashtags", post.getHashtags());
            intent.putExtra("postid", post.getPostId());
            intent.putExtra("userid", post.getUser_id());
            intent.putExtra("username", post.getUsername());
            intent.putExtra("photos", post.getPhotos());
            mContext.startActivity(intent);
        }
    };

    @Override
    public int getItemCount() {
        return postList == null ? 0 : postList.size();
    }

    class myHolder extends RecyclerView.ViewHolder {
        ItemPostsBinding binding;

        public myHolder(ItemPostsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


}


