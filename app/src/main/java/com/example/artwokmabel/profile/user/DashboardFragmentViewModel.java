package com.example.artwokmabel.profile.user;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import com.example.artwokmabel.models.MainPost;
import com.example.artwokmabel.models.Message;
import com.example.artwokmabel.repos.FirestoreRepo;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class DashboardFragmentViewModel extends ViewModel {

    public DashboardFragmentViewModel() {
    }

    public LiveData<List<Message>> getDashboardMessagesObservable(String userId) {
        return FirestoreRepo.getInstance().getUserDashboardMessages(userId);
    }

    public void addDashboardMessage(String from, String to, String message){
        FirestoreRepo.getInstance().addDashboardMessage(from, to, message);
    }
}