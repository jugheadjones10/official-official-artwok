package com.example.artwokmabel.RubbishReference;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.artwokmabel.R;

import java.util.ArrayList;

public class SearchBarActivity extends AppCompatActivity {

    private static final String TAG = "SearchBarActivity";

    //vars
    private ArrayList<String> mImageUrls = new ArrayList<>();
    private ArrayList<String> mUsernames = new ArrayList<>();
    private ArrayList<String> mUserstatuses = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_bar);
        Log.d(TAG, "onCreate: started");

        initImageBitmaps();
    }
    private void initImageBitmaps() {
        Log.d(TAG, "initImageBitmaps: preparing bitmaps.");

        mImageUrls.add("https://firebasestorage.googleapis.com/v0/b/artwok-database.appspot.com/o/profile_1.png?alt=media&token=b3c1500c-987c-4b55-be14-0da3ade2973c");
        mUsernames.add("Bryn");
        mUserstatuses.add("23, Orchard");

        mImageUrls.add("https://firebasestorage.googleapis.com/v0/b/artwok-database.appspot.com/o/profile_2.png?alt=media&token=ae0a3039-dab9-4cc4-93b9-973320798055");
        mUsernames.add("Guy");
        mUserstatuses.add("40, Queenstown");

        mImageUrls.add("https://firebasestorage.googleapis.com/v0/b/artwok-database.appspot.com/o/profile_3.png?alt=media&token=4d1334b0-5023-49f6-85bc-1721c5a0ad43");
        mUsernames.add("Hway");
        mUserstatuses.add("29, Chinatown");

        initRecyclerView();
    }

    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: init recyclerview.");
        RecyclerView recyclerview = findViewById(R.id.rv_resultlist);
        SearchRecyclerViewAdapter adapter = new SearchRecyclerViewAdapter(mImageUrls, mUsernames, mUserstatuses, this);
        recyclerview.setAdapter(adapter);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
    }
}

//import android.content.Context;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.EditText;
//import android.widget.ImageButton;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.bumptech.glide.Glide;
//import com.firebase.ui.database.FirebaseRecyclerAdapter;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.Query;
//
//public class SearchBarActivity extends AppCompatActivity {
//
//    private EditText SearchField;
//    private ImageButton SearchIcon;
//
//    private RecyclerView ResultList;
//    private FirebaseRecyclerAdapter<SearchUsersActivity, UsersViewHolder> ResultListAdapter;
//
//    private DatabaseReference mUserDatabase;
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_search_bar);
//
//        mUserDatabase = FirebaseDatabase.getInstance().getReference("users");
//
//        SearchField = findViewById(R.id.et_searchfield);
//        SearchIcon = findViewById(R.id.btn_searchicon);
//
//        ResultList = findViewById(R.id.rv_resultlist);
//        ResultList.setHasFixedSize(true);
//        ResultList.setLayoutManager(new LinearLayoutManager(this));
//
//        SearchIcon.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//                String searchText = SearchField.getText().toString();
//
//                firebaseUserSearch(searchText);
//            }
//        });
//    }
//
//    private void firebaseUserSearch (String searchText) {
//        Toast.makeText(SearchBarActivity.this, "Searching", Toast.LENGTH_LONG).show();
//
//        Query firebaseSearchQuery = mUserDatabase.orderByChild("name").startAt(searchText).endAt(searchText + "\uf8ff");
//
//        FirebaseRecyclerAdapter<SearchUsersActivity, UsersViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<SearchUsersActivity, UsersViewHolder>(
//
//                SearchUsersActivity.class,
//                R.layout.activity_searchresult,
//                UsersViewHolder.class,
//                mUserDatabase
//
//        ) {
//            @Override
//            protected void onBindViewHolder(@NonNull UsersViewHolder usersViewHolder, int i, @NonNull SearchUsersActivity searchUsersActivity) {
//                usersViewHolder.setDetails(getApplicationContext(), searchUsersActivity.getName(), searchUsersActivity.getStatus(), SearchUsersActivity.getImage());
//            }
//
//            @NonNull
//            @Override
//            public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                return null;
//            }
//        };
//
//        ResultList.setAdapter(firebaseRecyclerAdapter);
//    }
//
//
//    // View Holder Class
//    public static class UsersViewHolder extends RecyclerView.ViewHolder {
//
//        View mView;
//
//        public UsersViewHolder(@NonNull View itemView) {
//            super(itemView);
//
//            mView = itemView;
//
//        }
//
//        public void setDetails(Context ctx, String userName, String userStatus, String userImage) {
//            TextView user_name = mView.findViewById(R.id.tv_name);
//            TextView user_status = mView.findViewById(R.id.tv_status);
//            ImageView user_image = mView.findViewById(R.id.iv_profile);
//
//            user_name.setText(userName);
//            user_status.setText(userStatus);
//
//            Glide.with(ctx).load(userImage).into(user_image);
//
//        }
//
//    }
//}
