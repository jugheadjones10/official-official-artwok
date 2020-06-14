package com.example.artwokmabel.profile.people;

import android.content.Context;
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

import com.example.artwokmabel.HomeGraphDirections;
import com.example.artwokmabel.ProfileGraphDirections;
import com.example.artwokmabel.R;
import com.example.artwokmabel.Utils.TransactFragment;
import com.example.artwokmabel.databinding.ItemFollowerBinding;
import com.example.artwokmabel.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class FollowersAdapter extends RecyclerView.Adapter<FollowersAdapter.myHolder> {
    private Context mContext;
    private List<User> followersList;
    private List<User> followingList;

    private FirebaseAuth mAuth;
    private PeopleAdapterViewModel viewModel;
    private static FollowersAdapter instance;
    private NavController navController;

    private String forWhat;

    public static FollowersAdapter getInstance(){
        return instance;
    }


    public FollowersAdapter(Context context, String forWhat, NavController navController){
        this.mAuth = FirebaseAuth.getInstance();
        this.mContext = context;
        this.forWhat = forWhat;
        this.navController = navController;
        instance = this;

        viewModel = ViewModelProviders.of((FragmentActivity)context).get(PeopleAdapterViewModel.class);
    }

    @NonNull
    @Override
    public FollowersAdapter.myHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        ItemFollowerBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_follower, parent,false);

        return new FollowersAdapter.myHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FollowersAdapter.myHolder holder, int i) {
        User data = followersList.get(i);

        if(followingList != null){
            ArrayList<String> followingIds = new ArrayList<>();
            for(User followingUser : followingList){
                followingIds.add(followingUser.uid);
            }
            if(followingIds.contains(data.getUid())){
                holder.binding.followingButton.setText("Following");
            }else{
                holder.binding.followingButton.setText("Follow");
            }

        }else{
            holder.binding.followingButton.setText("Follow");
        }

        holder.binding.setOnuserclicked(new OnUserClicked());
        holder.binding.setFollowbutton(holder.binding.followingButton);
        holder.binding.setUser(data);
        Picasso.get()
                .load(data.getProfile_url())
                .placeholder(R.drawable.loading_image)
                .error(R.drawable.rick_and_morty)
                .into(holder.binding.contactPicture);


        //Todo: do i need to set on click listener for carousel view too?

        holder.binding.setOnchatclicked(new FollowersAdapter.OnChatClicked());
        holder.binding.setOnfollowingclicked(new FollowersAdapter.OnFollowingClicked());
    }

    public void setFollowingsList(final List<User> users) {
        this.followingList = users;
    }

    public void setFollowersList(final List<User> users) {
        if (this.followersList == null) {
            this.followersList = users;
            notifyItemRangeInserted(0, users.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return FollowersAdapter.this.followersList.size();
                }

                @Override
                public int getNewListSize() {
                    return users.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return FollowersAdapter.this.followersList.get(oldItemPosition).getUid() ==
                            users.get(newItemPosition).getUid();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    return FollowersAdapter.this.followersList.get(oldItemPosition).getUid() ==
                            users.get(newItemPosition).getUid();
                }
            });
            this.followersList = users;

            if(forWhat.equals("peoplepage")){
                FollowingAdapter.getInstance().notifyDataSetChanged();
            }
            notifyDataSetChanged();
            result.dispatchUpdatesTo(this);
        }
    }
    
    public class OnFollowingClicked{
        public void onFollowingClicked(Button followingButton, User user){
            if(followingButton.getText().toString().equals("Following")){
                followingButton.setText("Follow");

                viewModel.removeUserFollowing(mAuth.getCurrentUser().getUid(), user.getUid());
            }else{
                followingButton.setText("Following");

                viewModel.addUserFollowing(mAuth.getCurrentUser().getUid(), user.getUid());
            }

        }
    }

    public class OnChatClicked{
        public void onChatClicked(){

        }
    }

    public class OnUserClicked{
        public void onUserClicked(User user){
            int currentDestination = navController.getCurrentDestination().getId();
            if(currentDestination == R.id.searchFragment) {
                HomeGraphDirections.ActionGlobalProfileFragment action =
                        HomeGraphDirections.actionGlobalProfileFragment(user.getUid());
                navController.navigate(action);
            }else if(currentDestination == R.id.peopleFragment){
                ProfileGraphDirections.ActionProfileGraphSelf action =
                        ProfileGraphDirections.actionProfileGraphSelf(user.getUid());
                navController.navigate(action);
            }
        }
    }

    @Override
    public int getItemCount() {
        return followersList == null ? 0 : followersList.size();
    }

    class myHolder extends RecyclerView.ViewHolder {
        ItemFollowerBinding binding;

        public myHolder(ItemFollowerBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}


