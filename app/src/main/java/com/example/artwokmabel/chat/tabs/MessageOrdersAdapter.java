package com.example.artwokmabel.chat.tabs;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.artwokmabel.R;
import com.example.artwokmabel.Utils.TransactFragment;
import com.example.artwokmabel.chat.offerchat.OfferActivity;
import com.example.artwokmabel.chat.personalchat.ChatActivity;
import com.example.artwokmabel.databinding.ItemMessageChatsBinding;
import com.example.artwokmabel.databinding.ItemMessageOrdersBinding;
import com.example.artwokmabel.models.OrderChat;
import com.example.artwokmabel.models.User;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MessageOrdersAdapter extends RecyclerView.Adapter<MessageOrdersAdapter.CustomViewHolder>{

    private List<OrderChat> orderChatsList;
    private Context context;

    public MessageOrdersAdapter(Context context) {
        this.context = context;
    }

    @Override
    public MessageOrdersAdapter.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemMessageOrdersBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_message_orders, parent, false);
        return new MessageOrdersAdapter.CustomViewHolder(binding);
    }


    public void setOrderChatsList(final List<OrderChat> orderChatsList) {
        if (this.orderChatsList == null) {
            this.orderChatsList = orderChatsList;
            notifyItemRangeInserted(0, orderChatsList.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return MessageOrdersAdapter.this.orderChatsList.size();
                }

                @Override
                public int getNewListSize() {
                    return orderChatsList.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return MessageOrdersAdapter.this.orderChatsList.get(oldItemPosition).getPostid() ==
                            orderChatsList.get(newItemPosition).getPostid();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    return MessageOrdersAdapter.this.orderChatsList.get(oldItemPosition).getPostid() ==
                            orderChatsList.get(newItemPosition).getPostid();
                }
            });
            this.orderChatsList = orderChatsList;
            notifyDataSetChanged();
            result.dispatchUpdatesTo(this);
        }
    }


    @Override
    public void onBindViewHolder(MessageOrdersAdapter.CustomViewHolder holder, int position) {
        OrderChat orderChat = orderChatsList.get(position);

        holder.binding.setOnprofileclicked(new OnProfileClicked());
        holder.binding.setOrderchat(orderChat);
        holder.binding.setOnchatclicked(new MessageOrdersAdapter.OnChatClicked());
        //holder.binding.setOrderChatcallback(new OnOrderChatClicked());

        Picasso.get().load(orderChat.getPhotos().get(0)).into(holder.binding.messageChatsImageview);
    }


    public class OnChatClicked{
        public void onChatClicked(OrderChat orderChat){
            Intent orderChatIntent = new Intent(context, OfferActivity.class);
            orderChatIntent.putExtra("orderchat", orderChat);
//            chatIntent.putExtra("message_following_username", user.getOrderChatname());
//            chatIntent.putExtra("message_following_profile_img", user.getProfile_url());
            context.startActivity(orderChatIntent);
        }
    }

    public class OnProfileClicked{
        public void onProfileClicked(OrderChat orderChat){
            //Not sure if buyer id is the correct id to go to - check this part again when doing latest msg and time for orders
            new TransactFragment().loadFragment(context, orderChat.getBuyerId());
        }
    }

    @Override
    public int getItemCount() {
        return orderChatsList == null ? 0 : orderChatsList.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        ItemMessageOrdersBinding binding;

        public CustomViewHolder(ItemMessageOrdersBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
