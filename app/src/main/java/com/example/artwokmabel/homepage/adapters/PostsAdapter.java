package com.example.artwokmabel.homepage.adapters;

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
import com.example.artwokmabel.Utils.TransactFragment;
import com.example.artwokmabel.databinding.ItemPostBinding;
import com.example.artwokmabel.homepage.callbacks.MainPostClickCallback;
import com.example.artwokmabel.homepage.homepagewrapper.HomeTabsFragment;
import com.example.artwokmabel.homepage.post.PostActivity;
import com.example.artwokmabel.homepage.user.IndivUserFragment;
import com.example.artwokmabel.models.Comment;
import com.example.artwokmabel.models.MainPost;
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

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.myHolder> {
    private Context mContext;

    private List<MainPost> postList;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private PostsAdapterViewModel viewModel;

//    public PostsAdapter(Context context, List<MainPost> postList) {
//        this.postList = postList;
//        this.mContext = context;
//    }

    public PostsAdapter(Context context){
        this.mAuth = FirebaseAuth.getInstance();
        this.mContext = context;

        viewModel = ViewModelProviders.of((FragmentActivity)context).get(PostsAdapterViewModel.class);
    }

    @NonNull
    @Override
    public myHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        ItemPostBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_post, parent,false);

        return new myHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull myHolder holder, int i) {
        MainPost data = postList.get(i);
        holder.binding.setPost(data);
        holder.binding.setPostclickcallback(postClickCallback);
        holder.binding.setProfilecallback(new OnProfileClicked());
        holder.binding.setSharecallback(shareClickCallback);
        holder.binding.setOnfavpostclicked(new OnFavPostClicked());
//        holder.binding.setFavorite(holder.binding.favorite);

        holder.binding.postWebView.loadData(data.getDesc(), "text/html", "UTF-8");

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

//        holder.binding.carouselView.setImageClickListener(new ImageClickListener() {
//            @Override
//            public void onClick(int position) {
//                postClickCallback.onClick(data);
//            }
//        });

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

//        if(images != null){
//            holder.binding.carouselView.setPageCount(images.size());
//            if(images.size() == 1){
//                holder.binding.carouselView.setRadius(0);
//            }
//            holder.binding.carouselView.setImageListener(imageListener);
//        }


        //CHECK THIS PART AFTER MAKING BOTH YOURS AND OTHERS POSTS APPEAR
        //Todo: huge rearchitecture of favorites functionality



//        if(data.getUser_id().equals(mAuth.getCurrentUser().getUid())){
//            holder.binding.favorite.setImageResource(R.drawable.menu);
//        }else{
//            viewModel.getUserFavPostsObservable().observe((FragmentActivity)mContext, new Observer<List<String>>() {
//                @Override
//                public void onChanged(@Nullable List<String> favPosts) {
//                    if (favPosts != null) {
//                        if(favPosts.contains(data.getPostId())){
//                            holder.binding.favorite.setImageResource(R.drawable.like);
//                        }else{
//                            holder.binding.favorite.setImageResource(R.drawable.favourite_post);
//                        }
//                    }
//                }
//            });
//        }
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


//    public final MainPostClickCallback profileImageClickCallback = new MainPostClickCallback() {
//        @Override
//        public void onClick(MainPost post) {
//            IndivUserFragment indivUserFrag = new IndivUserFragment();
//            Bundle args = new Bundle();
//            args.putString("poster_username", post.getUsername());
//            indivUserFrag.setArguments(args);
//            HomePageActivity.getInstance().loadFragment(indivUserFrag);
//        }
//    };

    public final MainPostClickCallback shareClickCallback = new MainPostClickCallback() {
        @Override
        public void onClick(MainPost post) {
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody = "Here is the share content body";
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            HomeTabsFragment.getInstance().startActivity(Intent.createChooser(sharingIntent, "Share via"));
        }
    };

    public final MainPostClickCallback postClickCallback = new MainPostClickCallback() {
        @Override
        public void onClick(MainPost post) {
            Intent intent = new Intent(mContext, PostActivity.class);
            intent.putExtra("post", post);
            mContext.startActivity(intent);
        }
    };

    public class OnFavPostClicked{
        public void onFavPostClicked(MainPost post, ImageView favorite){
            viewModel.switchUserFavPostsNonObservable(post, favorite);
        }
    }

    public class OnProfileClicked{
        public void onProfileClicked(MainPost post){
            new TransactFragment().loadFragment(mContext, post.getUser_id());
        }
    }

    @Override
    public int getItemCount() {
        return postList == null ? 0 : postList.size();
    }

    class myHolder extends RecyclerView.ViewHolder {
        ItemPostBinding binding;

        public myHolder(ItemPostBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


}


