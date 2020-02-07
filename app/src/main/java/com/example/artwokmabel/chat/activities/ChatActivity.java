package com.example.artwokmabel.chat.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.artwokmabel.R;
import com.example.artwokmabel.chat.adapters.ChatsViewAdapter;
import com.example.artwokmabel.chat.models.Comment;
import com.example.artwokmabel.databinding.ActivityChatBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    final String TAG = "ChatActivity";

    private String destinatonUid;
    private String uid;
    public String chatRoomUid;

    private List<Comment> commentsList;
    private ChatsViewAdapter adapter;


    private ActivityChatBinding binding;
    private ChatActivityViewModel viewModel;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
    private static ChatActivity instance;

    public static ChatActivity getInstance(){
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat);

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();  //채팅을 요구 하는 아아디 즉 단말기에 로그인된 UID

        destinatonUid = getIntent().getStringExtra("destinationUid"); // 채팅을 당하는 아이디
        chatRoomUid = getIntent().getStringExtra("chatRoomId");

        Log.d("chat", "$$$$$$" + destinatonUid);
        Log.d("chat", "$$$$$$" + chatRoomUid);

        adapter = new ChatsViewAdapter();
        binding.chatRecyclerview.setAdapter(adapter);

        binding.btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Todo: is there a better way to implement the below?
                Comment newComment = new Comment(
                        uid,
                        binding.editComment.getText().toString(),
                        System.currentTimeMillis(),
                        getIntent().getStringExtra("https://www.google.com/url?sa=i&url=https%3A%2F%2Fstock.adobe.com%2Fphotos&psig=AOvVaw03ql978lJrqkjeM79twqL6&ust=1577424792592000&source=images&cd=vfe&ved=0CAIQjRxqFwoTCPCf1LXL0uYCFQAAAAAdAAAAABAD")
                        ///This is not MY pic - it's the other person's
                );

                viewModel.addNewComment(newComment);

                //Below is to speed up the message fetching process
                commentsList.add(newComment);
                adapter.setMessages(commentsList);
                adapter.notifyDataSetChanged();

                binding.editComment.setText("");
            }
        });
        retrieveRoomMessages();
    }

    void retrieveRoomMessages(){

        viewModel = ViewModelProviders.of(this).get(ChatActivityViewModel.class);
        viewModel.getRoomMessagesObservable().observe(this, new Observer<List<Comment>>() {
            @Override
            public void onChanged(@Nullable List<Comment> messages) {

                Log.d("WDA", "I CHANGED MTHFKERS");
                if (messages != null) {
                    commentsList = messages;
                    adapter.setMessages(messages);
                }
            }
        });
    }
    // sliding animation effect when back key pressed
//    @Override
//    public void onBackPressed() {
//        finish();
//        overridePendingTransition(R.anim.fromleft, R.anim.toright);
//    }
}

//// -----------------------------------------------------------------------------------------------

