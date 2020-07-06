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

import com.example.artwokmabel.ProfileGraphDirections;
import com.example.artwokmabel.R;
import com.example.artwokmabel.Utils.TransactFragment;
import com.example.artwokmabel.databinding.ItemFollowingBinding;
import com.example.artwokmabel.models.User;
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

    @Override
    public void onBindViewHolder(@NonNull FollowingAdapter.myHolder holder, int i) {
        User data = followingsList.get(i);

        holder.binding.followingButton.setText("Following");

        holder.binding.setOnuserclicked(new OnUserClicked());
        holder.binding.setFollowbutton(holder.binding.followingButton);
        holder.binding.setUser(data);
        Picasso.get()
                .load(data.getProfile_url())
                .placeholder(R.drawable.loading_image)
                .error(R.drawable.rick_and_morty)
                .into(holder.binding.contactPicture);


        //Todo: do i need to set on click listener for carousel view too?

        holder.binding.setOnchatclicked(new OnChatClicked());
        holder.binding.setOnfollowingclicked(new OnFollowingClicked());
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
        public void onChatClicked(User user){

        }
    }

    public class OnUserClicked{
        public void onUserClicked(User user){
            //Unlike FollowersAdapter, this adapter only appears in the profile graph, hence there is no need to check for
            //current destination. FollowersAdapter appears in both SearchFragment and in Profile.
            ProfileGraphDirections.ActionProfileGraphSelf action =
                    ProfileGraphDirections.actionProfileGraphSelf(user.getUid());
            navController.navigate(action);
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


