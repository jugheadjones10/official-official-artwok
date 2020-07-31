package com.example.artwokmabel.chat.offerchat;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.artwokmabel.R;
import com.example.artwokmabel.Utils.DecimalDigitsInputFilter;
import com.example.artwokmabel.chat.MessageFragmentDirections;
import com.example.artwokmabel.chat.personalchat.ChatFragmentArgs;
import com.example.artwokmabel.chat.personalchat.MessageAdapter;
import com.example.artwokmabel.databinding.ActivityOfferBinding;
import com.example.artwokmabel.databinding.CustomOfferBarBinding;
import com.example.artwokmabel.databinding.FragmentOfferBinding;
import com.example.artwokmabel.databinding.LayoutOfferPriceBinding;
import com.example.artwokmabel.models.AgreementDetails;
import com.example.artwokmabel.models.Listing;
import com.example.artwokmabel.models.Message;
import com.example.artwokmabel.models.OfferMessage;
import com.example.artwokmabel.models.OrderChat;
import com.example.artwokmabel.profile.settings.SettingsFragmentDirections;
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

public class OfferFragment extends Fragment {

    private FragmentOfferBinding binding;
    private CustomOfferBarBinding offerBarBinding;
    private LayoutOfferPriceBinding offerPriceBinding;
    private NavController navController;
    private String messageFollowingId, messageFollowingUsername, messageFollowingProfileImg, messageMeId, buyerId;
    private String theOtherId;
    private OrderChat orderChat;
    private Listing liveListing;
    private AgreementDetails liveAgreementDetails;
    private OfferViewModel offerViewModel;

    private FirebaseAuth mAuth;
    private DatabaseReference RootRef;

    private LinearLayoutManager linearLayoutManager;
    private MessageAdapter messageAdapter;
    private final List<Message> messagesList = new ArrayList<>();
    private String saveCurrentTime, saveCurrentDate;

    //private AgreementDetails agreementDetails;

    private static OfferFragment instance;
    public static OfferFragment getInstance(){
        return instance;
    }

