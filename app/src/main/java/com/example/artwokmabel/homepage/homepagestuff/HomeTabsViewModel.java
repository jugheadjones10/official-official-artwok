package com.example.artwokmabel.homepage.homepagestuff;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.artwokmabel.Repositories.FirestoreRepo;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class HomeTabsViewModel extends AndroidViewModel {
    private FirebaseAuth mAuth;
    private final LiveData<ArrayList<String>> categoryListObservable;

    public HomeTabsViewModel(Application application) {
        super(application);

        mAuth = FirebaseAuth.getInstance();
        // If any transformation is needed, this can be simply done by Transformations class ...
        categoryListObservable = FirestoreRepo.getInstance().getUserTabsList(mAuth.getCurrentUser().getUid());
    }

    public LiveData<ArrayList<String>> getCategoryListObservable() {
        return categoryListObservable;
    }
}
