package com.example.artwokmabel.homepage.request.tab;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.artwokmabel.models.Request;
import com.example.artwokmabel.repos.FirestoreRepo;

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
