//package com.example.artwokmabel.HomePage.Fragments;
//
//import android.content.Context;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.ListView;
//import android.widget.RelativeLayout;
//
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.view.ViewCompat;
//import androidx.fragment.app.Fragment;
//
//import com.example.artwokmabel.HomePage.Activities.IndivListingActivity;
//import com.example.artwokmabel.HomePage.Activities.IndivPostActivity;
//import com.example.artwokmabel.HomePage.Adapters.CommentListAdapter;
//import com.example.artwokmabel.HomePage.Models.Comment;
//import com.example.artwokmabel.R;
//import com.example.artwokmabel.Utils.ListViewHeightExpander;
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.android.material.appbar.AppBarLayout;
//import com.google.android.material.bottomappbar.BottomAppBar;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.firestore.DocumentSnapshot;
//import com.google.firebase.firestore.EventListener;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.FirebaseFirestoreException;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Locale;
//import java.util.TimeZone;
//
//public class ViewCommentsFragment extends Fragment{
//
//    private static final String TAG = "ViewCommentsFragment";
//
//    //firebase
//    private FirebaseAuth mAuth;
//    private FirebaseDatabase mFirebaseDatabase;
//    private DatabaseReference myRef;
//
//    //US firestore
//    private FirebaseFirestore db;
//
//    //widgets
//    private EditText mComment;
//    private ListView mListView;
//    private RelativeLayout addComment;
//    private BottomAppBar bar;
//
//    //vars
//    private ArrayList<Comment> mComments;
//    private Context mContext;
//    private ArrayList databaseComments;
//
//    private String postid;
//    public String userid;
//
//    private CommentListAdapter adapter;
//    private static ViewCommentsFragment instance;
//
//    public static ViewCommentsFragment getInstance(){
//        return instance;
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_view_comments, container, false);
//        mListView = (ListView) view.findViewById(R.id.listView);
//
//        ((AppCompatActivity) getActivity()).setSupportActionBar(bar);
//
//        mComments = new ArrayList<>();
//        databaseComments = new ArrayList();
//        mContext = getActivity();
//        instance = this;
//
//        setHasOptionsMenu(true);
//
//        Bundle bundle = this.getArguments();
//        if (bundle != null) {
//            postid = bundle.getString("postid");
//            userid = bundle.getString("userid");
//        }
//
//        try{
//            setupFirebaseAuth();
//
//        }catch (NullPointerException e){
//            Log.e(TAG, "onCreateView: NullPointerException: " + e.getMessage() );
//        }
//
//        return view;
//    }
//
//
//    private void setupFirebaseAuth(){
//        Log.d(TAG, "setupFirebaseAuth: setting up firebase auth.");
//
//        db = FirebaseFirestore.getInstance();
//        db.collection("Users")
//                .document(userid)
//                .collection("Posts")
//                .document(postid)
//                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
//                    @Override
//                    public void onEvent(@Nullable DocumentSnapshot snapshot,
//                                        @Nullable FirebaseFirestoreException e) {
//                        if (e != null) {
//                            Log.w(TAG, "Listen failed.", e);
//                            return;
//                        }
//
//                        if (snapshot != null && snapshot.exists()) {
//
//                            if(snapshot.getData().get("comments") != null){
//                                databaseComments = (ArrayList) snapshot.getData().get("comments");
//                                Log.d(TAG, "This is my database Comments FUCK FUCK FUCK: " + databaseComments);
//                                mComments.clear();
//                                for(int i = 0; i< databaseComments.size(); i++){
//                                    HashMap oneComment =  (HashMap) databaseComments.get(i);
//
//                                    Comment firstComment = new Comment();
//                                    firstComment.setComment(oneComment.get("comment").toString());
//                                    firstComment.setUser_id(oneComment.get("user_id").toString());
//                                    firstComment.setDate_created(oneComment.get("date_created").toString());
//
//                                    Log.d(TAG, "UCK FUCK FUCK: " + firstComment);
//
//                                    mComments.add(firstComment);
//                                }
//                                Log.d(TAG, "UCK FUCK FUCK: THIS IS MCOMMENTS NOW " + mComments);
//                            }
//                            setupWidgets();
//
//                        } else {
//
//                            Log.d(TAG, "Current data: null");
//                        }
//                    }
//                });
//    }
//
//    public void addNewComment(String newComment){
//        HashMap<String, String> new_comment = new HashMap<String, String>();
//        new_comment.put("user_id", userid);
//        new_comment.put("comment", newComment);
//        new_comment.put("date_created", getTimestamp());
//        databaseComments.add(new_comment);
//        //The purpose of the above is to pass in databaseComments into the .update() call below. State of databaseComments is NOT preserved
//
//        Comment firstComment = new Comment();
//        firstComment.setComment(newComment);
//        firstComment.setUser_id(userid);
//        firstComment.setDate_created(getTimestamp());
//        mComments.add(firstComment);
//
//        //Eventuall this number 5 will have to change - different phone sizes of users
//        if(adapter.getCount() > 5){
//            ViewCompat.setNestedScrollingEnabled(IndivPostActivity.getInstance().nestedScroll, true);
//
//            AppBarLayout.LayoutParams layoutParams= (AppBarLayout.LayoutParams)
//                    IndivPostActivity.getInstance().collapsingToolbarLayout.getLayoutParams();
//            layoutParams.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED);
//            IndivPostActivity.getInstance().collapsingToolbarLayout.setLayoutParams(layoutParams);
//
//            //One small problem - currently it scrolls to the last post minus one. Include the last post.
//            Integer listHeight = mListView.getHeight();//Height is in pixels
//            IndivPostActivity.getInstance().indivAppBar.setExpanded(false, true);
//            IndivPostActivity.getInstance().nestedScroll.smoothScrollTo(0, listHeight);
//        }else{
//
//        }
//
//        Log.d(TAG, "addNewComment: adding new comment: " + newComment);
//        db.collection("Users")
//                .document(userid)
//                .collection("Posts")
//                .document(postid)
//                .update("comments", databaseComments)
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        Log.d(TAG, "DocumentSnapshot successfully written!");
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.w(TAG, "Error writing document", e);
//                    }
//                });
//
//    }
//
////    private String getCallingActivityFromBundle(){
////        Log.d(TAG, "getPhotoFromBundle: arguments: " + getArguments());
////
////        Bundle bundle = this.getArguments();
////        if(bundle != null) {
////            return bundle.getString("View Comments");
////        }else{
////            return null;
////        }
////    }
//
//    private void setupWidgets(){
//
//         adapter = new CommentListAdapter(mContext,
//                R.layout.layout_comment, mComments, db, userid, postid, databaseComments);
//         mListView.setAdapter(adapter);
//
//        if(adapter.getCount() > 5){
//
//        }else{
//            ViewCompat.setNestedScrollingEnabled(IndivPostActivity.getInstance().nestedScroll, false);
//            IndivPostActivity.getInstance().indivAppBar.setExpanded(true,true);
//
//            //Changing layout params prevents scrolling when the user swipes by touching the carouselView (collapsing toolbar)
//            AppBarLayout.LayoutParams layoutParams= (AppBarLayout.LayoutParams)
//                    IndivPostActivity.getInstance().collapsingToolbarLayout.getLayoutParams();
//            layoutParams.setScrollFlags(0);
//            IndivPostActivity.getInstance().collapsingToolbarLayout.setLayoutParams(layoutParams);
//        }
//
//        ListViewHeightExpander.setListViewHeightBasedOnChildren(mListView);
//
//    }
//
//    private String getTimestamp(){
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH);
//        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Singapore"));
//        return sdf.format(new Date());
//    }
//
//}
