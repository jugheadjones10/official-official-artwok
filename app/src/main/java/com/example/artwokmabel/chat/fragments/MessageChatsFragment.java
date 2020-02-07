package com.example.artwokmabel.chat.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.artwokmabel.chat.adapters.MessageChatsFragmentRecyclerViewAdapter;
import com.example.artwokmabel.chat.models.ChatChatModel;
import com.example.artwokmabel.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MessageChatsFragment extends Fragment {
    private final String TAG = "MessageChatsFragment";
    public ViewPager viewPager;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd. hh:mm");

    FirebaseAuth mAuth;
    FirebaseFirestore db;
    public MessageChatsFragmentRecyclerViewAdapter adapter;
    private List<ChatChatModel> chatchatModels = new ArrayList<>();

    private static MessageChatsFragment instance;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.message_chats_fragment, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.chats_fragment_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));

        adapter = new MessageChatsFragmentRecyclerViewAdapter(getActivity(),chatchatModels);
        recyclerView.setAdapter(adapter);
        instance = this;

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        db.collection("Users")
                .document(currentUser.getUid())
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                chatchatModels.clear();
                if (snapshot != null && snapshot.exists()) {
                    ArrayList<String> chatRooms = (ArrayList<String>) snapshot.get("chatrooms");

                 try {
                    for (String chatRoom : chatRooms) {
                        db.collection("Chatrooms")
                                .document(chatRoom)
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot document = task.getResult();

                                            if (document.exists()) {
                                                ArrayList<String> party = (ArrayList<String>) document.get("participants");

                                                String otherUser[] = {null};
                                                for(String user: party){
                                                    if(!currentUser.getUid().equals(user)){
                                                        otherUser[0] = user;
                                                    }else{
                                                        otherUser[0] = currentUser.getUid();
                                                    }
                                                }

                                                db.collection("Users")
                                                        .document(otherUser[0])
                                                        .get()
                                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                if (task.isSuccessful()) {
                                                                    DocumentSnapshot document = task.getResult();
                                                                    if (document.exists()) {
                                                                        String otherUserPic = (String)document.get("profile_url");

                                                                        ChatChatModel chatItem = new ChatChatModel(
                                                                            otherUser[0],
                                                                            otherUserPic,
                                                                            chatRoom,
                                                                                "Fk"
                                                                        );
                                                                        chatchatModels.add(chatItem);
                                                                        adapter.notifyDataSetChanged();
                                                                    } else {
                                                                        Log.d(TAG, "No such document");
                                                                    }
                                                                } else {
                                                                    Log.d(TAG, "get failed with ", task.getException());
                                                                }
                                                            }
                                                        });

                                            } else {
                                                Log.d(TAG, "No such document");
                                            }
                                        } else {
                                            Log.d(TAG, "get failed with ", task.getException());
                                        }
                                    }
                                });
                    }

                } catch (NullPointerException nul){


                 }
                }else {
                    Log.d(TAG," data: null");
                }
            }
        });

//        FirebaseDatabase.getInstance().getReference().child("chatrooms").orderByChild("users/"+currentUser.getUid()).equalTo(true).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                chatchatModels.clear();
//                for (DataSnapshot item : dataSnapshot.getChildren()) {
//                    chatchatModels.add(item.getValue(ChatChatModel.class));
//                    //MAY BECOME PROBLEM POINT
//                }
//                adapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

        return view;
     }

    public static MessageChatsFragment getInstance(){
        return instance;
    }

