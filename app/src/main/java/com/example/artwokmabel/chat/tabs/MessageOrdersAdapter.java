package com.example.artwokmabel.chat.tabs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.artwokmabel.ChatGraphDirections;
import com.example.artwokmabel.R;
import com.example.artwokmabel.chat.MessageFragmentDirections;
import com.example.artwokmabel.databinding.ItemMessageOrdersBinding;
import com.example.artwokmabel.models.OrderChat;
import com.example.artwokmabel.models.User;
import com.example.artwokmabel.repos.FirestoreRepo;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MessageOrdersAdapter extends RecyclerView.Adapter<MessageOrdersAdapter.CustomViewHolder> implements Filterable {

    private List<OrderChat> orderChatsList;
    private List<OrderChat> orderChatsListFiltered;

    private Context context;
    private MessageOrdersViewModel viewModel;
    private FirebaseAuth mAuth;
    private NavController navController;

    private static MessageOrdersAdapter instance;

    public static MessageOrdersAdapter getInstance(){
        return instance;
    }

    public MessageOrdersAdapter(Context context, NavController navController) {
        this.instance = this;
        this.context = context;
        this.mAuth = FirebaseAuth.getInstance();
        this.navController = navController;
        viewModel = ViewModelProviders.of((FragmentActivity)context).get(MessageOrdersViewModel.class);
    }

    @Override
    public MessageOrdersAdapter.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemMessageOrdersBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_message_orders, parent, false);
        return new MessageOrdersAdapter.CustomViewHolder(binding);
    }


    public void setOrderChatsList(final List<OrderChat> orderChatsList) {
        if (this.orderChatsList == null) {
            this.orderChatsList = orderChatsList;
            this.orderChatsListFiltered = orderChatsList;
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
                    return MessageOrdersAdapter.this.orderChatsList.get(oldItemPosition).getListing().getPostid() ==
                            orderChatsList.get(newItemPosition).getListing().getPostid();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    return MessageOrdersAdapter.this.orderChatsList.get(oldItemPosition).getListing().getPostid() ==
                            orderChatsList.get(newItemPosition).getListing().getPostid();
                }
            });
            this.orderChatsList = orderChatsList;
            notifyDataSetChanged();
            result.dispatchUpdatesTo(this);
        }
    }


    @Override
    public void onBindViewHolder(MessageOrdersAdapter.CustomViewHolder holder, int position) {
        OrderChat orderChat = orderChatsListFiltered.get(position);

        holder.binding.setOnprofileclicked(new OnProfileClicked());
        holder.binding.setOrderchat(orderChat);
        holder.binding.setOnchatclicked(new MessageOrdersAdapter.OnChatClicked());
        holder.binding.messageChatsTextviewTimestamp.setText(orderChat.getLastInteractionTime());
        holder.binding.messageCount.setVisibility(View.GONE);
        //holder.binding.setOrderChatcallback(new OnOrderChatClicked());

        viewModel.getUserObservable(orderChat.getListing().getUserid()).observe((FragmentActivity)context, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                Picasso.get().load(user.getProfile_url()).into(holder.binding.messageChatsPerson);
            }
        });

        class OnNumUnreads implements FirestoreRepo.OnNumUnreadsGotten{
            public void onNumUnreadsGotten(int numUnreads){
                if(numUnreads > 0) {
                    holder.binding.messageCount.setText(String.format("%d", numUnreads));
                    holder.binding.messageCount.setVisibility(View.VISIBLE);
                }
            }
        }

        FirestoreRepo.getInstance().getNumberOfUnreadOfferMessages(mAuth.getCurrentUser().getUid(), orderChat.getBuyerId(), orderChat.getListing().getPostid(), new OnNumUnreads());

        Picasso.get().load(orderChat.getListing().getPhotos().get(0)).into(holder.binding.messageChatsImageview);
    }


    public class OnChatClicked{
        public void onChatClicked(OrderChat orderChat){
            MessageFragmentDirections.ActionChatGraphToOfferFragment action =
                    MessageFragmentDirections.actionChatGraphToOfferFragment(orderChat);
            navController.navigate(action);
        }
    }

    public class OnProfileClicked{
        public void onProfileClicked(OrderChat orderChat){
            //Not sure if buyer id is the correct id to go to - check this part again when doing latest msg and time for orders
            ChatGraphDirections.ActionGlobalListingFragment3 action =
                    ChatGraphDirections.actionGlobalListingFragment3(orderChat.getListing());
            navController.navigate(action);
        }
    }

    @Override
    public int getItemCount() {
        return orderChatsListFiltered == null ? 0 : orderChatsListFiltered.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        ItemMessageOrdersBinding binding;

        public CustomViewHolder(ItemMessageOrdersBinding binding) {
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
                    orderChatsListFiltered = orderChatsList;
                } else {
                    List<OrderChat> filteredList = new ArrayList<>();
                    for (OrderChat chathead : orderChatsList) {
                        //|| user.getPhone().contains(charSequence
                        //Might need to add an or operator below to search for other things like intro
                        if (chathead.getListing().getName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(chathead);
                        }
                    }

                    orderChatsListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = orderChatsListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                orderChatsListFiltered = (ArrayList<OrderChat>) filterResults.values;

                notifyDataSetChanged();
            }
        };
    }
}
