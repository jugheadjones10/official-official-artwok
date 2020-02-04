package com.example.artwokmabel.profile.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.artwokmabel.R;
import com.example.artwokmabel.rubbish.ProfileListings;

import java.util.List;

public class ProfileListingsAdapter extends RecyclerView.Adapter<ProfileListingsAdapter.MyViewHolder> {

    private Context mContext;
    private List<ProfileListings> mData;

    public ProfileListingsAdapter(Context mContext, List<ProfileListings> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.item_profile_listings, parent, false);
        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.title.setText(mData.get(position).getTitle());
        holder.price.setText(mData.get(position).getPrice());
        holder.imageView.setImageResource(mData.get(position).getThumbnail());

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView title, price;
        ImageView imageView;

        public MyViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.item_profile_listings_title_textview);
            price = itemView.findViewById(R.id.item_profile_listings_price_textview);
            imageView = itemView.findViewById(R.id.item_profile_listings_imageview);

        }

    }
}
