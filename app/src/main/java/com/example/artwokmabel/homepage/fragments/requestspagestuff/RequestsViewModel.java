package com.example.artwokmabel.homepage.fragments.requestspagestuff;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.artwokmabel.Repositories.FirestoreRepo;

import java.util.List;

public class RequestsViewModel extends ViewModel {

    private final LiveData<List<Request>> requestsObservable;

    public RequestsViewModel() {
        requestsObservable = FirestoreRepo.getInstance().getRequests();
    }

    public LiveData<List<Request>> getRequestsObservable() {
        return requestsObservable;
    }
}
