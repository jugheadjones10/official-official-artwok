package com.example.artwokmabel.chat.offerchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.artwokmabel.R;
import com.example.artwokmabel.chat.personalchat.ChatActivity;
import com.example.artwokmabel.chat.personalchat.MessageAdapter;
import com.example.artwokmabel.databinding.ActivityChatBinding;
import com.example.artwokmabel.databinding.ActivityOfferBinding;
import com.example.artwokmabel.databinding.CustomChatBarBinding;
import com.example.artwokmabel.databinding.CustomOfferBarBinding;
import com.example.artwokmabel.models.Listing;
import com.example.artwokmabel.models.Message;
import com.example.artwokmabel.models.OfferMessage;
import com.example.artwokmabel.models.OrderChat;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OfferActivity extends AppCompatActivity {

    private ActivityOfferBinding binding;
    private CustomOfferBarBinding offerBarBinding;
    private String messageFollowingId, messageFollowingUsername, messageFollowingProfileImg, messageMeId, buyerId;
    private String theOtherId;
    private OrderChat orderChat;

    private FirebaseAuth mAuth;
    private DatabaseReference RootRef;

    private LinearLayoutManager linearLayoutManager;
    private MessageAdapter messageAdapter;
    private final List<Message> messagesList = new ArrayList<>();


    private String saveCurrentTime, saveCurrentDate;

    public String getListingId(){
        return orderChat.getListing().getPostid();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_offer);
        binding.setOnofferclicked(new OnOfferClicked());
        binding.reviewButton.setVisibility(View.GONE);

        inflateChatBar();

        mAuth = FirebaseAuth.getInstance();
        messageMeId = mAuth.getCurrentUser().getUid();
        RootRef = FirebaseDatabase.getInstance().getReference();

        orderChat = (OrderChat) getIntent().getSerializableExtra("orderchat");
        if(messageMeId.equals(orderChat.getListing().getUserid())){
            buyerId = orderChat.getBuyerId();
            //Also might need to put in a check here to see if buyerId is null or not.
            theOtherId = buyerId;
        }else{
            theOtherId = orderChat.getListing().getUserid();
        }

        initializeControllers();

        offerBarBinding.setOrderchat(orderChat);
        Picasso.get().load(orderChat.getListing().getPhotos().get(0)).into(offerBarBinding.customProfileImage);

        binding.sendMessageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                SendMessage();
            }
        });

    }

    public class OnOfferClicked{
        public void onOfferClicked(){

            AlertDialog.Builder builder = new AlertDialog.Builder(OfferActivity.this);

            //builder.setTitle("Name");

            class DecimalDigitsInputFilter implements InputFilter {

                Pattern mPattern;

                public DecimalDigitsInputFilter() {
                    mPattern = Pattern.compile("[0-9]*+((\\.[0-9]?)?)||(\\.)?");
                }

                @Override
                public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                    Matcher matcher = mPattern.matcher(dest);
                    if (!matcher.matches())
                        return "";
                    return null;
                }
            }

            final View customLayout = getLayoutInflater().inflate(R.layout.layout_offer_price, null);

            EditText priceEdit = customLayout.findViewById(R.id.price_edit);
            Button offerButton = customLayout.findViewById(R.id.offer_button);

            priceEdit.setFilters(new InputFilter[]{new DecimalDigitsInputFilter()});

            builder.setView(customLayout);

            AlertDialog dialog = builder.create();
            dialog.show();

            offerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendOfferMessage(priceEdit.getText().toString());
                    dialog.dismiss();
                }
            });
        }
    }

    private void initializeControllers() {
        messageAdapter = new MessageAdapter(messagesList, orderChat);
        linearLayoutManager = new LinearLayoutManager(this);
        binding.privateMessagesListOfUsers.setLayoutManager(linearLayoutManager);
        binding.privateMessagesListOfUsers.setAdapter(messageAdapter);

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
        saveCurrentTime = currentTime.format(calendar.getTime());
    }

    private void inflateChatBar(){

        setSupportActionBar(binding.mainAppBar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);

        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        offerBarBinding = DataBindingUtil.inflate(layoutInflater, R.layout.custom_offer_bar, null, false);
        //binding.mainAppBar.inflateMenu(R.menu.offer_menu);

        actionBar.setCustomView(offerBarBinding.getRoot());
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        RootRef.child("Offers").child(messageMeId).child(theOtherId).child(orderChat.getListing().getPostid())
            .addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    ArrayList<OfferMessage> messages = new ArrayList<>();

                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        if(snapshot.child("type").getValue().equals("null")){
                            messages.add(snapshot.getValue(OfferMessage.class));
                        }
                    }

                    if(messages.size() != 0){
                        OfferMessage lastMessage = messages.get(messages.size() - 1);

                        if(lastMessage.getAcceptStatus().equals("accepted")){
                            if(messageMeId.equals(orderChat.getListing().getUserid())) {
                                binding.offerButton.setText("Delivered");

                                binding.offerButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        RootRef.child("Offers")
                                                .child(messageMeId)
                                                .child(theOtherId)
                                                .child(orderChat.getListing().getPostid())
                                                .child(lastMessage.getMessageID())
                                                .child("acceptStatus")
                                                .setValue("delivered");

                                        RootRef.child("Offers")
                                                .child(theOtherId)
                                                .child(messageMeId)
                                                .child(orderChat.getListing().getPostid())
                                                .child(lastMessage.getMessageID())
                                                .child("acceptStatus")
                                                .setValue("delivered");

                                    }
                                });

                            } else{
                                binding.offerButton.setText("Offered");
                                binding.offerButton.setEnabled(false);
                            }

                        }else if(lastMessage.getAcceptStatus().equals("delivered")){

                            if(messageMeId.equals(orderChat.getListing().getUserid())) {
                                binding.offerButton.setText("Delivered 😜");
                                binding.offerButton.setEnabled(false);

                            } else{
                                binding.offerButton.setText("Received");
                                binding.offerButton.setEnabled(true);

                                binding.offerButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        RootRef.child("Offers")
                                                .child(messageMeId)
                                                .child(theOtherId)
                                                .child(orderChat.getListing().getPostid())
                                                .child(lastMessage.getMessageID())
                                                .child("acceptStatus")
                                                .setValue("received");

                                        RootRef.child("Offers")
                                                .child(theOtherId)
                                                .child(messageMeId)
                                                .child(orderChat.getListing().getPostid())
                                                .child(lastMessage.getMessageID())
                                                .child("acceptStatus")
                                                .setValue("received");
                                    }
                                });
                            }
                        }else if(lastMessage.getAcceptStatus().equals("received")){
                            binding.offerButton.setText("Received 🤩");
                            binding.offerButton.setEnabled(false);

                            //Add small review button to offer button here
                            if(!messageMeId.equals(orderChat.getListing().getUserid())) {
                                binding.reviewButton.setVisibility(View.VISIBLE);
                                binding.reviewButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        binding.reviewButton.setVisibility(View.GONE);
                                        ReviewFragment reviewFragment = new ReviewFragment();
                                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                                        fragmentTransaction.add(R.id.inner_constraint, reviewFragment);
                                        fragmentTransaction.addToBackStack(null);
                                        fragmentTransaction.commit();
                                    }
                                });
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        RootRef.child("Offers").child(messageMeId).child(theOtherId).child(orderChat.getListing().getPostid())
            .addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s)
                {
                    Message message;
                    if(dataSnapshot.child("type").getValue().equals("null")){
                        message = dataSnapshot.getValue(OfferMessage.class);
                    }else{
                        message = dataSnapshot.getValue(Message.class);
                    }

                    if(getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED)){
                        if(message.getRead() != null){
                            if(message.getRead().equals("false")){
                                Map<String, Object> childUpdates = new HashMap<>();
                                childUpdates.put("/Offers/" + messageMeId + "/" + theOtherId + "/" + orderChat.getListing().getPostid() + "/" + message.getMessageID() + "/" + "read", "true");
                                RootRef.updateChildren(childUpdates);
                            }
                        }else{
                            Map<String, Object> childUpdates = new HashMap<>();
                            childUpdates.put("/Offers/" + messageMeId + "/" + theOtherId + "/" + orderChat.getListing().getPostid() + "/" + message.getMessageID() + "/" + "read", "true");
                            RootRef.updateChildren(childUpdates);
                        }
                    }

                    messagesList.add(message);
                    messageAdapter.notifyDataSetChanged();
                    binding.privateMessagesListOfUsers.smoothScrollToPosition(binding.privateMessagesListOfUsers.getAdapter().getItemCount());
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    for(int i = 0; i < messagesList.size(); i++){
                        if(messagesList.get(i).getMessageID().equals(dataSnapshot.getKey())){

                            Message message;
                            if(dataSnapshot.child("type").getValue().equals("null")){
                                message = dataSnapshot.getValue(OfferMessage.class);
                            }else{
                                message = dataSnapshot.getValue(Message.class);
                            }
                            //Actually the above check is redundant

                            messagesList.set(i, message);
                        }
                    }

                    messageAdapter.notifyDataSetChanged();
                    binding.privateMessagesListOfUsers.smoothScrollToPosition(binding.privateMessagesListOfUsers.getAdapter().getItemCount());
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

    private void sendOfferMessage(String price){

        Map messageBodyDetails = new HashMap();

        String messageSenderRef = "Offers/" + messageMeId + "/" + theOtherId + "/" + orderChat.getListing().getPostid();
        String messageReceiverRef = "Offers/" + theOtherId + "/" + messageMeId + "/" + orderChat.getListing().getPostid();

        DatabaseReference userMessageKeyRef = RootRef.child("Offers")
                .child(messageMeId).child(theOtherId).child(orderChat.getListing().getPostid()).push();

        String messagePushID = userMessageKeyRef.getKey();

        Map messageTextBody = new HashMap();
        messageTextBody.put("price", price);
        messageTextBody.put("acceptStatus", "pending");

        messageTextBody.put("message", "null");
        messageTextBody.put("type", "null");
        messageTextBody.put("from", messageMeId);
        messageTextBody.put("to", theOtherId);
        messageTextBody.put("messageID", messagePushID);
        messageTextBody.put("time", saveCurrentTime);
        messageTextBody.put("date", saveCurrentDate);
        messageTextBody.put("nanopast", System.currentTimeMillis());
        messageTextBody.put("read", "false");


        messageBodyDetails = new HashMap();
        messageBodyDetails.put(messageSenderRef + "/" + messagePushID, messageTextBody);
        messageBodyDetails.put( messageReceiverRef + "/" + messagePushID, messageTextBody);

        RootRef.updateChildren(messageBodyDetails).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task)
            {
                if (task.isSuccessful())
                {
                    //Toast.makeText(OfferActivity.this, "Message Sent Successfully...", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(OfferActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
                binding.inputMessage.setText("");
            }
        });

    }



    private void SendMessage()
    {
        String messageText = binding.inputMessage.getText().toString();

        if (TextUtils.isEmpty(messageText))
        {
            Toast.makeText(this, "first write your message...", Toast.LENGTH_SHORT).show();
        }else{

            Map messageBodyDetails = new HashMap();

            String messageSenderRef = "Offers/" + messageMeId + "/" + theOtherId + "/" + orderChat.getListing().getPostid();
            String messageReceiverRef = "Offers/" + theOtherId + "/" + messageMeId + "/" + orderChat.getListing().getPostid();

            DatabaseReference userMessageKeyRef = RootRef.child("Offers")
                    .child(messageMeId).child(theOtherId).child(orderChat.getListing().getPostid()).push();

            String messagePushID = userMessageKeyRef.getKey();

            Map messageTextBody = new HashMap();

            messageTextBody.put("message", messageText);
            messageTextBody.put("type", "text");
            messageTextBody.put("from", messageMeId);
            messageTextBody.put("to", theOtherId);
            messageTextBody.put("messageID", messagePushID);
            messageTextBody.put("time", saveCurrentTime);
            messageTextBody.put("date", saveCurrentDate);
            messageTextBody.put("nanopast", System.currentTimeMillis());
            messageTextBody.put("read", "false");

            messageBodyDetails = new HashMap();
            messageBodyDetails.put(messageSenderRef + "/" + messagePushID, messageTextBody);
            messageBodyDetails.put( messageReceiverRef + "/" + messagePushID, messageTextBody);

            RootRef.updateChildren(messageBodyDetails).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task)
                {
                    if (task.isSuccessful())
                    {
                    }
                    else
                    {
                        Toast.makeText(OfferActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                    binding.inputMessage.setText("");
                }
            });
        }
    }

}
