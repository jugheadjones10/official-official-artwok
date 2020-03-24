package com.example.artwokmabel.chat.personalchat;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.artwokmabel.R;
import com.example.artwokmabel.models.Message;
import com.example.artwokmabel.models.OfferMessage;
import com.example.artwokmabel.models.OrderChat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Message> userMessageList;
    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;
    private OrderChat orderChat;
    private Button offerButton;

    private final int OFFER = 0, TALK = 1;

    public MessageAdapter (List<Message> userMessagesList, OrderChat orderChat)
    {
        this.userMessageList = userMessagesList;
        this.orderChat = orderChat;
        mAuth = FirebaseAuth.getInstance();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {
        public TextView senderMessageText, receiverMessageText;
        public CircleImageView receiverProfileImage;
        public ImageView messageSenderPicture, messageReceiverPicture;
        public TextView myTime, friendTime;
//        public ViewStub dateDivider;
        public TextView dateText;
        public LinearLayout dateDividerLayout;
        public RelativeLayout relativeLayout;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);

            senderMessageText = itemView.findViewById(R.id.sender_messsage_text);
            receiverMessageText = itemView.findViewById(R.id.receiver_message_text);
            receiverProfileImage = itemView.findViewById(R.id.message_profile_image);
            messageReceiverPicture = itemView.findViewById(R.id.message_receiver_image_view);
            messageSenderPicture = itemView.findViewById(R.id.message_sender_image_view);
            myTime = itemView.findViewById(R.id.my_time);
            friendTime = itemView.findViewById(R.id.friend_time);
            dateDividerLayout = itemView.findViewById(R.id.date_divider_layout);
            dateText = itemView.findViewById(R.id.dateText);
            relativeLayout = itemView.findViewById(R.id.chat_relative_layout);
//            dateDivider = itemView.findViewById(R.id.date_divider);
//            dateDivider = itemView.findViewById(R.id.text_date_divider);
        }
    }

    public class OfferMessageViewHolder extends RecyclerView.ViewHolder {
        public Button acceptButton, declineButton;
        public CircleImageView receiverProfileImage;
        public TextView priceText;
        public LinearLayout acceptDeclineLayout;

        public OfferMessageViewHolder(@NonNull View itemView) {
            super(itemView);

            acceptButton = itemView.findViewById(R.id.accept_button);
            declineButton = itemView.findViewById(R.id.decline_button);
            receiverProfileImage = itemView.findViewById(R.id.message_profile_image);
            priceText = itemView.findViewById(R.id.offer_price);
            acceptDeclineLayout = itemView.findViewById(R.id.accept_decline_linear_layout);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        switch (viewType) {
            case OFFER:
                View offer = inflater.inflate(R.layout.layout_offer_message, viewGroup, false);
                viewHolder = new OfferMessageViewHolder(offer);
                break;
            case TALK:
                View talk = inflater.inflate(R.layout.custom_messages_layout, viewGroup, false);
                viewHolder = new MessageViewHolder(talk);
                break;
            default:
                View normal = inflater.inflate(R.layout.custom_messages_layout, viewGroup, false);
                viewHolder = new MessageViewHolder(normal);
                break;
        }

        return viewHolder;

    }


    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, int i) {

        switch (viewHolder.getItemViewType()) {
            case OFFER:
                OfferMessage offerMessage = (OfferMessage) userMessageList.get(i);
                OfferMessageViewHolder offerViewHolder = (OfferMessageViewHolder) viewHolder;

                offerViewHolder.priceText.setText(offerMessage.getPrice());

                Log.d("seeformyself", offerMessage.getAcceptStatus());

                if(offerMessage.getAcceptStatus().equals("accepted") || offerMessage.getAcceptStatus().equals("received") || offerMessage.getAcceptStatus().equals("delivered")){
                    Log.d("seeformyself", "ACCEPTED RAN");
                    offerViewHolder.acceptDeclineLayout.setVisibility(View.VISIBLE);

                    offerViewHolder.declineButton.setVisibility(View.GONE);
                    offerViewHolder.acceptButton.setVisibility(View.VISIBLE);

                    offerViewHolder.acceptButton.setText("Accepted");
                    offerViewHolder.acceptButton.setEnabled(false);

                }else if(offerMessage.getAcceptStatus().equals("declined")){

                    Log.d("seeformyself", "DECLINE RAN");
                    offerViewHolder.acceptDeclineLayout.setVisibility(View.VISIBLE);

                    offerViewHolder.acceptButton.setVisibility(View.GONE);
                    offerViewHolder.declineButton.setVisibility(View.VISIBLE);

                    offerViewHolder.declineButton.setText("Declined");
                    offerViewHolder.declineButton.setEnabled(false);

                }else if(offerMessage.getAcceptStatus().equals("pending")){
                    Log.d("seeformyself", "PENDING RAN");

                    if(mAuth.getCurrentUser().getUid().equals(offerMessage.getFrom())){

                        offerViewHolder.acceptDeclineLayout.setVisibility(View.GONE);

                    }else{

                        offerViewHolder.acceptButton.setEnabled(true);
                        offerViewHolder.declineButton.setEnabled(true);

                        offerViewHolder.acceptDeclineLayout.setVisibility(View.VISIBLE);
                        offerViewHolder.acceptButton.setVisibility(View.VISIBLE);
                        offerViewHolder.declineButton.setVisibility(View.VISIBLE);

                        offerViewHolder.acceptButton.setText("Accept");
                        offerViewHolder.declineButton.setText("Decline");

                        offerViewHolder.acceptButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                setAcceptStaus(offerMessage, "accepted");
                            }
                        });

                        offerViewHolder.declineButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                setAcceptStaus(offerMessage, "declined");

                            }
                        });

                    }
                }


                break;
            case TALK:
                String messageSenderId = mAuth.getCurrentUser().getUid();

                Message messages = (Message) userMessageList.get(i);

                MessageViewHolder messageViewHolder = (MessageViewHolder) viewHolder;

                //Insert date dividers
                messageViewHolder.dateDividerLayout.setVisibility(View.GONE);

                RelativeLayout.LayoutParams linearParams = (RelativeLayout.LayoutParams) messageViewHolder.dateDividerLayout.getLayoutParams();
                linearParams.setMargins(0, 0, 0, 0);
                messageViewHolder.dateDividerLayout.setLayoutParams(linearParams);

                RecyclerView.LayoutParams relativeParams = (RecyclerView.LayoutParams)messageViewHolder.relativeLayout.getLayoutParams();
                relativeParams.setMargins(0, 0, 0, 0);
                messageViewHolder.relativeLayout.setLayoutParams(relativeParams);

                if(i != 0){
                    Message previousMessage = userMessageList.get(i - 1);

                    SimpleDateFormat sdf = new SimpleDateFormat("E, dd MMMM yyyy");

                    if(!sdf.format(new Date(messages.getNanopast())).equals(sdf.format(new Date(previousMessage.getNanopast())))) {

                        messageViewHolder.dateDividerLayout.setVisibility(View.VISIBLE);
                        messageViewHolder.dateText.setText(sdf.format(new Date(messages.getNanopast())));


                        RelativeLayout.LayoutParams insideLinearParams = (RelativeLayout.LayoutParams) messageViewHolder.dateDividerLayout.getLayoutParams();
                        insideLinearParams.setMargins(0, -30, 0, 0);
                        messageViewHolder.dateDividerLayout.setLayoutParams(insideLinearParams);

                        RecyclerView.LayoutParams insideRelativeParams = (RecyclerView.LayoutParams)messageViewHolder.relativeLayout.getLayoutParams();
                        insideRelativeParams.setMargins(0, 40, 0, 0);
                        messageViewHolder.relativeLayout.setLayoutParams(insideRelativeParams);

//                        messageViewHolder.dateDivider.setLayoutResource(R.layout.view_date_divider);
//                        messageViewHolder.dateDivider.setOnInflateListener(new ViewStub.OnInflateListener() {
//                            @Override
//                            public void onInflate(ViewStub stub, View inflated) {
//                                TextView date = inflated.findViewById(R.id.dateText);
//                                date.setText(sdf.format(new Date(messages.getNanopast())));
//                            }
//                        });
//
//                        if (messageViewHolder.dateDivider.getParent() != null) {
//                            messageViewHolder.dateDivider.inflate();
//                        } else {
//                            messageViewHolder.dateDivider.setVisibility(View.GONE);
//                        }

                    }
                }

                String fromUserID = messages.getFrom();
                String fromMessageType = messages.getType();

                usersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(fromUserID);

                usersRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild("image")) {
                            String receiverImage = dataSnapshot.child("image").getValue().toString();

                            Picasso.get().load(receiverImage).placeholder(R.drawable.ic_user).into(messageViewHolder.receiverProfileImage);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                messageViewHolder.receiverMessageText.setVisibility(View.GONE);
                messageViewHolder.senderMessageText.setVisibility(View.GONE);

                messageViewHolder.myTime.setVisibility(View.GONE);
                messageViewHolder.friendTime.setVisibility(View.GONE);

                messageViewHolder.receiverProfileImage.setVisibility(View.GONE);

                messageViewHolder.messageSenderPicture.setVisibility(View.GONE);
                messageViewHolder.messageReceiverPicture.setVisibility(View.GONE);

                if (fromMessageType.equals("text")) {
                    if (fromUserID.equals(messageSenderId)) {
                        messageViewHolder.senderMessageText.setVisibility(View.VISIBLE);
                        messageViewHolder.myTime.setVisibility(View.VISIBLE);

                        messageViewHolder.senderMessageText.setBackgroundResource(R.drawable.sender_messages_layout);
                        messageViewHolder.senderMessageText.setTextColor(Color.BLACK);
                        messageViewHolder.senderMessageText.setText(messages.getMessage());
                        messageViewHolder.myTime.setText(messages.getReadableNanopastDate());
                        // + "\n \n" + messages.getReadableNanopastDate()
                    } else {
                        messageViewHolder.receiverProfileImage.setVisibility(View.VISIBLE);
                        messageViewHolder.receiverMessageText.setVisibility(View.VISIBLE);
                        messageViewHolder.friendTime.setVisibility(View.VISIBLE);

                        messageViewHolder.receiverMessageText.setBackgroundResource(R.drawable.receiver_messages_layout);
                        messageViewHolder.receiverMessageText.setTextColor(Color.BLACK);
                        messageViewHolder.receiverMessageText.setText(messages.getMessage());
                        messageViewHolder.friendTime.setText(messages.getReadableNanopastDate());
                        //+ "\n \n" + messages.getReadableNanopastDate()
                    }
                }

                break;

            default:
                break;
        }


    }

    private void setAcceptStaus(OfferMessage offerMessage, String offerStatus){
        FirebaseDatabase.getInstance().getReference()
                .child("Offers")
                .child(offerMessage.getTo())
                .child(offerMessage.getFrom())
                .child(orderChat.getListing().getPostid())
                .child(offerMessage.getMessageID())
                .child("acceptStatus")
                .setValue(offerStatus);

        FirebaseDatabase.getInstance().getReference()
                .child("Offers")
                .child(offerMessage.getFrom())
                .child(offerMessage.getTo())
                .child(orderChat.getListing().getPostid())
                .child(offerMessage.getMessageID())
                .child("acceptStatus")
                .setValue(offerStatus);
    }

    @Override
    public int getItemViewType(int position) {
        if (userMessageList.get(position) instanceof OfferMessage) {
            return OFFER;
        } else if (userMessageList.get(position) instanceof Message) {
            return TALK;
        }
        return -1;
    }

    @Override
    public int getItemCount() {
        return userMessageList.size();
    }

}