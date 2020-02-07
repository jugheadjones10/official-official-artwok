package com.example.artwokmabel.rubbish;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.artwokmabel.models.Categories;
import com.example.artwokmabel.R;

import java.util.List;

public class CategoriesAdapterOld extends RecyclerView.Adapter<CategoriesAdapterOld.MyViewHolder> {

    private Context mContext;
    private List<Categories> mData;

    public CategoriesAdapterOld(Context mContext, List<Categories> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.z_item_categories, parent, false);
        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.textView.setText(mData.get(position).getTitle());
        holder.imageView.setImageResource(mData.get(position).getThumbnail());

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        ImageView imageView;

        public MyViewHolder(View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.item_categories_textview);
            imageView = itemView.findViewById(R.id.item_categories_imageview);

        }

    }
}