//    public class MessageChatsFragmentRecyclerViewAdapter extends RecyclerView.Adapter<MessageChatsFragmentRecyclerViewAdapter.CustomViewHolder> {
//
//        List<ChatChatModel> chatchatModels;
////        private ArrayList<String> destinationUsers = new ArrayList<>();
//
//        public MessageChatsFragmentRecyclerViewAdapter(List<ChatChatModel> chatChatModels) {
//            this.chatchatModels = chatChatModels;
////            FirebaseDatabase.getInstance().getReference().child("chatrooms").orderByChild("users/"+uid).equalTo(true).addListenerForSingleValueEvent(new ValueEventListener() {
////                @Override
////                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
////                    chatchatModels.clear();
////                    for (DataSnapshot item : dataSnapshot.getChildren()) {
////                        chatchatModels.add(item.getValue(ChatChatModel.class));
////                    }
////                    notifyDataSetChanged();
////                }
////
////                @Override
////                public void onCancelled(@NonNull DatabaseError databaseError) {
////
////                }
////            });
//        }
//
//        @NonNull
//        @Override
//        public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_chats,parent,false);
//
//            return new CustomViewHolder(view);
//        }
//
//
//        @Override
//        public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
//            ArrayList<String> destinationUsers = new ArrayList<>();
//
//            String destinationUid = null;
//
//            // 일일 챗방에 있는 유저를 체크
//            for(String user: chatchatModels.get(position).users.keySet()){
//                // check for all users from every chat rooms except for myself
//                if(!user.equals(uid)){
//                    destinationUid = user;
//                    destinationUsers.add(destinationUid);
//                }
//            }
//
//            //Set the circle view in "chats" to be the face of destinationUid + username, which is the person I'm conversing with
//            FirebaseDatabase.getInstance().getReference().child("users").child(destinationUid).addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    UserUserModel useruserModel =  dataSnapshot.getValue(UserUserModel.class);
//
//                    if(useruserModel != null){
//                        Glide.with(holder.itemView.getContext())
//                                .load(useruserModel.getProfileImageUrl())
//                                .apply(new RequestOptions().circleCrop())
//                                .into(holder.imageView);
//                        holder.textView_title.setText(useruserModel     .getUserName());
//                    }
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//
//                }
//            });
//
//
//            //메시지를 내림 차순으로 정렬 후 마지막 메세지의 키값을 가져옴
//            Map<String,ChatChatModel.Comment> commentMap = new TreeMap<>(Collections.reverseOrder());
//            commentMap.putAll(chatchatModels.get(position).comments);
//            String lastMessageKey = (String) commentMap.keySet().toArray()[0];
//            holder.textView_last_message.setText(chatchatModels.get(position).comments.get(lastMessageKey).message);
//
//            // when the user presses on the chatroom item, enters the chatroom
//            holder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent intent = new Intent(view.getContext(), ChatActivity.class);
//                    intent.putExtra("destinationUid", destinationUsers.get(position));
//
//                    ActivityOptions activityOptions = null;
//                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
//                        activityOptions = ActivityOptions.makeCustomAnimation(view.getContext(), R.anim.fromright, R.anim.toleft);
//                        startActivity(intent, activityOptions.toBundle());
//                    }
//
//                }
//            });
//
//            // setting up timestamp for chatrooms
//            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Singapore"));
//
//            long unixTime = (long) chatchatModels.get(position).comments.get(lastMessageKey).timestamp;
//            Date date = new Date(unixTime);
//            holder.textView_timestamp.setText(simpleDateFormat.format(date));
//        }
//
//        @Override
//        public int getItemCount() {
//            return chatchatModels.size();
//        }
//
//        public class CustomViewHolder extends RecyclerView.ViewHolder {
//
//            public ImageView imageView;
//            public TextView textView_title;
//            public TextView textView_last_message;
//            public TextView textView_timestamp;
//
//            private CustomViewHolder(View view) {
//                super(view);
//
//                imageView = (ImageView) view.findViewById(R.id.message_chats_imageview);
//                textView_title = (TextView)view.findViewById(R.id.message_chats_textview_title);
//                textView_last_message = (TextView)view.findViewById(R.id.message_chats_textview_lastmessage);
//                textView_timestamp = view.findViewById(R.id.message_chats_textview_timestamp);
//
//            }
//        }
//    }
}

// -------------------------------------------------------------------------------------------------

