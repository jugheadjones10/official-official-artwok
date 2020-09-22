package com.example.artwokmabel.homepage.postsfeed;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
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
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.artwokmabel.ChatGraphDirections;
import com.example.artwokmabel.HomeGraphDirections;
import com.example.artwokmabel.ProfileGraphDirections;
import com.example.artwokmabel.R;
import com.example.artwokmabel.Utils.TimeWrangler;
import com.example.artwokmabel.databinding.ItemPostBinding;
import com.example.artwokmabel.homepage.adapters.PostsAdapterViewModel;
import com.example.artwokmabel.homepage.post.PostActivityViewModel;
import com.example.artwokmabel.models.MainPost;
import com.example.artwokmabel.models.User;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.firebase.ui.firestore.paging.LoadingState;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class FirestorePagingAdapterProfileImpl extends FirestorePagingAdapter<MainPost, PostViewHolder> {

    private User user;
    private Context mContext;
    private PostsAdapterViewModel postsViewModel;
    private PostActivityViewModel postViewModel;
    private NavController navController;
    private SwipeRefreshLayout swipeRefreshLayout;

    public FirestorePagingAdapterProfileImpl(FirestorePagingOptions<MainPost> options, User user, Context mContext, NavController navController, SwipeRefreshLayout swipeRefreshLayout){
        super(options);
        this.user = user;
        this.mContext = mContext;
        this.navController = navController;
        this.swipeRefreshLayout = swipeRefreshLayout;
        postsViewModel = ViewModelProviders.of((FragmentActivity)mContext).get(PostsAdapterViewModel.class);
        postViewModel = ViewModelProviders.of((FragmentActivity)mContext).get(PostActivityViewModel.class);

    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPostBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_post, parent,false);

        return new PostViewHolder(binding);
    }

    private void onPostClicked(MainPost post){
        int currentGraph = navController.getGraph().getId();
        if(currentGraph == R.id.home_graph){
            HomeGraphDirections.ActionGlobalPostFragment action =
                    HomeGraphDirections.actionGlobalPostFragment(post);
            navController.navigate(action);
        }else if(currentGraph == R.id.profile_graph){
            ProfileGraphDirections.ActionGlobalPostFragment2 action =
                    ProfileGraphDirections.actionGlobalPostFragment2(post);
            navController.navigate(action);
        }else{
            ChatGraphDirections.ActionGlobalPostFragment3 action =
                    ChatGraphDirections.actionGlobalPostFragment3(post);
            navController.navigate(action);
        }
    }


    @Override
    protected void onBindViewHolder(@NonNull PostViewHolder postViewHolder, int i, @NonNull MainPost mainPost) {
        Log.d("postinfo", "Here's one post username : " + mainPost.getUsername());
        Log.d("postinfo", "Here's one post username : " + mainPost.getTimestamp());

        postViewHolder.binding.setPost(mainPost);
        postViewHolder.binding.setTime(TimeWrangler.changeNanopastToReadableDate(mainPost.getNanopast()));
        postViewHolder.binding.setFavorite(postViewHolder.binding.favorite);

//        if(mainPost.getUser_id().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
            postViewHolder.binding.favorite.setImageResource(R.drawable.ic_menu);
//        }else {
//            if(user.getFav_posts().contains(mainPost.getPostId())){
//                postViewHolder.binding.favorite.setImageResource(R.drawable.like);
//            }else{
//                postViewHolder.binding.favorite.setImageResource(R.drawable.heart_button);
//            }
//        }

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
                    //TODO: A smarter method to do the below would be to make the original fragment pass in the action to the
                    //adapter, instead of making the adapter do the detective work of finding the action to execute on its own.
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
//                    if(mainPost.getUser_id().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
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
//                    }else {
//                        if(((BitmapDrawable)favorite.getDrawable()).getBitmap() == ((BitmapDrawable) ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.like, null)).getBitmap()){
//                            favorite.setImageResource(R.drawable.heart_button);
//                            postsViewModel.removeUserPostFavs(post.getPostId());
//                        }else{
//                            favorite.setImageResource(R.drawable.like);
//                            postsViewModel.addUserPostFavs(post.getPostId());
//                        }
//                    }

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
                                .placeholder(R.drawable.loading_image)
                                .error(R.drawable.loading_image)
                                .into(postViewHolder.binding.profile);
                        } else {

                        }
                    }
                });
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
