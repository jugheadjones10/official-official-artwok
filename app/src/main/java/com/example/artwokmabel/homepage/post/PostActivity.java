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
import androidx.fragment.app.FragmentTransaction;

import com.example.artwokmabel.homepage.homepagewrapper.HomeTabsFragment;
import com.example.artwokmabel.R;
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

public class PostActivity extends AppCompatActivity {

    private TextView post_desc;
    private TextView post_hashtags;
    private CarouselView carouselView;
    private ImageButton favorite, share;
    public NestedScrollView nestedScroll;
    public AppBarLayout indivAppBar;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    public CollapsingToolbarLayout collapsingToolbarLayout;
    TextInputLayout commentBar;
    TextInputEditText commentEdit;

    private static PostActivity instance = null;

    int phoneHeight;
    Boolean heightSet;

    public static PostActivity getInstance(){
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        instance = this;
        post_desc = (TextView)findViewById(R.id.post_desc);
        post_hashtags = (TextView)findViewById(R.id.post_hashtags);
        carouselView = findViewById(R.id.post_image_me);
        nestedScroll = findViewById(R.id.nested_scroll);
        indivAppBar = findViewById(R.id.indiv_appbar);
        collapsingToolbarLayout = findViewById(R.id.indiv_toolbar_collapse);
        favorite = findViewById(R.id.favorite);
        share = findViewById(R.id.share_button_indiv);

        commentBar = findViewById(R.id.comment_bar);
        commentEdit = findViewById(R.id.edit_comment);

        heightSet = false;

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        phoneHeight = metrics.heightPixels;

        Toolbar toolbar = findViewById(R.id.indiv_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getIncomingIntent();

        commentBar.bringToFront();
        //SET POSITION OF COMMENT BAR AND BRING TO FRONT

        ViewTreeObserver vto = commentBar.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if(heightSet == false){
                    //SET POSITION OF COMMENT BAR AND BRING TO FRONT
                    ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) commentBar.getLayoutParams();
                    Log.d("FUCK", "THIS IS SCREEN HEIGHT" + phoneHeight);
                    params.topMargin = phoneHeight - commentBar.getHeight();
                    Log.d("FUCK", "Height of comment bar in pixels" + commentBar.getHeight());
                    heightSet = true;
                }
            }

        });


        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "Here is the share content body";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                HomeTabsFragment.getInstance().startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });


        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        if(getIntent().getStringExtra("userid").equals(mAuth.getCurrentUser().getUid())){
            favorite.setImageResource(R.drawable.menu);
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
                                if(favs.contains(getIntent().getStringExtra("postid"))){
                                    favorite.setImageResource(R.drawable.like);
                                }else{
                                    favorite.setImageResource(R.drawable.favourite_post);
                                }
                            }
                        }
                    });

            favorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    db.collection("Users")
                            .document(mAuth.getCurrentUser().getUid())
                            .get()
                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    ArrayList<String> favs = (ArrayList<String>) documentSnapshot.get("fav_posts");
                                    if(favs.contains(getIntent().getStringExtra("postid"))){
                                       favorite.setImageResource(R.drawable.favourite_post);
                                        db.collection("Users")
                                                .document( mAuth.getCurrentUser().getUid())
                                                .update("fav_posts", FieldValue.arrayRemove(getIntent().getStringExtra("postid")));
                                    }else{
                                        favorite.setImageResource(R.drawable.like);
                                        db.collection("Users")
                                                .document( mAuth.getCurrentUser().getUid())
                                                .update("fav_posts", FieldValue.arrayUnion(getIntent().getStringExtra("postid")));
                                    }
                                }
                            });
                }
            });
        }


        CommentsFragment fragment = new CommentsFragment();
        Bundle args = new Bundle();
        args.putString("postid", getIntent().getStringExtra("postid"));
        args.putString("userid", getIntent().getStringExtra("userid"));
        fragment.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.indiv_post_container, fragment);
        transaction.commit();

        //Add on click listener to send icon
        commentBar.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!commentEdit.getText().toString().equals("")){
                    Log.d("FUCK", "onClick: attempting to submit new comment.");
                    fragment.addNewComment(commentEdit.getText().toString());

                    commentEdit.setText("");

                    closeKeyboard();
                }else{
                    Toast.makeText(getApplicationContext(), "you can't post a blank comment", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
        Log.d("TAG", "getIncomingIntent: checking for incoming intents.");

        if(getIntent().hasExtra("description") && getIntent().hasExtra("hashtags")){
            Log.d("TAG", "getIncomingIntent: found intent extras.");

            String descText = getIntent().getStringExtra("description");
            String hashText = getIntent().getStringExtra("hashtags");
            String userName = getIntent().getStringExtra("username");

            ArrayList<String> images = getIntent().getStringArrayListExtra("photos");
            ImageListener imageListener = new ImageListener() {
                @Override
                public void setImageForPosition(int position, ImageView imageView) {
                    //imageView.setImageResource(sampleImages[position]);
                    Picasso.get()
                            .load(images.get(position))
                            .placeholder(R.drawable.user)
                            .error(R.drawable.rick_and_morty)
                            .into(imageView);
                }
            };

            if(images != null){
                carouselView.setPageCount(images.size());
                carouselView.setImageListener(imageListener);

                if(images.size() == 1){
                    carouselView.setRadius(0);
                }
            }


            post_desc.setText(descText);
            post_hashtags.setText(userName);
            //NOTICE :  natively set icons, title, and subtitle on toolbar next time
        }
    }
}
