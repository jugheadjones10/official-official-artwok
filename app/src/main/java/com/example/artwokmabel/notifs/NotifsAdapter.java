package com.example.artwokmabel.notifs;

import android.app.NotificationManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.artwokmabel.databinding.ItemFollowerBinding;
import com.example.artwokmabel.databinding.ItemNotifBinding;
import com.example.artwokmabel.homepage.adapters.ListingsAdapter;
import com.example.artwokmabel.models.Listing;
import com.example.artwokmabel.models.Notification;
import com.example.artwokmabel.R;
import com.example.artwokmabel.profile.people.FollowersAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class NotifsAdapter extends RecyclerView.Adapter<NotifsAdapter.NotifViewHolder> {

    private Context mContext;
    private List<Notification> notifsList;

    public NotifsAdapter(Context context){
        this.mContext =  context;
    }

    @Override
    public int getItemCount() {
        return notifsList == null ? 0 : notifsList.size();
    }

    @Override
    public NotifViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemNotifBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_notif, parent,false);
        return new NotifViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull NotifViewHolder holder, int position) {
        Notification data = notifsList.get(position);

        holder.binding.setNotification(data);
        Picasso.get().load(data.getProtagPic()).into(holder.binding.notifProtagImg);
    }


    public void setNotifsList(final List<Notification> notifs) {
        if (this.notifsList == null) {
            this.notifsList = notifs;
            notifyItemRangeInserted(0, notifs.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return NotifsAdapter.this.notifsList.size();
                }

                @Override
                public int getNewListSize() {
                    return notifs.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return NotifsAdapter.this.notifsList.get(oldItemPosition).getNotifId().equals(
                            notifs.get(newItemPosition).getNotifId());
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    return NotifsAdapter.this.notifsList.get(oldItemPosition).getNotifId().equals(
                            notifs.get(newItemPosition).getNotifId());
                }
            });
            this.notifsList = notifs;
            notifyDataSetChanged();
            result.dispatchUpdatesTo(this);
        }
    }


    public class NotifViewHolder extends RecyclerView.ViewHolder {
        ItemNotifBinding binding;

        public NotifViewHolder(@NonNull ItemNotifBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}

