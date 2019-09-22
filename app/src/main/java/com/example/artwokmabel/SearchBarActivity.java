package com.example.artwokmabel;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class SearchBarActivity extends AppCompatActivity {

    private EditText SearchField;
    private ImageButton SearchIcon;

    private RecyclerView ResultList;

    private DatabaseReference mUserDatabase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchbar);

        mUserDatabase = FirebaseDatabase.getInstance().getReference("users");

        SearchField = findViewById(R.id.et_searchfield);
        SearchIcon = findViewById(R.id.btn_searchicon);

        ResultList = findViewById(R.id.rv_resultlist);
        ResultList.setHasFixedSize(true);
        ResultList.setLayoutManager(new LinearLayoutManager(this));

        SearchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String searchText = SearchField.getText().toString();

                firebaseUserSearch(searchText);
            }
        });
    }

    private void firebaseUserSearch(String searchText) {

        Toast.makeText(SearchBarActivity.this, "Searching...", Toast.LENGTH_LONG).show();

        Query firebaseSearchQuery = mUserDatabase.orderByChild("name").startAt(searchText).endAt(searchText + "\uf8ff");

        FirebaseRecyclerAdapter<SearchUsersActivity, UsersViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<SearchUsersActivity, UsersViewHolder>(

                SearchUsersActivity.class,
                R.layout.activity_searchresult,
                UsersViewHolder.class,
                mUserDatabase

        ) {
            @Override
            protected void populateViewHolder(UsersViewHolder usersViewHolder, SearchUsersActivity searchUsersActivity, int i) {

                usersViewHolder.setDetails(getApplicationContext(), searchUsersActivity.getName(), searchUsersActivity.getStatus(), searchUsersActivity.getImage());

            }
        };

        ResultList.setAdapter(firebaseRecyclerAdapter);
    }


    // View Holder Class

    public static class UsersViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public UsersViewHolder(@NonNull View itemView) {
            super(itemView);

            mView = itemView;

        }

        public void setDetails(Context ctx, String userName, String userStatus, String userImage) {
            TextView user_name = mView.findViewById(R.id.tv_name);
            TextView user_status = mView.findViewById(R.id.tv_status);
            ImageView user_image = mView.findViewById(R.id.iv_profile);

            user_name.setText(userName);
            user_status.setText(userStatus);

            Glide.with(ctx).load(userImage).into(user_image);

        }

    }
}
