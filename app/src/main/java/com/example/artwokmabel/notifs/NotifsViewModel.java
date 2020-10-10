package com.example.artwokmabel.notifs;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.artwokmabel.models.Notification;
import com.example.artwokmabel.repos.FirestoreRepo;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class NotifsViewModel extends ViewModel {

    private FirebaseAuth mAuth;
    private final LiveData<List<Notification>> userNotificationsObservable;

    public NotifsViewModel() {
        mAuth = FirebaseAuth.getInstance();
        userNotificationsObservable = FirestoreRepo.getInstance().getUserNotifications(mAuth.getCurrentUser().getUid());
    }

    public LiveData<List<Notification>> getUserNotificationsObservable() {
        return userNotificationsObservable;
    }

}
