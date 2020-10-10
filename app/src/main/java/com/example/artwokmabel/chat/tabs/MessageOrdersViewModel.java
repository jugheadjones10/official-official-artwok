package com.example.artwokmabel.chat.tabs;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.artwokmabel.models.OrderChat;
import com.example.artwokmabel.models.User;
import com.example.artwokmabel.repos.FirestoreRepo;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class MessageOrdersViewModel extends ViewModel {

    private FirebaseAuth mAuth;
    private final LiveData<List<OrderChat>> ordersAndSellsObservable;

    public MessageOrdersViewModel() {
        mAuth = FirebaseAuth.getInstance();
        ordersAndSellsObservable = FirestoreRepo.getInstance().getOrdersAndSells(mAuth.getCurrentUser().getUid());
    }

    public LiveData<List<OrderChat>> getOrdersAndSellsObservable() {
        return ordersAndSellsObservable;
    }

    public LiveData<User> getUserObservable(String userId){
        return FirestoreRepo.getInstance().getUser(userId);
    }

    public LiveData<Integer> getNumOfUnreadInOffersTab(){
        return FirestoreRepo.getInstance().getNumOfUnreadInOffersTab(mAuth.getCurrentUser().getUid());
    }

}
