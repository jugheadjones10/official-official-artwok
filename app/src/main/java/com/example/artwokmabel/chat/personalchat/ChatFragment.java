package com.example.artwokmabel.chat.personalchat;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.artwokmabel.R;
import com.example.artwokmabel.databinding.ActivityChatBinding;
import com.example.artwokmabel.databinding.CustomChatBarBinding;
import com.example.artwokmabel.databinding.FragmentChatBinding;
import com.example.artwokmabel.homepage.callbacks.ImagePickerCallback;
import com.example.artwokmabel.models.ImageMessage;
import com.example.artwokmabel.models.Message;
import com.example.artwokmabel.models.OrderChat;
import com.example.artwokmabel.profile.uploadlisting.UploadListingAcitvity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static android.app.Activity.RESULT_OK;

public class ChatFragment extends Fragment {

    private CustomChatBarBinding chatBarBinding;
    private FragmentChatBinding binding;

    private String messageFollowingId, messageFollowingUsername, messageFollowingProfileImg, messageMeId;

    private FirebaseAuth mAuth;
    private DatabaseReference RootRef;
    private StorageReference storageReference;

    private LinearLayoutManager linearLayoutManager;
    private MessageAdapter messageAdapter;
    private final List<Message> messagesList = new ArrayList<>();

