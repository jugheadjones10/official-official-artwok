package com.example.artwokmabel.chat.adapters;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.artwokmabel.chat.activities.ChatActivity;
import com.example.artwokmabel.chat.models.ChatChatModel;
import com.example.artwokmabel.R;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

public class MessageChatsFragmentRecyclerViewAdapter extends RecyclerView.Adapter<MessageChatsFragmentRecyclerViewAdapter.CustomViewHolder> {

    List<ChatChatModel> chatchatModels;
    private Context context;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd. hh:mm");

//        private ArrayList<String> destinationUsers = new ArrayList<>();

    public MessageChatsFragmentRecyclerViewAdapter(Context context, List<ChatChatModel> chatChatModels) {
        this.chatchatModels = chatChatModels;
        this.context = context;
//            FirebaseDatabase.getInstance().getReference().child("chatrooms").orderByChild("users/"+uid).equalTo(true).addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    chatchatModels.clear();
//                    for (DataSnapshot item : dataSnapshot.getChildren()) {
//                        chatchatModels.add(item.getValue(ChatChatModel.class));
//                    }
//                    notifyDataSetChanged();
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                }
//            });
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_chats,parent,false);

        return new CustomViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
          ChatChatModel chatRoom = chatchatModels.get(position);

//        ArrayList<String> destinationUsers = new ArrayList<>();

//        mAuth = FirebaseAuth.getInstance();
//        FirebaseUser currentUser = mAuth.getCurrentUser();

//        String destinationUid = null;
//        for(String user: chatchatModels.get(position).getUsers().keySet()){
//            // check for all users from every chat rooms except for myself
//            if(!user.equals(currentUser.getUid())){
//                destinationUid = user;
//                destinationUsers.add(destinationUid);
//            }
//        }

        //Set the circle view in "chats" to be the face of destinationUid + username, which is the person I'm conversing with
//        FirebaseDatabase.getInstance().getReference().child("users").child(destinationUid).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                UserUserModel useruserModel =  dataSnapshot.getValue(UserUserModel.class);
//
//                if(useruserModel != null){
//                    Glide.with(holder.itemView.getContext())
//                            .load(useruserModel.getProfileImageUrl())
//                            .apply(new RequestOptions().circleCrop())
//                            .into(holder.imageView);
//                    holder.textView_title.setText(useruserModel     .getUserName());
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
        Glide.with(holder.itemView.getContext())
                .load(chatRoom.getOtherUserPic())
                .apply(new RequestOptions().circleCrop())
                .into(holder.imageView);
        holder.textView_title.setText(chatRoom.getOtherUser());


        //메시지를 내림 차순으로 정렬 후 마지막 메세지의 키값을 가져옴
//        Map<String,ChatChatModel.Comment> commentMap = new TreeMap<>(Collections.reverseOrder());
//        commentMap.putAll(chatchatModels.get(position).comments);
//        String lastMessageKey = (String) commentMap.keySet().toArray()[0];
         holder.textView_last_message.setText(chatRoom.getLastMsg());

        // when the user presses on the chatroom item, enters the chatroom
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ChatActivity.class);
                intent.putExtra("destinationUid", chatRoom.getOtherUser());
                intent.putExtra("otherUserPic", chatRoom.getOtherUserPic());
                intent.putExtra("chatRoomId", chatRoom.getChatRoomid());

                ActivityOptions activityOptions = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    activityOptions = ActivityOptions.makeCustomAnimation(view.getContext(), R.anim.fromright, R.anim.toleft);
                    context.startActivity(intent, activityOptions.toBundle());
                }

            }
        });

        // setting up timestamp for chatrooms
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Singapore"));

//        long unixTime = (long) chatchatModels.get(position).comments.get(lastMessageKey).timestamp;
//        Date date = new Date(unixTime);
//        holder.textView_timestamp.setText(simpleDateFormat.format(date));
    }

    @Override
    public int getItemCount() {
        return chatchatModels.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;
        public TextView textView_title;
        public TextView textView_last_message;
        public TextView textView_timestamp;

        private CustomViewHolder(View view) {
            super(view);

            imageView = (ImageView) view.findViewById(R.id.message_chats_imageview);
            textView_title = (TextView)view.findViewById(R.id.message_chats_textview_title);
            textView_last_message = (TextView)view.findViewById(R.id.message_chats_textview_lastmessage);
            textView_timestamp = view.findViewById(R.id.message_chats_textview_timestamp);
        }
    }
}