//public class ChatActivity extends AppCompatActivity {
//    private String destinationUid;
//    private ImageButton button;
//    private EditText editText;
//
//    private String uid;
//    private String chatRoomUid;
//
//    private RecyclerView recyclerView;
//
//    private UserUserModel destinationUserModel;
//
//    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_chat);
//
//        uid = LoginActivity.uid;  //채팅을 요구 하는 아아디 즉 단말기에 로그인된 UID
//        destinationUid = getIntent().getStringExtra("destinationUid"); // 채팅을 당하는 아이디
//        button = findViewById(R.id.btn_chat);
//        editText = findViewById(R.id.et_chat);
//        recyclerView = findViewById(R.id.chat_recyclerview);
//
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ChatChatModel chatchatModel = new ChatChatModel();
//                chatchatModel.users.put(uid, true);
//                chatchatModel.users.put(destinationUid, true);
//                if (chatRoomUid == null) {
//                    button.setEnabled(false);
//                    FirebaseDatabase.getInstance().getReference().child("chatrooms").push().setValue(chatchatModel)
//                            .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                @Override
//                                public void onSuccess(Void aVoid) {
//                                    checkChatRoom();
//                                }
//                            });
//                } else {
//                    ChatChatModel.Comment comment = new ChatChatModel.Comment();
//                    comment.uid = uid;
//                    comment.message = editText.getText().toString();
//                    comment.timestamp = ServerValue.TIMESTAMP;
//                     FirebaseDatabase.getInstance().getReference().child("chatrooms").child(chatRoomUid).child("comments").push().setValue(comment)
//                             .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                 @Override
//                                 public void onComplete(@NonNull Task<Void> task) {
//                                     // PUSH NOTIFICATIONS
////                                     sendGcm();
//                                     // reset editText after message has been sent
//                                     editText.setText("");
//                                 }
//                             });
//                }
//            }
//        });
//        checkChatRoom();
//    }
//// PUSH NOTIFICATIONS PUSH NOTIFICATIONS PUSH NOTIFICATIONS PUSH NOTIFICATIONS PUSH NOTIFICATIONS PUSH NOTIFICATIONS PUSH NOTIFICATIONS
//
////    void sendGcm() {
////        Gson gson = new Gson();
////
////        String userName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
////
////        NotificationModel notificationModel = new NotificationModel();
////        notificationModel.to = destinationUserModel.pushToken;
////        notificationModel.notification.title = userName;
////        notificationModel.notification.text = editText.getText().toString();
////        notificationModel.data.title = userName;
////        notificationModel.data.text = editText.getText().toString();
////
////        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf8"),gson.toJson(notificationModel));
////
////        Request request = new Request.Builder()
////                .header("Content-Type","application/json")
////                .addHeader("authorization","key=AIzaSyAsRMxpi2xU5pmyPAkYWNuN0FMuzwFiKrE")
////                .url("https://fcm.googleapis.com/fcm/send")
////                .post(requestBody)
////                .build();
////        OkHttpClient okHttpClient = new OkHttpClient();
////        okHttpClient.newCall(request).enqueue(new Callback() {
////            @Override
////            public void onFailure(@NotNull Call call, @NotNull IOException e) {
////
////            }
////
////            @Override
////            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
////
////            }
////        });
////
////    }
//
//    void checkChatRoom() {
//
//        FirebaseDatabase.getInstance().getReference().child("chatrooms").orderByChild("users/"+uid).equalTo(true).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for(DataSnapshot item : dataSnapshot.getChildren()){
//                    ChatChatModel chatchatModel = item.getValue(ChatChatModel.class);
//                    if(chatchatModel.users.containsKey(destinationUid)){
//                        chatRoomUid = item.getKey();
//                        button.setEnabled(true);
//                        recyclerView.setLayoutManager(new LinearLayoutManager(ChatActivity.this));
//                        recyclerView.setAdapter(new RecyclerViewAdapter());
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//    }
//    class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
//
//        List<ChatChatModel.Comment> comments;
//        UserUserModel useruserModel;
//        public RecyclerViewAdapter() {
//            comments = new ArrayList<>();
//
//            FirebaseDatabase.getInstance().getReference().child("users").child(destinationUid)
//                    .addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(DataSnapshot dataSnapshot) {
//                            useruserModel = dataSnapshot.getValue(UserUserModel.class);
//                            getMessageList();
//                        }
//
//                        @Override
//                        public void onCancelled(DatabaseError databaseError) {
//
//                        }
//                    });
//        }
//
//        void getMessageList(){
//
//            FirebaseDatabase.getInstance().getReference().child("chatrooms").child(chatRoomUid).child("comments")
//                    .addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(DataSnapshot dataSnapshot) {
//                            comments.clear();
//
//                            for (DataSnapshot item : dataSnapshot.getChildren()) {
//                                comments.add(item.getValue(ChatChatModel.Comment.class));
//                            }
//                            // refresh message list
//                            notifyDataSetChanged();
//                            // move to the last comment position
//                            recyclerView.scrollToPosition(comments.size() - 1);
//
//                        }
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                        }
//                    });
//        }
//
//        @NonNull
//        @Override
//        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat,parent,false);
//
//            return new MessageViewHolder(view);
//        }
//
//        @Override
//        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position){
//                MessageViewHolder messageViewHolder = ((MessageViewHolder) holder);
//
//                if (comments.get(position).uid.equals(uid)) {
//                    //for my messages (sending)
//                    messageViewHolder.textView_message.setText(comments.get(position).message);
//                    messageViewHolder.textView_message.setBackgroundResource(R.drawable.my_message);
//                    messageViewHolder.linearLayout_destination.setVisibility(View.INVISIBLE);
//                    messageViewHolder.textView_name.setVisibility(View.INVISIBLE);
//                    messageViewHolder.textView_message.setTextSize(15);
//                    messageViewHolder.textView_name.setTextSize(5);
//                    messageViewHolder.linearLayout_main.setGravity(Gravity.RIGHT);
//                } else {
//                    // for their messages (receiving)
//                    Glide.with(holder.itemView.getContext())
//                            .load(destinationUserModel.profileImageUrl)
//                            .apply(new RequestOptions().circleCrop())
//                            .into(messageViewHolder.imageView_profile);
//                    messageViewHolder.textView_name.setText(destinationUserModel.userName);
//                    messageViewHolder.linearLayout_destination.setVisibility(View.VISIBLE);
//                    messageViewHolder.textView_message.setBackgroundResource(R.drawable.their_message);
//                    messageViewHolder.textView_message.setText(comments.get(position).message);
//                    messageViewHolder.textView_message.setTextSize(15);
//                    messageViewHolder.textView_name.setTextSize(12);
//                    messageViewHolder.linearLayout_main.setGravity(Gravity.LEFT);
//                }
//                long unixTime = (long) comments.get(position).timestamp;
//                Date date = new Date(unixTime);
//                simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Singapore"));
//                String time = simpleDateFormat.format(date);
//                messageViewHolder.textView_timestamp.setText(time);
//        }
//
//        @Override
//
//        public int getItemCount() {
//            return comments.size();
//        }
//    }
//
//    private class MessageViewHolder extends RecyclerView.ViewHolder {
//        public TextView textView_message;
//        public TextView textView_name;
//        public ImageView imageView_profile;
//        public LinearLayout linearLayout_destination;
//        public LinearLayout linearLayout_main;
//        public TextView textView_timestamp;
//
//        public MessageViewHolder(View view) {
//            super(view);
//            textView_message = view.findViewById(R.id.messageItem_textView_message);
//            textView_name = (TextView)view.findViewById(R.id.messageItem_textView_name);
//            imageView_profile = (ImageView)view.findViewById(R.id.messageItem_imageview_profile);
//            linearLayout_destination = (LinearLayout)view.findViewById(R.id.messageItem_linearlayout_destination);
//            linearLayout_main = (LinearLayout)view.findViewById(R.id.messageItem_linearlayout_main);
//            textView_timestamp = (TextView)view.findViewById(R.id.messageItem_textView_timestamp);
//
//        }
//    }
//    // sliding animation effect when back key pressed
//    @Override
//    public void onBackPressed() {
//        finish();
//        overridePendingTransition(R.anim.fromleft, R.anim.toright);
//    }
//}