//public class MessageChatsFragment extends Fragment {
//
//    public ViewPager viewPager;
//    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd. hh:mm");
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.message_chats_fragment, container, false);
//
//        RecyclerView recyclerView = view.findViewById(R.id.chats_fragment_recyclerview);
//        recyclerView.setAdapter(new MessageChatsFragmentRecyclerViewAdapter());
//        recyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));
//
//        return view;
//    }
//
//    class MessageChatsFragmentRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
//
//        private List<ChatChatModel> chatchatModels = new ArrayList<>();
//        private String uid;
//        private ArrayList<String> destinationUsers = new ArrayList<>();
//
//        public MessageChatsFragmentRecyclerViewAdapter() {
//            uid = LoginActivity.uid;
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
//        }
//
//        @NonNull
//        @Override
//        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_chats,parent,false);
//            return new CustomViewHolder(view);
//        }
//
//        @Override
//        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
//
//            final CustomViewHolder customViewHolder = (CustomViewHolder)holder;
//            String destinationUid = null;
//
//            // 일일 챗방에 있는 유저를 체크
//            for(String user: chatchatModels.get(position).users.keySet()){
//                // check for all users from every chat rooms except for myself
//                if(!user.equals(uid)){
//                    destinationUid = user;
//                    destinationUsers.add(destinationUid);
//                }
//            }
//            FirebaseDatabase.getInstance().getReference().child("users").child(destinationUid).addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    UserUserModel useruserModel =  dataSnapshot.getValue(UserUserModel.class);
//                    Glide.with(customViewHolder.itemView.getContext())
//                            .load(useruserModel.profileImageUrl)
//                            .apply(new RequestOptions().circleCrop())
//                            .into(customViewHolder.imageView);
//
//                    customViewHolder.textView_title.setText(useruserModel.userName);
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//
//                }
//            });
//
//            //메시지를 내림 차순으로 정렬 후 마지막 메세지의 키값을 가져옴
//            Map<String,ChatChatModel.Comment> commentMap = new TreeMap<>(Collections.reverseOrder());
//            commentMap.putAll(chatchatModels.get(position).comments);
//            String lastMessageKey = (String) commentMap.keySet().toArray()[0];
//            customViewHolder.textView_last_message.setText(chatchatModels.get(position).comments.get(lastMessageKey).message);
//
//            // when the user presses on the chatroom item, enters the chatroom
//            customViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent intent = new Intent(view.getContext(), ChatActivity.class);
//                    intent.putExtra("destinationUid", destinationUsers.get(position));
//
//                    ActivityOptions activityOptions = null;
//                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
//                        activityOptions = ActivityOptions.makeCustomAnimation(view.getContext(), R.anim.fromright, R.anim.toleft);
//                        startActivity(intent, activityOptions.toBundle());
//                    }
//
//                }
//            });
//
//            // setting up timestamp for chatrooms
//            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Singapore"));
//
//            long unixTime = (long) chatchatModels.get(position).comments.get(lastMessageKey).timestamp;
//            Date date = new Date(unixTime);
//            customViewHolder.textView_timestamp.setText(simpleDateFormat.format(date));
//        }
//
//        @Override
//        public int getItemCount() {
//            return chatchatModels.size();
//        }
//
//        private class CustomViewHolder extends RecyclerView.ViewHolder {
//
//            public ImageView imageView;
//            public TextView textView_title;
//            public TextView textView_last_message;
//            public TextView textView_timestamp;
//
//            public CustomViewHolder(View view) {
//                super(view);
//
//                imageView = (ImageView) view.findViewById(R.id.message_chats_imageview);
//                textView_title = (TextView)view.findViewById(R.id.message_chats_textview_title);
//                textView_last_message = (TextView)view.findViewById(R.id.message_chats_textview_lastmessage);
//                textView_timestamp = view.findViewById(R.id.message_chats_textview_timestamp);
//
//            }
//        }
//    }
//}