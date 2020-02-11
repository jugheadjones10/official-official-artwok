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

import com.example.artwokmabel.R;
import com.example.artwokmabel.chat.inchat.ChatActivity;
import com.example.artwokmabel.chat.models.UserUserModel;
import com.example.artwokmabel.databinding.ItemMessageChatsBinding;
import com.example.artwokmabel.databinding.ItemMessageFollowingBinding;
import com.example.artwokmabel.models.User;
import com.example.artwokmabel.profile.people.PeopleAdapterViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MessageChatsAdapter extends RecyclerView.Adapter<MessageChatsAdapter.CustomViewHolder>{

    private List<User> usersList;
    private Context context;

    public MessageChatsAdapter(Context context) {
        this.context = context;
    }

    @Override
    public MessageChatsAdapter.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemMessageChatsBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_message_chats, parent, false);
        return new MessageChatsAdapter.CustomViewHolder(binding);
    }


    public void setUsersList(final List<User> users) {
        if (this.usersList == null) {
            this.usersList = users;
            notifyItemRangeInserted(0, users.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return MessageChatsAdapter.this.usersList.size();
                }

                @Override
                public int getNewListSize() {
                    return users.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return MessageChatsAdapter.this.usersList.get(oldItemPosition).getUid() ==
                            users.get(newItemPosition).getUid();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    return MessageChatsAdapter.this.usersList.get(oldItemPosition).getUid() ==
                            users.get(newItemPosition).getUid();
                }
            });
            this.usersList = users;
            notifyDataSetChanged();
            result.dispatchUpdatesTo(this);
        }
    }


    @Override
    public void onBindViewHolder(MessageChatsAdapter.CustomViewHolder holder, int position) {
        User user = usersList.get(position);

        holder.binding.setUser(user);
        holder.binding.setOnchatclicked(new MessageChatsAdapter.OnChatClicked());
        //holder.binding.setUsercallback(new OnUserClicked());

        Picasso.get().load(user.getProfile_url()).into(holder.binding.messageChatsImageview);
    }


    public class OnChatClicked{
        public void onChatClicked(User user){
            Intent chatIntent = new Intent(context, ChatActivity.class);
            chatIntent.putExtra("message_following_id", user.getUid());
            chatIntent.putExtra("message_following_username", user.getUsername());
            chatIntent.putExtra("message_following_profile_img", user.getProfile_url());
            context.startActivity(chatIntent);
        }
    }


    @Override
    public int getItemCount() {
        return usersList == null ? 0 : usersList.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        ItemMessageChatsBinding binding;

        public CustomViewHolder(ItemMessageChatsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
