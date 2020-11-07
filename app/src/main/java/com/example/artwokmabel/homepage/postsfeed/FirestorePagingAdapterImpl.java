package com.example.artwokmabel.homepage.postsfeed;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.artwokmabel.HomeGraphDirections;
import com.example.artwokmabel.ProfileGraphDirections;
import com.example.artwokmabel.R;
import com.example.artwokmabel.Utils.TimeWrangler;
import com.example.artwokmabel.databinding.ItemFeedListingBinding;
import com.example.artwokmabel.databinding.ItemPostBinding;
import com.example.artwokmabel.homepage.adapters.ListingsAdapterViewModel;
import com.example.artwokmabel.homepage.adapters.PostsAdapterViewModel;
import com.example.artwokmabel.homepage.listing.ListingActivityViewModel;
import com.example.artwokmabel.homepage.post.PostActivityViewModel;
import com.example.artwokmabel.models.Listing;
import com.example.artwokmabel.models.ListingPost;
import com.example.artwokmabel.models.MainPost;
import com.example.artwokmabel.models.User;
import com.example.artwokmabel.repos.FirestoreRepo;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.firebase.ui.firestore.paging.LoadingState;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.ImageClickListener;
import com.synnapps.carouselview.ImageListener;

import java.util.ArrayList;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class FirestorePagingAdapterImpl extends FirestorePagingAdapter<ListingPost, RecyclerView.ViewHolder> {

    private static int TYPE_POST = 1;
    private static int TYPE_LISTING = 2;

    private User user;
    private Context mContext;
    private PostsAdapterViewModel postsViewModel;
    private ListingsAdapterViewModel listingsViewModel;
    private PostActivityViewModel postViewModel;
    private ListingActivityViewModel listingViewModel;
    private NavController navController;
    private SwipeRefreshLayout swipeRefreshLayout;

    public FirestorePagingAdapterImpl(FirestorePagingOptions<ListingPost> options, User user, Context mContext, NavController navController, SwipeRefreshLayout swipeRefreshLayout){
        super(options);
        this.user = user;
        this.mContext = mContext;
        this.navController = navController;
        this.swipeRefreshLayout = swipeRefreshLayout;
        postsViewModel = ViewModelProviders.of((FragmentActivity)mContext).get(PostsAdapterViewModel.class);
        listingsViewModel = ViewModelProviders.of((FragmentActivity)mContext).get(ListingsAdapterViewModel.class);
        postViewModel = ViewModelProviders.of((FragmentActivity)mContext).get(PostActivityViewModel.class);
        listingViewModel = ViewModelProviders.of((FragmentActivity)mContext).get(ListingActivityViewModel.class);
    }


    class PostViewHolder extends RecyclerView.ViewHolder {
        ItemPostBinding binding;

        public PostViewHolder(ItemPostBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


    class ListingViewHolder extends RecyclerView.ViewHolder {
        ItemFeedListingBinding binding;

        public ListingViewHolder(ItemFeedListingBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == TYPE_POST){
            Log.d("checkpost", "Yes, me, a post has been verified at onCreateViewHolder");
            ItemPostBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_post, parent,false);
            return new PostViewHolder(binding);
        }else{
            ItemFeedListingBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_feed_listing, parent,false);
            return new ListingViewHolder(binding);
        }
    }

    private void onPostClicked(MainPost post){
        //TODO: A smarter method to do the below would be to make the original fragment pass in the action to the
        //adapter, instead of making the adapter do the detective work of finding the action to execute on its own.
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

    @Override
    protected void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i, @NonNull ListingPost listingPost) {
        if(getItemViewType(i) == TYPE_POST){
            Log.d("checkpost", "Yes, me, a post has been verified at onBindViewHolder");
            MainPost mainPost = new MainPost(
                listingPost.getUserid(),
                listingPost.getDesc(),
                listingPost.getHashtags(),
                listingPost.getPostid(),
                listingPost.getUsername(),
                listingPost.getPhotos(),
                listingPost.getTimestamp(),
                listingPost.getNanopast()
            );

            PostViewHolder postViewHolder = (PostViewHolder)viewHolder;
            postViewHolder.binding.setPost(mainPost);
            postViewHolder.binding.setTime(TimeWrangler.changeNanopastToReadableDate(mainPost.getNanopast()));
            postViewHolder.binding.setFavorite(postViewHolder.binding.favorite);

            if(mainPost.getUserid().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()) || mainPost.getUserid().equals(FirestoreRepo.artwokId)){
                postViewHolder.binding.favorite.setImageResource(R.drawable.ic_menu);
            }else {
                if(user.getFav_posts().contains(mainPost.getPostId())){
                    postViewHolder.binding.favorite.setImageResource(R.drawable.like);
                }else{
                    postViewHolder.binding.favorite.setImageResource(R.drawable.heart_button);
                }
            }

            postViewHolder.binding.postWebView.setOnTouchListener(new View.OnTouchListener() {
                private final static long MAX_TOUCH_DURATION = 100;
                private long m_DownTime;
                @SuppressLint("ClickableViewAccessibility")
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            m_DownTime = event.getEventTime(); //init time
                            return true;
                        case MotionEvent.ACTION_UP:
                            if(event.getEventTime() - m_DownTime <= MAX_TOUCH_DURATION)
                                //On click action
                                onPostClicked(mainPost);
                                break;
                        default:
                            break; //No-Op
                    }
                    return false;
                }
            });

            postViewHolder.binding.setCallbacks(new PostCallback(
                    //On post clicked
                    (MainPost post) -> {
                        onPostClicked(post);
                    },
                    //On profile clicked
                    (MainPost post) -> {
                        int currentDestination = navController.getCurrentDestination().getId();
                        if(currentDestination == R.id.home_graph || currentDestination == R.id.favoritesFragment){
                            HomeGraphDirections.ActionGlobalProfileFragment2 action =
                                    HomeGraphDirections.actionGlobalProfileFragment2(post.getUserid());
                            navController.navigate(action);
                        }
                    },
                    //On share clicked
                    (MainPost post) -> {

                    },
                    //On fav clicked
                    (MainPost post, ImageView favorite) -> {
                        if(mainPost.getUserid().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()) || mainPost.getUserid().equals(FirestoreRepo.artwokId)){
                            String[] items = {"Delete", "Report"};
                            new MaterialAlertDialogBuilder(mContext)
                                .setItems(items, (dialog, which) -> {
                                    if(which == 0){
                                        //If delete selected
                                        new MaterialAlertDialogBuilder(mContext)
                                                .setTitle("Delete Post?")
                                                .setMessage("This action cannot be reversed!")
                                                .setNeutralButton("Cancel", null)
                                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        postViewModel.deleteUserPost(post.getPostId());
                                                    }
                                                })
                                                .show();
                                    }else{
                                        //If report selected
                                        final EditText taskEditText = new EditText(mContext);
                                        new MaterialAlertDialogBuilder(mContext)
                                                .setTitle("Report this post")
                                                .setMessage("Describe why you feel this post inappropriate")
                                                .setView(taskEditText)
                                                .setPositiveButton("Report", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        String report = String.valueOf(taskEditText.getText());
                                                        postViewModel.sendPostReport(report, post.getPostId());
                                                    }
                                                })
                                                .setNeutralButton("Cancel", null)
                                                .show();
                                    }
                                })
                                .show();
                        }else {
                            if(((BitmapDrawable)favorite.getDrawable()).getBitmap() == ((BitmapDrawable) ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.like, null)).getBitmap()){
                                favorite.setImageResource(R.drawable.heart_button);
                                postsViewModel.removeUserPostFavs(post.getPostId());
                            }else{
                                favorite.setImageResource(R.drawable.like);
                                postsViewModel.addUserPostFavs(post.getPostId());
                            }
                        }

                    }
            ));

            String encoded = Base64.encodeToString(mainPost.getDesc().getBytes(), Base64.DEFAULT);
            postViewHolder.binding.postWebView.loadData(encoded, "text/html", "base64");

            //Change this later on by adding profile url at the point of upload
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("Users")
                .document(mainPost.getUserid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();

                            Picasso.get()
                                    .load(document.getString("profile_url"))
                                    .placeholder(R.drawable.placeholder_black_new)
                                    .error(R.drawable.placeholder_color_new)
                                    .into(postViewHolder.binding.profile);
                        } else {

                        }
                    }
                });
        }else{
            Listing listingModel = new Listing(
                    listingPost.getUserid(),
                    listingPost.getReturn_exchange(),
                    listingPost.getPrice(),
                    listingPost.getPhotos(),
                    listingPost.getName(),
                    listingPost.getHashtags(),
                    listingPost.getDesc(),
                    listingPost.getDelivery(),
                    listingPost.getUsername(),
                    listingPost.getPostid(),
                    listingPost.getNanopast(),
                    listingPost.getCategories()
            );

            ListingViewHolder listingViewHolder = (ListingViewHolder) viewHolder;

            listingViewHolder.binding.setListing(listingModel);
            listingViewHolder.binding.setFavorite(listingViewHolder.binding.favorite);
            listingViewHolder.binding.setTime(TimeWrangler.changeNanopastToReadableDate(listingModel.getNanopast()));

            listingViewHolder.binding.listingImage.setImageClickListener(new ImageClickListener() {
                @Override
                public void onClick(int position) {
                    if(navController.getCurrentDestination().getId() == R.id.profile_graph){
                        ProfileGraphDirections.ActionGlobalListingFragment2 action =
                                ProfileGraphDirections.actionGlobalListingFragment2(listingModel);
                        navController.navigate(action);
                    }else{
                        HomeGraphDirections.ActionGlobalListingFragment action =
                                HomeGraphDirections.actionGlobalListingFragment(listingModel);
                        navController.navigate(action);
                    }
                }
            });

            if(listingModel.getUserid().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                listingViewHolder.binding.favorite.setImageResource(R.drawable.ic_menu);
            }else {
                if(user.getFav_listings().contains(listingModel.getPostid())){
                    listingViewHolder.binding.favorite.setImageResource(R.drawable.like);
                }else{
                    listingViewHolder.binding.favorite.setImageResource(R.drawable.heart_button);
                }
            }

            listingViewHolder.binding.setCallbacks(new ListingCallback(
                //On listing clicked
                (Listing listing) -> {
                    //TODO: A smarter method to do the below would be to make the original fragment pass in the action to the
                    //adapter, instead of making the adapter do the detective work of finding the action to execute on its own.
                    if(navController.getCurrentDestination().getId() == R.id.profile_graph){
                        ProfileGraphDirections.ActionGlobalListingFragment2 action =
                                ProfileGraphDirections.actionGlobalListingFragment2(listing);
                        navController.navigate(action);
                    }else{
                        HomeGraphDirections.ActionGlobalListingFragment action =
                                HomeGraphDirections.actionGlobalListingFragment(listing);
                        navController.navigate(action);
                    }
                },
                //On profile clicked
                (Listing listing) -> {
                    int currentDestination = navController.getCurrentDestination().getId();
                    if(currentDestination == R.id.home_graph || currentDestination == R.id.favoritesFragment){
                        HomeGraphDirections.ActionGlobalProfileFragment2 action =
                                HomeGraphDirections.actionGlobalProfileFragment2(listing.getUserid());
                        navController.navigate(action);
                    }
                },
                //On share clicked
                (Listing listing) -> {

                },
                //On fav clicked
                (Listing listing, ImageView favorite) -> {
                    if(listingModel.getUserid().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                        String[] items = {"Delete", "Report"};
                        new MaterialAlertDialogBuilder(mContext)
                                .setItems(items, (dialog, which) -> {
                                    if(which == 0){
                                        //If delete selected
                                        new MaterialAlertDialogBuilder(mContext)
                                                .setTitle("Delete Listing?")
                                                .setMessage("This action cannot be reversed!")
                                                .setNeutralButton("Cancel", null)
                                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        listingViewModel.deleteUserListing(listing.getPostid());
                                                    }
                                                })
                                                .show();
                                    }else{
                                        //If report selected
                                        final EditText taskEditText = new EditText(mContext);
                                        new MaterialAlertDialogBuilder(mContext)
                                                .setTitle("Report this listing")
                                                .setMessage("Describe why you feel this post inappropriate")
                                                .setView(taskEditText)
                                                .setPositiveButton("Report", new DialogInterface.OnClickListener() {
                                                        @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        String report = String.valueOf(taskEditText.getText());
                                                        listingViewModel.sendListingReport(report, listing.getPostid());
                                                    }
                                                })
                                                .setNeutralButton("Cancel", null)
                                                .show();
                                    }
                                })
                                .show();
                    }else {
                        if(((BitmapDrawable)favorite.getDrawable()).getBitmap() == ((BitmapDrawable) ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.like, null)).getBitmap()){
                            favorite.setImageResource(R.drawable.heart_button);
                            listingsViewModel.removeUserListingFavs(listing.getPostid());
                        }else{
                            favorite.setImageResource(R.drawable.like);
                            listingsViewModel.addUserListingFavs(listing.getPostid());
                        }
                    }
                }
            ));

            //Use the below if you use ImageView instead of CarouselView