    //Change everything within to real-time info. Only use orderChat for the people and listing id.

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        instance = this;
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_offer, container, false);
        binding.setOfferFragment(this);
        binding.reviewButton.setVisibility(View.GONE);

        //Firebase
        RootRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        messageMeId = mAuth.getCurrentUser().getUid();

        //Receive argument
        orderChat = OfferFragmentArgs.fromBundle(getArguments()).getOrderchat();

        //If I am selling, the theOtherId is set to the buyerId (from the order chat)
        //If I am buying, then theOtherId is set to the sellerId (from the listing)
        if(messageMeId.equals(orderChat.getListing().getUserid())){
            buyerId = orderChat.getBuyerId();
            //Also might need to put in a check here to see if buyerId is null or not.
            theOtherId = buyerId;
        }else{
            theOtherId = orderChat.getListing().getUserid();
        }

        //ViewModel
        offerViewModel = new ViewModelProvider(requireActivity()).get(OfferViewModel.class);

        //Connect to viewModel to get LiveData for Listing and AgreementDetails
        connectToViewModels();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_container);

        inflateChatBar();

        messageAdapter = new MessageAdapter(messagesList, orderChat, getContext());
        linearLayoutManager = new LinearLayoutManager(requireActivity());
        binding.privateMessagesListOfUsers.setLayoutManager(linearLayoutManager);
        binding.privateMessagesListOfUsers.setAdapter(messageAdapter);
    }

    private void connectToViewModels(){
        offerViewModel.getListing(orderChat.getListing().getPostid()).observe(getViewLifecycleOwner(), new Observer<Listing>() {
            @Override
            public void onChanged(Listing listing) {
                if(listing != null){
                    offerBarBinding.setListing(listing);
                    liveListing = listing;
                }
            }
        });

        offerViewModel.getAgreementDetails().observe(getViewLifecycleOwner(), new Observer<AgreementDetails>() {
            @Override
            public void onChanged(AgreementDetails agreementDetails) {
                if(agreementDetails != null){
                    liveAgreementDetails = agreementDetails;
                }
            }
        });
    }

    private void inflateChatBar(){
        ((AppCompatActivity)requireActivity()).setSupportActionBar(binding.mainAppBar);
        ((AppCompatActivity)requireActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)requireActivity()).getSupportActionBar().setDisplayShowCustomEnabled(true);
        ((AppCompatActivity)requireActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);

        LayoutInflater layoutInflater = (LayoutInflater) requireActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        offerBarBinding = DataBindingUtil.inflate(layoutInflater, R.layout.custom_offer_bar, null, false);
        //binding.mainAppBar.inflateMenu(R.menu.offer_menu);

        offerBarBinding.setOfferFragment(this);
        Picasso.get().load(orderChat.getListing().getPhotos().get(0)).into(offerBarBinding.customProfileImage);

        ((AppCompatActivity)requireActivity()).getSupportActionBar().setCustomView(offerBarBinding.getRoot());
    }

    public void onOfferClicked(){

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());

        //final View customLayout = getLayoutInflater().inflate(R.layout.layout_offer_price, null);
        offerPriceBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.layout_offer_price, null, false);
        offerPriceBinding.setListing(liveListing);
        offerPriceBinding.setAgreementDetails(liveAgreementDetails);

        builder.setView(offerPriceBinding.getRoot());

        AlertDialog dialog = builder.create();
        dialog.show();

        offerPriceBinding.agreeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendOfferMessage(offerPriceBinding.priceEditText.getText().toString());
                dialog.dismiss();
            }
        });
    }

    public void onDocumentClicked(){
        OfferFragmentDirections.ActionOfferFragmentToOfferAgreementFragment action =
                OfferFragmentDirections.actionOfferFragmentToOfferAgreementFragment(liveListing.getPostid());
        navController.navigate(action);
    }

    @Override
    public void onStart() {
        super.onStart();

        RootRef.child("Offers").child(messageMeId).child(theOtherId).child(orderChat.getListing().getPostid())
            .addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    ArrayList<OfferMessage> messages = new ArrayList<>();

                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        if(snapshot.child("type").getValue().equals("null")){
                            messages.add(snapshot.getValue(OfferMessage.class));
                        }else if(snapshot.child("type").getValue().equals("agreement-details")){
                            offerViewModel.setAgreementDetails(snapshot.getValue(AgreementDetails.class));
                            //What if I press go to document before the agreementDetails data is loaded?
                            //May need to pass agreementDetails data to ViewModel
                        }
                    }

                    if(messages.size() != 0){
                        OfferMessage lastMessage = messages.get(messages.size() - 1);

                        if(lastMessage.getAcceptStatus().equals("accepted")){
                            //If I am the seller, I am given the option to deliver, and indicate as delivered
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

                                            OfferFragmentDirections.ActionOfferFragmentToReviewFragment action =
                                                    OfferFragmentDirections.actionOfferFragmentToReviewFragment(orderChat.getListing().getPostid());
                                            navController.navigate(action);
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
                        if(!dataSnapshot.child("type").getValue().equals("agreement-details")){
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
                    Toast.makeText(requireActivity(), "Error", Toast.LENGTH_SHORT).show();
                }
                binding.inputMessage.setText("");
            }
        });

    }

    public void SendMessage() {
        String messageText = binding.inputMessage.getText().toString();

        if (TextUtils.isEmpty(messageText))
        {
            Toast.makeText(requireActivity(), "first write your message...", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(requireActivity(), "Error", Toast.LENGTH_SHORT).show();
                    }
                    binding.inputMessage.setText("");
                }
            });
        }
    }

    public void SendAgreementInfo(String deadline, String sellerRequest, String buyerRequest) {

        Map messageBodyDetails = new HashMap();

        String messageSenderRef = "Offers/" + messageMeId + "/" + theOtherId + "/" + orderChat.getListing().getPostid();
        String messageReceiverRef = "Offers/" + theOtherId + "/" + messageMeId + "/" + orderChat.getListing().getPostid();

        String messagePushID;

        if(liveAgreementDetails == null){
            DatabaseReference userMessageKeyRef = RootRef.child("Offers")
                    .child(messageMeId).child(theOtherId).child(orderChat.getListing().getPostid()).push();
            messagePushID = userMessageKeyRef.getKey();
            Log.d("agreementpush", "The agreement was null" + messagePushID);
        }else{
            messagePushID = liveAgreementDetails.getMessageID();
            Log.d("agreementpush", "The agreement was not null" + messagePushID);
        }


        Map messageTextBody = new HashMap();

        //messageTextBody.put("message", "");
        messageTextBody.put("type", "agreement-details");
        //messageTextBody.put("from", messageMeId);
        //messageTextBody.put("to", theOtherId);
        messageTextBody.put("messageID", messagePushID);
        messageTextBody.put("deadline", deadline);
        messageTextBody.put("sellerRequest", sellerRequest);
        messageTextBody.put("buyerRequest", buyerRequest);
        //messageTextBody.put("time", saveCurrentTime);
        //messageTextBody.put("date", saveCurrentDate);
        //messageTextBody.put("nanopast", System.currentTimeMillis());
        //messageTextBody.put("read", "false");

        messageBodyDetails = new HashMap();
        messageBodyDetails.put(messageSenderRef + "/" + messagePushID, messageTextBody);
        messageBodyDetails.put( messageReceiverRef + "/" + messagePushID, messageTextBody);

        RootRef.updateChildren(messageBodyDetails).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task)
            {
                if (task.isSuccessful())
                {
                    //Log.d("agreementpush", task.getResult().toString());
                }
                else
                {
                    Toast.makeText(requireActivity(), "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}
