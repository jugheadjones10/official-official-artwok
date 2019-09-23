package com.example.artwokmabel;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList<String> mImages = new ArrayList<>();
    private ArrayList<String> mUsernames = new ArrayList<>();
    private ArrayList<String> mUserstatuses = new ArrayList<>();
    private Context mContext;

    public RecyclerViewAdapter(ArrayList<String> images, ArrayList<String> usernames, ArrayList<String> userstatuses, Context context) {
        this.mImages = images;
        this.mUsernames = usernames;
        this.mUserstatuses = userstatuses;
        this.mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_searchresult, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: called");

        Glide.with(mContext)
                .asBitmap()
                .load(mImages.get(position))
                .into(holder.image);

        holder.username.setText(mUsernames.get(position));
        holder.userstatus.setText(mUserstatuses.get(position));

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked on: " + mUsernames.get(position));

                Toast.makeText(mContext, mUsernames.get(position), Toast.LENGTH_LONG).show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return mUsernames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView image;
        TextView username, userstatus;
        RelativeLayout parentLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.iv_profile);
            username = itemView.findViewById(R.id.tv_name);
            userstatus = itemView.findViewById(R.id.tv_status);
            parentLayout = itemView.findViewById(R.id.ly_usersresults);
        }
    }

}
