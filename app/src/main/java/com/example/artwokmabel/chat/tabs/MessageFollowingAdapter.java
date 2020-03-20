package com.example.artwokmabel.chat.tabs;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;

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
import com.example.artwokmabel.databinding.MessageFollowingFragmentBinding;
import com.example.artwokmabel.models.MainPost;
import com.example.artwokmabel.models.User;
import com.example.artwokmabel.profile.people.PeopleAdapterViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MessageFollowingAdapter extends RecyclerView.Adapter<MessageFollowingAdapter.CustomViewHolder> implements Filterable {

    private List<User> usersList;
    private List<User> usersListFiltered;

    private Context context;
    private PeopleAdapterViewModel viewModel;
    private FirebaseAuth mAuth;

    private static MessageFollowingAdapter instance;

    public static MessageFollowingAdapter getInstance(){
        return instance;
    }

    public MessageFollowingAdapter(Context context) {
        this.instance = this;
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
            this.usersListFiltered = users;
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
            //this.usersListFiltered = users;
            notifyDataSetChanged();
            result.dispatchUpdatesTo(this);
        }
    }


    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        User user = usersListFiltered.get(position);
        
        holder.binding.setUser(user);
        holder.binding.setFollowbutton(holder.binding.followingButton);
        holder.binding.setOnfollowingclicked(new OnFollowingClicked());
        holder.binding.setOnchatclicked(new OnChatClicked());
        holder.binding.setOnprofileclicked(new OnProfileClicked());

        Picasso.get().load(user.getProfile_url()).into(holder.binding.contactPicture);
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
        return usersListFiltered == null ? 0 : usersListFiltered.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        ItemMessageFollowingBinding binding;

        public CustomViewHolder(ItemMessageFollowingBinding binding) {
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
                    usersListFiltered = usersList;
                } else {
                    List<User> filteredList = new ArrayList<>();
                    for (User user : usersList) {
                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match

                        //|| user.getPhone().contains(charSequence
                        //Might need to add an or operator below to search for other things like intro
                        if (user.getUsername().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(user);
                        }
                    }

                    usersListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = usersListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                usersListFiltered = (ArrayList<User>) filterResults.values;

                notifyDataSetChanged();
            }
        };
    }
}
