package com.example.artwokmabel.homepage.post;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.artwokmabel.databinding.ActivityPost2Binding;
import com.example.artwokmabel.databinding.ActivityPostBinding;
import com.example.artwokmabel.homepage.adapters.ListingsAdapter;
import com.example.artwokmabel.homepage.callbacks.ShareClickCallback;
import com.example.artwokmabel.homepage.homepagewrapper.HomeTabsFragment;
import com.example.artwokmabel.R;
import com.example.artwokmabel.homepage.listing.ListingActivityViewModel;
import com.example.artwokmabel.models.Comment;
import com.example.artwokmabel.models.Listing;
import com.example.artwokmabel.models.MainPost;
import com.example.artwokmabel.models.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.util.ArrayList;
import java.util.List;

public class PostActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
//
//    TextInputLayout commentBar;
//    TextInputEditText commentEdit;

    private static PostActivity instance = null;

    int phoneHeight;
    Boolean heightSet;

    public ActivityPost2Binding binding;
    private PostActivityViewModel viewModel;
    private MainPost post;
    private CommentsAdapter commentsAdapter;

    private String postId;
    private String posterUserId;

    public static PostActivity getInstance(){
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        instance = this;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_post_2);

        mAuth = FirebaseAuth.getInstance();
//        heightSet = false;
//
//        DisplayMetrics metrics = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(metrics);
//        phoneHeight = metrics.heightPixels;

        setSupportActionBar(binding.indivToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getIncomingIntent();

        commentsAdapter = new CommentsAdapter(this, postId, posterUserId);
        binding.commentsRecyclerView.setAdapter(commentsAdapter);

//        commentBar.bringToFront();
        //SET POSITION OF COMMENT BAR AND BRING TO FRONT

//        ViewTreeObserver vto = commentBar.getViewTreeObserver();
//        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                if(heightSet == false){
//                    //SET POSITION OF COMMENT BAR AND BRING TO FRONT
//                    ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) commentBar.getLayoutParams();
//                    Log.d("FUCK", "THIS IS SCREEN HEIGHT" + phoneHeight);
//                    params.topMargin = phoneHeight - commentBar.getHeight();
//                    Log.d("FUCK", "Height of comment bar in pixels" + commentBar.getHeight());
//                    heightSet = true;
//                }
//            }
//
//        });


        //Add in share functionality to triple dot?
//        binding.shareButtonIndiv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                new ShareClickCallback().onClick();
//                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
//                sharingIntent.setType("text/plain");
//                String shareBody = "Here is the share content body";
//                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
//                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
//                HomeTabsFragment.getInstance().startActivity(Intent.createChooser(sharingIntent, "Share via"));
//            }
//        });


        viewModel = ViewModelProviders.of(this).get(PostActivityViewModel.class);

        viewModel.getUserObservable(mAuth.getCurrentUser().getUid()).observe(this, new Observer<User>() {
            @Override
            public void onChanged(@Nullable User user) {
                if (user != null) {

                    if(posterUserId.equals(mAuth.getCurrentUser().getUid())){
                        binding.favorite.setVisibility(View.GONE);
                    }else{
                        ArrayList<String> favs = user.getFav_posts();
                        Log.d("favfav", favs.toString());

                        if(favs != null && favs.contains(postId)){
                            binding.favorite.setImageResource(R.drawable.like);
                        }else{
                            binding.favorite.setImageResource(R.drawable.favourite_post);
                        }

                        binding.favorite.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if(favs != null && favs.contains(postId)){
                                    viewModel.removeUserPostFavs(postId);
                                }else{
                                    viewModel.addUserPostFavs(postId);
                                }
                            }
                        });

                    }
                }
            }
        });

        viewModel.getUserObservable(posterUserId).observe(this, new Observer<User>() {
            @Override
            public void onChanged(@Nullable User user) {
                if (user != null) {
                    binding.setUser(user);
                    Picasso.get().load(user.getProfile_url()).into(binding.profilePicture);
                }
            }
        });


        viewModel.getCommentsObservable(postId, posterUserId).observe(this, new Observer<List<Comment>>() {
            @Override
            public void onChanged(@Nullable List<Comment> commentsList) {
                if (commentsList != null) {
                    commentsAdapter.setCommentsList(commentsList);
                }
            }
        });

        binding.addComment.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!binding.commentEditText.getText().toString().equals("")){

                    viewModel.addComment(binding.commentEditText.getText().toString(), mAuth.getCurrentUser().getUid(), posterUserId, postId);

                    binding.commentEditText.setText("");
                    closeKeyboard();
                }else{
                    Toast.makeText(getApplicationContext(), "you can't post a blank comment", Toast.LENGTH_SHORT).show();
                }
            }
        });

//        CommentsFragment fragment = new CommentsFragment();
//        Bundle args = new Bundle();
//        args.putString("postid", postId);
//        args.putString("userid", posterUserId);
//        fragment.setArguments(args);
//
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.replace(R.id.indiv_post_container, fragment);
//        transaction.commit();

        //Add on click listener to send icon
//        commentBar.setEndIconOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(!commentEdit.getText().toString().equals("")){
//                    Log.d("FUCK", "onClick: attempting to submit new comment.");
//                    fragment.addNewComment(commentEdit.getText().toString());
//
//                    commentEdit.setText("");
//
//                    closeKeyboard();
//                }else{
//                    Toast.makeText(getApplicationContext(), "you can't post a blank comment", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void closeKeyboard(){
        View view = getCurrentFocus();
        if(view != null){
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void getIncomingIntent(){

        post = (MainPost) getIntent().getSerializableExtra("post");

        postId = post.getPostId();
        posterUserId = post.getUser_id();

        ArrayList<String> images = post.getPhotos();
        ImageListener imageListener = new ImageListener() {
            @Override
            public void setImageForPosition(int position, ImageView imageView) {
                Picasso.get()
                        .load(images.get(position))
                        .placeholder(R.drawable.user)
                        .error(R.drawable.rick_and_morty)
                        .into(imageView);
            }
        };

        if(images != null){
            binding.postCarousel.setPageCount(images.size());
            binding.postCarousel.setImageListener(imageListener);

            if(images.size() == 1){
                binding.postCarousel.setRadius(0);
            }
        }

//
//
//        if(getIntent().hasExtra("description") && getIntent().hasExtra("hashtags")){
//            Log.d("TAG", "getIncomingIntent: found intent extras.");
//
//            String descText = getIntent().getStringExtra("description");
//            String hashText = getIntent().getStringExtra("hashtags");
//            String userName = getIntent().getStringExtra("username");
//
//            postId = getIntent().getStringExtra("postid");
//            posterUserId = getIntent().getStringExtra("userid");
//
//            ArrayList<String> images = getIntent().getStringArrayListExtra("photos");
//            ImageListener imageListener = new ImageListener() {
//                @Override
//                public void setImageForPosition(int position, ImageView imageView) {
//                    //imageView.setImageResource(sampleImages[position]);
//                    Picasso.get()
//                            .load(images.get(position))
//                            .placeholder(R.drawable.user)
//                            .error(R.drawable.rick_and_morty)
//                            .into(imageView);
//                }
//            };
//
//            if(images != null){
//                binding.postCarousel.setPageCount(images.size());
//                binding.postCarousel.setImageListener(imageListener);
//
//                if(images.size() == 1){
//                    binding.postCarousel.setRadius(0);
//                }
//            }
//
//
//            binding.postDesc.setText(descText);
//            binding.postHashtags.setText(userName);
//            //NOTICE :  natively set icons, title, and subtitle on toolbar next time
//        }
    }
}
