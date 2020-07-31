package com.example.artwokmabel.chat.offerchat;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.artwokmabel.models.AgreementDetails;
import com.example.artwokmabel.models.Listing;
import com.example.artwokmabel.models.NormalChat;
import com.example.artwokmabel.repos.FirestoreRepo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OfferViewModel extends ViewModel {

    private final MutableLiveData<AgreementDetails> agreementDetails = new MutableLiveData<>();

    public OfferViewModel() {
    }

    public void setAgreementDetails(AgreementDetails agreementDetails){
        this.agreementDetails.setValue(agreementDetails);
    }

    public LiveData<AgreementDetails> getAgreementDetails(){
        return agreementDetails;
    }

    public LiveData<Listing> getListing(String listingId){
        return FirestoreRepo.getInstance().getListingLiveData(listingId);
    }

    public void updateListing(int price, String delivery, String returnExchange, Listing listing){
        FirestoreRepo.getInstance().updateListing(price, delivery, returnExchange, listing);
    }

    public void updateOfferDetails(String deadline, String sellerRequest, String buyerRequest){
        OfferFragment.getInstance().SendAgreementInfo(deadline, sellerRequest, buyerRequest);
    }

}
