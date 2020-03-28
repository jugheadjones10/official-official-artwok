package com.example.artwokmabel.chat.tabs;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.artwokmabel.R;
import com.example.artwokmabel.Utils.TransactFragment;
import com.example.artwokmabel.chat.personalchat.ChatActivity;
import com.example.artwokmabel.databinding.ItemMessageChatsBinding;
import com.example.artwokmabel.models.NormalChat;
import com.example.artwokmabel.models.User;
import com.example.artwokmabel.repos.FirestoreRepo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MessageChatsAdapter extends RecyclerView.Adapter<MessageChatsAdapter.CustomViewHolder> implements Filterable {

    private List<NormalChat> chatsList;
    private List<NormalChat> chatsListFiltered;
    private Context context;
    private MessageChatsViewModel viewModel;

    private static MessageChatsAdapter instance;
    private FirebaseAuth mAuth;

    public static MessageChatsAdapter getInstance(){
        return instance;
    }

    public MessageChatsAdapter(Context context) {
        this.instance = this;
        this.context = context;
        this.viewModel = ViewModelProviders.of((FragmentActivity)context).get(MessageChatsViewModel.class);
        this.mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public MessageChatsAdapter.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemMessageChatsBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_message_chats, parent, false);
        return new MessageChatsAdapter.CustomViewHolder(binding);
    }


    public void setNormalChatsList(final List<NormalChat> chatHeads) {
        if (this.chatsList == null) {
            this.chatsList = chatHeads;
            this.chatsListFiltered = chatHeads;
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
        NormalChat chatHead = chatsListFiltered.get(position);

        holder.binding.setUser(chatHead.getUser());
        holder.binding.setNormalchat(chatHead);
        holder.binding.setOnchatclicked(new MessageChatsAdapter.OnChatClicked());
        holder.binding.setOnprofileclicked(new OnProfileClicked());
        holder.binding.messageCount.setVisibility(View.GONE);

        class OnNumUnreads implements FirestoreRepo.OnNumUnreadsGotten{
            public void onNumUnreadsGotten(int numUnreads){
                if(numUnreads > 0) {
                    holder.binding.messageCount.setText(String.format("%d", numUnreads));
                    holder.binding.messageCount.setVisibility(View.VISIBLE);
                }
            }
        }
        FirestoreRepo.getInstance().getNumberOfUnreadMessages(mAuth.getCurrentUser().getUid(), chatHead.getUser().getUid(), new OnNumUnreads());

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

    public class OnProfileClicked{
        public void onProfileClicked(User user){
            new TransactFragment().loadFragment(context, user.getUid());
        }
    }

    @Override
    public int getItemCount() {
        return chatsListFiltered == null ? 0 : chatsListFiltered.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        ItemMessageChatsBinding binding;

        public CustomViewHolder(ItemMessageChatsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    chatsListFiltered = chatsList;
                } else {
                    List<NormalChat> filteredList = new ArrayList<>();
                    for (NormalChat chathead : chatsList) {
                        //|| user.getPhone().contains(charSequence
                        //Might need to add an or operator below to search for other things like intro
                        if (chathead.getUser().getUsername().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(chathead);
                        }
                    }

                    chatsListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = chatsListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                chatsListFiltered = (ArrayList<NormalChat>) filterResults.values;

                notifyDataSetChanged();
            }
        };
    }
}
