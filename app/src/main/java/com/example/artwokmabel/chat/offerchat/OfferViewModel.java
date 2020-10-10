package com.example.artwokmabel.chat.offerchat;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.artwokmabel.UploadViewModel;
import com.example.artwokmabel.models.AgreementDetails;
import com.example.artwokmabel.models.Listing;
import com.example.artwokmabel.repos.FirestoreRepo;

public class OfferViewModel extends ViewModel implements UploadViewModel {

    private MutableLiveData<Uri> imagePath = new MutableLiveData<>();
    private MutableLiveData<Uri> videoPath = new MutableLiveData<>();

    private String listingsId;

    private final MutableLiveData<AgreementDetails> agreementDetails = new MutableLiveData<>();

    public OfferViewModel() {
    }

    public String getListingsId() {
        return listingsId;
    }

    public void setListingsId(String listingsId) {
        this.listingsId = listingsId;
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

    public void updateOfferDetails(AgreementDetails agreementDetails){
        setAgreementDetails(agreementDetails);
//        OfferFragment.getInstance().SendAgreementInfo(deadline, sellerRequest, buyerRequest);
    }


    @Override
    public void setResultOk(Uri imagePath) {
        this.imagePath.setValue(imagePath);
    }

    @Override
    public void setVideoResultOk(Uri videoPath) {
        this.videoPath.setValue(videoPath);
    }

    @Override
    public LiveData<Uri> getImagePath() {
        return imagePath;
    }

    @Override
    public LiveData<Uri> getVideoPath() {
        return videoPath;
    }
}
