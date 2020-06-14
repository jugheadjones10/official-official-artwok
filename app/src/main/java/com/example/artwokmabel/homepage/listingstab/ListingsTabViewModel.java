package com.example.artwokmabel.homepage.listingstab;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.navigation.NavController;

import com.example.artwokmabel.models.Listing;
import com.example.artwokmabel.repos.FirestoreRepo;

import java.util.List;

public class ListingsTabViewModel extends ViewModel {

    private final LiveData<List<Listing>> listingsObeservable;
    private NavController navController;

    public ListingsTabViewModel(String cat) {
        Log.d("viewModelCat", cat);
        listingsObeservable = FirestoreRepo.getInstance().getSingleCategoryListings(cat);
    }

    public LiveData<List<Listing>> getListingsObservable() {
        return listingsObeservable;
    }

    public void setNavController(NavController navController){
        this.navController = navController;
    }

    public NavController getNavController(){
        return navController;
    }

}