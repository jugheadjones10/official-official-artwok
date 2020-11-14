package com.example.artwokmabel.profile.people;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.artwokmabel.ProfileGraphDirections;
import com.example.artwokmabel.R;
import com.example.artwokmabel.databinding.ItemFollowingBinding;
import com.example.artwokmabel.models.User;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FollowingAdapter extends RecyclerView.Adapter<FollowingAdapter.myHolder> {
    private Context mContext;
    private List<User> followingsList;

    private FirebaseAuth mAuth;
    private PeopleAdapterViewModel viewModel;
    private static FollowingAdapter instance;
    private NavController navController;

    public static FollowingAdapter getInstance(){
        return instance;
    }

    public FollowingAdapter(Context context, NavController navController){
        this.mAuth = FirebaseAuth.getInstance();
        this.mContext = context;
        this.navController = navController;
        instance = this;

        viewModel = ViewModelProviders.of((FragmentActivity)context).get(PeopleAdapterViewModel.class);
    }

    @NonNull
    @Override
    public FollowingAdapter.myHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        ItemFollowingBinding binding =  DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_following, parent,false);

        return new FollowingAdapter.myHolder(binding);
    }

    //Set ViewModel in recycler view item layout.
    @Override
    public void onBindViewHolder(@NonNull FollowingAdapter.myHolder holder, int i) {
        User data = followingsList.get(i);

        holder.binding.setUser(data);
        holder.binding.setButton(holder.binding.followingButton);

        holder.binding.setButtontext("Following");
        holder.binding.followingButton.setBackgroundColor(mContext.getResources().getColor(R.color.category_title_grey));

        holder.binding.setCallbacks(new UserItemCallback(
            //On User Clicked
            (User user) -> {
                ProfileGraphDirections.ActionProfileGraphSelf action =
                        ProfileGraphDirections.actionProfileGraphSelf(user.getUid());
                navController.navigate(action);
            },
            //On Button Clicked
            (Button button, User user) -> {
                new MaterialAlertDialogBuilder(mContext)
                        .setTitle("Unfollow user?")
                        .setMessage("")
                        .setNeutralButton("Cancel", null)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                button.setText("Follow");
                                button.setBackgroundColor(mContext.getResources().getColor(R.color.artwok_background_blue));
                                viewModel.removeUserFollowing(mAuth.getCurrentUser().getUid(), user.getUid());
                            }
                        })
                        .show();
            },
            //On Chat Clicked
            (User user) -> {
                if(navController.getCurrentDestination().getId() == R.id.profile_graph){
                    PeopleFragmentDirections.ActionPeopleFragmentToChatFragment action =
                            PeopleFragmentDirections.actionPeopleFragmentToChatFragment(user.getUid(), user.getUsername(), user.getProfile_url());
                    navController.navigate(action);
                }else if(navController.getCurrentDestination().getId() == R.id.chat_graph){
                    PeopleFragmentDirections.ActionPeopleFragmentToChatFragment action =
                            PeopleFragmentDirections.actionPeopleFragmentToChatFragment(user.getUid(), user.getUsername(), user.getProfile_url());
                    navController.navigate(action);
                }

            }
        ));

        Picasso.get()
                .load(data.getProfile_url())
                .placeholder(R.drawable.loading_image)
                .error(R.drawable.placeholder_color_new)
                .into(holder.binding.contactPicture);

    }

    public void setFollowingsList(final List<User> users) {
        if (this.followingsList == null) {
            this.followingsList = users;
            notifyItemRangeInserted(0, users.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return FollowingAdapter.this.followingsList.size();
                }

                @Override
                public int getNewListSize() {
                    return users.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return FollowingAdapter.this.followingsList.get(oldItemPosition).getUid() ==
                            users.get(newItemPosition).getUid();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    return FollowingAdapter.this.followingsList.get(oldItemPosition).getUid() ==
                            users.get(newItemPosition).getUid();
                }
            });
            this.followingsList = users;
            notifyDataSetChanged();
            if(FollowersAdapter.getInstance() != null){
                FollowersAdapter.getInstance().notifyDataSetChanged();
            }
            result.dispatchUpdatesTo(this);
        }
    }

    @Override
    public int getItemCount() {
            return followingsList == null ? 0 : followingsList.size();
    }

    class myHolder extends RecyclerView.ViewHolder {
        ItemFollowingBinding binding;

        public myHolder(ItemFollowingBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}


