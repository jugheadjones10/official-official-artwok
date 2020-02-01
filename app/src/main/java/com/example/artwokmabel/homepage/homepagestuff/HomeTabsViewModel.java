package com.example.artwokmabel.homepage.homepagestuff;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.artwokmabel.Repositories.FirestoreRepo;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class HomeTabsViewModel extends ViewModel {
    private FirebaseAuth mAuth;
    private final LiveData<ArrayList<String>> categoryListObservable;
    private ArrayList<String> categoriesListMaintainable;

    public HomeTabsViewModel() {
        mAuth = FirebaseAuth.getInstance();
        // If any transformation is needed, this can be simply done by Transformations class ...
        categoryListObservable = FirestoreRepo.getInstance().getUserTabsList(mAuth.getCurrentUser().getUid());
    }

    public LiveData<ArrayList<String>> getCategoryListObservable() {
        return categoryListObservable;
    }

    public ArrayList<String> getCategoriesListMaintainable() {
        return categoriesListMaintainable;
    }

    public void setCategoriesListMaintainable(ArrayList<String> categoriesListMaintainable) {
        this.categoriesListMaintainable = categoriesListMaintainable;
    }
}
