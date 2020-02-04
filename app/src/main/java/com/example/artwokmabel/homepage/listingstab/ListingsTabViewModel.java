package com.example.artwokmabel.homepage.listingstab;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.artwokmabel.models.Listing;
import com.example.artwokmabel.repos.FirestoreRepo;

import java.util.List;

public class ListingsTabViewModel extends ViewModel {

    private final LiveData<List<Listing>> listingsObeservable;

    public ListingsTabViewModel(String cat) {
        Log.d("viewModelCat", cat);
        listingsObeservable = FirestoreRepo.getInstance().getSingleCategoryListings(cat);
    }

    public LiveData<List<Listing>> getListingsObeservable() {
        return listingsObeservable;
    }

}