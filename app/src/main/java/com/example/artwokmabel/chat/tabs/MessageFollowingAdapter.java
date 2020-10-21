package com.example.artwokmabel.chat.tabs;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.artwokmabel.ChatGraphDirections;
import com.example.artwokmabel.R;
import com.example.artwokmabel.chat.MessageFragmentDirections;
import com.example.artwokmabel.databinding.ItemMessageFollowingBinding;
import com.example.artwokmabel.models.User;
import com.example.artwokmabel.profile.people.PeopleAdapterViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
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
    private NavController navController;

    private static MessageFollowingAdapter instance;

    public static MessageFollowingAdapter getInstance(){
        return instance;
    }

    public MessageFollowingAdapter(Context context, NavController navController) {
        this.instance = this;
        this.context = context;
        this.mAuth = FirebaseAuth.getInstance();
        this.navController = navController;
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

//                new MaterialAlertDialogBuilder(get)
//                        .setTitle("Unfollow user?")
//                        .setMessage("")
//                        .setNeutralButton("Cancel", null)
//                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                button.setText("Follow");
//                                button.setBackgroundColor(mContext.getResources().getColor(R.color.artwok_background_blue));
//                                viewModel.removeUserFollowing(mAuth.getCurrentUser().getUid(), user.getUid());
//                            }
//                        })
//                        .show();

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
            ChatGraphDirections.ActionGlobalProfileFragment3 action =
                    ChatGraphDirections.actionGlobalProfileFragment3(user.getUid());
            navController.navigate(action);
        }
    }

    public class OnChatClicked{
        public void onChatClicked(User user){
            MessageFragmentDirections.ActionChatGraphToChatFragment action =
                    MessageFragmentDirections.actionChatGraphToChatFragment(user.getUid(), user.getUsername(), user.getProfile_url());
            navController.navigate(action);
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
