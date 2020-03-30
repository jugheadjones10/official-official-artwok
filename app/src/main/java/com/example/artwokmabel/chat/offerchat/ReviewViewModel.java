package com.example.artwokmabel.chat.offerchat;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.artwokmabel.models.NormalChat;
import com.example.artwokmabel.repos.FirestoreRepo;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class ReviewViewModel extends ViewModel {

    private FirebaseAuth mAuth;

    public ReviewViewModel() {
        mAuth = FirebaseAuth.getInstance();
    }

    public void uploadNewReview(float rating, String listingId, String publicReview, String privateReview){

        FirestoreRepo.getInstance().uploadNewReview(rating, listingId, mAuth.getCurrentUser().getUid(), publicReview, privateReview);
    }

}
