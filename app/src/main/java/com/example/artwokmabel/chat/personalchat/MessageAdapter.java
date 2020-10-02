package com.example.artwokmabel.chat.personalchat;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.example.artwokmabel.R;
import com.example.artwokmabel.Utils.DecimalDigitsInputFilter;
import com.example.artwokmabel.chat.offerchat.OfferFragment;
import com.example.artwokmabel.chat.offerchat.OfferViewModel;
import com.example.artwokmabel.databinding.CustomMessagesLayoutBinding;
import com.example.artwokmabel.databinding.ItemNormalListingBinding;
import com.example.artwokmabel.databinding.LayoutImageMessageBinding;
import com.example.artwokmabel.databinding.LayoutOfferMessageBinding;
import com.example.artwokmabel.databinding.LayoutOfferPriceBinding;
import com.example.artwokmabel.homepage.adapters.ListingsAdapter;
import com.example.artwokmabel.models.AgreementDetails;
import com.example.artwokmabel.models.ImageMessage;
import com.example.artwokmabel.models.Listing;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Message> userMessageList;
    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;
    private OrderChat orderChat;
    private Button offerButton;
    private Context mContext;

    private final int OFFER = 0, TALK = 1, IMAGE = 2;

    public MessageAdapter (List<Message> userMessagesList, OrderChat orderChat, Context mContext) {
        this.userMessageList = userMessagesList;
        this.orderChat = orderChat;
        this.mContext = mContext;
        mAuth = FirebaseAuth.getInstance();
    }


    public class MessageViewHolder extends RecyclerView.ViewHolder {
        private CustomMessagesLayoutBinding binding;
        public MessageViewHolder(CustomMessagesLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public class OfferMessageViewHolder extends RecyclerView.ViewHolder {
        private LayoutOfferMessageBinding binding;
        public OfferMessageViewHolder(LayoutOfferMessageBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public class ImageMessageViewHolder extends RecyclerView.ViewHolder{
        private LayoutImageMessageBinding binding;
        public ImageMessageViewHolder(LayoutImageMessageBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        switch (viewType) {
            case OFFER:
                LayoutOfferMessageBinding offerMessageBinding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()), R.layout.layout_offer_message, viewGroup,false);
                viewHolder = new OfferMessageViewHolder(offerMessageBinding);
                break;
            case TALK:
                CustomMessagesLayoutBinding customMessagesLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()), R.layout.custom_messages_layout, viewGroup,false);
                viewHolder = new MessageViewHolder(customMessagesLayoutBinding);
                break;
            case IMAGE:
                LayoutImageMessageBinding layoutImageMessageBinding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()), R.layout.layout_image_message, viewGroup,false);
                viewHolder = new ImageMessageViewHolder(layoutImageMessageBinding);
                break;
            default:
                CustomMessagesLayoutBinding defaultMessageBinding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()), R.layout.custom_messages_layout, viewGroup,false);
                viewHolder = new MessageViewHolder(defaultMessageBinding);
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

                //Insert date divider
                offerViewHolder.binding.dateDividerLayout.setVisibility(View.GONE);
                if (i != 0) {
                    Message previousMessage = userMessageList.get(i - 1);
                    SimpleDateFormat sdf = new SimpleDateFormat("E, dd MMMM yyyy");
                    if (!sdf.format(new Date(offerMessage.getNanopast())).equals(sdf.format(new Date(previousMessage.getNanopast())))) {

                        offerViewHolder.binding.dateDividerLayout.setVisibility(View.VISIBLE);
                        offerViewHolder.binding.dateText.setText(sdf.format(new Date(offerMessage.getNanopast())));
                    }
                }

                offerViewHolder.binding.setOfferprice(offerMessage.getPrice());

                if (offerMessage.getAcceptStatus().equals("accepted") || offerMessage.getAcceptStatus().equals("received") || offerMessage.getAcceptStatus().equals("delivered") || offerMessage.getAcceptStatus().equals("reviewed")) {
                    Log.d("seeformyself", "ACCEPTED RAN");
                    offerViewHolder.binding.acceptDeclineLinearLayout.setVisibility(View.VISIBLE);

                    offerViewHolder.binding.declineButton.setVisibility(View.GONE);
                    offerViewHolder.binding.acceptButton.setVisibility(View.VISIBLE);

                    offerViewHolder.binding.acceptButton.setText("Accepted");
                    offerViewHolder.binding.acceptButton.setEnabled(false);

                }else if(offerMessage.getAcceptStatus().equals("invalid")){

                    if(!mAuth.getCurrentUser().getUid().equals(offerMessage.getFrom())){
                        offerViewHolder.binding.acceptDeclineLinearLayout.setVisibility(View.VISIBLE);

                        offerViewHolder.binding.declineButton.setVisibility(View.VISIBLE);
                        offerViewHolder.binding.acceptButton.setVisibility(View.VISIBLE);

                        offerViewHolder.binding.acceptButton.setEnabled(false);
                        offerViewHolder.binding.declineButton.setEnabled(false);
                    }else{
                        offerViewHolder.binding.acceptDeclineLinearLayout.setVisibility(View.GONE);
                    }

                }else if(offerMessage.getAcceptStatus().equals("declined")){

                    Log.d("seeformyself", "DECLINE RAN");
                    offerViewHolder.binding.acceptDeclineLinearLayout.setVisibility(View.VISIBLE);

                    offerViewHolder.binding.acceptButton.setVisibility(View.GONE);
                    offerViewHolder.binding.declineButton.setVisibility(View.VISIBLE);

                    offerViewHolder.binding.declineButton.setText("Declined");
                    offerViewHolder.binding.declineButton.setEnabled(false);

                }else if(offerMessage.getAcceptStatus().equals("pending")){
                    Log.d("seeformyself", "PENDING RAN");

                    if(mAuth.getCurrentUser().getUid().equals(offerMessage.getFrom())){

                        offerViewHolder.binding.acceptDeclineLinearLayout.setVisibility(View.GONE);

                    }else{

                        offerViewHolder.binding.acceptButton.setEnabled(true);
                        offerViewHolder.binding.declineButton.setEnabled(true);

                        offerViewHolder.binding.acceptDeclineLinearLayout.setVisibility(View.VISIBLE);
                        offerViewHolder.binding.acceptButton.setVisibility(View.VISIBLE);
                        offerViewHolder.binding.declineButton.setVisibility(View.VISIBLE);

                        offerViewHolder.binding.acceptButton.setText("Accept");
                        offerViewHolder.binding.declineButton.setText("Decline");

                        offerViewHolder.binding.acceptButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

                                LayoutOfferPriceBinding offerPriceBinding = DataBindingUtil.inflate(((Activity)mContext).getLayoutInflater(), R.layout.layout_offer_price, null, false);

                                OfferViewModel viewModel = new ViewModelProvider((ViewModelStoreOwner) mContext).get(OfferViewModel.class);

                                viewModel.getAgreementDetails().observe((LifecycleOwner) mContext, new Observer<AgreementDetails>() {
                                    @Override
                                    public void onChanged(AgreementDetails agreementDetails) {
                                        if(agreementDetails != null){
                                            offerPriceBinding.setAgreementDetails(agreementDetails);
                                        }
                                    }
                                });

//                                viewModel.getAgreementDetails().observe((LifecycleOwner) mContext, new Observer<AgreementDetails>() {
//                                    @Override
//                                    public void onChanged(AgreementDetails agreementDetails) {
//                                        if(agreementDetails != null){
                                    offerPriceBinding.setAgreementDetails(OfferFragment.getInstance().agreementDetails);
//                                        }
//                                    }
//                                });

                                builder.setView(offerPriceBinding.getRoot());

                                AlertDialog dialog = builder.create();
                                dialog.show();

                                offerPriceBinding.agreeButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        setAcceptStaus(offerMessage, "accepted");
                                        dialog.dismiss();
                                    }
                                });
                            }
                        });

                        offerViewHolder.binding.declineButton.setOnClickListener(new View.OnClickListener() {
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

                Message message = (Message) userMessageList.get(i);
                MessageViewHolder messageViewHolder = (MessageViewHolder) viewHolder;

                //Insert date dividers
                messageViewHolder.binding.dateDividerLayout.setVisibility(View.GONE);
                if(i != 0){
                    Message previousMessage = userMessageList.get(i - 1);
                    SimpleDateFormat sdf = new SimpleDateFormat("E, dd MMMM yyyy");
                    if(!sdf.format(new Date(message.getNanopast())).equals(sdf.format(new Date(previousMessage.getNanopast())))) {
                        messageViewHolder.binding.dateDividerLayout.setVisibility(View.VISIBLE);
                        messageViewHolder.binding.dateText.setText(sdf.format(new Date(message.getNanopast())));
                    }
                }

                String fromUserID = message.getFrom();
                String fromMessageType = message.getType();

                messageViewHolder.binding.receiverMessageText.setVisibility(View.GONE);
                messageViewHolder.binding.senderMessageText.setVisibility(View.GONE);

                messageViewHolder.binding.myTime.setVisibility(View.GONE);
                messageViewHolder.binding.friendTime.setVisibility(View.GONE);

                if (fromMessageType.equals("text")) {
                    if (fromUserID.equals(messageSenderId)) {
                        //If I sent the message
                        messageViewHolder.binding.senderMessageText.setVisibility(View.VISIBLE);
                        messageViewHolder.binding.myTime.setVisibility(View.VISIBLE);

                        messageViewHolder.binding.senderMessageText.setBackgroundResource(R.drawable.sender_messages_layout);
                        //messageViewHolder.binding.senderMessageText.setTextColor(Color.BLACK);
                        messageViewHolder.binding.senderMessageText.setText(message.getMessage());
                        messageViewHolder.binding.myTime.setText(message.getReadableNanopastDate());
                    } else {
                        //If I'm receiving the message
                        messageViewHolder.binding.receiverMessageText.setVisibility(View.VISIBLE);
                        messageViewHolder.binding.friendTime.setVisibility(View.VISIBLE);

                        messageViewHolder.binding.receiverMessageText.setBackgroundResource(R.drawable.receiver_messages_layout);
                        //messageViewHolder.binding.receiverMessageText.setTextColor(Color.BLACK);
                        messageViewHolder.binding.receiverMessageText.setText(message.getMessage());
                        messageViewHolder.binding.friendTime.setText(message.getReadableNanopastDate());
                    }
                }else{
                    //FIND OUT WHAT THE POINT OF THIS CLAUSE IS
                    //Checks if the user looking at the thing is the admin or not
                    if((message.getFrom().equals(message.getTo()) && messageSenderId.equals(message.getFrom()))
                        || (!message.getFrom().equals(message.getTo()) && !messageSenderId.equals(message.getTo()))){
                        messageViewHolder.binding.senderMessageText.setVisibility(View.VISIBLE);
                        messageViewHolder.binding.myTime.setVisibility(View.VISIBLE);

                        messageViewHolder.binding.senderMessageText.setBackgroundResource(R.drawable.sender_messages_layout);
                        messageViewHolder.binding.senderMessageText.setTextColor(Color.BLACK);
                        messageViewHolder.binding.senderMessageText.setText(message.getMessage());
                        messageViewHolder.binding.myTime.setText(message.getReadableNanopastDate());
                    }else{
                        messageViewHolder.binding.receiverMessageText.setVisibility(View.VISIBLE);
                        messageViewHolder.binding.friendTime.setVisibility(View.VISIBLE);

                        messageViewHolder.binding.receiverMessageText.setBackgroundResource(R.drawable.receiver_messages_layout);
                        messageViewHolder.binding.receiverMessageText.setTextColor(Color.BLACK);
                        messageViewHolder.binding.receiverMessageText.setText(message.getMessage());
                        messageViewHolder.binding.friendTime.setText(message.getReadableNanopastDate());
                    }
                }

                break;

            case IMAGE:

                String myId = mAuth.getCurrentUser().getUid();

                ImageMessage imageMessage = (ImageMessage) userMessageList.get(i);
                ImageMessageViewHolder imageMessageViewHolder = (ImageMessageViewHolder) viewHolder;

                //Insert date dividers
                imageMessageViewHolder.binding.dateDividerLayout.setVisibility(View.GONE);
                if(i != 0){
                    Message previousMessage = userMessageList.get(i - 1);
                    SimpleDateFormat sdf = new SimpleDateFormat("E, dd MMMM yyyy");
                    if(!sdf.format(new Date(imageMessage.getNanopast())).equals(sdf.format(new Date(previousMessage.getNanopast())))) {
                        imageMessageViewHolder.binding.dateDividerLayout.setVisibility(View.VISIBLE);
                        imageMessageViewHolder.binding.dateText.setText(sdf.format(new Date(imageMessage.getNanopast())));
                    }
                }

                imageMessageViewHolder.binding.friendImageFrame.setVisibility(View.GONE);
                imageMessageViewHolder.binding.myImageFrame.setVisibility(View.GONE);

                String imageFromUserId = imageMessage.getFrom();

                if (imageFromUserId.equals(myId)) {
                    //If I'm sending the image
                    imageMessageViewHolder.binding.myImageFrame.setVisibility(View.VISIBLE);
                    Picasso.get()
                            .load(imageMessage.getImageUrl())
                            .transform(new RoundedCornersTransformation(10, 0))
                            .resize(300, 300)
                            .placeholder(R.drawable.placeholder_black_new)
                            .error(R.drawable.placeholder_color_new)
                            .into(imageMessageViewHolder.binding.myImage);

                } else {

                    imageMessageViewHolder.binding.friendImageFrame.setVisibility(View.VISIBLE);
                    Picasso.get()
                            .load(imageMessage.getImageUrl())
                            .transform(new RoundedCornersTransformation(10, 0))
                            .resize(300, 300)
                            .placeholder(R.drawable.placeholder_black_new)
                            .error(R.drawable.placeholder_color_new)
                            .into(imageMessageViewHolder.binding.friendImage);
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
        Message message = userMessageList.get(position);
        if (message instanceof OfferMessage) {
            return OFFER;
        }else if(message instanceof ImageMessage){
            return IMAGE;
        }  else {
            return TALK;
        }
    }

    @Override
    public int getItemCount() {
        return userMessageList.size();
    }

}