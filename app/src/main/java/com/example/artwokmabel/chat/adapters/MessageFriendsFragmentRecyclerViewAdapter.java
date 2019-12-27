package com.example.artwokmabel.chat.adapters;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.artwokmabel.chat.activities.ChatProfileActivity;
import com.example.artwokmabel.chat.models.UserUserModel;
import com.example.artwokmabel.R;
import com.example.artwokmabel.databinding.ItemMessageFriendsBinding;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MessageFriendsFragmentRecyclerViewAdapter extends RecyclerView.Adapter<MessageFriendsFragmentRecyclerViewAdapter.CustomViewHolder>{

    private List<UserUserModel> useruserModels;
    private Context context;

    public MessageFriendsFragmentRecyclerViewAdapter(Context context) {
//        this.useruserModels = userUserModels;
        this.context = context;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemMessageFriendsBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_message_friends, parent, false);
        return new CustomViewHolder(binding);
    }


    public void setUsersList(final List<UserUserModel> users) {
        if (this.useruserModels == null) {
            this.useruserModels = users;
            notifyItemRangeInserted(0, users.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return MessageFriendsFragmentRecyclerViewAdapter.this.useruserModels.size();
                }

                @Override
                public int getNewListSize() {
                    return users.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return MessageFriendsFragmentRecyclerViewAdapter.this.useruserModels.get(oldItemPosition).getUid() ==
                            users.get(newItemPosition).getUid();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    return MessageFriendsFragmentRecyclerViewAdapter.this.useruserModels.get(oldItemPosition).getUid() ==
                            users.get(newItemPosition).getUid();
                }
            });
            this.useruserModels = users;
            result.dispatchUpdatesTo(this);
        }
    }


    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {

        UserUserModel user = useruserModels.get(position);
        holder.binding.setUser(user);
        holder.binding.setUsercallback(new OnUserClicked());

        Picasso.get().load(user.getProfileImageUrl()).into(holder.binding.messageFriendsImageview);

//        Glide.with
//                (holder.itemView.getContext())
//                .load(useruserModels.get(position).getProfileImageUrl())
//                .apply(new RequestOptions().circleCrop())
//                .into((holder).imageView);

//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // when the user presses on one of the items from friend list, a profile activity pops up
//                Intent intent = new Intent(view.getContext(), ChatProfileActivity.class);
//                intent.putExtra("destinationUid", useruserModels.get(position).getUid());
//
//                // create an animation effect sliding from left to right
//                ActivityOptions activityOptions = null;
//                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
//                    activityOptions = ActivityOptions.makeCustomAnimation(view.getContext(), R.anim.fromtop, R.anim.tobottom);
//                    context.startActivity(intent, activityOptions.toBundle());
//                } else {
//                    context.startActivity(intent);
//                }
//
//            }
//        });
    }

    public class OnUserClicked{
        public void onUserClicked(UserUserModel user){
            // when the user presses on one of the items from friend list, a profile activity pops up
            Intent intent = new Intent(context, ChatProfileActivity.class);
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

    @Override
    public int getItemCount() {
        return useruserModels == null ? 0 : useruserModels.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        ItemMessageFriendsBinding binding;

        public CustomViewHolder(ItemMessageFriendsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
