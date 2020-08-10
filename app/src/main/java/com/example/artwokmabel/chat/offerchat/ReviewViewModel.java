package com.example.artwokmabel.chat.offerchat;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.artwokmabel.models.NormalChat;
import com.example.artwokmabel.models.OrderChat;
import com.example.artwokmabel.repos.FirestoreRepo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class ReviewViewModel extends ViewModel {

    private FirebaseAuth mAuth;
    private DatabaseReference RootRef;

    public ReviewViewModel() {
        mAuth = FirebaseAuth.getInstance();
        RootRef = FirebaseDatabase.getInstance().getReference();
    }

    public void uploadNewReview(float rating, String listingId, String publicReview, String privateReview){

        FirestoreRepo.getInstance().uploadNewReview(rating, listingId, mAuth.getCurrentUser().getUid(), publicReview, privateReview);
    }

    public void setOfferAsReviewed(OrderChat orderChat, String lastMessageId){

        String buyerId;
        String theOtherId;
        String messageMeId = mAuth.getCurrentUser().getUid();
        if(messageMeId.equals(orderChat.getListing().getUserid())){
            buyerId = orderChat.getBuyerId();
            theOtherId = buyerId;
        }else{
            theOtherId = orderChat.getListing().getUserid();
        }

        RootRef.child("Offers")
                .child(messageMeId)
                .child(theOtherId)
                .child(orderChat.getListing().getPostid())
                .child(lastMessageId)
                .child("acceptStatus")
                .setValue("reviewed");

        RootRef.child("Offers")
                .child(theOtherId)
                .child(messageMeId)
                .child(orderChat.getListing().getPostid())
                .child(lastMessageId)
                .child("acceptStatus")
                .setValue("reviewed");
    }

}
