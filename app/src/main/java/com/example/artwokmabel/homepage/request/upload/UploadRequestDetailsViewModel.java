package com.example.artwokmabel.homepage.request;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.artwokmabel.repos.FirestoreRepo;
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
