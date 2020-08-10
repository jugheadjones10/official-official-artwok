package com.example.artwokmabel.homepage.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.artwokmabel.HomeGraphDirections;
import com.example.artwokmabel.ProfileGraphDirections;
import com.example.artwokmabel.R;
import com.example.artwokmabel.databinding.ItemPostBinding;
import com.example.artwokmabel.homepage.callbacks.MainPostClickCallback;
import com.example.artwokmabel.homepage.homepagewrapper.HomeTabsFragment;
import com.example.artwokmabel.models.MainPost;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.ImageListener;

import java.util.ArrayList;
import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.myHolder> {
    private Context mContext;

    private List<MainPost> postList;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private PostsAdapterViewModel viewModel;
    private NavController navController;

    public PostsAdapter(Context context, NavController navController){
        this.mAuth = FirebaseAuth.getInstance();
        this.mContext = context;
        this.navController = navController;
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
//        holder.binding.setPostclickcallback(postClickCallback);
//        holder.binding.setProfilecallback(new OnProfileClicked());
//        holder.binding.setSharecallback(shareClickCallback);
        holder.binding.setFavorite(holder.binding.favorite);
        holder.binding.setPost(data);

        String encoded = Base64.encodeToString(data.getDesc().getBytes(), Base64.DEFAULT);

        holder.binding.postWebView.getSettings().setJavaScriptEnabled(true);
        class JsObject {
            @JavascriptInterface
            public void toString(String jsResult) {
                float webViewHeight = (Integer.parseInt(jsResult) * mContext.getResources().getDisplayMetrics().density);

//                ((Activity)mContext).runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        if(Math.round(webViewHeight) > 500){
//                            ViewGroup.LayoutParams params = holder.binding.postWebView.getLayoutParams();
//                            params.height = 500;
//                            holder.binding.postWebView.setLayoutParams(params);
//                        }
//                    }
//                });
                Log.d("myHeight", "parsed JSResult : " + Float.toString(webViewHeight));
            }
        }
        holder.binding.postWebView.addJavascriptInterface(new JsObject(), "HTMLOUT");
        holder.binding.postWebView.loadData(encoded, "text/html", "base64");
        holder.binding.postWebView.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                view.loadUrl("javascript:( function () { var h = document.body.scrollHeight; HTMLOUT.toString(h); } ) ()");
            }
        });

        //This click listener doesn't work. Find a way to detect a click on the web view
        //Try onTouchEvent?
        holder.binding.parentLayoutCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postClickCallback.onClick(data);
            }
        });

        ArrayList<String> images = data.getPhotos();
        ImageListener imageListener = new ImageListener() {
            @Override
            public void setImageForPosition(int position, ImageView imageView) {
                Picasso.get()
                .load(images.get(position))
                .placeholder(R.drawable.loading_image)
                .error(R.drawable.placeholder_color_new)
                .into(imageView);
            }
        };

        //Change this later on by adding profile url at the point of upload
//        db = FirebaseFirestore.getInstance();
//        db.collection("Users")
//            .document(data.getUser_id())
//            .get()
//            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                    if (task.isSuccessful()) {
//                        DocumentSnapshot document = task.getResult();
//
//                        Picasso.get()
//                                .load(document.getString("profile_url"))
//                                .placeholder(R.drawable.loading_image)
//                                .error(R.drawable.placeholder_color_new)
//                                .into(holder.binding.profile);
//                    } else {
//
//                    }
//                }
//            });

        //CHECK THIS PART AFTER MAKING BOTH YOURS AND OTHERS POSTS APPEAR
        //Todo: huge rearchitecture of favorites functionality
        if(data.getUser_id().equals(mAuth.getCurrentUser().getUid())){
            holder.binding.favorite.setImageResource(R.drawable.ic_menu);
        }else{
//            holder.binding.setOnfavpostclicked(new OnFavPostClicked());
//            viewModel.getUserFavPostsObservable().observe((FragmentActivity) mContext, new Observer<List<String>>() {
//                @Override
//                public void onChanged(List<String> favPosts) {
//                    if (favPosts.contains(data.getPostId())) {
//                        holder.binding.favorite.setImageResource(R.drawable.like);
//                    } else {
//                        holder.binding.favorite.setImageResource(R.drawable.heart_button);
//                    }
//                }
//            });

//            ArrayList<String> retrievedFavPosts = (ArrayList<String>) viewModel.getUserFavPostsObservable().getValue();
//            if (retrievedFavPosts != null) {
//                if(retrievedFavPosts.contains(data.getPostId())){
//                    holder.binding.favorite.setImageResource(R.drawable.like);
//                }else{
//                    holder.binding.favorite.setImageResource(R.drawable.favourite_post);
//                }
//            }

//            viewModel.getUserFavPostsObservable().observe((FragmentActivity)mContext, new Observer<List<String>>() {
//                @Override
//                public void onChanged(@Nullable List<String> retrievedFavPosts) {
//                    if (retrievedFavPosts != null) {
//                        favPosts = (ArrayList<String>) retrievedFavPosts;
//                        if(favPosts.contains(data.getPostId())){
//                            holder.binding.favorite.setImageResource(R.drawable.like);
//                        }else{
//                            holder.binding.favorite.setImageResource(R.drawable.favourite_post);
//                        }
//                    }
//                }
//            });
        }
    }


    public void setPostsList(final List<MainPost> posts) {
        if (this.postList == null) {
            this.postList = posts;
            notifyItemRangeInserted(0, posts.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return postList.size();
                }

                @Override
                public int getNewListSize() {
                    return posts.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return postList.get(oldItemPosition).getPostId().equals(
                            posts.get(newItemPosition).getPostId());
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    return postList.get(oldItemPosition).getPostId().equals(
                            posts.get(newItemPosition).getPostId());
//                    return postList.get(oldItemPosition).equals(
//                            posts.get(newItemPosition));
                }
            });

            this.postList = posts;
            notifyDataSetChanged();
            result.dispatchUpdatesTo(this);
        }
    }

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
            int currentDestination = navController.getCurrentDestination().getId();
            if(currentDestination == R.id.home_graph || currentDestination == R.id.favoritesFragment){
                HomeGraphDirections.ActionGlobalPostFragment action =
                        HomeGraphDirections.actionGlobalPostFragment(post);
                navController.navigate(action);
            }else if(currentDestination == R.id.profile_graph){
                ProfileGraphDirections.ActionGlobalPostFragment2 action =
                        ProfileGraphDirections.actionGlobalPostFragment2(post);
                navController.navigate(action);
            }
        }
    };

    public class OnFavPostClicked{
        public void onFavPostClicked(MainPost post, ImageView favorite){
            viewModel.switchUserFavPostsNonObservable(post, favorite);
//            if(((BitmapDrawable)favorite.getDrawable()).getBitmap() == ((BitmapDrawable) ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.like, null)).getBitmap()){
//                favorite.setImageResource(R.drawable.heart_button);
//                viewModel.removeUserPostFavs(post.getPostId());
//            }else{
//                favorite.setImageResource(R.drawable.like);
//                viewModel.addUserPostFavs(post.getPostId());
//            }
        }
    }

    public class OnProfileClicked{
        public void onProfileClicked(MainPost post){
            int currentDestination = navController.getCurrentDestination().getId();
            if(currentDestination == R.id.home_graph || currentDestination == R.id.favoritesFragment){
                HomeGraphDirections.ActionGlobalProfileFragment action =
                        HomeGraphDirections.actionGlobalProfileFragment(post.getUser_id());
                navController.navigate(action);
            }
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