    private String saveCurrentTime, saveCurrentDate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_chat, container, false);
        mAuth = FirebaseAuth.getInstance();
        messageMeId = mAuth.getCurrentUser().getUid();
        RootRef = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();

        messageFollowingId = ChatFragmentArgs.fromBundle(getArguments()).getFollowingId();
        messageFollowingUsername = ChatFragmentArgs.fromBundle(getArguments()).getFollowingUsername();
        messageFollowingProfileImg = ChatFragmentArgs.fromBundle(getArguments()).getFollowingProfileImg();

        return binding.getRoot();
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inflateChatBar();
        initializeControllers();
        chatBarBinding.customProfileName.setText(messageFollowingUsername);
        Picasso.get().load(messageFollowingProfileImg).placeholder(R.drawable.ic_user).into(chatBarBinding.customProfileImage);

        binding.sendMessageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                SendMessage();
            }
        });

        binding.sendFilesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ImagePickerCallback(requireActivity(), UploadListingAcitvity.REQUEST_IMAGE).onImagePickerClicked();
            }
        });

        //binding.inputMessage.requestFocus();
        //DisplayLastSeen();
    }

    private void initializeControllers() {
        messageAdapter = new MessageAdapter(messagesList, new OrderChat());
        linearLayoutManager = new LinearLayoutManager(requireContext());

        binding.privateMessagesListOfUsers.setLayoutManager(linearLayoutManager);
        binding.privateMessagesListOfUsers.setAdapter(messageAdapter);

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
        saveCurrentTime = currentTime.format(calendar.getTime());
    }

    private void inflateChatBar(){
        ((AppCompatActivity)requireActivity()).setSupportActionBar(binding.mainAppBar);
        ActionBar actionBar =   ((AppCompatActivity)requireActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);

        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        chatBarBinding = DataBindingUtil.inflate(layoutInflater, R.layout.custom_chat_bar, null, false);

        actionBar.setCustomView(chatBarBinding.getRoot());
    }


    @Override
    public void onStart() {
        super.onStart();

        RootRef.child("Messages").child(messageMeId).child(messageFollowingId)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s)
                    {

                        Message message;
                        if(dataSnapshot.child("type").getValue().equals("image")) {
                            message = dataSnapshot.getValue(ImageMessage.class);
                        }else{
                            message = dataSnapshot.getValue(Message.class);
                        }

                        //If not this listener(because it runs in the background even though this activity isn't in the foreground)
                        //will keep marking messages as read even though I'm outside the chat activity
                        if(getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED)){
                            if(message.getRead() != null && message.getRead().equals("false")){
                                Map<String, Object> childUpdates = new HashMap<>();
                                childUpdates.put("/Messages/" + messageMeId + "/" + messageFollowingId + "/" + message.getMessageID() + "/" + "read", "true");
                                RootRef.updateChildren(childUpdates);
                            }
                        }

                        messagesList.add(message);
                        messageAdapter.notifyDataSetChanged();
                        binding.privateMessagesListOfUsers.scrollToPosition(binding.privateMessagesListOfUsers.getAdapter().getItemCount() - 1);
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }



    private void SendMessage() {
        String messageText = binding.inputMessage.getText().toString();

        if (TextUtils.isEmpty(messageText))
        {
            Toast.makeText(requireContext(), "first write your message...", Toast.LENGTH_SHORT).show();
        }
        else
        {
            String messageSenderRef = "Messages/" + messageMeId + "/" + messageFollowingId;
            String messageReceiverRef = "Messages/" + messageFollowingId + "/" + messageMeId;

            DatabaseReference userMessageKeyRef = RootRef.child("Messages")
                    .child(messageMeId).child(messageFollowingId).push();

            String messagePushID = userMessageKeyRef.getKey();

            Map messageTextBody = new HashMap();
            messageTextBody.put("message", messageText);
            messageTextBody.put("type", "text");
            messageTextBody.put("from", messageMeId);
            messageTextBody.put("to", messageFollowingId);
            messageTextBody.put("messageID", messagePushID);
            messageTextBody.put("time", saveCurrentTime);
            messageTextBody.put("date", saveCurrentDate);
            messageTextBody.put("nanopast", System.currentTimeMillis());
            messageTextBody.put("read", "false");

            Map messageBodyDetails = new HashMap();
            messageBodyDetails.put(messageSenderRef + "/" + messagePushID, messageTextBody);
            messageBodyDetails.put( messageReceiverRef + "/" + messagePushID, messageTextBody);

            RootRef.updateChildren(messageBodyDetails).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task)
                {
                    if (task.isSuccessful())
                    {
                        //Toast.makeText(ChatActivity.this, "Message Sent Successfully...", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(requireActivity(), "Error", Toast.LENGTH_SHORT).show();
                    }
                    binding.inputMessage.setText("");
                }
            });
        }
    }


    @Override
    public void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UploadListingAcitvity.REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {

                if (data != null) {
                    Uri fileUri = data.getParcelableExtra("path");

                    Random random = new Random();
                    int randomNumber = random.nextInt();
                    String fileName = Integer.toString(randomNumber);

                    StorageReference fileToUpload = storageReference.child("Images").child(mAuth.getCurrentUser().getUid()).child(fileName); // randomize name

                    fileToUpload.putFile(fileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            storageReference.child("Images").child(mAuth.getCurrentUser().getUid()).child(fileName).getDownloadUrl().addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {

                                    Log.d("LOG: ", "postImageUris " + task.getResult().toString());
                                    sendImageMessage(task.getResult().toString());

                                }
                            });

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(requireActivity(), "Upload image failed", Toast.LENGTH_LONG).show();
                        }
                    });

                } else { // no pics selected

                }
            }
        }
    }

    //Combine this wiht sendMessage to reduce code length
    private void sendImageMessage(String imageUrl){

        String messageSenderRef = "Messages/" + messageMeId + "/" + messageFollowingId;
        String messageReceiverRef = "Messages/" + messageFollowingId + "/" + messageMeId;

        DatabaseReference userMessageKeyRef = RootRef.child("Messages")
                .child(messageMeId).child(messageFollowingId).push();

        String messagePushID = userMessageKeyRef.getKey();

        Map messageTextBody = new HashMap();
        messageTextBody.put("message", "image");
        messageTextBody.put("type", "image");
        messageTextBody.put("from", messageMeId);
        messageTextBody.put("to", messageFollowingId);
        messageTextBody.put("messageID", messagePushID);
        messageTextBody.put("time", saveCurrentTime);
        messageTextBody.put("date", saveCurrentDate);
        messageTextBody.put("imageUrl", imageUrl);
        messageTextBody.put("nanopast", System.currentTimeMillis());
        messageTextBody.put("read", "false");

        Map messageBodyDetails = new HashMap();
        messageBodyDetails.put(messageSenderRef + "/" + messagePushID, messageTextBody);
        messageBodyDetails.put( messageReceiverRef + "/" + messagePushID, messageTextBody);

        RootRef.updateChildren(messageBodyDetails).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task)
            {
                if (task.isSuccessful())
                {
                    //Toast.makeText(ChatActivity.this, "Message Sent Successfully...", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(requireActivity(), "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    //Further configure the below after completing the tutorial
    private void DisplayLastSeen()
    {
        RootRef.child("Users").child(messageFollowingId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        if (dataSnapshot.child("userState").hasChild("state")) {
                            String state = dataSnapshot.child("userState").child("state").getValue().toString();
                            String date = dataSnapshot.child("userState").child("date").getValue().toString();
                            String time = dataSnapshot.child("userState").child("time").getValue().toString();

                            if (state.equals("online"))
                            {
                                chatBarBinding.customUserLastSeen.setText("online");
                            }
                            else if (state.equals("offline"))
                            {
                                chatBarBinding.customUserLastSeen.setText("Last Seen: " + date + " " + time);
                            }
                        } else
                        {
                            chatBarBinding.customUserLastSeen.setText("offline");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

}
