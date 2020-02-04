package com.example.artwokmabel.homepage.post;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.artwokmabel.models.Comment;
import com.example.artwokmabel.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CommentsFragment extends Fragment {

    private static final String TAG = "ViewCommentsFragment";

    //firebase
    private FirebaseAuth mAuth;

    //US firestore
    private FirebaseFirestore db;

    //widgets
    private RecyclerView mListView;
    private BottomAppBar bar;

    //vars
    public List<Comment> mComments = new ArrayList<>();
    private Context mContext;

    // Args
    public String postid;
    public String userid;

    private static CommentsFragment instance;
    public CommentsAdapter adapter;

    public static CommentsFragment getInstance(){
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comments, container, false);
        mListView = view.findViewById(R.id.listView);
        instance = this;

        ((AppCompatActivity) getActivity()).setSupportActionBar(bar);
        setHasOptionsMenu(true);

        mContext = getActivity();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mListView.setLayoutManager(layoutManager);

        adapter = new CommentsAdapter(mContext, mComments);
        mListView.setAdapter(adapter);

        setHasOptionsMenu(true);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            postid = bundle.getString("postid");
            userid = bundle.getString("userid");
        }

        retrieveComments();
        setUpHeights();

        return view;
    }

    private void setUpHeights(){
        if(adapter.getItemCount() > 5){
            //Todo: collapsing toolbar doesn't collapse when i first enter a post with moe than 5 comments
        }else{
            ViewCompat.setNestedScrollingEnabled(PostActivity.getInstance().nestedScroll, false);
            PostActivity.getInstance().indivAppBar.setExpanded(true,true);

            //Changing layout params prevents scrolling when the user swipes by touching the carouselView (collapsing toolbar)
            AppBarLayout.LayoutParams layoutParams= (AppBarLayout.LayoutParams)
                    PostActivity.getInstance().collapsingToolbarLayout.getLayoutParams();
            layoutParams.setScrollFlags(0);
            PostActivity.getInstance().collapsingToolbarLayout.setLayoutParams(layoutParams);
        }

        //ListViewHeightExpander.setListViewHeightBasedOnChildren(mListView);
    }

    private void retrieveComments(){
        Log.d(TAG, "setupFirebaseAuth: setting up firebase auth.");


        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        db.collection("Users")
                .document(userid)
                .collection("Posts")
                .document(postid)
                .collection("Comments")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                        Log.d("Userid", userid);
                        Log.d("Postid", postid);
                        mComments.clear();

                        for(DocumentSnapshot commentDoc : queryDocumentSnapshots){
                            Log.d("Comment", commentDoc.toString());

                            db.collection("Users")
                                    .document(commentDoc.getString("user_id"))
                                    .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                        @Override
                                        public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                                            if (e != null) {
                                                Log.w("TAG", "Listen failed.", e);
                                                return;
                                            }

                                            Comment newComment = new Comment(
                                                    commentDoc.getString("comment"),
                                                    commentDoc.getString("user_id"),
                                                    (long) commentDoc.get("date_created"),
                                                    snapshot.getString("username"),
                                                    snapshot.getString("profile_url"),
                                                    commentDoc.getId());

                                            Log.d("wd", newComment.toString());
                                            mComments.add(newComment);
                                            adapter.notifyDataSetChanged();
                                            //Position of adapter.notifyDataSetChanged() is one of the most important things out there
                                        }
                                    });
                        }
                        class SortMain implements Comparator<Comment> {
                            public int compare(Comment a, Comment b){
                                return (int)a.getDate_created() - (int)b.getDate_created();
                            }
                        }
                        Collections.sort(mComments, new SortMain());
                        adapter.notifyDataSetChanged();
                    }
                });
    }


    public void addNewComment(String userComment){

        if(adapter.getItemCount() > 5){
            ViewCompat.setNestedScrollingEnabled(PostActivity.getInstance().nestedScroll, true);

            AppBarLayout.LayoutParams layoutParams= (AppBarLayout.LayoutParams)
                    PostActivity.getInstance().collapsingToolbarLayout.getLayoutParams();
            layoutParams.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED);
            PostActivity.getInstance().collapsingToolbarLayout.setLayoutParams(layoutParams);

            //One small problem - currently it scrolls to the last post minus one. Include the last post.
            Integer listHeight = mListView.getHeight();//Height is in pixels
            PostActivity.getInstance().indivAppBar.setExpanded(false, true);
            PostActivity.getInstance().nestedScroll.smoothScrollTo(0, listHeight);
        }else{

        }

        mAuth = FirebaseAuth.getInstance();

        DocumentReference newCommentRef = db.collection("Users")
                .document(userid)
                .collection("Posts")
                .document(postid)
                .collection("Comments")
                .document();

        db.collection("Users")
                .document(userid)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot snapshot) {
                        Comment newComment = new Comment(
                                userComment,
                                mAuth.getCurrentUser().getUid(),
                                System.currentTimeMillis(),
                                snapshot.getString("username"),
                                snapshot.getString("profile_url"),
                                newCommentRef.getId()
                        );

                        mComments.add(newComment);
                        newCommentRef.set(newComment);

                        class SortMain implements Comparator<Comment> {
                            public int compare(Comment a, Comment b){
                                return (int)a.getDate_created() - (int)b.getDate_created();
                            }
                        }
                        Collections.sort(mComments, new SortMain());
                        adapter.notifyDataSetChanged();
                    }
                });
    }
}
