package com.example.artwokmabel.chat.activities;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.artwokmabel.Repositories.FirestoreRepo;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class ChatProfileViewModel extends ViewModel {
    private FirebaseAuth mAuth;
    private final LiveData<ArrayList<String>> chatRoomsObservable;

    public ChatProfileViewModel() {

        mAuth = FirebaseAuth.getInstance();
        // If any transformation is needed, this can be simply done by Transformations class ...
        chatRoomsObservable = FirestoreRepo.getInstance().getUserChatrooms(mAuth.getCurrentUser().getUid());
    }

    public LiveData<ArrayList<String>> getChatRoomsObservable() {
        return chatRoomsObservable;
    }

    public String setNewChatRoom(String destinationUid){
        ArrayList<String> participants = new ArrayList<>();
        participants.add(destinationUid);
        participants.add(mAuth.getCurrentUser().getUid());

        Log.d("play", participants.toString());
        return FirestoreRepo.getInstance().setNewChatroom(participants);
    }
}
