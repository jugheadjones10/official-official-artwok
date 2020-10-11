package com.example.artwokmabel.notifs;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.example.artwokmabel.NotifGraphDirections;
import com.example.artwokmabel.databinding.ItemListingNotifBinding;
import com.example.artwokmabel.databinding.ItemNotifBinding;
import com.example.artwokmabel.models.Listing;
import com.example.artwokmabel.models.MainPost;
import com.example.artwokmabel.models.Notification;
import com.example.artwokmabel.R;
import com.example.artwokmabel.repos.FirestoreRepo;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NotifsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static int TYPE_LISTING = 1;
    private static int TYPE_OTHERS = 2;

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
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_LISTING) {
            ItemListingNotifBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_listing_notif, parent,false);
            return new ListingNotifViewHolder(binding);
        } else {
            ItemNotifBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_notif, parent,false);
            return new NotifViewHolder(binding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Notification data = notifsList.get(position);

        if(holder.getItemViewType() == TYPE_LISTING){
            ListingNotifViewHolder listingNotifViewHolder = (ListingNotifViewHolder)holder;
            listingNotifViewHolder.binding.setOnnotifclicked(new OnNotifcationClicked());
            listingNotifViewHolder.binding.setNotification(data);
            Picasso.get().load(data.getProtagPic()).into(listingNotifViewHolder.binding.notifProtagImg);
        }else{
            NotifViewHolder notifViewHolder = (NotifViewHolder)holder;
            notifViewHolder.binding.setOnnotifclicked(new OnNotifcationClicked());
            notifViewHolder.binding.setNotification(data);
            Picasso.get().load(data.getProtagPic()).into(notifViewHolder.binding.notifProtagImg);
        }

    }

    public class OnNotifcationClicked{
        public void onNotificationClicked(Notification notif){

            if(notif.getAction() == Notification.COMMENT){
                Log.d("notifhunter", "Comment was pressed");
                FirestoreRepo.getInstance().getPost(notif.getProtagId(), new FirestoreRepo.PostRetrieved() {
                    @Override
                    public void onPostRetrieved(MainPost post) {
                        NotifGraphDirections.ActionGlobalPostFragment4 action =
                                NotifGraphDirections.actionGlobalPostFragment4(post);
                        navController.navigate(action);
                    }
                });

            }else if (notif.getAction() == Notification.FOLLOWED){
                Log.d("notifhunter", "followed was pressed");
                NotifGraphDirections.ActionGlobalProfileFragment action =
                        NotifGraphDirections.actionGlobalProfileFragment(notif.getProtagId());
                navController.navigate(action);
            }else if(notif.getAction() == Notification.OTHERS_UPLOAD_LISTING){
                Log.d("notifhunter", "Others listing was pressed");
                FirestoreRepo.getInstance().getListing(notif.getProtagId(), new FirestoreRepo.ListingRetrieved() {
                    @Override
                    public void onListingRetrieved(Listing listing) {
                        NotifGraphDirections.ActionGlobalListingFragment4 action =
                                NotifGraphDirections.actionGlobalListingFragment4(listing);
                        navController.navigate(action);
                    }
                });
            }else if(notif.getAction() == Notification.OTHERS_UPLOAD_POST){
                Log.d("notifhunter", "Others post was pressed");
                FirestoreRepo.getInstance().getPost(notif.getProtagId(), new FirestoreRepo.PostRetrieved() {
                    @Override
                    public void onPostRetrieved(MainPost post) {
                        NotifGraphDirections.ActionGlobalPostFragment4 action =
                                NotifGraphDirections.actionGlobalPostFragment4(post);
                        navController.navigate(action);
                    }
                });
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

    @Override
    public int getItemViewType(int position) {
        if (notifsList.get(position).getAction() == Notification.OTHERS_UPLOAD_LISTING) {
            return TYPE_LISTING;
        } else {
            return TYPE_OTHERS;
        }
    }

    public class NotifViewHolder extends RecyclerView.ViewHolder {
        ItemNotifBinding binding;

        public NotifViewHolder(@NonNull ItemNotifBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public class ListingNotifViewHolder extends RecyclerView.ViewHolder {
        ItemListingNotifBinding binding;

        public ListingNotifViewHolder(@NonNull ItemListingNotifBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}

