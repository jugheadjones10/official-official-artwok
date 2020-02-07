//package com.example.artwokmabel.Chat.Adapters;
//
//import android.app.ActivityOptions;
//import android.content.Intent;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.request.RequestOptions;
//import com.example.artwokmabel.Chat.Activites.ChatActivity;
//import com.example.artwokmabel.Chat.Activites.ChatProfileActivity;
//import com.example.artwokmabel.Chat.Fragments.MessageFriendsFragment;
//import com.example.artwokmabel.Chat.Models.UserUserModel;
//import com.example.artwokmabel.R;
//
//import java.util.List;
//
//public class FriendsListAdapter extends RecyclerView.Adapter <FriendsListAdapter.ViewHolder> {
//
//    public List<UserUserModel> usersList;
//
//    public FriendsListAdapter(List<UserUserModel> usersList) {
//        this.usersList = usersList;
//    }
//
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_friends, parent, false);
//        return new ViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//
//        Glide.with
//                    (holder.itemView.getContext())
//                    .load(usersList.get(position).profileImageUrl)
//                    .apply(new RequestOptions().circleCrop())
//                    .into(((ViewHolder) holder).mProfile);
//            ((ViewHolder) holder).mUsername.setText(usersList.get(position).userName);
//
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    // when the user presses on one of the items from friend list, a profile activity pops up
//                    Intent intent = new Intent(view.getContext(), ChatActivity.class);
//                    intent.putExtra("destinationUid", usersList.get(position).uid);
//                    // create an animation effect sliding from left to right
//                    ActivityOptions activityOptions = null;
//                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
//                        activityOptions = ActivityOptions.makeCustomAnimation(view.getContext(), R.anim.fromtop, R.anim.tobottom);
//                        view.getContext().startActivity(intent, activityOptions.toBundle());
//                    } else {
//                        view.getContext().startActivity(intent);
//                    }
//
//                }
//            });
//    }
//
//    @Override
//    public int getItemCount() {
//        return usersList.size();
//    }
//
//    public class ViewHolder extends RecyclerView.ViewHolder {
//
//        View mView;
//
//        public TextView mUsername;
//        public ImageView mProfile;
//
//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
//            mView = itemView;
//
//            mUsername = mView.findViewById(R.id.message_friends_textview);
//            mProfile = mView.findViewById(R.id.message_friends_imageview);
//        }
//    }
//}