//            Picasso.get()
//                    .load(listingModel.getPhotos().get(0))
//                    .transform(new RoundedCornersTransformation(10, 0))
//                    .resize(300, 300)
//                    .placeholder(R.drawable.placeholder_black_new)
//                    .error(R.drawable.placeholder_color_new)
//                    .into(listingViewHolder.binding.listingImage);

            /////////////////////////////////////
//
//            listingViewHolder.binding.setListingclickcallback(new ListingsAdapter.OnListingClicked());
//            listingViewHolder.binding.normalListingLike.bringToFront();
//
            ArrayList<String> images = listingModel.getPhotos();
            ImageListener imageListener = new ImageListener() {
                @Override
                public void setImageForPosition(int position, ImageView imageView) {
                    Picasso.get()
                        .load(images.get(position))
                        .transform(new RoundedCornersTransformation(10, 0))
                        .resize(300, 300)
                        .placeholder(R.drawable.placeholder_black_new)
                        .error(R.drawable.placeholder_color_new)
                        .into(imageView);
                }
            };

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("Users")
                    .document(listingModel.getUserid())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();

                                Picasso.get()
                                        .load(document.getString("profile_url"))
                                        .placeholder(R.drawable.placeholder_black_new)
                                        .error(R.drawable.placeholder_color_new)
                                        .into(listingViewHolder.binding.profile);
                            } else {

                            }
                        }
                    });

