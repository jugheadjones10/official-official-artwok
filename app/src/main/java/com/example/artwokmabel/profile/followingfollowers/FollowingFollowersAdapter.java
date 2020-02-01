package com.example.artwokmabel.profile.followingfollowers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.artwokmabel.R;
import com.example.artwokmabel.databinding.ItemFollowingsBinding;
import com.example.artwokmabel.homepage.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FollowingFollowersAdapter extends RecyclerView.Adapter<FollowingFollowersAdapter.myHolder> {
    private Context mContext;

    private List<User> followingsList;
    private List<User> followersList;

    private FirebaseAuth mAuth;
    private String type;

    public FollowingFollowersAdapter(Context context, String type){
        this.mAuth = FirebaseAuth.getInstance();
        this.mContext = context;
        this.type = type;
    }

    @NonNull
    @Override
    public FollowingFollowersAdapter.myHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        ItemFollowingsBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_followings, parent,false);

        return new FollowingFollowersAdapter.myHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FollowingFollowersAdapter.myHolder holder, int i) {
        User data;
        if(type.equals("following")){
            data = followingsList.get(i);

            holder.binding.followingButton.setText("Following");
        }else{
            data = followersList.get(i);

            if(followingsList.contains(data.getUid())){
                holder.binding.followingButton.setText("Following");
            }else{
                holder.binding.followingButton.setText("Follow");
            }
        }

        holder.binding.setUser(data);
        Picasso.get()
                .load(data.getProfile_url())
                .placeholder(R.drawable.loading_image)
                .error(R.drawable.rick_and_morty)
                .into(holder.binding.contactPicture);


        //Todo: do i need to set on click listener for carousel view too?

//        holder.binding.setOnchatclicked(new OnChatClicked());
//        holder.binding.setOnfollowingclicked(new OnFollowingClicked());
    }

    public void setFollowingsList(final List<User> users) {
        if (this.followingsList == null) {
            this.followingsList = users;
            notifyItemRangeInserted(0, users.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return FollowingFollowersAdapter.this.followingsList.size();
                }

                @Override
                public int getNewListSize() {
                    return users.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return FollowingFollowersAdapter.this.followingsList.get(oldItemPosition).getUid() ==
                            users.get(newItemPosition).getUid();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    return FollowingFollowersAdapter.this.followingsList.get(oldItemPosition).getUid() ==
                            users.get(newItemPosition).getUid();
                }
            });
            this.followingsList = users;
            result.dispatchUpdatesTo(this);
        }
    }

    public void setFollowersList(final List<User> users) {
        if (this.followersList == null) {
            this.followersList = users;
            notifyItemRangeInserted(0, users.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return FollowingFollowersAdapter.this.followersList.size();
                }

                @Override
                public int getNewListSize() {
                    return users.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return FollowingFollowersAdapter.this.followersList.get(oldItemPosition).getUid() ==
                            users.get(newItemPosition).getUid();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    return FollowingFollowersAdapter.this.followersList.get(oldItemPosition).getUid() ==
                            users.get(newItemPosition).getUid();
                }
            });
            this.followersList = users;
            result.dispatchUpdatesTo(this);
        }
    }


    public class OnFollowingClicked{
        public void onFollowingClicked(){

        }
    }

    public class OnChatClicked{
        public void onChatClicked(){

        }
    }

    @Override
    public int getItemCount() {
        if(type == "following"){
            return followingsList == null ? 0 : followingsList.size();
        }else{
            return followersList == null ? 0 : followersList.size();
        }
    }

    class myHolder extends RecyclerView.ViewHolder {
        ItemFollowingsBinding binding;

        public myHolder(ItemFollowingsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


}


