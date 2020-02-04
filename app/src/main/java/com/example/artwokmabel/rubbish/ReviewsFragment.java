package com.example.artwokmabel.homepage.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.artwokmabel.homepage.adapters.ReviewsAdapter;
import com.example.artwokmabel.homepage.models.Review;
import com.example.artwokmabel.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

//import com.example.artwokmabel.Utils.ListViewHeightExpander;

public class ReviewsFragment extends Fragment {

    private static final String TAG = "ViewCommentsFragment";

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;

    //US firestore
    private FirebaseFirestore db;

    //widgets
    private RecyclerView mListView;

    //vars
    private List<Review> mReviews = new ArrayList<>();
    private Context mContext;

    private ReviewsAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_comments, container, false);
        mListView = view.findViewById(R.id.listView);

        mContext = getActivity();

        adapter = new ReviewsAdapter(mContext, mReviews);
        mListView.setAdapter(adapter);

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

        return view;
    }


    private void setupFirebaseAuth(){
        Log.d(TAG, "setupFirebaseAuth: setting up firebase auth.");

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();

    }

}