//            listingViewHolder.binding.listingImage.setImageClickListener(new ImageClickListener() {
//                @Override
//                public void onClick(int position) {
//                    new ListingsAdapter.OnListingClicked().onListClicked(data);
//                }
//            });

            if(images != null){
                listingViewHolder.binding.listingImage.setPageCount(images.size());
                listingViewHolder.binding.listingImage.setImageListener(imageListener);
            }
//
//            if(data.getUserid().equals(mAuth.getCurrentUser().getUid())){
//                //myHolder.binding.normalListingLike.setImageResource(R.drawable.ic_menu);
//            }else {
//                listingViewHolder.binding.setOnfavlistingclicked(new OnFavListingClicked());
//                postsViewModel.getUserFavListingsObservable().observe((FragmentActivity) mContext, new Observer<List<String>>() {
//                    @Override
//                    public void onChanged(@Nullable List<String> favListings) {
//                        if (favListings != null) {
//                            if (favListings.contains(data.getPostid())) {
//                                listingViewHolder.binding.normalListingLike.setImageResource(R.drawable.like);
//                            } else {
//                                listingViewHolder.binding.normalListingLike.setImageResource(R.drawable.favourite_post);
//                            }
//                        }
//                    }
//                });
//            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        DocumentSnapshot snapshot = getItem(position);
        ListingPost listingPost = FirestoreRepo.getInstance().changeDocToListingPostModel(snapshot);

        Log.d("checkpost", listingPost.getUsername());
        Log.d("checkpost", "AWD " + listingPost.getReturn_exchange());
        if (TextUtils.isEmpty(listingPost.getReturn_exchange())) {
            Log.d("checkpost", "Yes, me, a post has been verified at getItemViewType");
            return TYPE_POST;
        } else {
            Log.d("checkpost", "Yes, me, listing was seen");
            return TYPE_LISTING;
        }
    }

    @Override
    protected void onError(@NonNull Exception e) {
        super.onError(e);
        Log.e("Firestorepaging", e.getMessage());
    }

    @Override
    protected void onLoadingStateChanged(@NonNull LoadingState state) {
        Log.d("loadingstate", state.toString());
        switch (state) {
            case LOADING_INITIAL:
            case LOADING_MORE:
                swipeRefreshLayout.setRefreshing(true);
                break;

            case LOADED:
                swipeRefreshLayout.setRefreshing(false);
                break;

            case ERROR:
                Toast.makeText(
                        mContext,
                        "Error Occurred!",
                        Toast.LENGTH_SHORT
                ).show();

                swipeRefreshLayout.setRefreshing(false);
                break;

            case FINISHED:
                swipeRefreshLayout.setRefreshing(false);
                break;
        }
    }
}
