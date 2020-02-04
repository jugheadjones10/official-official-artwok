package com.example.artwokmabel.homepage.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.artwokmabel.homepage.models.Notification;
import com.example.artwokmabel.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NotifsAdapter extends RecyclerView.Adapter<NotifsAdapter.NotifViewHolder> {

    private Context mContext;
    private ArrayList<Notification> notifsList;

    public NotifsAdapter(Context context, ArrayList<Notification> notifications){
        this.mContext =  context;
        this.notifsList = notifications;
    }

    @Override
    public int getItemCount() {
        return notifsList.size();
    }

    @Override
    public NotifViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notif, parent, false);

        return new NotifViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotifViewHolder holder, int position) {
        Picasso.get().load(notifsList.get(position).getProtagPic()).into(holder.imageView);
        holder.textView.setText(notifsList.get(position).getProtagUsername());

        if(1 == 1){
            holder.actionView.setText("started following you!");
        }
    }

    public class NotifViewHolder extends RecyclerView.ViewHolder {
        // Initialize Variable
        ImageView imageView;
        TextView textView, actionView;

        public NotifViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.notif_protag_img);
            textView = itemView.findViewById(R.id.notif_protag_text);
            actionView = itemView.findViewById(R.id.notif_action);
        }
    }

}

