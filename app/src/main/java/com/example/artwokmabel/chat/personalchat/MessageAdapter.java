package com.example.artwokmabel.chat.personalchat;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
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

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);

            senderMessageText = (TextView) itemView.findViewById(R.id.sender_messsage_text);
            receiverMessageText = (TextView) itemView.findViewById(R.id.receiver_message_text);
            receiverProfileImage = (CircleImageView) itemView.findViewById(R.id.message_profile_image);
            messageReceiverPicture = itemView.findViewById(R.id.message_receiver_image_view);
            messageSenderPicture = itemView.findViewById(R.id.message_sender_image_view);
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


//        View view = LayoutInflater.from(viewGroup.getContext())
//                .inflate(R.layout.custom_messages_layout, viewGroup, false);
//
//        mAuth = FirebaseAuth.getInstance();
//
//        return new MessageViewHolder(view);
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


//                                FirebaseDatabase.getInstance().getReference()
//                                        .child("Offers")
//                                        .child(offerMessage.getTo())
//                                        .child(offerMessage.getFrom())
//                                        .child(listingId)
//                                        .child(offerMessage.getMessageID())
//                                        .child("acceptStatus")
//                                        .setValue("accepted");
//
//                                FirebaseDatabase.getInstance().getReference()
//                                        .child("Offers")
//                                        .child(offerMessage.getFrom())
//                                        .child(offerMessage.getTo())
//                                        .child(listingId)
//                                        .child(offerMessage.getMessageID())
//                                        .child("acceptStatus")
//                                        .setValue("accepted");
                            }
                        });

                        offerViewHolder.declineButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                setAcceptStaus(offerMessage, "declined");

//                                FirebaseDatabase.getInstance().getReference()
////                                        .child("Offers")
////                                        .child(offerMessage.getTo())
////                                        .child(offerMessage.getFrom())
////                                        .child(listingId)
////                                        .child(offerMessage.getMessageID())
////                                        .child("acceptStatus")
////                                        .setValue("declined");
////
////                                FirebaseDatabase.getInstance().getReference()
////                                        .child("Offers")
////                                        .child(offerMessage.getFrom())
////                                        .child(offerMessage.getTo())
////                                        .child(listingId)
////                                        .child(offerMessage.getMessageID())
////                                        .child("acceptStatus")
////                                        .setValue("declined");
                            }
                        });
                    }
                }


                break;
            case TALK:
                String messageSenderId = mAuth.getCurrentUser().getUid();
                //Message messages = userMessageList.get(i);

                Message messages = (Message) userMessageList.get(i);
                MessageViewHolder messageViewHolder = (MessageViewHolder) viewHolder;

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
                messageViewHolder.receiverProfileImage.setVisibility(View.GONE);
                messageViewHolder.senderMessageText.setVisibility(View.GONE);
                messageViewHolder.messageSenderPicture.setVisibility(View.GONE);
                messageViewHolder.messageReceiverPicture.setVisibility(View.GONE);

                if (fromMessageType.equals("text")) {
                    if (fromUserID.equals(messageSenderId)) {
                        messageViewHolder.senderMessageText.setVisibility(View.VISIBLE);

                        messageViewHolder.senderMessageText.setBackgroundResource(R.drawable.sender_messages_layout);
                        messageViewHolder.senderMessageText.setTextColor(Color.BLACK);
                        messageViewHolder.senderMessageText.setText(messages.getMessage() + "\n \n" + messages.getTime() + " - " + messages.getDate());
                    } else {
                        messageViewHolder.receiverProfileImage.setVisibility(View.VISIBLE);
                        messageViewHolder.receiverMessageText.setVisibility(View.VISIBLE);

                        messageViewHolder.receiverMessageText.setBackgroundResource(R.drawable.receiver_messages_layout);
                        messageViewHolder.receiverMessageText.setTextColor(Color.BLACK);
                        messageViewHolder.receiverMessageText.setText(messages.getMessage() + "\n \n" + messages.getTime() + " - " + messages.getDate());
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
                .child(orderChat.getPostid())
                .child(offerMessage.getMessageID())
                .child("acceptStatus")
                .setValue(offerStatus);

        FirebaseDatabase.getInstance().getReference()
                .child("Offers")
                .child(offerMessage.getFrom())
                .child(offerMessage.getTo())
                .child(orderChat.getPostid())
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


//    public void addMessageToList(Message message){
//        List<Message> userMessageList = this.userMessageList;
//        if(userMessageList == null){
//            userMessageList = new ArrayList<>();
//        }
//        userMessageList.add(message);
//        setUserMessageList(userMessageList);
//    }
//
//    public void setUserMessageList(final List<Message> messages) {
//        if (this.userMessageList == null) {
//            this.userMessageList = messages;
//            notifyItemRangeInserted(0, messages.size());
//        } else {
//            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
//                @Override
//                public int getOldListSize() {
//                    return MessageAdapter.this.userMessageList.size();
//                }
//
//                @Override
//                public int getNewListSize() {
//                    return messages.size();
//                }
//
//                @Override
//                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
//                    return MessageAdapter.this.userMessageList.get(oldItemPosition).getMessageID() ==
//                            messages.get(newItemPosition).getMessageID();
//                }
//
//                @Override
//                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
//                    return MessageAdapter.this.userMessageList.get(oldItemPosition).getMessageID() ==
//                            messages.get(newItemPosition).getMessageID();
//                }
//            });
//            this.userMessageList = messages;
//            //notifyDataSetChanged();
//            //FollowingAdapter.getInstance().notifyDataSetChanged();
//            result.dispatchUpdatesTo(this);
//        }
//    }



    @Override
    public int getItemCount() {
        return userMessageList.size();
    }

}