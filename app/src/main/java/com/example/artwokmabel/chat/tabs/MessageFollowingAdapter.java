package com.example.artwokmabel.chat.tabs;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.artwokmabel.Utils.TransactFragment;
import com.example.artwokmabel.chat.personalchat.ChatActivity;
import com.example.artwokmabel.chat.models.UserUserModel;
import com.example.artwokmabel.R;
import com.example.artwokmabel.databinding.ItemMessageFollowingBinding;
import com.example.artwokmabel.models.MainPost;
import com.example.artwokmabel.models.User;
import com.example.artwokmabel.profile.people.PeopleAdapterViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MessageFollowingAdapter extends RecyclerView.Adapter<MessageFollowingAdapter.CustomViewHolder>{

    private List<User> usersList;
    private Context context;
    private PeopleAdapterViewModel viewModel;
    private FirebaseAuth mAuth;

    public MessageFollowingAdapter(Context context) {
        this.context = context;
        this.mAuth = FirebaseAuth.getInstance();
        viewModel = ViewModelProviders.of((FragmentActivity)context).get(PeopleAdapterViewModel.class);
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemMessageFollowingBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_message_following, parent, false);
        return new CustomViewHolder(binding);
    }

    public void setUsersList(final List<User> users) {
        if (this.usersList == null) {
            this.usersList = users;
            notifyItemRangeInserted(0, users.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return MessageFollowingAdapter.this.usersList.size();
                }

                @Override
                public int getNewListSize() {
                    return users.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return MessageFollowingAdapter.this.usersList.get(oldItemPosition).getUid() ==
                            users.get(newItemPosition).getUid();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    return MessageFollowingAdapter.this.usersList.get(oldItemPosition).getUid() ==
                            users.get(newItemPosition).getUid();
                }
            });
            this.usersList = users;
            notifyDataSetChanged();
            result.dispatchUpdatesTo(this);
        }
    }


    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        User user = usersList.get(position);
        
        holder.binding.setUser(user);
        holder.binding.setFollowbutton(holder.binding.followingButton);
        holder.binding.setOnfollowingclicked(new OnFollowingClicked());
        holder.binding.setOnchatclicked(new OnChatClicked());
        holder.binding.setOnprofileclicked(new OnProfileClicked());

        Picasso.get().load(user.getProfile_url()).into(holder.binding.contactPicture);
    }

    public class OnUserClicked{
        public void onUserClicked(UserUserModel user){
            // when the user presses on one of the items from friend list, a profile activity pops up
            Intent intent = new Intent(context, ChatActivity.class);
            intent.putExtra("destinationUid", user.getUid());
            intent.putExtra("destinationProfileUrl", user.getProfileImageUrl());
            intent.putExtra("destinationUsername", user.getUserName());
            intent.putExtra("destinationChatrooms", user.getChatrooms());

            // create an animation effect sliding from left to right
            ActivityOptions activityOptions = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                activityOptions = ActivityOptions.makeCustomAnimation(context, R.anim.fromtop, R.anim.tobottom);
                context.startActivity(intent, activityOptions.toBundle());
            } else {
                context.startActivity(intent);
            }
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

    public class OnProfileClicked{
        public void onProfileClicked(User user){
            new TransactFragment().loadFragment(context, user.getUid());
        }
    }

    public class OnChatClicked{
        public void onChatClicked(User user){
            Intent chatIntent = new Intent(context, ChatActivity.class);
            chatIntent.putExtra("message_following_id", user.getUid());
            chatIntent.putExtra("message_following_username", user.getUsername());
            chatIntent.putExtra("message_following_profile_img", user.profile_url);
            context.startActivity(chatIntent);
        }
    }


    @Override
    public int getItemCount() {
        return usersList == null ? 0 : usersList.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        ItemMessageFollowingBinding binding;

        public CustomViewHolder(ItemMessageFollowingBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
