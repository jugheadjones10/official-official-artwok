package com.example.artwokmabel.notifs;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.artwokmabel.Utils.TransactFragment;
import com.example.artwokmabel.databinding.ItemFollowerBinding;
import com.example.artwokmabel.databinding.ItemNotifBinding;
import com.example.artwokmabel.homepage.adapters.ListingsAdapter;
import com.example.artwokmabel.homepage.adapters.ListingsAdapterViewModel;
import com.example.artwokmabel.homepage.listing.ListingActivity;
import com.example.artwokmabel.homepage.post.PostActivity;
import com.example.artwokmabel.models.Listing;
import com.example.artwokmabel.models.MainPost;
import com.example.artwokmabel.models.Notification;
import com.example.artwokmabel.R;
import com.example.artwokmabel.profile.people.FollowersAdapter;
import com.example.artwokmabel.repos.FirestoreRepo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class NotifsAdapter extends RecyclerView.Adapter<NotifsAdapter.NotifViewHolder> {

    private Context mContext;
    private List<Notification> notifsList;
    private NotifsViewModel viewModel;
    private NavController navController;

    public NotifsAdapter(Context context, NavController navController){
        this.mContext =  context;
        this.viewModel = ViewModelProviders.of((FragmentActivity)context).get(NotifsViewModel.class);
        this.navController = navController;
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

        holder.binding.setOnnotifclicked(new OnNotifcationClicked());
        holder.binding.setNotification(data);
        Picasso.get().load(data.getProtagPic()).into(holder.binding.notifProtagImg);
    }

    public class OnNotifcationClicked{
        public void onNotificationClicked(Notification notif){

            Intent intent;
            if(notif.getAction() == Notification.COMMENT){
                intent = new Intent(mContext, PostActivity.class);
                FirestoreRepo.getInstance().getPost(notif.getProtagId(), new FirestoreRepo.PostRetrieved() {
                    @Override
                    public void onPostRetrieved(MainPost post) {
                        intent.putExtra("post", post);
                        mContext.startActivity(intent);
                    }
                });
            }else if (notif.getAction() == Notification.FOLLOWED){

                NotifsFragmentDirections.ActionNotifGraphToProfileFragment2 action =
                        NotifsFragmentDirections.actionNotifGraphToProfileFragment2(notif.getProtagId());
                navController.navigate(action);

            }else if(notif.getAction() == Notification.OTHERS_UPLOAD_LISTING){
                intent = new Intent(mContext, ListingActivity.class);
                FirestoreRepo.getInstance().getListing(notif.getProtagId(), new FirestoreRepo.ListingRetrieved() {
                    @Override
                    public void onListingRetrieved(Listing listing) {
                        intent.putExtra("listing", listing);
                        mContext.startActivity(intent);
                    }
                });
            }else{

            }
        }
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

