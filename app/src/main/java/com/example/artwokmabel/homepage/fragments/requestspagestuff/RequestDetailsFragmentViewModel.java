package com.example.artwokmabel.homepage.fragments.requestspagestuff;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.artwokmabel.Repositories.FirestoreRepo;
import com.example.artwokmabel.homepage.models.Category;

import java.util.List;

public class RequestDetailsFragmentViewModel extends ViewModel {

    private final LiveData<List<Category>> allCategoriesObservable;

    public RequestDetailsFragmentViewModel() {
        allCategoriesObservable = FirestoreRepo.getInstance().getAllCategories();
    }

    public LiveData<List<Category>> getAllCategoriesObservable() {
        return allCategoriesObservable;
    }
}
