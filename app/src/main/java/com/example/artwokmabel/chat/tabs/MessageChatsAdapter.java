package com.example.artwokmabel.chat.tabs;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.artwokmabel.R;
import com.example.artwokmabel.chat.personalchat.ChatActivity;
import com.example.artwokmabel.databinding.ItemMessageChatsBinding;
import com.example.artwokmabel.models.NormalChat;
import com.example.artwokmabel.models.User;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MessageChatsAdapter extends RecyclerView.Adapter<MessageChatsAdapter.CustomViewHolder>{

    private List<NormalChat> chatsList;
    private Context context;

    public MessageChatsAdapter(Context context) {
        this.context = context;
    }

    @Override
    public MessageChatsAdapter.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemMessageChatsBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_message_chats, parent, false);
        return new MessageChatsAdapter.CustomViewHolder(binding);
    }


    public void setNormalChatsList(final List<NormalChat> chatHeads) {
        if (this.chatsList == null) {
            this.chatsList = chatHeads;
            notifyItemRangeInserted(0, chatHeads.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return MessageChatsAdapter.this.chatsList.size();
                }

                @Override
                public int getNewListSize() {
                    return chatHeads.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return MessageChatsAdapter.this.chatsList.get(oldItemPosition).getUser().getUid() ==
                            chatHeads.get(newItemPosition).getUser().getUid();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    return MessageChatsAdapter.this.chatsList.get(oldItemPosition).getUser().getUid() ==
                            chatHeads.get(newItemPosition).getUser().getUid();
                }
            });
            this.chatsList = chatHeads;
            notifyDataSetChanged();
            result.dispatchUpdatesTo(this);
        }
    }


    @Override
    public void onBindViewHolder(MessageChatsAdapter.CustomViewHolder holder, int position) {
        NormalChat chatHead = chatsList.get(position);

        holder.binding.setUser(chatHead.getUser());
        holder.binding.setNormalchat(chatHead);
        holder.binding.setOnchatclicked(new MessageChatsAdapter.OnChatClicked());
        //holder.binding.setUsercallback(new OnUserClicked());

        Picasso.get().load(chatHead.getUser().getProfile_url()).into(holder.binding.messageChatsImageview);
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
        return chatsList == null ? 0 : chatsList.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        ItemMessageChatsBinding binding;

        public CustomViewHolder(ItemMessageChatsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
