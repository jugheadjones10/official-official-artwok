package com.example.artwokmabel.homepage.listing;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.artwokmabel.models.Listing;
import com.example.artwokmabel.models.Review;
import com.example.artwokmabel.repos.FirestoreRepo;
import com.example.artwokmabel.models.User;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class ListingActivityViewModel extends ViewModel {

    private LiveData<User> userObservable;
    private FirebaseAuth mAuth;

    public ListingActivityViewModel() {
        mAuth = FirebaseAuth.getInstance();
    }

    public LiveData<User> getUserObservable(String uid) {
        userObservable = FirestoreRepo.getInstance().getUser(uid);
        return userObservable;
    }

    public void addUserListingFavs(String listingId){
        FirestoreRepo.getInstance().addUserListingFavs(listingId, mAuth.getCurrentUser().getUid());
    }

    public void removeUserListingFavs(String listingId){
        FirestoreRepo.getInstance().removeUserListingFavs(listingId,  mAuth.getCurrentUser().getUid());
    }

    public void addUserRequestFavs(String requestId){
        FirestoreRepo.getInstance().addUserRequestFavs(requestId, mAuth.getCurrentUser().getUid());
    }

    public void removeUserRequestFavs(String requestId){
        FirestoreRepo.getInstance().removeUserRequestFavs(requestId, mAuth.getCurrentUser().getUid());
    }

    public void deleteUserListing(String listingId){
        FirestoreRepo.getInstance().deleteUserListing(listingId);
    }


    public LiveData<List<Review>> getListingReviews(Listing listing){
        return FirestoreRepo.getInstance().getListingReviews(listing);
    }
}
